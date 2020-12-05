package expression;

import java.util.Stack;

public class ExpressionShuntingYard {
	static String infixToPostfix(String infix) {
		final String ops = "abhhg";

		StringBuilder sb = new StringBuilder();
		Stack<Integer> s = new Stack<>();
		String[] tokens = infix.split(
				("((?<=[><()])|(?=[><()]))|((?<=&&)|(?=&&))|((?<===)|(?===))|((?<=!=)|(?=!=))|(((?<=\\|\\|)|(?=\\|\\|)))"));
		for (String token : tokens) {
			token = token.trim();

			if (token.isEmpty()) {
				continue;
			}

			if (token.equals("&&")) {
				token = "g";
			}

			if (token.equals(">")) {
				token = "b";
			}

			if (token.equals("<")) {
				token = "a";
			}

			char c = token.charAt(0);
			int idx = ops.indexOf(c);

			if (idx != -1) {
				if (s.isEmpty()) {
					s.push(idx);
				} else {
					while (!s.isEmpty()) {
						int prec2 = s.peek() / 2;
						int prec1 = idx / 2;
						if (prec2 > prec1 || (prec2 == prec1 && c != 'g')) {
							sb.append(ops.charAt(s.pop())).append(' ');
						} else {
							break;
						}
					}

					s.push(idx);
				}
			} else if (c == '(') {
				s.push(-2);
			} else if (c == ')') {
				while (s.peek() != -2) {
					sb.append(ops.charAt(s.pop())).append(' ');
				}

				s.pop();
			} else {
				sb.append(token).append(' ');
			}
		}
		while (!s.isEmpty()) {
			sb.append(ops.charAt(s.pop())).append(' ');
		}

		return sb.toString();
	}
}