
package com.zanoccio.packetkit.headers;

import java.lang.reflect.Field;

import com.zanoccio.packetkit.IP4Address;
import com.zanoccio.packetkit.MACAddress;
import com.zanoccio.packetkit.NetworkInterface;
import com.zanoccio.packetkit.PacketFragment;
import com.zanoccio.packetkit.PacketUtilities;
import com.zanoccio.packetkit.exceptions.InvalidFieldException;
import com.zanoccio.packetkit.exceptions.PacketKitException;

public abstract class AbstractPacketHeader implements PacketHeader {

	protected NetworkInterface networkinterface;


	/**
	 * Associate this packet with a network interface; many packet types have
	 * their fields autowired by the network interface.
	 */
	@Override
	public void associate(NetworkInterface networkinterface) {
		this.networkinterface = networkinterface;
	}


	/**
	 * Constructs the packet, creating the byte array that can be sent directly
	 * through the network interface.
	 */
	@Override
	public byte[] construct() throws PacketKitException {
		PacketSkeleton skeleton = PacketSkeletonRegistry.getInstance().retrieve(getClass());
		if (skeleton.isFixedSize()) {
			int size = skeleton.getSize().intValue();
			byte[] buffer = new byte[size];
			int index = 0;

			for (FragmentSlot slot : skeleton.getSlots()) {
				Field field = slot.field;
				boolean reflect = false;

				// apply values from the network interface
				switch (slot.type) {
				case IP4ADDRESS:
					addBytes(buffer, index, networkinterface.getIP4Address().getBytes(), slot.size);
					index += slot.size;
					break;

				case MACADDRESS:
					addBytes(buffer, index, networkinterface.getMACAddress().getBytes(), slot.size);
					index += slot.size;
					break;

				default:
					reflect = true;
				}

				if (!reflect)
					// look at the next slot
					continue;

				Object fieldvalue = null;
				try {
					fieldvalue = field.get(this);
				} catch (IllegalArgumentException e) {
					throw new InvalidFieldException(field, e);
				} catch (IllegalAccessException e) {
					throw new InvalidFieldException(field, e);
				}

				if (fieldvalue == null)
					throw new InvalidFieldException(field, new NullPointerException());

				// translate field values into bytes
				switch (slot.type) {
				case INT:
					Integer integer = (Integer) fieldvalue;
					addBytes(buffer, index, PacketUtilities.toByteArray(integer.intValue()), slot.size);
					index += slot.size;
					break;

				case SHORT:
					Short shortint = (Short) fieldvalue;
					addBytes(buffer, index, PacketUtilities.toByteArray(shortint.shortValue()), slot.size);
					index += slot.size;
					break;

				case PACKETFRAGMENT:
					PacketFragment fragment = (PacketFragment) fieldvalue;
					addBytes(buffer, index, fragment.getBytes(), slot.size);
					index += slot.size;
					break;

				default:
					throw new UnsupportedOperationException(slot.type + " is not yet supported");
				}
			}

			return buffer;
		}

		throw new UnsupportedOperationException("dynamic packet construction is not yet implemented");
	}


	private void addBytes(byte[] buffer, int index, byte[] bytes) {
		addBytes(buffer, index, bytes, bytes.length);
	}


	private void addBytes(byte[] buffer, int index, byte[] bytes, int size) {
		if (bytes.length < size) {
			for (int i = 0; i < size - bytes.length; i++)
				buffer[index++] = 0;
			size = bytes.length;
		}

		for (int i = bytes.length - size; i < bytes.length; i++)
			buffer[index++] = bytes[i];
	}


	/**
	 * Populates this packet by deconstructing a byte array into the various
	 * fields of a packet.
	 * 
	 * @param bytes
	 * @throws PacketKitException
	 */
	@SuppressWarnings("boxing")
	public void deconstruct(byte[] bytes) throws PacketKitException {
		PacketSkeleton skeleton = PacketSkeletonRegistry.getInstance().retrieve(getClass());

		if (skeleton.isFixedSize()) {
			int size = skeleton.getSize().intValue();
			int index = 0;

			for (FragmentSlot slot : skeleton.getSlots()) {
				Field field = slot.field;
				Object value;

				switch (slot.type) {
				case IP4ADDRESS:
					value = new IP4Address(extract(bytes, index, slot.size));
					break;

				case MACADDRESS:
					value = new MACAddress(extract(bytes, index, slot.size));
					break;

				case SHORT:
					value = PacketUtilities.shortFromByteArray(extract(bytes, index, slot.size));
					break;

				case INT:
					value = PacketUtilities.intFromByteArray(extract(bytes, index, slot.size));
					break;

				case PACKETFRAGMENT:
					PacketFragment fragment;
					try {
						fragment = (PacketFragment) slot.field.getType().newInstance();
					} catch (InstantiationException e) {
						throw new InvalidFieldException(field, e);
					} catch (IllegalAccessException e) {
						throw new InvalidFieldException(field, e);
					}

					fragment.fromBytes(bytes, index, slot.size);
					value = fragment;
					break;
				}
			}
		}

		throw new UnsupportedOperationException("dynamic packet deconstruction is not yet implemented");
	}


	private byte[] extract(byte[] buffer, int start, int length) {
		byte[] bytes = new byte[length];
		for (int i = 0; i < length; i++)
			bytes[i] = buffer[start + i];
		return bytes;
	}
}
