package interpreter;

import java.util.LinkedList;
import command.Command;
import expression.Expression;

public class CheckParser {
	Server server;

	public CheckParser(Server server) {
		this.server = server;
	}

	public LinkedList<Expression> execute(PeekableScanner text, LinkedList<Command> doCommands) throws Exception {
		Command cmd;

		while (text.hasNext()) {
			String currentCmd = text.next().trim();

			if (currentCmd.isEmpty()) {
				continue;
			}

			if (currentCmd.trim().equals("}")) {
				throw new EndOfControlException();
			}

			if (!text.hasNext() || text.peek().trim().charAt(0) != '=') {
				try {
					cmd = server.getExp().getNewCommand(currentCmd, server);
				} catch (Exception e) {
					throw new Exception("Cannot find the command: " + currentCmd);
				}

				cmd.test(text, doCommands);
			} else {
				text.next();
				String cmd3 = text.next().trim();

				if (cmd3.equals("bind")) {
					cmd3 = text.next().trim();
					server.getExp().getNewCommand("bind", server, currentCmd, cmd3).test(text, doCommands);
				} else {
					server.getExp().getNewCommand("=", server, currentCmd, cmd3).test(text, doCommands);
				}
			}

		}

		return null;
	}
}