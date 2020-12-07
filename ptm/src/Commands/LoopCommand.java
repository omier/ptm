package Commands;

import Singletones.VariablesHolder;
import StringHandling.Parser;
import StringHandling.StringToArgumentParser;
import StringHandling.TypeArguments;
import java.util.Arrays;
import java.util.List;

public class LoopCommand implements ICommand {

	int numOfParameters = 3;

	@Override
	public void doCommand(List<Object> parameters) {
		if (validateCommand(parameters)) {

			while (isTrue((String) parameters.get(1))) {
				String[] commands = ((String) parameters.get(2)).split("&");

				Parser.getInstance().parse(Arrays.copyOfRange(commands, 1, commands.length));
			}
		}
	}

	private boolean isTrue(String condition) {
		String[] strArray = condition.split("((?<=[<>])|(?=[<>]))");
		double variableValue = VariablesHolder.getInstance().getSpecificVar(strArray[0]).getValue();
		String operator = strArray[1];
		int num = Integer.parseInt(strArray[2]);
		if (operator.equals(">")) {

			return variableValue > num;
		} else {
			return variableValue < num;
		}

	}

	@Override
	public boolean validateCommand(List<Object> parameters) {
		return parameters.size() == numOfParameters;
	}

	@Override
	public int getArguments(String[] args, int idx, List<Object> parameters) {
		return StringToArgumentParser.getInstance().parse(args, idx, parameters, TypeArguments.String,
				TypeArguments.Condition, TypeArguments.Block);
	}
}
