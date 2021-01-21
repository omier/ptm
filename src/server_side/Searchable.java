package server_side;

import java.util.ArrayList;

public interface Searchable {
	@SuppressWarnings("rawtypes")
	State getInitialState();

	@SuppressWarnings("rawtypes")
	State getGoalState();

	@SuppressWarnings("rawtypes")
	ArrayList<State> getAllPossibleStates(State S);
}
