package vault.shared.serialobjects;

import java.util.Random;

public class StoreObject {
	
	private long uuid;
	
	public StoreObject() {
		this.uuid = new Random().nextLong();
	}
	
	public long getUUID() {
		return this.uuid;
	}

}
