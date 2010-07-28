
package com.zanoccio.packetkit.headers;

import java.lang.reflect.Field;
import java.util.List;

import com.zanoccio.packetkit.NetworkInterface;
import com.zanoccio.packetkit.PacketFragment;
import com.zanoccio.packetkit.PacketUtilities;
import com.zanoccio.packetkit.exceptions.PacketKitException;

public abstract class AbstractPacketHeader implements PacketHeader {

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


	//
	// Construct
	//

	@Override
	public List<Byte> construct() throws PacketKitException {
		PacketSkeleton skeleton = PacketSkeleton.SKELETON_CACHE.get(getClass());
		if (skeleton == null)
			skeleton = new PacketSkeleton(getClass());

		if (skeleton.isFixedSize()) {
			int size = skeleton.getSize().intValue();
			byte[] buffer = new byte[size];
			int index = 0;

		}

		// //
		// // Build the Actual Packet
		// //
		//
		// ArrayList<Byte> list = new ArrayList<Byte>(packetsize);
		// for (FragmentSlot slot : queue) {
		// Field field = slot.field;
		// try {
		// boolean accessible = field.isAccessible();
		// if (!accessible)
		// field.setAccessible(true);
		//
		// Object obj = field.get(this);
		// byte[] bytes = getBytes(obj);
		// if (slot.fixed)
		// addBytes(list, bytes, slot.size);
		// else
		// addBytes(list, bytes);
		//
		// if (!accessible)
		// field.setAccessible(false);
		// } catch (Exception e) {
		// throw new InvalidFieldException(field, e);
		// }
		// }
		//
		// byte[] bytes = new byte[list.size()];
		// for (int i = 0; i < bytes.length; i++)
		// bytes[i] = list.get(i).byteValue();
		//
		// return list;

		return null;
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


	//
	// Reconstruct
	//

	public void reconstruct(byte[] bytes) {

	}
}
