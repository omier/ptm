package commands;

public class IfCommand extends ConditionCommand {

	@Override
	public void executeCommand(String[] array) {
		commands.get(0).calculate();
		
		if (((PredicateCommand) commands.get(0).getC()).getBool() != 0) {
			for (int i = 1; i < commands.size(); i++) {
				commands.get(i).calculate();
			}
			
			commands.get(0).calculate();
		}
	}
}