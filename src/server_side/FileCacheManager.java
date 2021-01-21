package server_side;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class FileCacheManager<Problem, Solution> implements CacheManager<Problem, Solution> {
	public static final String CACHE_FILE_NAME = "cache";
	public HashMap<Problem, Solution> disc;
	public Properties prop;

	@SuppressWarnings("unchecked")
	public FileCacheManager() {
		prop = new Properties();

		try {
			prop.load(new FileInputStream(CACHE_FILE_NAME));
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.disc = new HashMap<>();

		if (prop != null) {
			Enumeration<?> E = prop.propertyNames();
			while (E.hasMoreElements()) {
				Problem key = (Problem) E.nextElement();
				if (key != null) {
					this.disc.put(key, (Solution) prop.get(key));
				}
			}
		}
	}

	@Override
	public Boolean Check(Problem in) {
		if (disc.isEmpty()) {
			return false;
		}

		return disc.containsKey(in);
	}

	@Override
	public Solution Extract(Problem in) {
		return disc.get(in);
	}

	@Override
	public void Save(Problem in, Solution out) {
		disc.put(in, out);
		prop.putAll(this.disc);

		try {
			prop.store(new FileOutputStream(CACHE_FILE_NAME), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
