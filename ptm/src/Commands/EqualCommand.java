package Commands;

import Client.Client;
import Expression.ExpressionConvertor;
import Expression.RegularVar;
import Expression.BindVar;
import Expression.Var;
import Singletones.VariablesHolder;
import StringHandling.StringToArgumentParser;
import StringHandling.TypeArguments;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class EqualCommand implements ICommand {

	private int numOfParameters = 3;

	@Override
	public void doCommand(List<Object> parameters) {
		if (validateCommand(parameters)) {
			Var currentVar = VariablesHolder.getInstance().getSpecificVar((String) parameters.get(0));
			if (currentVar instanceof RegularVar || currentVar == null) {
				if (parameters.get(2) instanceof Double) {
					VariablesHolder.getInstance().setSpecificVariable((String) parameters.get(0),
							new RegularVar((String) parameters.get(0), (double) parameters.get(2)));
				} else {
					String expression = (String) parameters.get(2);
					String[] toConvert = expression.split("((?<=[-+/*()])|(?=[-+/*()]))");
					String str = ExpressionConvertor.infixToPostfix(Arrays.asList(toConvert));
					double ans = ExpressionConvertor.calculatePostfix(str);

					RegularVar newVar = new RegularVar((String) parameters.get(0), ans);
					VariablesHolder.getInstance().setSpecificVariable((String) parameters.get(0), newVar);
				}
			} else {
				BindVar tempVar = (BindVar) currentVar;
				String targetName = tempVar.getPath();
				String expression = parameters.get(2).toString();
				String[] toConvert = expression.split("((?<=[-+/*()])|(?=[-+/*()]))");
				String str = ExpressionConvertor.infixToPostfix(Arrays.asList(toConvert));
				double ans = ExpressionConvertor.calculatePostfix(str);

				VariablesHolder.getInstance().changeAllBindedVars(targetName, ans);

				String toSend = "set " + targetName + " " + ans;

				try {
					Client.getInstance().SendData(toSend);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
		TypeArguments type = TypeArguments.String;
		if (isNumeric(args[idx + 1])) {
			type = TypeArguments.Double;
		}
		
		return StringToArgumentParser.getInstance().parse(args, idx - 1, parameters, TypeArguments.String,
				TypeArguments.String, type);
	}

	private boolean isNumeric(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
