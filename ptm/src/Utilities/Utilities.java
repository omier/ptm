package Utilities;

import Expression.Var;
import Singletones.VariablesHolder;

public class Utilities {

	public static boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static Var getVar(String name) {
		if (VariablesHolder.getInstance().getVariables().containsKey(name)) {
			return VariablesHolder.getInstance().getSpecificVar(name);
		}
		
		return null;
	}

	public static String SyncSyntax(String scriptSyntax) {
		String[] arr = scriptSyntax.split("/");
		
		return arr[arr.length - 2] + "_" + arr[arr.length - 1];
	}
}
