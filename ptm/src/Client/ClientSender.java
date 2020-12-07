package Client;

import java.net.Socket;

public class ClientSender {
	private final Socket clientSocket;

	ClientSender(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
}
