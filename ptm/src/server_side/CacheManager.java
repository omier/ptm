package server_side;

public interface CacheManager<Problem, Solution> {
	Solution Get(Problem p);
	void Save(Problem p, Solution s);
}
