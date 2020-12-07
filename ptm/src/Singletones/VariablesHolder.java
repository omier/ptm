package Singletones;

import Expression.BindVar;
import Expression.Var;
import java.util.HashMap;

public class VariablesHolder {

	private static boolean isInitialized = false;
	private static HashMap<String, Var> variables;

	private static class VariablesHolderPrivate {
		public static final VariablesHolder helper = new VariablesHolder();
	}

	public static VariablesHolder getInstance() {
		HashInitializer();
		return VariablesHolder.VariablesHolderPrivate.helper;
	}

	private static void HashInitializer() {
		if (!isInitialized) {
			variables = new HashMap<>();
			isInitialized = true;
		}
	}

	public HashMap<String, Var> getVariables() {
		return variables;
	}

	public Var getSpecificVar(String key) {
		if (variables.containsKey(key)) {
			return variables.get(key);
		} else {
			return null;
		}
	}

	public void setSpecificVariable(String key, Var value) {
		variables.put(key, value);
	}

	public void changeAllBindedVars(String targetKey, double value) {
		variables.forEach((key, variable) -> {
			if (variable instanceof BindVar) {
				if (((BindVar) variable).getPath().equals(targetKey)) {
					variable.setValue(value);
				}
			}
		});
	}
}
