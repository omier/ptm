package Commands;

import Client.Client;
import Server.DataReaderServer;
import StringHandling.StringToArgumentParser;
import StringHandling.TypeArguments;

import java.util.List;

public class DisconnectCommand implements ICommand {

	private int numOfParameters = 1;

	@Override
	public void doCommand(List<Object> parameters) {
		if (validateCommand(parameters)) {
			Client.getInstance().disconnect();
			DataReaderServer.getInstance().CloseServer();
		}
	}

	@Override
	public boolean validateCommand(List<Object> parameters) {
		return parameters.size() == numOfParameters;
	}

	@Override
	public int getArguments(String[] args, int idx, List<Object> parameters) {
		return StringToArgumentParser.getInstance().parse(args, idx, parameters, TypeArguments.String);
	}
}
