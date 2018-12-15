package vault.shared;

import vault.client.Log;
import vault.client.LogType;

public class ManagedThread extends Thread {
	
	/*
	 * All threads must extend this class in order to use the ThreadManager
	 * It simply extends the java Thread object and adds a custom short ID for ThreadManager
	 * to serialize and manage threads with
	 */
	
	private short threadID;
	private String className;
	
	public ManagedThread(String className) {
		//Call the default thread constructor
		super();
		
		//Generate a random identifier (w/o collision), and set name
		this.threadID = ThreadManager.getRandomIdentifier();
		this.className = className;
		
		//Register this thread with the ThreadManager
		ThreadManager.register(this);
		Log.write(LogType.MT, "Thread registered // class=" + className + ", id=" + threadID);
	}
	
	//Returns the ID of this thread
	public short getThreadIdentifier() {
		return this.threadID;
	}
	
	//Unregister from thread manager
	public void unregister() {
		ThreadManager.unregister(this);
	}

}
