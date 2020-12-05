package server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;

public class ServerHandler implements ClientHandler {

	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient, HashMap<String, Double> serverData)
			throws Exception {
		BufferedReader userInput = new BufferedReader(new InputStreamReader(inFromClient));

		int i = 0;

		String line;
		while (!(line = userInput.readLine()).equals("bye")) {
			System.out.println(line);

			i++;
			if (i > 20) {
				break;
			}
		}
	}

}
