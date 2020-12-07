package StringHandling;

import java.util.List;

public class StringToArgumentParser {

	private static class StringToArgumentParserHolder {
		public static final StringToArgumentParser helper = new StringToArgumentParser();
	}

	public static StringToArgumentParser getInstance() {
		return StringToArgumentParser.StringToArgumentParserHolder.helper;
	}

	public int parse(String[] args, int index, List<Object> emptyList, TypeArguments... typeArguments) {
		for (TypeArguments typeArgument : typeArguments) {
			Object tmp = parsByType(args[index], typeArgument);
			if (tmp != null) {
				emptyList.add(tmp);

			}
			
			index++;
		}
		
		return index;
	}

	private Object parsByType(String value, TypeArguments type) {
		try {
			switch (type) {
			case Integer:
				return Integer.parseInt(value);

			case String:
				return value;
			case Double:
				return Double.parseDouble(value);
			default:
				return value;
			}
		} catch (Exception e) {
			System.out.println("Unable to cast value: " + value + "to type: " + type);
			return null;
		}
	}
}
