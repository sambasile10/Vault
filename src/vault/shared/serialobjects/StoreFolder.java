package vault.shared.serialobjects;

import java.util.LinkedList;

public class StoreFolder extends StoreObject {
	
	private String name;
	private LinkedList<StoreObject> children;
	
	public StoreFolder(String name, LinkedList<StoreObject> children) {
		this.name = name;
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LinkedList<StoreObject> getChildren() {
		return children;
	}

	public void setChildren(LinkedList<StoreObject> children) {
		this.children = children;
	}

}
