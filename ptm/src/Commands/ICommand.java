package Commands;

import java.util.List;

public interface ICommand {
	
	public void doCommand(List<Object> parameters);
	
	public boolean validateCommand(List<Object> parameters);

	public int getArguments(String[] args, int idx, List<Object> parameters);

}
