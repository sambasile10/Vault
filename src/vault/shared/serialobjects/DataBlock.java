package vault.shared.serialobjects;

public class DataBlock {
	
	private byte[] data;
	private long transferID;
	private long sequencePosition; // (total bytes transfered / Config.DATA_BLOCK_SIZE), +1 for every block
	private boolean isFinal;
	
	public DataBlock(long transferID, long sequencePosition, byte[] data, boolean isFinal) {
		this.transferID = transferID;
		this.data = data;
		this.sequencePosition = sequencePosition;
		this.isFinal = isFinal;
	}
	
	public void markAsFinalBlock() {
		this.isFinal = true;
	}
	
	public boolean isFinalBlock() {
		return this.isFinal;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public long getTransferID() {
		return transferID;
	}
	
	public long getSequencePosition() {
		return sequencePosition;
	}

}
