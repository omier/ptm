package interpreter;

import java.util.ArrayList;

public class AutoPilotParser {
	public static volatile boolean stop = true;
	public static volatile boolean close = false;
	public static Thread thread;
	public int i = 0;
	CompParser p;

	public AutoPilotParser(CompParser p) {
		this.p = p;
	}

	public void parse() {
		p.parse();
		i = 0;
	}

	public void execute() {
		thread = new Thread(() -> {
			while (!close) {
				while (!stop && i < p.comds.size()) {
					p.comds.get(i).calculate();
					i++;
				}
			}
		});

		thread.start();
	}

	public void add(ArrayList<String[]> lines) {
		p.lines.clear();
		p.lines.addAll(lines);
		CompParser.symbolTable.put("stop", new Var(1));
		
		for (String[] s : p.lines) {
			if (s[0].equals("while")) {
				StringBuilder sb = new StringBuilder(s[s.length - 2]);
				sb.append("&&stop!=0");
				s[s.length - 2] = sb.toString();
			}
		}
	}

	public void stop() {
		Var v = CompParser.symbolTable.get("stop");
		
		if (v != null) {
			v.setV(0);
		}
		
		AutoPilotParser.stop = true;
	}

	public void Continue() {
		CompParser.symbolTable.get("stop").setV(1);
	}
}
