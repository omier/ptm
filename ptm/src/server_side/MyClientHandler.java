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
	CacheManager cm;
	Solver solver;
	public MyClientHandler() {
		this.cm = new FileCacheManager();
	}
	
	@Override
	public void handleClient(InputStream in, OutputStream out) {
		BufferedReader buffin=new BufferedReader(new InputStreamReader(in));
		PrintWriter buffout=new PrintWriter(new OutputStreamWriter(out));
			try {
				String line;
				
				ArrayList<String[]> lines=new ArrayList<>();
				while(!(line= buffin.readLine()).equals("end"))
				{
					lines.add(line.split(","));
				}
				int j=0;
				int[][]mat=new int[lines.size()][];
				for (int i = 0; i < mat.length; i++) {
					String[] tmp=lines.get(i);
					mat[i]=new int[tmp.length];
					for (String s : tmp) {
						mat[i][j]=Integer.parseInt(s);
						j++;
					}
					j=0;
				}
			Matrix m=new Matrix(mat);
			HillClimbing.Heuristic heuristic=new HillClimbing.Heuristic() {
				@Override
				public double cost(State s, State goalState) {
					String start=(String)(s.getState());
					String[] tmp=start.split(",");
					double x1=Integer.parseInt(tmp[0]);
					double y1=Integer.parseInt(tmp[1]);
					String end=(String)goalState.getState();
					tmp=end.split(",");
					double x2=Integer.parseInt(tmp[0]);
					double y2=Integer.parseInt(tmp[1]);
					return Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
				}
			};
			Searcher searcher=new BFS();
//			Searcher searcher=new HillClimbing(heuristic);
			solver=new SolverSearcher<>(searcher);
			m.setIntialState(line = buffin.readLine());
			m.setGoalState(line = buffin.readLine());
			String Solved = (String)cm.Get(m.toString());
			if(Solved == null)
			{
				Solved=(String) solver.solve(m);
				String[] arrows=Solved.split("->");
				Solved="";
				String[] arrow1;
				String[] arrow2;
				int x,y;
				for (int i = 0; i < arrows.length-1; i++) {
					arrow1=arrows[i].split(",");
					arrow2=arrows[i+1].split(",");
					x=Integer.parseInt(arrow2[0])-Integer.parseInt(arrow1[0]);
					y=Integer.parseInt(arrow2[1])-Integer.parseInt(arrow1[1]);
					if(x>0)
						Solved+="Down"+",";
					else if(x<0)
						Solved+="Up"+",";
					else
						if(y>0)
							Solved+="Right"+",";
						else
							Solved+="Left"+",";
				
						}
				cm.Save(m.toString(), Solved);
			}
			
			buffout.println(Solved.substring(0, Solved.length()-1));
			buffout.flush();
		}catch (IOException e) {
			e.printStackTrace();
		}	
	}

}
