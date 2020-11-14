package server_side;

import java.util.ArrayList;
import java.util.HashSet;

public class BFS<Solution> extends CommonSearcher<Solution> {

	@Override
	public Solution search(Searchable s) {
		openList.add(s.getInitialState());
		HashSet<State> visited = new HashSet<State>();
		
		visited.add(s.getInitialState());
		
		while (openList.size() > 0) {
			State currState = this.popOpenList();
			
			if (currState.equals(s.getGoalState()))
			{
				return backTrace(currState, s.getInitialState());
			}
			
			ArrayList<State> adj = s.getAllPossibleStates(currState);
			for (State state : adj) {
				if (!visited.contains(state)) {
					state.setCameFrom(currState);
					visited.add(state);
					openList.add(state);
				}
			}
		}
		
		return backTrace(s.getGoalState(), s.getInitialState());
	}

	@Override
	public int getNumberOfNodesEvaluated() {
		return evaluateNodes;
	}
	
}
