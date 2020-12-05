package command;

import expression.Expression;
import expression.ShuntingYard;
import interpreter.Server;

public abstract class CommonCommand implements Command {
	protected Server server;
	
	public CommonCommand(Server server) {
		this.server = server;
	}

	public Expression calcExpression(String exp) throws Exception {
		ShuntingYard calcExp = new ShuntingYard();
		Expression solvedExp = calcExp.calc(exp,server);
		
		return solvedExp;
	}
}
