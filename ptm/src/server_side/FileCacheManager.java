package server_side;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class FileCacheManager implements CacheManager<String, String> {
	private HashMap<String, String> hashMap;
	private final String fileName = "cache.txt";
	private File cacheFile;

	public FileCacheManager() {
		this.hashMap = new HashMap<String, String>();
		this.cacheFile = new File(this.fileName);
		
		try {
			if (!this.cacheFile.createNewFile()) {
				Scanner scanner = new Scanner(new FileReader(this.cacheFile));
				while (scanner.hasNextLine()) {
					String problem = scanner.nextLine();
					if (scanner.hasNextLine() && !problem.isEmpty()) {
						String solution = scanner.nextLine();
						if (!solution.isEmpty()) {
							this.hashMap.put(problem, solution);
						}
					}
				}
				
				scanner.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String Get(String problem) {
		return this.hashMap.get(problem);
	}

	public void Save(String problem, String solution) {
		try {
			FileWriter cacheFile = new FileWriter(this.cacheFile, true);
			
			cacheFile.write(problem + System.lineSeparator() + solution + System.lineSeparator());
			cacheFile.flush();
			cacheFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.hashMap.put(problem, solution);
	}
}
