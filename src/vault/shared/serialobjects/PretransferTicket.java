package vault.shared.serialobjects;

public class PretransferTicket {
	
	private StoreFile storeFile;
	private long transferID;
	
	public PretransferTicket() {}
	
	public static PretransferTicket buildFor(StoreFile storeFile) {
		PretransferTicket ptt = new PretransferTicket();
		ptt.setStoreFile(storeFile);
		ptt.setTransferID((long) Integer.parseInt(storeFile.getChecksum(), 16));
		return ptt;
	}

	public StoreFile getStoreFile() {
		return storeFile;
	}

	public void setStoreFile(StoreFile storeFile) {
		this.storeFile = storeFile;
	}

	public long getTransferID() {
		return transferID;
	}

	public void setTransferID(long transferID) {
		this.transferID = transferID;
	}

}
