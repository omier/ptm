package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import interpreter.Server;

public class ServerSide {
	private int port;
	private volatile boolean stop;
	public Server serv;
	private String[] tmpDataList;
	public int routines;
	public static volatile HashSet<Integer> ports = new HashSet<Integer>();

	public ServerSide(int port, ClientHandler ch, Server server, int routines) {
		this.port = port;
		stop = false;
		this.serv = server;
		this.routines = routines;

		tmpDataList = server.getSimVars().getVariables();
		for (String tmpData : tmpDataList) {
			serv.getServerData().put(tmpData, 0.0);
		}
	}

	private void runServer() throws Exception {
		if (this.ports.contains(new Integer(port))) {
			return;
		}

		ServerSocket server = new ServerSocket(port);
		server.setSoTimeout(1000);
		this.ports.add(new Integer(port));

		System.out.println("Waiting for the Flight Simulator to be opened...");
		while (!stop) {
			try {
				Socket aClient;
				synchronized (this) {
					aClient = server.accept();
					notifyAll();
				}

				System.out.println("Server: We are connected to the Flight Simulator!...");

				try {
					BufferedReader userInput = new BufferedReader(new InputStreamReader(aClient.getInputStream()));

					String line;
					while ((line = userInput.readLine()) != null) {

						String[] flightData = line.split(",");
						String[] arr = { "simX", "simY", "simZ" };
						for (int i = 0; i < arr.length; i++) {
							serv.getServerData().put(arr[i], Double.parseDouble(flightData[i]));
						}

						Thread.sleep(1000 / routines);
					}

					aClient.getInputStream().close();
					aClient.getOutputStream().close();
					aClient.close();
				} catch (IOException e) {
				}
			} catch (SocketTimeoutException e) {
			}
		}

		server.close();
		this.ports.remove(new Integer(port));
	}

	public void start() {
		new Thread(() -> {
			try {
				runServer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		stop = true;
	}
}
