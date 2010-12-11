
package packetkit.headers;

import java.util.HashMap;

import packetkit.exceptions.PacketKitException;


/**
 * A lightweight object that stores precomputed {@link PacketSkeleton}s for
 * classes.
 * 
 * @author wiktor
 */
public class PacketSkeletonRegistry {
	private static final PacketSkeletonRegistry INSTANCE = new PacketSkeletonRegistry();


	public static PacketSkeletonRegistry getInstance() {
		return INSTANCE;
	}


	// pre-skeletize all included classes
	static {
		try {
			PacketSkeletonRegistry.getInstance().register(ARPHeader.class);
			PacketSkeletonRegistry.getInstance().register(EthernetHeader.class);
		} catch (PacketKitException e) {
			System.out.println("Exception.");
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}
	}

	private final HashMap<Class<? extends PacketHeader>, PacketSkeleton> skeletonmap;


	private PacketSkeletonRegistry() {
		skeletonmap = new HashMap<Class<? extends PacketHeader>, PacketSkeleton>();
	}


	public void register(Class<? extends PacketHeader> packet) throws PacketKitException {
		PacketSkeleton skeleton = new PacketSkeleton(packet);
		synchronized (skeletonmap) {
			skeletonmap.put(packet, skeleton);
		}
	}


	public boolean isRegistered(Class<? extends PacketHeader> packet) {
		synchronized (skeletonmap) {
			return skeletonmap.containsKey(packet);
		}
	}


	public PacketSkeleton retrieve(Class<? extends PacketHeader> packet) throws PacketKitException {
		PacketSkeleton skeleton = null;
		synchronized (skeletonmap) {
			skeleton = skeletonmap.get(packet);
		}

		if (skeleton == null)
			skeleton = new PacketSkeleton(packet);

		synchronized (skeletonmap) {
			skeletonmap.put(packet, skeleton);
		}

		return skeleton;
	}
}
