package server_side;

import java.util.ArrayList;

public class HillClimbing<Solution> extends CommonSearcher<Solution> {

	public interface Heuristic {
		public double cost(State s, State goalState);
	}
	
	Heuristic h;
	
	public HillClimbing(Heuristic h) {
		this.h = h;
	}
	
	@Override
	public Solution search(Searchable s) {
		State initState = s.getInitialState();
		
		initState.setCost(this.h.cost(initState, s.getGoalState()));
		openList.add(initState);
		
		boolean isPeakOrPlateau = false;
		
		while (openList.size() > 0 && !isPeakOrPlateau) {
			State currState = this.popOpenList();
			ArrayList<State> states = s.getAllPossibleStates(currState);
			
			if (currState.equals(s.getGoalState()))
			{
				return backTrace(currState, s.getInitialState());
			}
			
			State min = states.get(0);
	        for (int nextSoln = 1; nextSoln < states.size(); nextSoln++) {
	            if (min.getCost() + this.h.cost(min, s.getGoalState()) > states.get(nextSoln).getCost() + this.h.cost(states.get(nextSoln), s.getGoalState())) {
	                min = states.get(nextSoln);
	            }
	        }
	        
	        if (min.getCost() + this.h.cost(min, s.getGoalState()) >= currState.getCost() + this.h.cost(currState, s.getGoalState())) {
	        	isPeakOrPlateau = true;
	        } else {
	        	min.setCameFrom(currState);
	        	openList.add(min);
	        }
		}
		
		return backTrace(s.getGoalState(), s.getInitialState());
	}

	@Override
	public int getNumberOfNodesEvaluated() {
		return evaluateNodes;
	}

}
