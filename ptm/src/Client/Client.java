package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	private static Socket socket;
	private static PrintWriter out;
	private volatile boolean stop;

	private static class ClientHolder {
		public static final Client helper = new Client();
	}

	public static Client getInstance() {
		return Client.ClientHolder.helper;
	}

	public void ConnectClient(String ip, int port) {
		this.stop = false;
		try {
			this.socket = new Socket(ip, port);
			this.out = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			}
		}
	}

	public void SendData(String data) throws IOException {
		this.out.println(data);
		this.out.flush();
	}

	public void disconnect() {
		this.out.println("bye");
		this.out.flush();
		out.close();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.stop = true;
	}
}