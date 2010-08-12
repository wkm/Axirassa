
package com.zanoccio.packetkit.headers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

import com.zanoccio.packetkit.IP4Address;
import com.zanoccio.packetkit.MACAddress;
import com.zanoccio.packetkit.PacketFragment;
import com.zanoccio.packetkit.PacketUtilities;
import com.zanoccio.packetkit.exceptions.CannotPopulateFromNetworkInterfaceException;
import com.zanoccio.packetkit.exceptions.InvalidFieldException;
import com.zanoccio.packetkit.exceptions.InvalidStaticFragmentTypeException;
import com.zanoccio.packetkit.exceptions.NotDeclareDynamicException;
import com.zanoccio.packetkit.exceptions.PacketKitException;
import com.zanoccio.packetkit.exceptions.SlotTakenException;
import com.zanoccio.packetkit.headers.annotations.DynamicSize;
import com.zanoccio.packetkit.headers.annotations.FixedSize;
import com.zanoccio.packetkit.headers.annotations.FromNetworkInterface;
import com.zanoccio.packetkit.headers.annotations.StaticFragment;

/**
 * Represents a cached form of a
 * 
 * @author wiktor
 * 
 */
public class PacketSkeleton {

	public static final int DEFAULT_FRAGMENT_SLOT = 0;
	public static final int DATA_FRAGMENT_SLOT = 100;
	public static final int CHECKSUM_FRAGMENT_SLOT = 200;

	public static HashMap<Class<? extends Object>, FragmentSlotType> VALIDPRIMITIVES;
	static {
		VALIDPRIMITIVES = new HashMap<Class<? extends Object>, FragmentSlotType>();
		VALIDPRIMITIVES.put(Integer.TYPE, FragmentSlotType.INT);
		VALIDPRIMITIVES.put(Short.TYPE, FragmentSlotType.SHORT);
	}

	private String name;
	private Class<? extends PacketHeader> klass;
	private boolean isfixedsize;
	private Integer size;

	private ArrayList<Field> fieldsfromnetworkinterface;
	private ArrayList<FragmentSlot> slotlist;


	public PacketSkeleton(Class<? extends PacketHeader> klass) throws PacketKitException {
		construct(klass);
	}


