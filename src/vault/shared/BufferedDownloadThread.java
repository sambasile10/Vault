package vault.shared;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import vault.client.ClientConfig;
import vault.client.Log;
import vault.client.LogType;
import vault.shared.serialobjects.PretransferTicket;
import vault.shared.serialobjects.StoreFile;

public class BufferedDownloadThread extends ManagedThread {
	
	private StoreFile storeFile;
	private long transferID;
	
	private FileOutputStream fileOutputStream;
	private File output;
	private long currentSequencePosition;
	
	public BufferedDownloadThread(PretransferTicket ptt) {
		super("vault.shared.BufferedDownloadThread");
		this.storeFile = ptt.getStoreFile();
		this.transferID = ptt.getTransferID();
		this.output = storeFile.getAbsoluteLocalFile();
		this.currentSequencePosition = 0L;
	}
	
	@Override
	public void start() {
		if(!output.exists()) {
			try { 
				output.createNewFile(); 
				fileOutputStream = new FileOutputStream(output, true);
				Log.write(LogType.IO, "Started new download process for " + storeFile.getLocalPath() + " (" + storeFile.getUUID() + ")");
			} catch (IOException e) { 
				e.printStackTrace(); 
				Log.write(LogType.IO, "[CRITICAL] Fatal I/O error occured while opening BufferedDownloadThread!");
				return;
			}
		}
	}
	
	public boolean writeData(byte[] data) {
		try {
			fileOutputStream.write(data);
			currentSequencePosition++;
			return true;
		} catch (IOException e) {
			Log.write(LogType.IO, "[CRITICAL] Fatal I/O error occured while writing data in BufferedDownloadThread!");
			e.printStackTrace();
			return false;
		}
	}
	
	public long getTransferID() {
		return this.transferID;
	}
	
	public StoreFile getStoreFile() {
		return this.storeFile;
	}
	
	public long getSequencePosition() {
		return this.currentSequencePosition;
	}

}
