package test;

import command.Command;
import interpreter.CommandFactory;
import interpreter.Interpreter;
import interpreter.Server;
import server.SimVarsByXML;
import server.SimulatorVariables;

public class MyInterpreter {

	public static int interpret(String[] lines){
		SimulatorVariables simVars = new SimVarsByXML("generic_small.xml");
		CommandFactory<Command> exp = new CommandFactory<Command>();
		
		Server server = new Server(exp, simVars);
		Interpreter inter = new Interpreter(server);
		int ans = 0;
		for (int i = 0; i < lines.length; i++) {
			ans = inter.compile(lines[i]);
			int s = ans;
		}
		// call your interpreter here
		
		return ans;
	}
}
