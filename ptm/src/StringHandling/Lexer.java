package StringHandling;

import java.util.ArrayList;

public class Lexer {
	private static class LexerHolder {
		public static final Lexer helper = new Lexer();
	}

	public static Lexer getInstance() {
		return LexerHolder.helper;
	}

	private static String addSpacesToEqualeSign(String line) {
		if (line.contains("=")) {
			if (!line.contains((" = "))) {
				return (line.replace("=", " = "));
			}
		}

		return line;
	}

	private static String removeUnnecessarySpaces(String line) {
		String[] temp = line.split("\\s+");
		int index = 0;

		String text = "";

		while (index < temp.length) {
			if (index == 0) {
				text += temp[index];
				index++;
			} else if (temp[index].equals("*") || temp[index].equals("/") || temp[index].equals("+")
					|| temp[index].equals("-") || temp[index].equals("<") || temp[index].equals(">")) {
				text += temp[index];
				text += temp[index + 1];
				index += 2;
			} else {
				text += " " + temp[index];
				index++;
			}
		}

		return text;
	}

	public String[] handleEqualBind(String str) {
		String[] temp = str.split("\\s+");

		ArrayList<String> strArr = new ArrayList<String>();

		for (int i = 0; i < temp.length; i++) {
			if (temp[i].equals("=") && temp[i + 1].equals("bind")) {
				strArr.add("= bind");
				i += 1;
			} else {
				strArr.add(temp[i]);
			}
		}

		return strArr.toArray(new String[0]);
	}

	public String[] lexer(String line) {
		line = replactTab(line);
		line = addSpacesToEqualeSign(line);
		line = removeUnnecessarySpaces(line);
		String[] handled = handleEqualBind(line);
		return (combineWhileScope(handled));
	}

	private String replactTab(String lines) {
		String toReturn = lines.replaceAll("\t", "& ");

		return toReturn;
	}

	private String[] combineWhileScope(String[] lines) {
		ArrayList<String> strArr = new ArrayList<String>();
		int index = 0;
		String str = "";

		while (index < lines.length) {
			if (lines[index].equals("{")) {
				index++;
				while (!lines[index].equals("}")) {
					str += lines[index] + " ";
					index++;
				}

				strArr.add(str);
				index++;
			} else {
				strArr.add(lines[index]);
				index++;
			}
		}

		return strArr.toArray(new String[0]);
	}

	public static Object[] removeElement(Object[] original, int element) {
		String[] n = new String[original.length - 1];
		System.arraycopy(original, 0, n, 0, element);
		System.arraycopy(original, element + 1, n, element, original.length - element - 1);

		return n;
	}
}