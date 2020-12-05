package command;

import java.util.LinkedList;
import interpreter.PeekableScanner;

public interface Command {
	public int execute() throws Exception;
	public boolean test(PeekableScanner text, LinkedList<Command> doCommands) throws Exception;
}
