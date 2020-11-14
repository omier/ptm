package server_side;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class MyTestClientHandler implements ClientHandler {
	Solver<String, String> solver;
	CacheManager<String, String> cm;
	
	public MyTestClientHandler() {
		this.solver = (String s) -> new StringBuilder(s).reverse().toString();
		this.cm = new FileCacheManager();
	}

	public void handleClient(InputStream inputStream, OutputStream outputStream) {
		Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(inputStream)));
		while (scanner.hasNextLine()) {
			String problem = scanner.nextLine();
			if (problem.equals("end")) {
				scanner.close();
				return;
			}

			String solution = this.cm.Get(problem);
			
			if (solution == null) {
				solution = this.solver.solve(problem);
				this.cm.Save(problem, solution);
			}
			
			PrintWriter writer = new PrintWriter(outputStream);
			writer.println(solution);
			writer.flush();
		}
	}
}
