package test;

import server_side.MyClientHandler;
import server_side.MySerialServer;
import server_side.Server;

public class TestSetter {
	static Server s;
	
	public static void runServer(int port) {
		// put the code here that runs your server
		s=new MySerialServer(port); // initialize
		s.start(new MyClientHandler());
	}

	public static void stopServer() {
		s.stop();
	}
	

}
