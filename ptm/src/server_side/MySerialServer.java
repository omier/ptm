package server_side;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MySerialServer implements Server {
	private int port;
	private Boolean stop;
	
	public MySerialServer(int port) {
		this.port = port;
		this.stop = false;
	}
	
	public void start(ClientHandler c) {
		new Thread(() -> this.runServer(c)).start();
	}
	
	public void runServer(ClientHandler c) {
		try {
			ServerSocket server = new ServerSocket(this.port);
			server.setSoTimeout(3000);
			while (!stop) {
				Socket client = server.accept();
				InputStream clientInputStream = client.getInputStream();
				OutputStream clientOutputStream = client.getOutputStream();
				
				c.handleClient(clientInputStream, clientOutputStream);
				
				clientInputStream.close();
				clientOutputStream.close();
				client.close();
			}
			
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		this.stop = true;
	}
}
