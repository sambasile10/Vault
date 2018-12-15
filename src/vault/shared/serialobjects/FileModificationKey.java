package vault.shared.serialobjects;

public enum FileModificationKey {
	
	NEW_FILE(0, "NEW FILE"), MODIFIED(1, "CHECKSUM MISMATCH"), DELETED_FILE(2, "DELETED FILE");
	
	String type;
	int id;
	
	FileModificationKey(int id, String type) {
		this.id = id;
		this.type = type;
	}
	
	int getID() {
		return this.id;
	}
	
	String getType() {
		return this.type;
	}

}
