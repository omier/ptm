package expression;

import interpreter.Server;
import interpreter.VarTable;

public class VarNumber implements Expression {
	Server server;
	String name;

	public VarNumber(String name, Server server) {
		this.server = server;
		this.name = name;
	}

	@Override
	public double calculate() {
		VarTable t = server.getSymbolTbl().get(name);

		if (t.getType().equals("double")) {
			return (double) t.getValue();
		} else {
			return server.getServerData().get(t.getValue());
		}
	}
}
