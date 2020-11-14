package server_side;

import java.util.HashSet;

public class State<T> implements Comparable<State<T>> {
	protected T state;
	private double cost;
	private State cameFrom;
	
	public State(T state) {
		this.state=state;
	}
	public State() {
		this.state=null;
		this.cost=0;
		this.cameFrom=null;
	}
	
	public boolean equals(State s) {
		return state.equals(s.getState());
	}

	public T getState() {
		return state;
	}

	public void setState(T state) {
		this.state = state;
		}

	@Override
	public String toString() {
		return "State [state=" + state + ", cost=" + cost + ", cameFrom=" + cameFrom + "]";
	}
	public double getCost() {
		HashSet<State> set = new HashSet<State>();
		return this.getCostRecursive(set);
	}
	
	public double getCostRecursive(HashSet<State> set) {
		if(this.getCameFrom()!=null) {
			if (!set.contains(this.getCameFrom())) {
				set.add(this.getCameFrom());
				return this.getCameFrom().getCostRecursive(set)+cost;
			}
		} else {
			return cost;			
		}
		
		return 0;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public State getCameFrom() {
		return cameFrom;
	}

	public void setCameFrom(State cameFrom) {
		this.cameFrom = cameFrom;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}
	@Override
	public int compareTo(State<T> state) {
		if (this.getCost() < state.getCost())
		{
			return -1;
		}
		
		else if(this.getCost() == state.getCost())
		{
			return 0;
		}
		
		return 1;
	}
}
