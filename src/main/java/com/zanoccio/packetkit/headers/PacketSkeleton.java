
package com.zanoccio.packetkit.headers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

import com.zanoccio.packetkit.PacketFragment;
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
	public static HashSet<Class<? extends Object>> VALIDPRIMITIVES;
	static {
		VALIDPRIMITIVES = new HashSet<Class<? extends Object>>();
		VALIDPRIMITIVES.add(Integer.TYPE);
	}

	public static HashMap<Class<? extends Object>, PacketSkeleton> SKELETON_CACHE;
	static {
		SKELETON_CACHE = new HashMap<Class<? extends Object>, PacketSkeleton>();
	}

	private String name;
	private Class<? extends PacketHeader> klass;
	private boolean isfixedsize;
	private Integer size;

	private ArrayList<Field> fieldsfromnetworkinterface;
	private PriorityQueue<FragmentSlot> queue;


	public PacketSkeleton(Class<? extends PacketHeader> klass) throws PacketKitException {
		construct(klass);
	}


	public void construct(Class<? extends PacketHeader> klass) throws PacketKitException {
		isfixedsize = true;

		this.name = klass.getName();
		this.klass = klass;

		Field[] fields = klass.getDeclaredFields();

		HashSet<Integer> slots = new HashSet<Integer>();
		queue = new PriorityQueue<FragmentSlot>();
		fieldsfromnetworkinterface = new ArrayList<Field>();

		int packetsize = 0;
		for (Field field : fields) {
			boolean isfragmentfixed = true;
			int fragmentsize;

			StaticFragment annotation = field.getAnnotation(StaticFragment.class);

			Class<? extends Object> fieldtype = field.getType();
			if (field.isAnnotationPresent(FromNetworkInterface.class))
				fieldsfromnetworkinterface.add(field);

			// no StaticFragment annotation, move along...
			if (annotation == null)
				continue;

			// check that the slot is unused
			int slot = annotation.slot();
			if (slots.contains(slot))
				throw new SlotTakenException(field);

			// verify the field's type
			if (!VALIDPRIMITIVES.contains(field.getType())) {
				Class<? extends Object>[] interfaces = field.getType().getInterfaces();
				boolean validtype = false;
				for (Class<? extends Object> iface : interfaces)
					if (iface == PacketFragment.class) {
						validtype = true;
						break;
					}

				if (!validtype)
					throw new InvalidStaticFragmentTypeException(field);
			}

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

			slots.add(Integer.valueOf(slot));
			queue.add(new FragmentSlot(slot, field, fragmentsize, isfragmentfixed));

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

		synchronized (SKELETON_CACHE) {
			SKELETON_CACHE.put(klass, this);
		}
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

		for (FragmentSlot slot : queue) {
			sb.append('\t').append(slot.slot).append(':');
			sb.append(slot.fixed).append("  ");
			sb.append(slot.size);
			sb.append("  ").append(slot.field.getName()).append("  ");
			sb.append(slot.field.getType().getCanonicalName());
			sb.append('\n');
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
}