package vault.client;

import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import vault.shared.ManagedThread;
import vault.shared.serialobjects.*;

public class NetClient extends ManagedThread {
	
	//Kryonet client object
	private Client client;
	
	//Construct the client object, register the thread, doesn't connect until start() is called
	public NetClient() {
		//Call ManagedThread constructor for registration
		super("vaultcli.NetClient");
		this.client = new Client();
		Log.write(LogType.NET, "Created client thread (id=" + this.getThreadIdentifier() + ") [init, not connected]");
	}
	
	//Overrides Thread.start() to add the connection
	@Override
	public void start() {
		Log.write(LogType.NET, "Connecting to " + ClientConfig.SERVER_IP + ":" + ClientConfig.COMM_PORT + "...");
		
		for(Class serial : ClientGlobal.SERIAL_OBJECTS) {
			client.getKryo().register(serial);
			Log.write(LogType.NET, "Client registered serialized object :: " + serial.getSimpleName());
		}
		
	    try {
	    	//Start the kryonet client's multithreading
			client.start();
			
			//Attempt connection to server ip with port in config file
			client.connect(5000, ClientConfig.SERVER_IP, ClientConfig.COMM_PORT);
		} catch (IOException e) {
			//Connection failed
			Log.write(LogType.NET, "Fatal error while connecting to server");
			e.printStackTrace();
			return;
		}
	    
	    if(client.isConnected()) {
	    	//Connection successful, start the remote listeners
	    	Log.write(LogType.NET, "Connected to remote server, starting listeners...");
	    	hookListeners();
	    } else {
	    	//Connection failed, but no exceptions were thrown
	    	Log.write(LogType.NET, "Connection to remote server not formed (unspecific error)");
	    	return;
	    }
	}
	
	//Add data received listeners to the client object, uses Kryo's serialization to differentiate data types
	private void hookListeners() {
		client.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				//TODO add listeners
				if(object instanceof StoreFileSystemStructure) {
					ClientGlobal.getFileSystemListener().updateRemoteFileSystem((StoreFileSystemStructure) object);
				}
				
				if(object instanceof ControlKey) {
					//TODO process control keys
				}
				
				if(object instanceof TransferControlKey) {
					ClientTransferManager.handleTransferControlKey((TransferControlKey) object);
				}
				
				if(object instanceof DataBlock) {
					ClientTransferManager.processReceivedDataBlock((DataBlock) object);
				}
				
				if(object instanceof PretransferTicket) {
					ClientTransferManager.createDownloadProcess((PretransferTicket) object);
				}
			}
		});
	}
	
	public void requestRemoteFileSystem() {
		client.sendTCP(ControlKey.PULL_FILESTRUCTURE);
	}
	
	public void sendTCP(Object obj) {
		client.sendTCP(obj);
	}
	
	public void authenticate(String plainTextAuthKey) {
		sendTCP(new AuthenticationKey(ClientConfig.CLIENT_UUID, DigestUtils.sha256(plainTextAuthKey)));
		Log.write(LogType.NET, "Attempting authentication by CLIENT_UUID=" + ClientConfig.CLIENT_UUID + ", key of length=" + plainTextAuthKey.length());
	}

}
