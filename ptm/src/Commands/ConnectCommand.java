package Commands;

import Client.Client;
import StringHandling.StringToArgumentParser;
import StringHandling.TypeArguments;
import java.util.List;

public class ConnectCommand implements ICommand {
	private final int numOfParameters = 3;

	@Override
	public void doCommand(List<Object> parameters) {
		if (validateCommand(parameters)) {
			String ip = (String) parameters.get(1);
			int port = (Integer) parameters.get(2);
			Client.getInstance().ConnectClient(ip, port);
		}
	}

	@Override
	public boolean validateCommand(List<Object> parameters) {
		return (parameters.size() == numOfParameters);
	}

	public int getArguments(String[] args, int idx, List<Object> emptyList) {
		return StringToArgumentParser.getInstance().parse(args, idx, emptyList, TypeArguments.String,
				TypeArguments.String, TypeArguments.Integer);
	}
}