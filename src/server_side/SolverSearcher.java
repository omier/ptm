package server_side;

public class SolverSearcher<Problem, Solution> implements Solver<Problem, Solution> {
	@SuppressWarnings("rawtypes")
	private Searcher s;

	@SuppressWarnings("rawtypes")
	public SolverSearcher(Searcher s) {
		this.s = s;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Solution Solve(Problem p) {
		return (Solution) s.search((Searchable) p);
	}
}