	@SuppressWarnings("unchecked")
	public void construct(Class<? extends PacketHeader> klass) throws PacketKitException {
		int slotindex = DEFAULT_FRAGMENT_SLOT;

		isfixedsize = true;

		this.name = klass.getName();
		this.klass = klass;

		Field[] fields = klass.getDeclaredFields();

		HashSet<Integer> slots = new HashSet<Integer>();
		PriorityQueue<FragmentSlot> queue = new PriorityQueue<FragmentSlot>();

		fieldsfromnetworkinterface = new ArrayList<Field>();

		//
		// Extract all annotated fields and sort by slot
		//
		int offset = 0;
		for (Field field : fields) {
			int fragmentslot;
			FragmentSlotType fragmenttype = null;
			FragmentSlot fragment = new FragmentSlot();

			// pull the StaticFragment annotation for this field
			StaticFragment annotation = field.getAnnotation(StaticFragment.class);

			// no StaticFragment annotation
			if (annotation == null)
				continue;

			// check whether a slot was specified
			if (annotation.slot() != StaticFragment.DEFAULT_SLOT)
				// use it
				fragmentslot = annotation.slot();
			else
				// otherwise, use the slot index
				fragmentslot = slotindex;

			// increment the slot index
			slotindex++;

			// check that the slot hasn't been taken
			if (slots.contains(slotindex))
				throw new SlotTakenException(field);

			// reserve the slot
			slots.add(slotindex);

			// get the type of the field
			Class<? extends Object> fieldtype = field.getType();

			// check whether this field is autowired
			if (field.isAnnotationPresent(FromNetworkInterface.class)) {
				if (fieldtype == IP4Address.class)
					fragmenttype = FragmentSlotType.IP4ADDRESS;
				else if (fieldtype == MACAddress.class)
					fragmenttype = FragmentSlotType.MACADDRESS;
				else
					throw new CannotPopulateFromNetworkInterfaceException(field, fieldtype);
			}

			// verify that the field's type is compatible with the framework
			if (!VALIDPRIMITIVES.containsKey(fieldtype)) {
				// if this type isn't being autowired
				if (fragmenttype != null)
					// and the type is being valid
					if (isValidType(fieldtype))
						fragmenttype = FragmentSlotType.PACKETFRAGMENT;
			}

			// ensure that the field is efficiently accessible
			if (!isFieldAccessible(field))
				throw new InvalidFieldException(field, "must be public or package scoped");

			// compute the size of this fragment
			if (annotation.size() == StaticFragment.DEFAULT_SIZE) {
				// check whether the field has a fixed size defines
				if (fieldtype.isAnnotationPresent(FixedSize.class)) {
					FixedSize sizeanno = fieldtype.getAnnotation(FixedSize.class);
					fragment.size = sizeanno.size();
				} else
					// the fragment is dynamic, and thus so is the packet
					fragment.size = FixedSize.DYNAMIC;
			} else {
				fragment.size = annotation.size();
			}

			// set the offset for this fragment
			fragment.offset = offset;
			fragment.slot = slotindex;

			// if this fragment had a size, increase the offset
			if (fragment.size != FixedSize.DYNAMIC)
				offset += fragment.size;

			// TODO final checks on the fragment (positive size, etc.)

			queue.add(fragment);
		}

		System.out.println("Queue: " + queue);

		//
		// Process each annotated field in slot-order
		//

		int packetsize = 0;
		// examine each field to construct this packet's skeleton
		for (Field field : fields) {
			FragmentSlotType slottype = null;

			boolean isfragmentfixed = true;
			int fragmentsize;

			StaticFragment annotation = field.getAnnotation(StaticFragment.class);

			// check whether this field is autowired by the network
			// interface
			Class<? extends Object> fieldtype = field.getType();
			if (field.isAnnotationPresent(FromNetworkInterface.class)) {
				fieldsfromnetworkinterface.add(field);

				if (fieldtype == IP4Address.class)
					slottype = FragmentSlotType.IP4ADDRESS;
				else if (fieldtype == MACAddress.class)
					slottype = FragmentSlotType.MACADDRESS;
				else
					throw new CannotPopulateFromNetworkInterfaceException(field, fieldtype);
			}

			// no StaticFragment annotation, move along...
			if (annotation == null)
				continue;

			// check that the slot is unused
			int slot = annotation.slot();
			if (slots.contains(slot))
				throw new SlotTakenException(field);

			// verify the field's type
			if (!VALIDPRIMITIVES.containsKey(fieldtype)) {
				Class<? extends Object>[] interfaces = fieldtype.getInterfaces();
				boolean validtype = false;
				for (Class<? extends Object> iface : interfaces)
					if (iface == PacketFragment.class) {
						validtype = true;
						break;
					}

				if (!validtype)
					throw new InvalidStaticFragmentTypeException(field);

				// if the slot type is already set it's being autowired
				if (slottype == null)
					slottype = FragmentSlotType.PACKETFRAGMENT;

			} else {
				slottype = VALIDPRIMITIVES.get(fieldtype);
			}

			// verify that the field is accessible
			int modifiers = field.getModifiers();
			if (Modifier.isPrivate(modifiers) || Modifier.isProtected(modifiers))
				throw new InvalidFieldException(field, " must be public or package scoped");

			if (annotation.size() < 0) {
				// if there is no explicit size
				if (fieldtype.isAnnotationPresent(FixedSize.class)) {
					// but the type has a @FixedSize annotation
					FixedSize sizeannotation = fieldtype.getAnnotation(FixedSize.class);
					fragmentsize = sizeannotation.size();
				} else {
					// too bad... dynamic
					isfixedsize = false;
					isfragmentfixed = false;
					fragmentsize = -1;
				}
			} else {
				fragmentsize = annotation.size();
			}

			//
			// now find the method for reconstructing the fragment from bytes
			//

			// create method signatures
			Class<? extends Object>[] signature = null;
			try {
				signature = new Class[] { Class.forName("[B"), Integer.TYPE, Integer.TYPE };
			} catch (ClassNotFoundException e1) {
				throw new PacketKitException(e1.toString());
			}

			Method constructor = null;
			try {
				switch (slottype) {
				case INT:
					constructor = PacketUtilities.class.getDeclaredMethod("intFromByteArray", signature);
					break;

				case SHORT:
					constructor = PacketUtilities.class.getDeclaredMethod("shortFromByteArray", signature);
					break;

				case IP4ADDRESS:
				case MACADDRESS:
				case PACKETFRAGMENT:
				default:
					constructor = fieldtype.getDeclaredMethod("fromBytes", signature);
					break;
				}

				// verify the constructor is static
				if (!Modifier.isStatic(constructor.getModifiers()))
					throw new InvalidFieldException(field, "the byte reconstructor method is not static: "
					        + constructor);

				// verify the constructor is accessible
				if (!Modifier.isPublic(constructor.getModifiers()))
					throw new InvalidFieldException(field, "the byte reconstructor method is not public " + constructor);
			} catch (SecurityException e) {
				throw new InvalidFieldException(field, e);
			} catch (NoSuchMethodException e) {
				throw new InvalidFieldException(field, e);
			}

			// construct the FragmentSlot
			FragmentSlot fragslot = new FragmentSlot();
			fragslot.type = slottype;
			fragslot.slot = slot;
			fragslot.size = fragmentsize;
			fragslot.offset = packetsize;
			fragslot.fixed = isfragmentfixed;
			fragslot.constructor = constructor;

			slots.add(Integer.valueOf(slot));
			queue.add(fragslot);

			if (fragmentsize > 0)
				packetsize += fragmentsize;
		}

		if (isFixedSize() == true)
			size = Integer.valueOf(packetsize);
		else {
			size = null;
			if (!klass.isAnnotationPresent(DynamicSize.class))
				throw new NotDeclareDynamicException(klass);
		}
	}


