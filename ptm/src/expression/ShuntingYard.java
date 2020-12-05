package expression;

import java.util.ArrayDeque;
import java.util.Stack;
import interpreter.Server;

public class ShuntingYard {
	public Expression calc(String exp, Server server) throws Exception {
		ArrayDeque<String> queue = new ArrayDeque<>();
		Stack<String> stack = new Stack<>();
		Stack<Expression> stackExp = new Stack<>();
		if (exp.startsWith("-")) {
			exp = "0" + exp;
		}

		String[] tokens = exp.split("(?<=[-+*/()])|(?=[-+*/()])");
		String lastToken = "";

		for (String token : tokens) {
			token = token.trim();
			if (token.equals("") || token == null) {
				continue;
			}

			if (isDouble(token)) {
				queue.add(token);
				lastToken = token;
				continue;
			}

			if (token.matches("^[a-zA-Z0-9_]+$")) {
				if (server.getSymbolTbl().containsKey(token)) {
					queue.add(token);
				} else {
					throw new Exception("Cannot find the variable: " + token);
				}
			}

			switch (token) {
			case "/":
			case "*":
			case "(":
				stack.push(token);
				break;
			case "+":
			case "-":
				if (token.equals("-") && lastToken.matches("^[[\\/\\*\\+\\-\\(]]$")) {
					stack.push("~");
					break;
				}

				while (!stack.empty() && (!stack.peek().equals("("))) {
					queue.add(stack.pop());
				}

				stack.push(token);
				break;
			case ")":
				while (!stack.peek().equals("(")) {
					queue.add(stack.pop());
				}

				stack.pop();
				break;
			}

			lastToken = token;
		}

		while (!stack.isEmpty()) {
			queue.add(stack.pop());
		}

		for (String str : queue) {
			if (isDouble(str)) {
				stackExp.push(new Number(Double.parseDouble(str)));
			} else if (str.matches("^[a-zA-Z0-9_]+$")) {
				stackExp.push(new VarNumber(str, server));
			} else {
				if (stackExp.isEmpty() == true) {
					throw new Exception("Invalid Expression: " + exp);
				}

				Expression right = stackExp.pop();
				Expression left = null;

				if (str.charAt(0) != '~') {
					if (stackExp.isEmpty() == true) {
						throw new Exception("Invalid Expression: " + exp);
					}

					left = stackExp.pop();
				}

				switch (str) {
				case "/":
					stackExp.push(new Div(left, right));
					break;
				case "*":
					stackExp.push(new Mul(left, right));
					break;
				case "+":
					stackExp.push(new Plus(left, right));
					break;
				case "-":
					stackExp.push(new Minus(left, right));
					break;
				case "~":
					stackExp.push(new Mul(new Number(-1), right));
					break;
				}
			}
		}
		
		return stackExp.pop();
	}

	private boolean isDouble(String val) {
		try {
			Double.parseDouble(val);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
