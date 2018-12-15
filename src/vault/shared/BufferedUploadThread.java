package vault.shared;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import vault.client.ClientConfig;
import vault.client.Log;
import vault.client.LogType;
import vault.shared.serialobjects.DataBlock;
import vault.shared.serialobjects.PretransferTicket;
import vault.shared.serialobjects.StoreFile;

public class BufferedUploadThread extends ManagedThread {
	
	private PretransferTicket ptt;
	
	private long currentBytePosition, currentSequencePosition;
	private File readIn;
	private RandomAccessFile inputFile;
	
	public BufferedUploadThread(PretransferTicket ptt) {
		super("vault.shared.BufferedUploadThread");
		
		this.ptt = ptt;
		this.currentBytePosition = 0L;
		this.currentSequencePosition = 0L;
		this.readIn = ptt.getStoreFile().getAbsoluteLocalFile();
	}
	
	@Override
	public void start() {
		try {
			inputFile = new RandomAccessFile(readIn, "r");
		} catch (Exception e) {
			Log.write(LogType.IO, "[CRITICAL] Fatal I/O error occured while opening random access file for BufferedUploadThread");
			e.printStackTrace();
		}
	}
	
	public DataBlock nextBlock() {
		byte[] data = new byte[ClientConfig.DATA_BLOCK_SIZE];
		
		//Ensure there is enough available data to fill the data block size
		long rafLength;
		try { rafLength = inputFile.length(); } catch (IOException e) { e.printStackTrace(); return null; }
		
		long dataRemaining = rafLength - currentBytePosition;
		int buffer = ClientConfig.DATA_BLOCK_SIZE;
		boolean isFinal = false;
		
		if(dataRemaining < ClientConfig.DATA_BLOCK_SIZE) {
			//less data remains than the data block size, adjust properly
			buffer = (int) dataRemaining;
			isFinal = true;
		}
		
		try {
			//Read bytes from current position through buffer length
			inputFile.seek(currentBytePosition);
			inputFile.read(data, 0, buffer);
			
			//Append new position
			currentBytePosition += buffer;
			currentSequencePosition++;
			return new DataBlock(this.getTransferID(), (currentSequencePosition-1), data, isFinal);
		} catch (IOException e) {
			//Failed to read data
			e.printStackTrace();
			Log.write(LogType.IO, "[CRITICAL] Fatal I/O error occured while reading RandomAccessFile on BufferedUploadThread!");
			return null;
		}
	}
	
	public StoreFile getStoreFile() {
		return ptt.getStoreFile();
	}
	
	public long getTransferID() {
		return ptt.getTransferID();
	}
	
}
