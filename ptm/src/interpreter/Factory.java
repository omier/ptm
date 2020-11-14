package interpreter;

import java.util.HashMap;
import java.util.Map;

public class Factory<T> {

	private interface Creator<T> {
		public T create(); // no unhandled exceptions
	}

	Map<String, Creator<T>> map;

	public Factory() {
		map = new HashMap<>();
	}

	public void insert(String key, Class<? extends T> c) {
		map.put(key, new Creator<T>() {
			@Override
			public T create() {
				try {
					return c.getDeclaredConstructor().newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}
		});
	}

	public T getNew(String key) {
		if (map.containsKey(key))
			return (T) map.get(key).create();
		return null;
	}
}
