package vault.shared.serialobjects;

import java.util.LinkedList;

public class StoreFileSystemStructure {
	
	private StoreFolder root;
	
	public StoreFileSystemStructure(StoreFolder root) {
		this.root = root;
	}
	
	public LinkedList<StoreFile> asFiles() {
		return recursiveListStoreFiles(this.root);
	}
	
	private LinkedList<StoreFile> recursiveListStoreFiles(StoreFolder folder) {
		LinkedList<StoreFile> files = new LinkedList<StoreFile>();
		for(StoreObject storeObj : folder.getChildren()) {
			if(storeObj instanceof StoreFile) {
				files.add((StoreFile)storeObj);
			} else if(storeObj instanceof StoreFolder) {
				files.addAll(recursiveListStoreFiles((StoreFolder)storeObj));
			}
		}
		
		return files;
	}

}
