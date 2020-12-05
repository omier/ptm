package command;

import java.util.LinkedList;
import interpreter.PeekableScanner;
import interpreter.Server;
import server.ServerHandler;
import server.ServerSide;

public class OpenServerCommand extends CommonCommand {

	public OpenServerCommand(Server server) {
		super(server);
	}

	private String par1, par2;

	@Override
	public int execute() {
		int port = (int) server.getCachedExp().get(par1).calculate();

		if (!server.serverSide.ports.contains(new Integer(port))) {
			server.serverSide = new ServerSide(port, new ServerHandler(), server, Integer.parseInt(par2));
			server.serverSide.start();
		}

		return 0;
	}

	@Override
	public boolean test(PeekableScanner text, LinkedList<Command> doCommands) throws Exception {
		par1 = text.next();
		par2 = text.next();

		server.getCachedExp().put(par1, calcExpression(par1));
		server.getCachedExp().put(par2, calcExpression(par2));

		doCommands.add(this);
		return true;
	}
}
