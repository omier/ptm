package commands;

import expressions.ShuntingYard;
import interpreter.CompParser;

public class ReturnCommand implements Command {

	@Override
	public void executeCommand(String[] array) {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < array.length; i++) {
			sb.append(array[i]);
		}

		CompParser.returnValue = ShuntingYard.calc(sb.toString());
	}
}