	/**
	 * Gives true if the given field is accessible (ie, either public or
	 * package-accessible)
	 */
	private boolean isFieldAccessible(Field field) {
		int modifiers = field.getModifiers();
		return !(Modifier.isPrivate(modifiers) || Modifier.isProtected(modifiers));
	}


	/**
	 * Gives true if the given class implements {@link PacketFragment}.
	 */
	private boolean isValidType(Class<? extends Object> fieldtype) {
		Class<? extends Object>[] interfaces = fieldtype.getInterfaces();
		boolean validtype = false;
		for (Class<? extends Object> iface : interfaces)
			if (iface == PacketFragment.class) {
				validtype = true;
				break;
			}

		return validtype;
	}


	public boolean isFixedSize() {
		return isfixedsize;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PacketSkeleton(").append(name).append('\n');
		sb.append("\t").append(klass.getCanonicalName()).append('\n');
		sb.append("\tfixed: " + isFixedSize()).append('\n');
		sb.append("\tsize: " + getSize()).append('\n');

		for (FragmentSlot slot : slotlist) {
			sb.append(String.format("\t%d:%s  %d  %15s  %15s  %s\n", slot.slot, slot.fixed ? "fixed " : "dynamic",
			                        slot.size, slot.type, slot.field.getName(), slot.field.getType().getCanonicalName()));
		}

		sb.append(")");
		return sb.toString();
	}


	public void deleteme(Integer size) {
		this.size = size;
	}


	public Integer getSize() {
		return size;
	}


	public List<FragmentSlot> getSlots() {
		return slotlist;
	}
}

enum FragmentSlotType {
	// general case
	PACKETFRAGMENT,
	CHECKSUM,
	DATA,

	// primitives
	INT,
	SHORT,

	// values from a network interface
	IP4ADDRESS,
	MACADDRESS;
}

/**
 * Lightweight container for an individual fragment of a packet.
 * 
 * @author wiktor
 */
class FragmentSlot implements Comparable<FragmentSlot> {
	public FragmentSlotType type;

	/**
	 * in what order this fragment should be filled relative to other fragments
	 * in this packet
	 */
	public int slot;

	/**
	 * the field this fragment is stored in within a {@link PacketFragment}
	 * container
	 */
	public Field field;

	/**
	 * the offset, in bytes, of this fragment from the start of a packet
	 */
	public int offset;

	/**
	 * the size, in bytes, of this fragment
	 */
	public int size;

	/**
	 * true if this fragment is fixed in size
	 */
	public boolean fixed;

	/**
	 * a reference to a Method with a signature of [byte[], int, int] ->
	 * [PacketFragment]
	 */
	public Method constructor;


	@Override
	public int compareTo(FragmentSlot other) {
		return this.slot - other.slot;
	}


	@Override
	public String toString() {
		return "FragmentSlot(" + slot + "\n\tfield: " + field + "\n\toffset:" + offset + "\n\tsize:" + size
		        + "\n\tfixed:" + fixed + "\n)";
	}
}