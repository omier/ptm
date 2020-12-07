package Commands;

import Server.DataReaderServer;
import StringHandling.StringToArgumentParser;
import StringHandling.TypeArguments;
import java.net.ServerSocket;
import java.util.List;

public class OpenServerCommand implements ICommand {
	public final int numOfParameters = 3;
	private int port;
	private int rps;

	ServerSocket listener;

	public OpenServerCommand() {}

	@Override
	public void doCommand(List<Object> parameters) {

		if (validateCommand(parameters)) {
			DataReaderServer.getInstance().OpenServer(port);
		}
	}

	@Override
	public boolean validateCommand(List<Object> parameters) {
		try {
			if (parameters.size() != numOfParameters) {
				return false;
			} else {
				port = (int) parameters.get(1);
				rps = (int) parameters.get(2);
			}

			return true;

		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public int getArguments(String[] args, int idx, List<Object> emptyList) {

		return StringToArgumentParser.getInstance().parse(args, idx, emptyList, TypeArguments.String,
				TypeArguments.Integer, TypeArguments.Integer);
	}

}