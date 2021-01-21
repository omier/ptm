package expressions;

import java.util.LinkedList;
import java.util.Stack;

public class CoditionBulider {
	public static double calcc(String expression) {
		StringBuilder deli = new StringBuilder(expression);
		int i = deli.indexOf("&");
		int j = deli.indexOf("|");
		
		if ( i <= 0 && j <= 0) {
			return ShuntingYardPredicate.calc(expression);
		}
		
		if ((j < i && j != -1) || i == -1) {
			i = j;
		}

		Number n1 = new Number(ShuntingYardPredicate.calc(deli.substring(0, i)));
		Number n2 = new Number(CoditionBulider.calc(deli.substring(i + 2)));
		Expression exp = null;

		switch (deli.charAt(i)) {
		case '|':
			exp = new Or(n1, n2);
			break;
		case '&':
			exp = new And(n1, n2);
			break;
		}

		return exp.calculate();
	}

	public static double calc(String expression) {
		LinkedList<String> queue = new LinkedList<>();
		Stack<String> stack = new Stack<>();
		String token = "";

		while (expression.contains("&") || expression.contains("|")) {
			int i = expression.indexOf("&");
			int j = expression.indexOf("|");

			if ((j < i && j != -1) || i == -1) {
				i = j;
			}

			token = ShuntingYardPredicate.calc(expression.substring(0, i)) + "";
			queue.addFirst(token);
			token = expression.charAt(i) + "";

			switch (token) {
			case "|":
				while (!stack.isEmpty())
					queue.addFirst(stack.pop());
				stack.push(token);
				break;
			case "&":
				while (!stack.isEmpty() && (stack.peek().equals("&")))
					queue.addFirst(stack.pop());
				stack.push(token);
				break;
			default:
				queue.addFirst(token);
			}

			expression = expression.substring(i + 2);
		}

		token = ShuntingYardPredicate.calc(expression) + "";
		queue.addFirst(token);
		while (!stack.isEmpty()) {
			queue.addFirst(stack.pop());
		}

		Expression finalExpression = buildExpression(queue);
		double answer = finalExpression.calculate();

		return Double.parseDouble(String.format("%.3f", answer));
	}

	private static Expression buildExpression(LinkedList<String> queue) {
		Expression right = null;
		Expression left = null;
		String currentExpression = queue.removeFirst();

		if (currentExpression.equals("|") || currentExpression.equals("&")) {
			right = buildExpression(queue);
			left = buildExpression(queue);
		}

		switch (currentExpression) {
		case "|":
			return new Or(left, right);
		case "&":
			return new And(left, right);
		default:
			return new Number(Double.parseDouble(String.format("%.2f", Double.parseDouble(currentExpression))));
		}
	}
}
