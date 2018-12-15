package vault.shared.serialobjects;

public enum TransferDirection {
	
	PUSH(0, "PUSH"), PULL(1, "PULL");
	
	String type;
	int mode;
	
	TransferDirection(int mode, String type) {
		this.mode = mode;
		this.type = type;
	}
	
	int getMode() {
		return this.mode;
	}
	
	String getType() {
		return this.type;
	}

}
