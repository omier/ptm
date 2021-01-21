package server_side;

public class State<T> {
	protected T state;
	private double cost;
	private State<T> cameFrom;

	public State(T state) {
		this.state = state;
	}

	public State() {
		this.state = null;
		this.cost = 0;
		this.cameFrom = null;
	}

	public boolean equals(State<T> s) {
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
		if (this.getCameFrom() != null) {
			return this.getCameFrom().getCost() + cost;
		}

		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public State<T> getCameFrom() {
		return cameFrom;
	}

	public void setCameFrom(State<T> cameFrom) {
		this.cameFrom = cameFrom;
	}

	@Override
	public int hashCode() {
		final int prime = 31;

		return prime * 1 + ((state == null) ? 0 : state.hashCode());
	}
}
