package vault.shared;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import vault.client.Log;
import vault.client.LogType;

public class ThreadManager {
	
	private static HashMap<Short, ManagedThread> threads;
	private static Random random;
	
	public static void init() {
		threads = new HashMap<Short, ManagedThread>();
		random = new Random();
		Log.write(LogType.MT, "ThreadManager initialized!");
	}
	
	public static void register(ManagedThread threadObj) {
		short uuid = getRandomIdentifier();
		threads.put(uuid, threadObj);
	}
	
	public static ManagedThread getThread(short uuid) {
		return threads.get(uuid);
	}
	
	public static short getIdentifier(ManagedThread threadObj) {
		return threadObj.getThreadIdentifier();
	}
	
	public static void unregister(short uuid) {
		threads.remove(uuid);
		Log.write(LogType.MT, "Unreigstered thread // uuid=" + uuid);
	}
	
	public static void unregister(ManagedThread thread) {
		threads.remove(thread.getThreadIdentifier());
		Log.write(LogType.MT, "Unregistered thread // " + thread.getId());
	}
	
	public static short getRandomIdentifier() {
		boolean generate = true;
		Set<Short> usedIDs = threads.keySet();
		short rnd = 0;
		
		while(generate) {
			rnd = (short) random.nextInt(1000);
			if(!usedIDs.contains(rnd)) {
				generate = false;
			}
		}
		
		return rnd;
	}

}
