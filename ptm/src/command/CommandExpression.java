package command;

import expression.Expression;

public class CommandExpression implements Expression {
	private Command c;
	private String[] s;

	public CommandExpression(Command c) {
		this.c = c;
	}

	public Command getC() {
		return c;
	}

	public void setC(Command c) {
		this.c = c;
	}

	public String[] getS() {
		return s;
	}

	public void setS(String[] s) {
		this.s = s;
	}

	@Override
	public double calculate() {
		try {
			c.doCommand(s);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
}
