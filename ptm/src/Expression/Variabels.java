package Expression;

import java.util.HashMap;

public class Variabels {

	private HashMap<String, Object> variables;

	private static class VariablesHolder {
		public static final Variabels helper = new Variabels();
	}

	public HashMap<String, Object> getVariables() {
		if (variables == null) {
			variables = new HashMap<>();
			return variables;
		} else {
			return variables;
		}
	}

	public void setRegularVar() {
	}

	public static Variabels getInstance() {
		return Variabels.VariablesHolder.helper;
	}
}
