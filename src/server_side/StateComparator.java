package server_side;

import java.util.Comparator;

public class StateComparator<T> implements Comparator<State<T>> {

	@Override
	public int compare(State<T> s1, State<T> s2) {
		if (s1.getCost() < s2.getCost()) {
			return -1;
		} else if (s1.getCost() == s2.getCost()) {
			return 0;
		}
		
		return 1;
	}
}
