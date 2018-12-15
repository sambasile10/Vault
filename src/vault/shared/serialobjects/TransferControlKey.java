package vault.shared.serialobjects;

public class TransferControlKey {
	
	private long transferID;
	private ControlKey baseKey;
	
	public TransferControlKey(long transferID, ControlKey baseKey) {
		this.transferID = transferID;
		this.baseKey = baseKey;
	}
	
	public long getTransferID() {
		return this.transferID;
	}
	
	public ControlKey getKey() {
		return this.baseKey;
	}

}
