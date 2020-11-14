package server_side;

import java.util.ArrayList;
import java.util.HashSet;

public class DFS<Solution> extends CommonSearcher<Solution> {

	@Override
	public Solution search(Searchable s) {
		HashSet<State> visited = new HashSet<State>();
		
		return this.DFSRecursive(s, s.getInitialState(), visited);
	}
	
	public Solution DFSRecursive(Searchable s, State currState, HashSet<State> visited) {
		visited.add(currState);
		this.evaluateNodes++;
		if (currState.equals(s.getGoalState()))
		{
			return backTrace(currState, s.getInitialState());
		}
		
		ArrayList<State> adj = s.getAllPossibleStates(currState);
		
		for (State state : adj) {
			if (!visited.contains(state)) {
				state.setCameFrom(currState);
				DFSRecursive(s, state, visited);
			}
		}
		
		return backTrace(s.getGoalState(), s.getInitialState());
	}

	@Override
	public int getNumberOfNodesEvaluated() {
		return this.evaluateNodes;
	}

}
