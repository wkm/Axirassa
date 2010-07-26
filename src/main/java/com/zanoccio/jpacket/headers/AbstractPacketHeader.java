
package com.zanoccio.jpacket.headers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

import com.zanoccio.jpacket.IP4Address;
import com.zanoccio.jpacket.MACAddress;
import com.zanoccio.jpacket.NetworkInterface;
import com.zanoccio.jpacket.PacketFragment;
import com.zanoccio.jpacket.PacketUtilities;
import com.zanoccio.jpacket.exceptions.CannotPopulateFromNetworkInterfaceException;
import com.zanoccio.jpacket.exceptions.CouldNotPopulateException;
import com.zanoccio.jpacket.exceptions.InvalidFieldException;
import com.zanoccio.jpacket.exceptions.InvalidFieldSizeException;
import com.zanoccio.jpacket.exceptions.InvalidStaticFragmentTypeException;
import com.zanoccio.jpacket.exceptions.JPacketException;
import com.zanoccio.jpacket.exceptions.NoNetworkInterfaceException;
import com.zanoccio.jpacket.exceptions.NullFieldException;
import com.zanoccio.jpacket.exceptions.SlotTakenException;
import com.zanoccio.jpacket.headers.annotations.FromNetworkInterface;
import com.zanoccio.jpacket.headers.annotations.StaticFragment;

public abstract class AbstractPacketHeader implements PacketHeader {

	public static HashSet<Class<? extends Object>> VALIDPRIMITIVES;
	static {
		VALIDPRIMITIVES = new HashSet<Class<? extends Object>>();
		VALIDPRIMITIVES.add(Integer.TYPE);
	}





	class FragmentSlot implements Comparable<FragmentSlot> {
		int slot;
		Field field;
		boolean fromnetworkinterface;
		int size;


		public FragmentSlot(int slot, Field field) {
			this(slot, field, false);
		}


		public FragmentSlot(int slot, Field field, boolean fromnetworkinterface) {
			this.slot = slot;
			this.field = field;
			this.fromnetworkinterface = fromnetworkinterface;
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


	public void associate(NetworkInterface networkinterface) {
		this.networkinterface = networkinterface;
	}


	@Override
	public List<Byte> construct() throws JPacketException {

		//
		// Construct the Packet Skeleton
		// TODO this should be done only once for a particular class
		//

		Field[] fields = getClass().getDeclaredFields();
		System.out.println(fields.length);
		HashSet<Integer> slots = new HashSet<Integer>();
		PriorityQueue<FragmentSlot> queue = new PriorityQueue<FragmentSlot>();

		int packetsize = 0;

		// check all fields for annotations
		for (Field field : fields) {
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

			// add the field's width to the packetsize
			packetsize += annotation.size();

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

						if (!accessible)
							field.setAccessible(false);
					} catch (JPacketException e) {
						// throw these directly
						throw e;
					} catch (Exception e) {
						// wrap all other exceptions
						throw new InvalidFieldException(field, e);
					}
				} else
					packetsize += annotation.size();

			} else {
				// if it is a primitive type a width must be specified
				if (annotation.size() < 0)
					throw new InvalidFieldSizeException(field, annotation.size());

				packetsize += annotation.size();
			}

			// shove this field onto the queue
			slots.add(Integer.valueOf(slot));
			queue.add(new FragmentSlot(slot, field, fromnetworkinterface));
		}

		//
		// Build the Actual Packet
		//
		System.out.println(queue);
		System.out.println("Packet Size: " + packetsize);

		ArrayList<Byte> list = new ArrayList<Byte>(packetsize);
		for (FragmentSlot slot : queue) {
			Field field = slot.field;
			try {
				boolean accessible = field.isAccessible();
				if (!accessible)
					field.setAccessible(true);

				Object obj = field.get(this);
				byte[] bytes = getBytes(obj);
				addBytes(list, bytes);

				System.out.println(field.getName() + ":");
				System.out.println("\t" + PacketUtilities.toHexDumpFragment(bytes));

				if (!accessible)
					field.setAccessible(false);
			} catch (Exception e) {
				throw new InvalidFieldException(field, e);
			}
		}

		byte[] bytes = new byte[list.size()];
		for (int i = 0; i < bytes.length; i++)
			bytes[i] = list.get(i).byteValue();

		System.out.println("Hexdump: ");
		System.out.println(PacketUtilities.toHexDump(bytes));
		System.out.println("======================================");

		return list;
	}


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
