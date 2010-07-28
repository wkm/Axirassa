
package com.zanoccio.packetkit.headers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

import com.zanoccio.packetkit.IP4Address;
import com.zanoccio.packetkit.MACAddress;
import com.zanoccio.packetkit.NetworkInterface;
import com.zanoccio.packetkit.PacketFragment;
import com.zanoccio.packetkit.PacketUtilities;
import com.zanoccio.packetkit.exceptions.CannotPopulateFromNetworkInterfaceException;
import com.zanoccio.packetkit.exceptions.CouldNotPopulateException;
import com.zanoccio.packetkit.exceptions.InvalidFieldException;
import com.zanoccio.packetkit.exceptions.InvalidFieldSizeException;
import com.zanoccio.packetkit.exceptions.InvalidStaticFragmentTypeException;
import com.zanoccio.packetkit.exceptions.NoNetworkInterfaceException;
import com.zanoccio.packetkit.exceptions.NullFieldException;
import com.zanoccio.packetkit.exceptions.PacketKitException;
import com.zanoccio.packetkit.exceptions.SlotTakenException;
import com.zanoccio.packetkit.headers.annotations.FromNetworkInterface;
import com.zanoccio.packetkit.headers.annotations.StaticFragment;

public abstract class AbstractPacketHeader implements PacketHeader {

	public static HashSet<Class<? extends Object>> VALIDPRIMITIVES;
	static {
		VALIDPRIMITIVES = new HashSet<Class<? extends Object>>();
		VALIDPRIMITIVES.add(Integer.TYPE);
	}





	class FragmentSlot implements Comparable<FragmentSlot> {
		int slot;
		Field field;
		int size;
		boolean fixed;


		public FragmentSlot(int slot, Field field, int size) {
			this(slot, field, size, false);
		}


		public FragmentSlot(int slot, Field field, int size, boolean fixed) {
			this.slot = slot;
			this.field = field;
			this.size = size;
			this.fixed = fixed;
		}


		@Override
		public int compareTo(FragmentSlot other) {
			return this.slot - other.slot;
		}


		@Override
		public String toString() {
			return "FragmentSlot(slot=" + slot + "; field=" + field.getName() + ")";
		}
	}


	protected NetworkInterface networkinterface;


	@Override
	public void associate(NetworkInterface networkinterface) {
		this.networkinterface = networkinterface;
	}


	@Override
	public List<Byte> construct() throws PacketKitException {

		//
		// Construct the Packet Skeleton
		// TODO this should be done only once for a particular class
		//

		Field[] fields = getClass().getDeclaredFields();
		HashSet<Integer> slots = new HashSet<Integer>();
		PriorityQueue<FragmentSlot> queue = new PriorityQueue<FragmentSlot>();

		int packetsize = 0;

		// check all fields for annotations
		for (Field field : fields) {
			int fragmentsize;
			StaticFragment annotation = field.getAnnotation(StaticFragment.class);

			Class<? extends Object> fieldtype = field.getType();
			// do we populate this from the network interface?
			boolean fromnetworkinterface = false;
			if (field.isAnnotationPresent(FromNetworkInterface.class)) {
				if (networkinterface == null)
					throw new NoNetworkInterfaceException(this);

				fromnetworkinterface = true;

				boolean accessible = field.isAccessible();
				if (!accessible)
					field.setAccessible(true);
				try {
					if (fieldtype == IP4Address.class)
						field.set(this, networkinterface.getIP4Address());
					else if (fieldtype == MACAddress.class)
						field.set(this, networkinterface.getMACAddress());
					else
						throw new CannotPopulateFromNetworkInterfaceException(field, fieldtype);
				} catch (Exception e) {
					throw new CouldNotPopulateException(this, e);
				} finally {
					if (!accessible)
						field.setAccessible(false);
				}
			}

			// no StaticFragment annotation, move along...
			if (annotation == null)
				continue;

			// check that the slot is unused
			int slot = annotation.slot();
			if (slots.contains(slots))
				throw new SlotTakenException(field);

			// if the field type isn't one of the accepted primitives
			if (!VALIDPRIMITIVES.contains(field.getType())) {

				// verify the field's class implements PacketFragment
				Class<? extends Object>[] interfaces = field.getType().getInterfaces();
				boolean validtype = false;
				for (Class<? extends Object> iface : interfaces)
					if (iface == PacketFragment.class) {
						validtype = true;
						break;
					}

				// is at least one of the interfaces PacketFragment?
				if (!validtype)
					throw new InvalidStaticFragmentTypeException(field);

				if (annotation.size() < 0) {
					// ask the PacketFragment for its width
					try {
						boolean accessible = field.isAccessible();
						if (!accessible)
							field.setAccessible(true);

						PacketFragment fragment = (PacketFragment) field.get(this);
						if (fragment == null)
							throw new NullFieldException(field);

						packetsize += fragment.size();
						fragmentsize = fragment.size();

						if (!accessible)
							field.setAccessible(false);
					} catch (PacketKitException e) {
						// throw these directly
						throw e;
					} catch (Exception e) {
						// wrap all other exceptions
						throw new InvalidFieldException(field, e);
					}
				} else {
					fragmentsize = annotation.size();
					packetsize += annotation.size();
				}

			} else {
				// if it is a primitive type a width must be specified
				if (annotation.size() < 0)
					throw new InvalidFieldSizeException(field, annotation.size());

				packetsize += annotation.size();
				fragmentsize = annotation.size();
			}

			boolean isfixed = annotation.fixed();

			// shove this field onto the queue
			slots.add(Integer.valueOf(slot));
			queue.add(new FragmentSlot(slot, field, fragmentsize, isfixed));
		}

		//
		// Build the Actual Packet
		//

		ArrayList<Byte> list = new ArrayList<Byte>(packetsize);
		for (FragmentSlot slot : queue) {
			Field field = slot.field;
			try {
				boolean accessible = field.isAccessible();
				if (!accessible)
					field.setAccessible(true);

				Object obj = field.get(this);
				byte[] bytes = getBytes(obj);
				if (slot.fixed)
					addBytes(list, bytes, slot.size);
				else
					addBytes(list, bytes);

				if (!accessible)
					field.setAccessible(false);
			} catch (Exception e) {
				throw new InvalidFieldException(field, e);
			}
		}

		byte[] bytes = new byte[list.size()];
		for (int i = 0; i < bytes.length; i++)
			bytes[i] = list.get(i).byteValue();

		return list;
	}


	@SuppressWarnings("boxing")
	private void addBytes(List<Byte> list, byte[] bytes, int size) {
		if (bytes.length < size) {
			for (int i = 0; i < size - bytes.length; i++)
				list.add((byte) 0);
			addBytes(list, bytes);
		} else
			for (int i = bytes.length - size; i < bytes.length; i++)
				list.add(bytes[i]);
	}


	@SuppressWarnings("boxing")
	private void addBytes(List<Byte> list, byte[] bytes) {
		for (byte b : bytes)
			list.add(b);
	}


	private byte[] getBytes(Object obj) {
		if (obj instanceof PacketFragment) {
			PacketFragment fragment = (PacketFragment) obj;
			return fragment.getBytes();
		} else if (obj instanceof Integer) {
			Integer integer = (Integer) obj;
			return PacketUtilities.toByteArray(integer.intValue());
		} else if (obj instanceof Short) {
			Short shortval = (Short) obj;
			return PacketUtilities.toByteArray(shortval.shortValue());
		} else
			return new byte[] {};
	}
}
