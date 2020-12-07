package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SimulatorDataReceiver implements Runnable {
	private final Socket flightGearsocket;
	private boolean running;

	SimulatorDataReceiver(Socket clientSocket, boolean running) {
		this.flightGearsocket = clientSocket;
		this.running = running;
	}

	String clientData = new String();

	@Override
	public void run() {

		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(flightGearsocket.getInputStream()));
			String line;
			while ((line = input.readLine()) != null && this.running) {

				fillServerHash(line);
				SetIsReading();
			}

			flightGearsocket.close();
		} catch (IOException e) {

		}

	}

	private void SetIsReading() {
		DataReaderServer.getInstance().isReaded = true;
	}

	public static boolean isReading = false;

	public static boolean getIsReading() {
		return isReading;
	}

	private void fillServerHash(String inputLine) {
		try {
			String[] parameters = inputLine.split(",");
			for (int i = 0; i < parameters.length; i++) {
				DataReaderServer.getInstance().setSpecificParameter(DataReaderServer.parametersNames[i],
						Double.parseDouble(parameters[i]));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}