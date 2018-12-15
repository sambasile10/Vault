package vault.shared.serialobjects;

import java.io.File;

import vault.client.ClientConfig;
import vault.shared.Crypto;

public class StoreFile extends StoreObject {
	
	private String name, localPath, checksum;
	private long modificationDate;
	
	public StoreFile() {}
	
	public StoreFile(String name, String checksum) {
		this.name = name;
		this.checksum = checksum;
	}
	
	public StoreFile(String name, String localPath, String checksum, long modificationDate) {
		this.name = name;
		this.localPath = localPath;
		this.checksum = checksum;
		this.modificationDate = modificationDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setModificationDate(long date) {
		this.modificationDate = date;
	}
	
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	
	public long getModificationDate() {
		return this.modificationDate;
	}
	
	public String getLocalPath() {
		return this.localPath;
	}
	
	public static StoreFile buildFromLocalFile(File file) {
		String relative = ClientConfig.VAULT_DIR.toURI().relativize(file.toURI()).getPath();
		return new StoreFile(file.getName(), relative, Crypto.hash(file), file.lastModified());
	}
	
	public static boolean equalFileSystemStruct(StoreFile sf1, StoreFile sf2) {
		return (sf1.name.equals(sf2.name) && sf1.localPath.equals(sf1.localPath));
	}
	
	public static StoreFile getMostRecent(StoreFile sf1, StoreFile sf2) {
		if(sf1.modificationDate > sf2.modificationDate) {
			return sf1;
		} else if(sf1.modificationDate < sf2.modificationDate) {
			return sf2;
		} else if(sf1.modificationDate == sf2.modificationDate) {
			return sf1;
		} else {
			return null;
		}
	}
	
	public File getAbsoluteLocalFile() {
		return new File(ClientConfig.VAULT_DIR + "/" + this.localPath);
	}
	
	@Override
	public boolean equals(Object object) {
		if(object == this) { return true; }
		if(object == null || object.getClass() != this.getClass()) { return false; }
		StoreFile storeFile = (StoreFile) object;
		
		return (storeFile.name.equals(this.name) && storeFile.checksum.equals(this.checksum) && 
				storeFile.localPath.equals(this.localPath) && storeFile.modificationDate == this.modificationDate);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31; 
		int result = 1; 
		result = prime * result + ((name == null) ? 0 : name.hashCode()); 
		result = (int) (prime * result + (this.modificationDate % prime)); 
		result = prime * result + ((checksum == null) ? 0 : checksum.hashCode()); 
		result = prime * result + ((localPath == null) ? 0 : localPath.hashCode()); 
		
		return result;
	}
	
	@Override
	public String toString() {
		return new String(this.name + " :: " + this.localPath + " // checksum: " + this.checksum + ", timestamp: " + this.modificationDate);
	}

}
