
package packetkit.headers;

import static packetkit.PacketUtilities.extractBytes;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import packetkit.ByteParser;
import packetkit.IP4Address;
import packetkit.MACAddress;
import packetkit.NetworkInterface;
import packetkit.PacketFragment;
import packetkit.PacketUtilities;
import packetkit.exceptions.CouldNotPopulateException;
import packetkit.exceptions.DeconstructionException;
import packetkit.exceptions.InvalidFieldException;
import packetkit.exceptions.PacketKitException;
import packetkit.exceptions.PacketValidationException;
import packetkit.headers.annotations.StaticFragment;


public abstract class AbstractPacketHeader implements PacketHeader {

	protected NetworkInterface networkinterface;


	@Override
	public void associate(NetworkInterface networkinterface) {
		this.networkinterface = networkinterface;
	}


	@Override
	public byte[] construct() throws PacketKitException {
		PacketSkeleton skeleton = PacketSkeletonRegistry.getInstance().retrieve(getClass());

		// start with the base packet size
		int size = skeleton.getSize().intValue();

		// compute the size of the dynamic fields
		for (FragmentSlot fragment : skeleton.getDynamicSlots()) {
			Object obj;
			try {
				obj = fragment.sizemethod.invoke(null, fragment.field.get(this));
			} catch (IllegalArgumentException e) {
				throw new CouldNotPopulateException(this, e);
			} catch (IllegalAccessException e) {
				throw new CouldNotPopulateException(this, e);
			} catch (InvocationTargetException e) {
				throw new CouldNotPopulateException(this, e);
			}

			size += (Integer) obj;
		}

		byte[] buffer = new byte[size];

		for (FragmentSlot slot : skeleton.getLogicalSlotOrder()) {
			Field field = slot.field;

			Object fieldvalue = null;
			try {
				fieldvalue = field.get(this);
			} catch (IllegalArgumentException e) {
				throw new InvalidFieldException(field, e);
			} catch (IllegalAccessException e) {
				throw new InvalidFieldException(field, e);
			}

			if (fieldvalue == null)
				// apply values from the network interface
				switch (slot.type) {
				case IP4ADDRESS:
					addBytes(buffer, slot.offset, networkinterface.getIP4Address().getBytes(), slot.size);
					break;

				case MACADDRESS:
					addBytes(buffer, slot.offset, networkinterface.getMACAddress().getBytes(), slot.size);
					break;

				case CHECKSUM:
					addBytes(buffer, slot.offset, ChecksumMethod.ONESCOMPLEMENT.compute(buffer, 0, size), slot.size);
					break;

				default:
					throw new InvalidFieldException(field, new NullPointerException());
				}

			else
				// translate field values into bytes
				switch (slot.type) {
				case INT:
					Integer integer = (Integer) fieldvalue;
					addBytes(buffer, slot.offset, PacketUtilities.toByteArray(integer.intValue()), slot.size);
					break;

				case SHORT:
					Short shortint = (Short) fieldvalue;
					addBytes(buffer, slot.offset, PacketUtilities.toByteArray(shortint.shortValue()), slot.size);
					break;

				case PACKETFRAGMENT:
					PacketFragment fragment = (PacketFragment) fieldvalue;
					addBytes(buffer, slot.offset, fragment.getBytes(), slot.size);
					break;

				case IP4ADDRESS:
					IP4Address ip4 = (IP4Address) fieldvalue;
					addBytes(buffer, slot.offset, ip4.getBytes());
					break;

				case MACADDRESS:
					MACAddress mac = (MACAddress) fieldvalue;
					addBytes(buffer, slot.offset, mac.getBytes());
					break;

				case DATA:
				case CHECKSUM:
					byte[] bytes = (byte[]) fieldvalue;
					addBytes(buffer, slot.offset, bytes);
					break;

				default:
					throw new UnsupportedOperationException(slot.type + " is not yet supported");
				}
		}

		return buffer;
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


	@Override
	public boolean deconstruct(byte[] bytes) throws PacketKitException {
		return deconstruct(new ByteParser(bytes, 0), bytes.length);
	}


	/**
	 * Populates this packet by deconstructing a byte array into the various
	 * fields of a packet.
	 * 
	 * @param container
	 * @throws PacketKitException
	 */
	@Override
	@SuppressWarnings("boxing")
	public boolean deconstruct(ByteParser container, int length) throws PacketKitException {
		PacketSkeleton skeleton = PacketSkeletonRegistry.getInstance().retrieve(getClass());

		if (skeleton.isFixedSize()) {
			int index = container.cursor;

			for (FragmentSlot slot : skeleton.getPhysicalSlotOrder()) {
				Field field = slot.field;
				Object value;

				try {
					if (slot.constructor != null) {
						value = slot.constructor.invoke(null, container.bytes, index, slot.size);
					} else {
						if (slot.size == StaticFragment.DEFAULT_SIZE)
							// pull whatever bytes remain
							value = extractBytes(container.bytes, index);
						else
							value = extractBytes(container.bytes, index, slot.size);
					}

					if (value == null)
						throw new DeconstructionException(container.bytes, field, value);

					field.set(this, value);
				} catch (RuntimeException e) {
					throw new DeconstructionException(container.bytes, e);
				} catch (IllegalAccessException e) {
					throw new DeconstructionException(container.bytes, e);
				} catch (InvocationTargetException e) {
					throw new DeconstructionException(container.bytes, e);
				}

				index += slot.size;

				if (index > container.cursor + length)
					return false;
			}

			container.cursor = index;

			// validate the packet
			if (!validate())
				throw new PacketValidationException(container.bytes, this);

			return true;
		}

		throw new UnsupportedOperationException("dynamic packet deconstruction is not yet implemented");
	}


	@Override
	public boolean validate() {
		return true;
	}
}
