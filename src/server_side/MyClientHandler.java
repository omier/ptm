package server_side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MyClientHandler implements ClientHandler {
	@SuppressWarnings("rawtypes")
	CacheManager cm;
	@SuppressWarnings("rawtypes")
	Solver solver;

	@SuppressWarnings("rawtypes")
	public MyClientHandler(CacheManager cm) {
		this.cm = cm;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void handleClient(InputStream in, OutputStream out) {
		BufferedReader Bin = new BufferedReader(new InputStreamReader(in));
		PrintWriter Bout = new PrintWriter(new OutputStreamWriter(out));

		try {
			String Line;
			
			ArrayList<String[]> lines = new ArrayList<>();
			while (!(Line = Bin.readLine()).equals("end")) {
				lines.add(Line.split(","));
			}

			int[][] mat = new int[lines.size()][];
			for (int i = 0; i < mat.length - 1; i++) {
				String[] line = lines.get(i);
				mat[i] = new int[line.length];
				for (int j = 0; j < line.length; j++) {
					mat[i][j] = Integer.parseInt(line[j]);
				}
			}

			Matrix m = new Matrix(mat);
			AStar.Heuristic heuristic = new AStar.Heuristic() {
				@Override
				public double cost(State s, State goalState) {
					String start = (String) (s.getState());
					String[] loc = start.split(",");
					double x1 = Integer.parseInt(loc[0]);
					double y1 = Integer.parseInt(loc[1]);
					
					String end = (String) goalState.getState();
					loc = end.split(",");
					double x2 = Integer.parseInt(loc[0]);
					double y2 = Integer.parseInt(loc[1]);
					
					return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
				}
			};

			Searcher searcher = new AStar(heuristic);
			solver = new SolverSearcher<>(searcher);
			m.setIntialState(Line = Bin.readLine());
			m.setGoalState(Line = Bin.readLine());

			String Solved;
			if (cm.Check(m.toString())) {
				Solved = (String) cm.Extract(m.toString());
			} else {
				Solved = (String) solver.Solve(m);
				String[] arrows = Solved.split("->");
				Solved = "";
				String[] arrow1;
				String[] arrow2;
				int x, y;
				for (int i = 0; i < arrows.length - 1; i++) {
					arrow1 = arrows[i].split(",");
					arrow2 = arrows[i + 1].split(",");
					x = Integer.parseInt(arrow2[0]) - Integer.parseInt(arrow1[0]);
					y = Integer.parseInt(arrow2[1]) - Integer.parseInt(arrow1[1]);
					if (x > 0) {
						Solved += "Down" + ",";
					} else if (x < 0) {
						Solved += "Up" + ",";
					} else if (y > 0) {
						Solved += "Right" + ",";
					} else {
						Solved += "Left" + ",";
					}
				}

				cm.Save(m.toString(), Solved);
			}

			Bout.println(Solved.substring(0, Solved.length() - 1));
			Bout.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
