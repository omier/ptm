package boot;

import server_side.MySerialServer;
import server_side.MyTestClientHandler;

public class Main {

	public static void main(String[] args) {
		if (args.length < 1 || args[0].isEmpty()) {
			System.err.println("No port given");
			return;
		}

		int port = Integer.parseInt(args[0]);
		MySerialServer server = new MySerialServer(port);
		server.start(new MyTestClientHandler());
	}

}
