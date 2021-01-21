package server_side;

public class MatrixState extends State<String> {

	public MatrixState(String state) {
		super(state);
		this.setCameFrom(null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;

		return prime * super.hashCode() + ((state == null) ? 0 : state.hashCode());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(State s) {
		return this.state.intern() == ((String) s.getState()).intern();
	}
}
