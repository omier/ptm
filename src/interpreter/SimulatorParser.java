package interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import commands.*;

public class SimulatorParser implements Parser {
	public static HashMap<String, Var> symbolTable = new HashMap<>();
	
	private HashMap<String, Command> commandTable = new HashMap<>();
	private ArrayList<String[]> lines;

	public SimulatorParser(ArrayList<String[]> lines) {
		this.lines = lines;
		commandTable.put("openDataServer", new OpenDataServer());
		commandTable.put("connect", new ConnectCommand());
		commandTable.put("while", new LoopCommand());
	}

	public void parse() {
		for (int i = 0; i < lines.size(); i++) {
			Command cmd = commandTable.get(lines.get(i)[0]);
			if (cmd != null && lines.get(i)[0].equals("while")) {
				int index = i;
				while (!lines.get(i)[0].equals("}")) {
					i++;
				}

				this.parseCondition(new ArrayList<String[]>(lines.subList(index, i)));
			}

			cmd.executeCommand(lines.get(i));
		}
	}

	private void parseCondition(ArrayList<String[]> array) {
		ArrayList<Command> comds = new ArrayList<>();
		
		for (int i = 1; i < array.size(); i++) {
			Command cmd = commandTable.get(array.get(i)[0]);
			if (cmd != null && array.get(i)[0].equals("while")) {
				int index = i;
				while (array.get(i)[0] != "}") {
					i++;
				}

				this.parseCondition(new ArrayList<String[]>(array.subList(index, i)));
			}

			comds.add(cmd);
		}
	}
}
