package command;

import server_side.MySerialServer;
import server_side.MyTestClientHandler;
import server_side.Server;

public class OpenDataServer implements Command {
	public static volatile boolean stop = false;
	public static Object wait = new Object();
	Server s;

	@Override
	public void doCommand(String[] array) {
		if (array.length < 1 || array[0].isEmpty()) {
			System.err.println("No port given");
			return;
		}

		int port = Integer.parseInt(array[0]);
		MySerialServer server = new MySerialServer(port);
		server.start(new MyTestClientHandler());
	}

}