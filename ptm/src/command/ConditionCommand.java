package command;

import java.util.List;

public class ConditionCommand implements Command {

	protected List<CommandExpression> commands;

	public List<CommandExpression> getCommands() {
		return commands;
	}

	public void setCommands(List<CommandExpression> commands) {
		this.commands = commands;
	}

	@Override
	public void doCommand(String[] array) {

	}

}
