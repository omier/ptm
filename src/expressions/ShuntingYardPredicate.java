package expressions;

import java.util.LinkedList;
import java.util.Stack;
import interpreter.CompParser;

public class ShuntingYardPredicate {
	public static double calc(String expression) {
		LinkedList<String> queue = new LinkedList<>();
		Stack<String> stack = new Stack<>();
		int len = expression.length();

		String token = "";
		String compToken = null;
		
		for (int i = 0; i < len; i++) {
			if (expression.charAt(i) >= '0' && expression.charAt(i) <= '9') {
				token = expression.charAt(i) + "";
				
				while ((i + 1 < len && expression.charAt(i + 1) >= '0' && expression.charAt(i + 1) <= '9')
						|| (i + 1 < len && expression.charAt(i + 1) == '.')) {
					token = token + expression.charAt(++i);
				}
			} else if ((expression.charAt(i) >= '<' && expression.charAt(i) <= '>') || expression.charAt(i) == '!') {
				if (expression.charAt(i + 1) == '=') {
					token = expression.charAt(i) + "";
					token = token + expression.charAt(++i);
				} else {
					token = expression.charAt(i) + "";
				}
			} else if ((expression.charAt(i) >= 'A' && expression.charAt(i) <= 'Z')
					|| (expression.charAt(i) >= 'a' && expression.charAt(i) <= 'z')) {
				token = expression.charAt(i) + "";
				while (i < expression.length() - 1
						&& ((expression.charAt(i + 1) >= 'A' && expression.charAt(i + 1) <= 'Z')
								|| (expression.charAt(i + 1) >= 'a' && expression.charAt(i + 1) <= 'z'))) {
					token = token + expression.charAt(++i);
				}
				
				token = CompParser.symbolTable.get(token).getV() + "";
			} else {
				token = expression.charAt(i) + "";
			}

			switch (token) {
			case "+":
			case "-":
				while (!stack.isEmpty() && !stack.peek().equals("(")) {
					queue.addFirst(stack.pop());
				}
				
				stack.push(token);
				
				break;
			case "||":
			case "&&":
			case "*":
			case "/":
				while (!stack.isEmpty() && (stack.peek().equals("*") || stack.peek().equals("/"))) {
					queue.addFirst(stack.pop());
				}
				
				stack.push(token);
				
				break;
			case "<":
			case "<=":
			case ">":
			case ">=":
			case "!=":
			case "==":
				compToken = token;
				
				break;
			case "(":
				stack.push(token);
				
				break;

			case ")":
				while (!stack.isEmpty() && !(stack.peek().equals("("))) {
					queue.addFirst(stack.pop());
				}
				
				stack.pop();
				
				break;
			default:
				queue.addFirst(token);
			}
		}
		
		while (!stack.isEmpty()) {
			queue.addFirst(stack.pop());
		}
		
		queue.addFirst(compToken);
		
		return Double.parseDouble(String.format("%.3f", buildExpression(queue).calculate()));
	}

	private static Expression buildExpression(LinkedList<String> queue) {
		Expression right = null;
		Expression left = null;
		String currentExpression = queue.removeFirst();
		
		if (currentExpression.equals("+") || currentExpression.equals("-") || currentExpression.equals("*")
				|| currentExpression.equals("/") || currentExpression.equals("<") || currentExpression.equals(">")
				|| currentExpression.equals("<=") || currentExpression.equals(">=") || currentExpression.equals("==")
				|| currentExpression.equals("!=")) {
			right = buildExpression(queue);
			left = buildExpression(queue);
		}
		
		switch (currentExpression) {
		case "+":
			return new Plus(left, right);
		case "-":
			return new Minus(left, right);
		case "*":
			return new Mul(left, right);
		case "/":
			return new Div(left, right);
		case "<=":
		case ">":
		case ">=":
		case "==":
		case "!=":
		case "<":
			return new PredicateExp(left, right, currentExpression);
		default:
			return new Number(Double.parseDouble(String.format("%.2f", Double.parseDouble(currentExpression))));
		}
	}
}
