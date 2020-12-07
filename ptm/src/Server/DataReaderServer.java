package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataReaderServer {
	private ServerSocket listener;

	private static class ServerHolder {
		public static final DataReaderServer helper = new DataReaderServer();
	}

	public volatile boolean isReaded = false;
	private static boolean isInitialized = false;

	public static String[] parametersNames = new String[] { "simX", "simY", "simZ" };
	private ConcurrentHashMap<String, Double> FlightGearData;

	public ConcurrentHashMap<String, Double> getFlightGearData() {
		if (!isInitialized) {
			FlightGearData = new ConcurrentHashMap<String, Double>();
			initHashMap();
			isInitialized = true;
		}

		return FlightGearData;
	}

	public static DataReaderServer getInstance() {

		return DataReaderServer.ServerHolder.helper;
	}

	private void initHashMap() {
		for (int i = 0; i < parametersNames.length; i++) {
			FlightGearData.put(parametersNames[i], -999.0);
		}
	}

	public void setSpecificParameter(String key, double value) {
		if (!isInitialized) {
			FlightGearData = new ConcurrentHashMap<>();
			isInitialized = true;
		}

		FlightGearData.put(key, value);
	}

	ServerSocket serverSocket;
	Socket clientSocket;
	private volatile boolean running;

	public void CloseServer() {
		running = false;
		try {
			clientSocket.close();
			serverSocket.close();
		} catch (IOException e) {

		}
	}

	public void OpenServer(int port) {
		final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(1);
		running = true;

		Runnable serverTask = new Runnable() {
			@Override
			public void run() {
				try {
					serverSocket = new ServerSocket(port);
					while (running) {
						clientSocket = serverSocket.accept();
						clientProcessingPool.submit(new SimulatorDataReceiver(clientSocket, running));
					}
				} catch (IOException e) {

				}
			}
		};
		
		serverThread = new Thread(serverTask);
		serverThread.start();
	}

	Thread serverThread;
}