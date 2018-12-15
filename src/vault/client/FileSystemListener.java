package vault.client;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import vault.shared.ManagedThread;
import vault.shared.serialobjects.*;

public class FileSystemListener extends ManagedThread {
	
	private StoreFileSystemStructure currentLocalFS, currentRemoteFS;
	private boolean syncLoop;
	
	public FileSystemListener() {
		super("vaultcli.FileSystemListener");
	}
	
	@Override
	public void start() {
		syncLoop = true;
		while(syncLoop) {
			ClientGlobal.getClient().requestRemoteFileSystem();
			try { sleep(15 * 1000); } catch (Exception e) {}
			sync();
			try { sleep(ClientConfig.FS_REFRESH_SECONDS * 1000); } catch (Exception e) {}
		}
	}
	
	private synchronized boolean sync() {
		Log.write(LogType.MT, "/// Starting sync operation ///");
		Log.write(LogType.IO, "Serializing local filesystem...");
		
		//Serialize local file system
		this.currentLocalFS = serializeFS();
		Log.write(LogType.IO, "Local file system serialized at " + currentLocalFS.asFiles().size() + " objects long!");
		
		//Compare local and remote file system
		Log.write(LogType.IO, "Comparing local and remote file system structure...");
		LinkedList<ModifiedFileTicket> fsModifications = queryChanges();
		Log.write(LogType.IO, "" + fsModifications.size() + " modifications queued for syncing!");
		for(ModifiedFileTicket mft : fsModifications) {
			Log.write(LogType.IO, mft.toString());
			
			//Open UL/DL threads
			if(mft.getTransferDirection() == TransferDirection.PUSH) {
				//upload file to server
				ClientTransferManager.createUploadProcess(mft);
			} else if(mft.getTransferDirection() == TransferDirection.PULL) {
				//download file from server
				ClientTransferManager.createDownloadProcess(mft);
			}
		}
		
		return true;
	}
	
	private LinkedList<ModifiedFileTicket> queryChanges() {
		LinkedList<ModifiedFileTicket> changes = new LinkedList<ModifiedFileTicket>();
		LinkedList<StoreFile> localFS = this.currentLocalFS.asFiles();
		LinkedList<StoreFile> remoteFS = this.currentRemoteFS.asFiles();
		LinkedList<StoreFile> searchList, compList;
		
		List<StoreFile> sfDiffs = remoteFS.stream().filter(elem -> !localFS.contains(elem)).collect(Collectors.toList());
		for(StoreFile sfDiff : sfDiffs) {
			//check which file system the difference belongs to
			if(localFS.contains(sfDiff)) {
				//compare local to remote copy (if remote copy exists)
				StoreFile otherVersion = findModifiedVersionOf(sfDiff, remoteFS);
				if(otherVersion != null) {
					//another version exists, determine newest
					if(sfDiff.getModificationDate() > otherVersion.getModificationDate()) {
						//the local version is the newest, pend upload to server
						changes.add(new ModifiedFileTicket(sfDiff, TransferDirection.PUSH, FileModificationKey.MODIFIED));
					} else if(sfDiff.getModificationDate() < otherVersion.getModificationDate()) {
						//the remote version is the newest, pend download from server
						changes.add(new ModifiedFileTicket(otherVersion, TransferDirection.PULL, FileModificationKey.MODIFIED));
					}
				} else if(otherVersion == null) {
					//another version does not exist, pend upload to server
					changes.add(new ModifiedFileTicket(sfDiff, TransferDirection.PUSH, FileModificationKey.NEW_FILE));
				}
				
			} else if(remoteFS.contains(sfDiff)) {
				//compare remote to local copy (if local copy exists)
				StoreFile otherVersion = findModifiedVersionOf(sfDiff, localFS);
				if(otherVersion != null) {
					//another version exists, determine newest
					if(sfDiff.getModificationDate() > otherVersion.getModificationDate()) {
						//the remote version is the newest, pend download from server
						changes.add(new ModifiedFileTicket(otherVersion, TransferDirection.PULL, FileModificationKey.MODIFIED));
					} else if(sfDiff.getModificationDate() < otherVersion.getModificationDate()) {
						//the local version is the newest, pend upload to server
						changes.add(new ModifiedFileTicket(sfDiff, TransferDirection.PUSH, FileModificationKey.MODIFIED));
					}
				} else if(otherVersion == null) {
					//another version does not exist, pend download from server
					changes.add(new ModifiedFileTicket(sfDiff, TransferDirection.PULL, FileModificationKey.NEW_FILE));
				}
			}
		}
		
		return changes;
	}
	
	private StoreFileSystemStructure serializeFS() {
		StoreFolder root = serializeDirectory(ClientConfig.VAULT_DIR);
		return new StoreFileSystemStructure(root);
	}
	
	private StoreFolder serializeDirectory(File dir) {
		LinkedList<StoreObject> objects = new LinkedList<StoreObject>();
		for(File f : dir.listFiles()) {
			if(f.isFile()) {
				StoreFile store = StoreFile.buildFromLocalFile(f);
				objects.add(store);
			} else if(f.isDirectory()) {
				StoreFolder store = serializeDirectory(f);
				objects.add(store);
			}
		}
		
		return new StoreFolder(dir.getName(), objects);
	}
	
	private StoreFile findModifiedVersionOf(StoreFile sfCheck, LinkedList<StoreFile> search) {
		for(StoreFile sf : search) {
			if(StoreFile.equalFileSystemStruct(sfCheck, sf)) {
				return sf;
			}
		}
		
		return null;
	}
	
	public void updateRemoteFileSystem(StoreFileSystemStructure remoteFS) {
		this.currentRemoteFS = remoteFS;
	}

}
