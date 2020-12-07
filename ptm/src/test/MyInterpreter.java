package test;

import StringHandling.Parser;

public class MyInterpreter {

	public static  int interpret(String[] lines) {
		Parser parser = Parser.getInstance();
		
		return (int) parser.parse(lines);
	}
}
