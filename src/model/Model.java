package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import interpreter.AutoPilotParser;
import interpreter.CompParser;

public class Model extends Observable implements Observer {
	public static volatile boolean stop = false;
	public static volatile boolean turn = true;
	public static volatile boolean head = false;
	public double startX;
	public double startY;
	public double planeX;
	public double planeY;
	public double markX;
	public double markY;
	public int[][] data;
	public double offset;
	public double currentlocationX;
	public double currentlocationY;
	public double currentHeading;
	public ArrayList<String[]> intersections = new ArrayList<>();
	public Thread route;
	public Thread rudder;
	public int indexPlan = 0;
	
	private SimulatorClient simulatorClient;
	private Interpreter interpreter;
	private static Socket socketPath;
	private static PrintWriter outPath;
	private static BufferedReader in;

	public Model() {
		simulatorClient = new SimulatorClient();
		interpreter = new Interpreter();
		
		route = new Thread(() -> {
			this.routeStart();
		});
		
		rudder = new Thread(() -> {
			this.rudderStart();
		});
	}

	public void GetPlane(double startX, double startY, double offset) {
		this.offset = offset;
		this.startX = startX;
		this.startY = startY;
		
		new Thread(() -> {
			Socket socket = null;
			
			try {
				socket = new Socket("127.0.0.1", 5402);
				System.out.println("Connected to the server");
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				while (!stop) {
					out.println("dump /position");
					out.flush();
					String line;
					ArrayList<String> lines = new ArrayList<>();
					
					while (!(line = br.readLine()).equals("</PropertyList>")) {
						if (!line.equals("")) {
							lines.add(line);
						}
					}
					
					br.readLine();
					out.println("get /instrumentation/heading-indicator/indicated-heading-deg");
					out.flush();
					
					String[] heading = br.readLine().split(" ");
					String longtitude = lines.get(2);
					String latitude = lines.get(3);
					String[] x = longtitude.split("[<>]");
					String[] y = latitude.split("[<>]");
					String[] data = { "plane", x[2], y[2], heading[3].substring(1, heading[3].length() - 1) };
					currentHeading = Double.parseDouble(heading[3].substring(1, heading[3].length() - 1));
					currentlocationX = Double.parseDouble(x[2]);
					currentlocationY = Double.parseDouble(y[2]);
					
					this.setChanged();
					this.notifyObservers(data);
					
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					socket.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		}).start();
	}

	public void parse(String[] script) {
		interpreter.interpet(script);
	}

	public void execute() {
		interpreter.execute();
	}

	public void stopAutoPilot() {
		interpreter.stop();
	}

	public void connectPath(String ip, int port) {
		try {
			socketPath = new Socket(ip, port);
			outPath = new PrintWriter(socketPath.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socketPath.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connectManual(String ip, int port) {
		simulatorClient.Connect(ip, port);
	}

	public void send(String[] data) {
		simulatorClient.Send(data);
	}

	public void findPath(int planeX, int planeY, int markX, int markY, int[][] data) {
		this.planeX = planeX;
		this.planeY = planeY;
		this.markX = markX;
		this.markY = markY;
		this.data = data;
		
		new Thread(() -> {
			for (int i = 0; i < data.length; i++) {
				System.out.print("\t");
				for (int j = 0; j < data[i].length - 1; j++) {
					outPath.print(data[i][j] + ",");
				}
				
				outPath.println(data[i][data[i].length - 1]);
			}
			
			outPath.println("end");
			outPath.println(planeX + "," + planeY);
			outPath.println(markX + "," + markY);
			outPath.flush();
			
			String line = "";
			
			try {
				line = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String[] split = line.split(",");

			String[] notfiy = new String[split.length + 1];
			notfiy[0] = "path";
			
			for (int i = 0; i < split.length; i++) {
				notfiy[i + 1] = split[i];
			}
			
			this.setChanged();
			this.notifyObservers(notfiy);
			this.buildFlyPlan(split);
			
			if (!route.isAlive()) {
				route.start();
			} else if (Model.turn == false) {
				route.interrupt();
				route.start();
			}
		}).start();
	}

	private void rudderStart() {
		while (!head && indexPlan < intersections.size()) {
			double deg;
			double heading = Integer.parseInt(intersections.get(indexPlan)[0]);
			double headingC = currentHeading;
			double turning = headingC;
			int degree = (int) (heading - headingC);
			int degreeCom = 360 - degree;
			
			if (degree < 0) {
				degree += 360;
			}
			
			if (degree < degreeCom) {
				turning = turnPlus(headingC);
				deg = (turning - headingC);
			} else {
				turning = turnMinus(headingC);
				deg = (turning - headingC);
			}
			
			if (deg >= 340) {
				deg = 360 - deg;
			} else if (deg < -340) {
				deg = -360 - deg;
			}
			
			if (Math.abs(heading - headingC) > 9 && Math.abs(heading - headingC) < 349) {
				CompParser.symbolTable.get("r").setV(deg / 20);
				CompParser.symbolTable.get("e").setV(0.095);
			} else {
				CompParser.symbolTable.get("r").setV(deg / 100);
				CompParser.symbolTable.get("e").setV(0.053);
			}
			
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void routeStart() {
		while (turn) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		rudder.start();
		double pathX = startX + (planeY - 1) * offset;
		double pathY = startY - (planeX - 1) * offset;
		double endX = startX + (markY - 1) * offset;
		double endY = startY - (markX - 1) * offset;
		double intersectionX, intersectionY;
		int radiusX = 17;
		
		while (!turn && indexPlan < intersections.size()) {
			int h = Integer.parseInt(intersections.get(indexPlan)[0]);
			int n = Integer.parseInt(intersections.get(indexPlan)[1]);
			
			switch (h) {
			case 360:
				intersectionX = pathX;
				intersectionY = pathY + (n - 1) * offset;
				break;
			case 45:
				intersectionX = pathX + (n - 1) * offset;
				intersectionY = pathY + (n - 1) * offset;
				break;
			case 90:
				intersectionX = pathX + (n - 1) * offset;
				intersectionY = pathY;
				break;
			case 135:
				intersectionX = pathX + (n - 1) * offset;
				intersectionY = pathY - (n - 1) * offset;
				break;
			case 180:
				intersectionX = pathX;
				intersectionY = pathY - (n - 1) * offset;
				;
				break;
			case 225:
				intersectionX = pathX - (n - 1) * offset;
				intersectionY = pathY - (n - 1) * offset;
				break;
			case 270:
				intersectionX = pathX - (n - 1) * offset;
				intersectionY = pathY;
				break;
			case 315:
				intersectionX = pathX - (n - 1) * offset;
				intersectionY = pathY + (n - 1) * offset;
				break;
			default:
				intersectionX = 0;
				intersectionY = 0;
			}
			
			if (indexPlan == intersections.size() - 1) {
				intersectionX = endX;
				intersectionY = endY;
			}
			
			while (Math.abs(currentlocationX - intersectionX) > radiusX * offset
					|| Math.abs(currentlocationY - intersectionY) > radiusX * offset) {
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			indexPlan++;
			pathX = currentlocationX;
			pathY = currentlocationY;
		}
		
		CompParser.symbolTable.get("goal").setV(1);
	}

	private void buildFlyPlan(String[] solution) {
		int count = 0;
		intersections = new ArrayList<String[]>();
		
		for (int i = 0; i < solution.length - 1; i++) {
			if (solution[i].equals(solution[i + 1])) {
				count++;
			} else {
				String[] arr = new String[2];
				arr[0] = solution[i];
				arr[1] = count + 1 + "";
				intersections.add(arr);
				count = 0;
			}
		}
		
		if (count != 0) {
			String[] arr = new String[2];
			arr[0] = solution[solution.length - 1];
			arr[1] = count + 1 + "";
			intersections.add(arr);
		}
		
		for (int i = 0; i < intersections.size(); i++) {
			if (Integer.parseInt(intersections.get(i)[1]) <= 5) {
				int index;
				
				if (i != 0) {
					index = i - 1;
				} else {
					index = i + 1;
				}
				
				String[] strings = new String[] { intersections.get(index)[0], String.valueOf(Integer.parseInt(intersections.get(index)[1]) + Integer.parseInt(intersections.get(i)[1])) };
				intersections.set(index, strings);
				intersections.remove(i);
			}
		}
		
		for (int i = 0; i < intersections.size() - 1; i++) {
			if (intersections.get(i)[0].equals(intersections.get(i + 1)[0])) {
				intersections.get(i)[1] = String.valueOf(Integer.parseInt(intersections.get(i)[1]) + Integer.parseInt(intersections.get(i + 1)[1]));
				intersections.remove(i + 1);
			}
		}
		
		for (int i = 0; i < intersections.size(); i++) {
			int degree = clacDegree(intersections.get(i)[0]);
			
			if (Integer.parseInt(intersections.get(i)[1]) <= 15 && Integer.parseInt(intersections.get(i)[1]) > 5) {
				if (i + 1 < intersections.size()) {
					if (degree != 360 && degree != 90) {
						if (degree < clacDegree(intersections.get(i + 1)[0])) {
							degree += 45;
						} else {
							degree -= 45;
						}
					} else if (degree == 360) {
						if (clacDegree(intersections.get(i + 1)[0]) == 90) {
							degree = 45;
						} else {
							degree -= 45;
						}
					} else if (degree == 90) {
						if (clacDegree(intersections.get(i + 1)[0]) == 360) {
							degree = 45;
						} else {
							degree -= 45;
						}
					}
				}
			}

			intersections.get(i)[0] = String.valueOf(degree).intern();
		}
	}

	private int clacDegree(String s) {
		switch (s) {
		case "Down":
			return 180;
		case "Right":
			return 90;
		case "Left":
			return 270;
		case "Up":
			return 360;
		default:
			return 0;
		}
	}

	public interface Turn {
		double Do(double x);
	}

	public void makeTurn(Turn t, double heading, double currentHeading) {
		double minus = Math.abs(heading - currentHeading);
		double h = currentHeading;
		
		while (minus > 30 && minus < 335 && !Model.turn) {
			double turnHeading = t.Do(h);
			CompParser.symbolTable.get("hroute").setV(turnHeading);
			
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			minus = Math.abs(heading - turnHeading);
			h = turnHeading;
		}
		
		CompParser.symbolTable.get("hroute").setV(heading);
	}

	public double turnPlus(double currentHeading) {
		int heading = (int) currentHeading + 7;
		
		if (heading > 360) {
			heading -= 360;
		}
		
		return heading;
	}

	public double turnMinus(double currentHeading) {
		int heading = (int) currentHeading - 7;
		if (heading < 0) {
			heading = 360 + heading;
		}
		
		return heading;
	}

	public void stopAll() {
		Model.stop = true;
		if (outPath != null) {
			outPath.close();
		}
		
		try {
			if (in != null) {
				in.close();
			}
			
			if (socketPath != null) {
				socketPath.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		simulatorClient.stop();
		AutoPilotParser.thread.interrupt();
		AutoPilotParser.close = true;
		Model.turn = true;
	}

	@Override
	public void update(Observable o, Object arg) {}
}
