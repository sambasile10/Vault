package vault.shared.serialobjects;

public class ModifiedFileTicket {
	
	private StoreFile storeFile;
	private TransferDirection transferDirection;
	private FileModificationKey modificationType;
	
	public ModifiedFileTicket(StoreFile storeFile, TransferDirection transferDirection, FileModificationKey modificationType) {
		this.storeFile = storeFile;
		this.transferDirection = transferDirection;
		this.modificationType = modificationType;
	}
	
	public StoreFile getStoreFile() {
		return this.storeFile;
	}
	
	public TransferDirection getTransferDirection() {
		return this.transferDirection;
	}
	
	public FileModificationKey getModificationType() {
		return this.modificationType;
	}
	
	@Override
	public String toString() {
		return new String("[" + modificationType.getType() + " // " + transferDirection.getType() + "] " + storeFile.toString());
	}

}