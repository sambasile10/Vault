package vault.client;

import java.util.LinkedList;

import vault.shared.BufferedDownloadThread;
import vault.shared.BufferedUploadThread;
import vault.shared.serialobjects.*;

public class ClientTransferManager {
	
	private static LinkedList<BufferedDownloadThread> downloadThreads;
	private static LinkedList<BufferedUploadThread> uploadThreads;
	
	public static void init() {
		downloadThreads = new LinkedList<BufferedDownloadThread>();
		uploadThreads = new LinkedList<BufferedUploadThread>();
		Log.write(LogType.NET, "TransferManager initialized!");
	}
	
	public static void createDownloadProcess(ModifiedFileTicket mfTicket) {
		PretransferTicket ppTicket = PretransferTicket.buildFor(mfTicket.getStoreFile());
		BufferedDownloadThread bdt = new BufferedDownloadThread(ppTicket);
		bdt.start();
		downloadThreads.add(bdt);
	}
	
	public static void createDownloadProcess(PretransferTicket ptt) {
		BufferedDownloadThread bdt = new BufferedDownloadThread(ptt);
		bdt.start();
		downloadThreads.add(bdt);
	}
	
	public static void createUploadProcess(ModifiedFileTicket mfTicket) {
		//Build pretrans ticket and init buffered upload thread
		PretransferTicket ppTicket = PretransferTicket.buildFor(mfTicket.getStoreFile());
		BufferedUploadThread but = new BufferedUploadThread(PretransferTicket.buildFor(mfTicket.getStoreFile()));
		
		//Inform server of inbound transfer
		ClientGlobal.getClient().sendTCP(ppTicket);
		ClientGlobal.getClient().sendTCP(new TransferControlKey(ppTicket.getTransferID(), ControlKey.TRANSFER_ACKNOWLEDGE));
		
		//Register upload thread
		but.start();
		uploadThreads.add(but);
	}
	
	public static boolean unregister(long transferID) {
		for(BufferedDownloadThread bdt : downloadThreads) {
			if(bdt.getTransferID() == transferID) {
				downloadThreads.remove(bdt);
				Log.write(LogType.NET, "Unregistered download thread // " + transferID);
				return true;
			}
		}
		
		for(BufferedUploadThread but : uploadThreads) {
			if(but.getTransferID() == transferID) {
				uploadThreads.remove(but);
				Log.write(LogType.NET, "Unregistered upload thread // " + transferID);
				return true;
			}
		}
		
		Log.write(LogType.NET, "[WARN] Cannot unregister thread, no process with transfer id '" + transferID + "'!");
		return false;
	}
	
	public static boolean isSyncActive() {
		return (!(downloadThreads.size() == 0 && uploadThreads.size() == 0));
	}
	
	public static boolean processReceivedDataBlock(DataBlock dataBlock) {
		for(BufferedDownloadThread bdt : downloadThreads) {
			if(bdt.getTransferID() == dataBlock.getTransferID()) {
				if(bdt.getSequencePosition() == dataBlock.getSequencePosition()) {
					if(bdt.writeData(dataBlock.getData())) {
						ClientGlobal.getClient().sendTCP(new TransferControlKey(dataBlock.getTransferID(), ControlKey.TRANSFER_CONTINUE));
						return true;
					} else {
						ClientGlobal.getClient().sendTCP(new TransferControlKey(dataBlock.getTransferID(), ControlKey.TRANSFER_ERROR));
						return false;
					}
					
				} else {
					Log.write(LogType.NET, "[CRITICAL] Error writing data block (" + 
				dataBlock.getTransferID() + ", " + dataBlock.getSequencePosition() + "), sequence position mismatch");
					return false;
				}
			}
		}
		
		Log.write(LogType.IO, "[CRITICAL] Processing of data block (" + dataBlock.getTransferID() + ") failed, no transfer with this ID exists");
		return false;
	}
	
	public static BufferedUploadThread getUploadStreamByID(long uuid) {
		for(BufferedUploadThread buThread : uploadThreads) {
			if(buThread.getTransferID() == uuid) {
				return buThread;
			}
		}
		
		return null;
	}
	
	public static BufferedDownloadThread getDownloadStreamByID(long uuid) {
		for(BufferedDownloadThread bdThread : downloadThreads) {
			if(bdThread.getTransferID() == uuid) {
				return bdThread;
			}
		}
		
		return null;
	}
	
	public static boolean handleTransferControlKey(TransferControlKey tcKey) {
		if(tcKey.getKey() == ControlKey.TRANSFER_CONTINUE) {
			//Server is requesting next data block
			ClientGlobal.getClient().sendTCP(getUploadStreamByID(tcKey.getTransferID()).nextBlock());
			return true;
		} else if(tcKey.getKey() == ControlKey.TRANSFER_END_STREAM) {
			//TODO handle EoS
		}
		
		return false;
	}

}
