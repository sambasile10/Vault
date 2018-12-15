package vault.client;

import vault.shared.*;
import vault.shared.serialobjects.*;

public class ClientGlobal {
	
	/*
	 * Final global variables
	 */
	public static final Class[] SERIAL_OBJECTS = { ControlKey.class, DataBlock.class, FileModificationKey.class, 
			ModifiedFileTicket.class, PretransferTicket.class, StoreFile.class, StoreFileSystemStructure.class,
			StoreFolder.class, StoreObject.class, TransferDirection.class, TransferControlKey.class };
	
	private static NetClient netClient;
	private static FileSystemListener fsListener;
	
	public static void start() {
		// TODO init client and FSL
		
		Log.writeNormal("CLIENT HEATING UP (ClientGlobal)");
		ClientConfig.loadConfiguration();
		Log.init();
		ThreadManager.init();
		ClientTransferManager.init();
		
		netClient = new NetClient();
		netClient.start();
		fsListener = new FileSystemListener();
		fsListener.start();
		
		Log.writeNormal("// ALL COMPONENT INITIALIZATION COMPLETED //");
	}
	
	public static NetClient getClient() {
		return netClient;
	}
	
	public static FileSystemListener getFileSystemListener() {
		return fsListener;
	}

}
