package commands;

import expressions.CoditionBulider;

public class PredicateCommand implements Command {
	double bool;

	public double getBool() {
		return bool;
	}

	public void setBool(double bool) {
		this.bool = bool;
	}

	@Override
	public void executeCommand(String[] array) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 1; i < array.length - 1; i++) {
			sb.append(array[i]);
		}
		
		bool = CoditionBulider.calc(sb.toString());
	}
}
