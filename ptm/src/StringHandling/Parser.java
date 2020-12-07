package StringHandling;

import Commands.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Parser {
	private static class ParserHolder {
		public static final Parser helper = new Parser();
	}

	private static boolean isInitialized = false;
	private static ConcurrentHashMap<String, ICommand> commandMap;

	private static double returnValue;

	public static void setReturnValue(double value) {
		returnValue = value;
	}

	public static Parser getInstance() {
		HashInitializer();
		return Parser.ParserHolder.helper;
	}

	private static void HashInitializer() {
		if (!isInitialized) {
			commandMap = new ConcurrentHashMap<String, ICommand>();
			commandMap.put("openDataServer", new OpenServerCommand());
			commandMap.put("connect", new ConnectCommand());
			commandMap.put("var", new VarCommand());
			commandMap.put("=", new EqualCommand());
			commandMap.put("= bind", new EqualBindCommand());
			commandMap.put("disconnect", new DisconnectCommand());
			commandMap.put("return", new ReturnCommand());
			commandMap.put("while", new LoopCommand());
			isInitialized = true;
		}
	}

	public double parse(String[] arguments) {
		String tempStr = "";
		for (String str : arguments) {
			tempStr += str + ' ';
		}

		String[] args = Lexer.getInstance().lexer(tempStr);

		int tmpindex = 0;
		int index = 0;

		while (index < args.length) {
			int currentCommandIndex = index;
			if (commandMap.containsKey(args[currentCommandIndex])) {
				List<Object> parameters = new ArrayList<Object>();
				index = commandMap.get(args[index]).getArguments(args, index, parameters);
				commandMap.get(args[currentCommandIndex]).doCommand(parameters);
				currentCommandIndex = index;
			} else {
				index++;
			}
		}

		return returnValue;
	}
}
