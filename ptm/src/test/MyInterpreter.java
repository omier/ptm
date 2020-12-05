package test;

import command.Command;
import interpreter.CommandFactory;
import interpreter.Interpreter;
import interpreter.Server;
import server.SimulatorVariables;
import server.TestSimVars;

public class MyInterpreter {
	static SimulatorVariables simVars = new TestSimVars();
	static CommandFactory<Command> exp = new CommandFactory<Command>();
	static Server server = new Server(exp, simVars);

	public static int interpret(String[] lines) {
		Interpreter inter = new Interpreter(server);
		int ans = 0;
		for (int i = 0; i < lines.length; i++) {
			ans = inter.compile(lines[i]);
		}

		return ans;
	}
}
