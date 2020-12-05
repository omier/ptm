package command;

import java.util.LinkedList;
import interpreter.PeekableScanner;
import interpreter.Server;
import interpreter.VarBind;
import interpreter.VarDouble;

public class AssignmentCommand extends CommonCommand {

	private String varName;
	private String varValue;

	public AssignmentCommand(Server server, String varName, String varValue) {
		super(server);
		this.varName = varName.trim();
		this.varValue = varValue.trim();
	}

	@Override
	public int execute() throws Exception {
		if (server.getSymbolTbl().get(varName).getType().equals("double")) {
			server.getSymbolTbl().put(varName, new VarDouble(server.getCachedExp().get(varValue).calculate()));
		} else {
			String varSimulator = (String) server.getSymbolTbl().get(varName).getValue();
			double varAmount = server.getCachedExp().get(varValue).calculate();
			server.client.sendSimulatorOrder(varSimulator, varAmount);
		}

		return 0;
	}

	@Override
	public boolean test(PeekableScanner text, LinkedList<Command> doCommands) throws Exception {
		if (!server.getSymbolTbl().containsKey(varName)) {
			throw new Exception("Variable " + varName + " is not exists");
		}

		if (varValue.contains("\"")) {
			server.getSymbolTbl().put(varName, new VarBind(null));
			doCommands.add(server.getExp().getNewCommand("bind", server, varName, varValue));
		} else if (varValue.equals("bind")) {
			server.getSymbolTbl().put(varName, new VarBind(null));
			varValue = text.next().trim().replaceAll("\"", "");
			doCommands.add(server.getExp().getNewCommand("bind", server, varName, varValue));
		} else if (varValue.matches("^[a-zA-Z0-9\\s-+*/()]+$")) {
			if (!server.getSymbolTbl().containsKey(varName)) {
				server.getSymbolTbl().put(varName, new VarDouble(0.0));
			}

			if (varValue.equals("return")) {
				throw new Exception("return word cannot be used as variable name.");
			}

			server.getCachedExp().put(varValue, calcExpression(varValue));
			doCommands.add(server.getExp().getNewCommand("=", server, varName, varValue));
		} else {
			throw new Exception("Syntax error at: " + varName + " = " + varValue);
		}

		return true;
	}

}
