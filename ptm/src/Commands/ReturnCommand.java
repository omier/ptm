package Commands;

import Expression.ExpressionConvertor;
import StringHandling.Parser;
import StringHandling.StringToArgumentParser;
import StringHandling.TypeArguments;

import java.util.Arrays;
import java.util.List;

public class ReturnCommand implements ICommand {

	private int numOfParameters = 2;

	@Override
	public void doCommand(List<Object> parameters) {
		if (validateCommand(parameters)) {
			String toReturn = (String) parameters.get(1);
			String[] toConvert = toReturn.split("((?<=[-+/*()])|(?=[-+/*()]))");
			String str = ExpressionConvertor.infixToPostfix(Arrays.asList(toConvert));
			double ans = ExpressionConvertor.calculatePostfix(str);
			Parser.getInstance().setReturnValue(ans);
		}
	}

	@Override
	public boolean validateCommand(List<Object> parameters) {
		return parameters.size() == numOfParameters;
	}

	@Override
	public int getArguments(String[] args, int idx, List<Object> parameters) {
		return StringToArgumentParser.getInstance().parse(args, idx, parameters, TypeArguments.String,
				TypeArguments.String);
	}

}