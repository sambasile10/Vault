package vault.server;

import com.esotericsoftware.kryonet.Server;

public class NetServer {
	
	private Server server;
	
	public NetServer() {
		server = new Server();
	}
	
	@Override
	public void start() {
		//Start the Kyronet server
		server.start();
	    server.bind(54555, 54777);
	}

}
