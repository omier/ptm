package interpreter;

import java.util.LinkedList;
import command.Command;
import command.ConditionParser;
import command.IfCommand;
import command.LoopCommand;

public class Interpreter {

	protected Server server;
	Thread interpreterThread;
	static ConditionParser control = null;

	public Interpreter(Server server) {
		super();
		this.server = server;
	}

	public int compile(String text) {
		LinkedList<Command> doCommands = new LinkedList<>();
		Lexer lexer = new Lexer(server);
		PeekableScanner sc = lexer.execute(text);

		CheckParser checkParser = new CheckParser(server);
		try {
			checkParser.execute(sc, doCommands);
		} catch (EndOfControlException ex) {
			try {
				this.control.execute();
				this.control = null;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		for (Command command : doCommands) {
			if (server.stop == true) {
				System.out.println("STOPPED THE INTERPRETER!");
				break;
			}

			try {
				if (command instanceof LoopCommand || command instanceof IfCommand) {
					this.control = (ConditionParser) command;
					return 0;
				} else if (this.control != null) {
					this.control.cmds.add(command);
					return 0;
				} else {
					command.execute();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		int result = 0;
		if (server.getSymbolTbl().containsKey("return")) {
			result = (int) (double) server.getSymbolTbl().get("return").getValue();

			if (server.serverSide != null) {
				server.serverSide.stop();
			}

			server.getSymbolTbl().clear();
		}

		return result;
	}

	public void start(String text) {
		interpreterThread = new Thread(() -> {
			this.compile(text);
		});

		interpreterThread.start();
	}

	public void stop() {
		server.stop = true;
	}
}