package Commands;

import Singletones.VariablesHolder;
import StringHandling.StringToArgumentParser;
import StringHandling.TypeArguments;
import java.util.List;

public class VarCommand implements ICommand {

	private int numOfParameters = 2;

	@Override
	public void doCommand(List<Object> parameters) {
		if (validateCommand(parameters)) {
			VariablesHolder.getInstance().setSpecificVariable((String) parameters.get(1), null);
		}
	}

	@Override
	public boolean validateCommand(List<Object> parameters) {
		if (parameters.size() != numOfParameters) {
			return false;
		}
		
		return true;
	}

	@Override
	public int getArguments(String[] args, int idx, List<Object> parameters) {
		return StringToArgumentParser.getInstance().parse(args, idx, parameters, TypeArguments.String,
				TypeArguments.String);
	}
}
