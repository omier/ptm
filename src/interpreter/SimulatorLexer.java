package interpreter;

import java.util.ArrayList;
import java.util.Scanner;

public class SimulatorLexer implements Lexer {
	private Scanner scan;
	private ArrayList<String[]> lines = new ArrayList<>();

	public SimulatorLexer(String v) {
		scan = new Scanner(v);
	}

	public ArrayList<String[]> lexicalCheck() {
		while (scan.hasNextLine()) {
			lines.add(scan.nextLine().split(" "));
		}
		
		return lines;
	}
}
