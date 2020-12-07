package Commands;

import Expression.BindVar;
import Server.DataReaderServer;
import Singletones.VariablesHolder;
import StringHandling.StringToArgumentParser;
import StringHandling.TypeArguments;
import java.util.List;

public class EqualBindCommand implements ICommand {

	private int numOfParameters = 3;

	@Override
	public void doCommand(List<Object> parameters) {
		if (validateCommand(parameters)) {
			while (!DataReaderServer.getInstance().isReaded) {}
			
			String sourceKey = (String) parameters.get(0);
			String targetKey = (String) parameters.get(2);
			double value = DataReaderServer.getInstance().getFlightGearData().get(targetKey);
			VariablesHolder.getInstance().setSpecificVariable(sourceKey, new BindVar(sourceKey, value, targetKey));
		}
	}

	@Override
	public boolean validateCommand(List<Object> parameters) {
		return (parameters.size() == numOfParameters);
	}

	@Override
	public int getArguments(String[] args, int idx, List<Object> parameters) {
		return StringToArgumentParser.getInstance().parse(args, idx - 1, parameters, TypeArguments.String,
				TypeArguments.String, TypeArguments.String);
	}
}
