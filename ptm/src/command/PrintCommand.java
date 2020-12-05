package command;

import java.util.LinkedList;
import interpreter.PeekableScanner;
import interpreter.Server;

public class PrintCommand extends CommonCommand {
	String content;

	public PrintCommand(Server server) {
		super(server);
	}

	@Override
	public int execute() throws Exception {
		if (content.contains("\"")) {
			System.out.println(content.replaceAll("\"", ""));
		} else {
			System.out.println(server.getCachedExp().get(content).calculate());
		}

		return 0;
	}

	@Override
	public boolean test(PeekableScanner text, LinkedList<Command> doCommands) throws Exception {
		String par1 = text.next().trim();
		if (!par1.matches("^\"[^\"]*\"$")) {
			server.getCachedExp().put(par1, calcExpression(par1));
		}

		this.content = par1;
		doCommands.add(this);

		return true;
	}
}
