package view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import viewmodel.ViewModel;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.net.URL;
import java.util.*;

public class FlightController implements Initializable, Observer {
	public double sceneX, sceneY;
	public double translateX, translateY;
	public DoubleProperty mapSceneX, mapSceneY;
	public DoubleProperty aileron;
	public DoubleProperty elevator;
	public DoubleProperty airplaneX;
	public DoubleProperty airplaneY;
	public DoubleProperty startX;
	public DoubleProperty startY;
	public DoubleProperty offset;
	public DoubleProperty heading;
	public int mapData[][];

	@FXML
	private Canvas airplane;
	@FXML
	private Canvas mapMark;
	@FXML
	private TextArea textArea;
	@FXML
	private TextField port;
	@FXML
	private TextField ip;
	@FXML
	private Button submit;
	@FXML
	private Slider throttle;
	@FXML
	private Slider rudder;
	@FXML
	private RadioButton auto;
	@FXML
	private MapDisplayer map;
	@FXML
	private RadioButton manual;
	@FXML
	private Circle border;
	@FXML
	private Circle joystick;
	@FXML
	private TitledPane background;

	private Stage stage = new Stage();
	private BooleanProperty path;
	private Image[] plane;
	private Image markImage;
	private ViewModel viewModel;
	private String[] solution;
	private final String POPUP_FXML_FILE = "Popup.fxml";
	private final String POPUP_CONNECT_WINDOW_TITLE = "Connect";
	private final String POPUP_GET_PATH_WINDOW_TITLE = "Get Path";
	private final String FILE_NAME_EXTENSION_FILTER = "csv";
	private final String AUTO_PILOT_SELECT = "auto";
	private final String MANUAL_SELECT = "manual";
	private final int MAP_MARK_X_OFFSET = 13;
	private final int MAP_MARK_X_HEIGHT = 25;
	private final int MAP_MARK_X_WIDTH = 25;
	private final int PLANE_HEIGHT = 25;
	private final int PLANE_WIDTH = 25;
	private final int PLANE_X_OFFSET = 10;
	private final int PLANE_Y_OFFSET = 6;
	private final Color PATH_COLOR = Color.BLACK.darker();
	private final String UP = "Up";
	private final String DOWN = "Down";
	private final String RIGHT = "Right";
	private final String LEFT = "Left";
	private final String IMAGE_EXT = ".png";
	private final String RESOURCES_PATH_PREFIX = "./resources";
	private final String PLANE_IMAGE_PATH_PREFIX = RESOURCES_PATH_PREFIX + "/plane";
	private final String MARK_IMAGE_PATH = RESOURCES_PATH_PREFIX + "/mark" + IMAGE_EXT;
	private static BtnID lastBtnClicked = null;

	enum BtnID {
		CONNECT_BTN_ID, GET_PATH_BTN_ID
	}

	public void LoadDate() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter(FILE_NAME_EXTENSION_FILTER, FILE_NAME_EXTENSION_FILTER));
		chooser.setCurrentDirectory(new File("./"));

		int v = chooser.showOpenDialog(null);

		if (v == JFileChooser.APPROVE_OPTION) {
			BufferedReader br = null;
			String line = "";
			String delim = ",";

			ArrayList<String[]> arr = new ArrayList<>();

			try {
				br = new BufferedReader(new FileReader(chooser.getSelectedFile()));
				String[] splitLine = br.readLine().split(delim);
				startX.setValue(Double.parseDouble(splitLine[0]));
				startY.setValue(Double.parseDouble(splitLine[1]));

				splitLine = br.readLine().split(delim);
				offset.setValue(Double.parseDouble(splitLine[0]));

				while ((line = br.readLine()) != null) {
					arr.add(line.split(delim));
				}

				mapData = new int[arr.size()][];

				for (int i = 0; i < arr.size(); i++) {
					mapData[i] = new int[arr.get(i).length];
				}

				for (int i = 0; i < arr.size(); i++) {
					for (int j = 0; j < arr.get(i).length; j++) {
						mapData[i][j] = Integer.parseInt(arr.get(i)[j]);
					}
				}

				this.viewModel.setData(mapData);
				this.drawAirplane();
				map.setMapData(mapData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void LoadText() {
		textArea.clear();

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./"));
		int v = fileChooser.showOpenDialog(null);

		if (v == JFileChooser.APPROVE_OPTION) {
			try {
				Scanner scn = new Scanner(new BufferedReader(new FileReader(fileChooser.getSelectedFile())));

				while (scn.hasNextLine()) {
					textArea.appendText(scn.nextLine());
					textArea.appendText(System.lineSeparator());
				}

				viewModel.parse();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void Connect() {
		Parent parent = null;

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(POPUP_FXML_FILE));
			parent = fxmlLoader.load();
			FlightController ctl = fxmlLoader.getController();

			ctl.viewModel = this.viewModel;
			stage.setTitle(POPUP_CONNECT_WINDOW_TITLE);
			stage.setScene(new Scene(parent));

			if (!stage.isShowing()) {
				stage.show();
				FlightController.lastBtnClicked = BtnID.CONNECT_BTN_ID;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void GetPath() {
		Parent parent = null;

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(POPUP_FXML_FILE));
			parent = fxmlLoader.load();

			FlightController ctl = fxmlLoader.getController();
			ctl.viewModel = this.viewModel;
			ctl.mapData = this.mapData;
			ctl.mapMark = this.mapMark;
			ctl.path = new SimpleBooleanProperty();
			ctl.path.bindBidirectional(this.path);

			stage.setTitle(POPUP_GET_PATH_WINDOW_TITLE);
			stage.setScene(new Scene(parent));

			if (!stage.isShowing()) {
				FlightController.lastBtnClicked = BtnID.GET_PATH_BTN_ID;
				stage.show();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void Submit() {
		this.viewModel.ip.bindBidirectional(ip.textProperty());
		this.viewModel.port.bindBidirectional(port.textProperty());

		if (FlightController.lastBtnClicked == BtnID.CONNECT_BTN_ID) {
			viewModel.connect();
			((Stage) submit.getScene().getWindow()).close();
		}

		if (FlightController.lastBtnClicked == BtnID.GET_PATH_BTN_ID) {
			double height = mapMark.getHeight();
			double width = mapMark.getWidth();
			int mapHeight = mapData.length;
			int mapWidth = mapData[0].length;

			viewModel.findPath(height / mapHeight, width / mapWidth);
			path.setValue(true);
			((Stage) submit.getScene().getWindow()).close();
		}

		ip.clear();
		port.clear();
	}

	public void AutoPilot() {
		Select(AUTO_PILOT_SELECT);
	}

	public void Manual() {
		Select(MANUAL_SELECT);
	}

	public void Select(String s) {
		if (s.equals(AUTO_PILOT_SELECT)) {
			if (manual.isSelected()) {
				manual.setSelected(false);
				auto.setSelected(true);
			}

			viewModel.execute();
		} else if (s.equals(MANUAL_SELECT) && auto.isSelected()) {
			auto.setSelected(false);
			manual.setSelected(true);
			viewModel.stopAutoPilot();
		}
	}

	public void drawAirplane() {
		if (airplaneX.getValue() != null && airplaneY.getValue() != null) {
			double height = airplane.getHeight();
			double width = airplane.getWidth();
			double heightRatio = height / mapData.length;
			double widthRatio = width / mapData[0].length;

			GraphicsContext ctx = airplane.getGraphicsContext2D();
			double lastX = airplaneX.getValue();
			double lastY = -airplaneY.getValue();
			ctx.clearRect(0, 0, width, height);

			int planeHeadingIndex = (heading.getValue().intValue() % 360) / 45;
			ctx.drawImage(plane[planeHeadingIndex], widthRatio * lastX, lastY * heightRatio, PLANE_HEIGHT, PLANE_WIDTH);
		}
	}

	public void drawMark() {
		if (mapData == null) {
			return;
		}

		double height = mapMark.getHeight();
		double width = mapMark.getWidth();
		double heightRatio = height / mapData.length;
		double widthRatio = width / mapData[0].length;
		GraphicsContext ctx = mapMark.getGraphicsContext2D();
		ctx.clearRect(0, 0, width, height);

		ctx.drawImage(markImage, mapSceneX.getValue() - MAP_MARK_X_OFFSET, mapSceneY.getValue(), MAP_MARK_X_HEIGHT,
				MAP_MARK_X_WIDTH);

		if (path.getValue()) {
			viewModel.findPath(heightRatio, widthRatio);
		}
	}

	public void drawLine() {
		double height = mapMark.getHeight();
		double width = mapMark.getWidth();
		double heightRatio = height / mapData.length;
		double widthRatio = width / mapData[0].length;
		double x = airplaneX.getValue() * widthRatio + PLANE_X_OFFSET * widthRatio;
		double y = airplaneY.getValue() * -heightRatio + PLANE_Y_OFFSET * heightRatio;
		GraphicsContext ctx = mapMark.getGraphicsContext2D();

		for (int i = 1; i < solution.length; i++) {
			if (solution[i].equals(UP)) {
				ctx.setStroke(PATH_COLOR);
				ctx.strokeLine(x, y, x, y - heightRatio);
				y -= heightRatio;
			} else if (solution[i].equals(LEFT)) {
				ctx.setStroke(PATH_COLOR);
				ctx.strokeLine(x, y, x - widthRatio, y);
				x -= widthRatio;
			} else if (solution[i].equals(RIGHT)) {
				ctx.setStroke(PATH_COLOR);
				ctx.strokeLine(x, y, x + widthRatio, y);
				x += widthRatio;
			} else if (solution[i].equals(DOWN)) {
				ctx.setStroke(PATH_COLOR);
				ctx.strokeLine(x, y, x, y + heightRatio);
				y += heightRatio;
			}
		}
	}

	EventHandler<MouseEvent> onClickMap = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {
			mapSceneX.setValue(e.getX());
			mapSceneY.setValue(e.getY());
			drawMark();
		}
	};

	EventHandler<MouseEvent> onClickJoystick = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
			sceneX = t.getSceneX();
			sceneY = t.getSceneY();
			translateX = ((Circle) (t.getSource())).getTranslateX();
			translateY = ((Circle) (t.getSource())).getTranslateY();
		}
	};

	EventHandler<MouseEvent> onMoveJoystick = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
			double newTranslateX = translateX + t.getSceneX() - sceneX;
			double newTranslateY = translateY + t.getSceneY() - sceneY;
			if (inCircle(newTranslateX, newTranslateY)) {
				((Circle) (t.getSource())).setTranslateX(newTranslateX);
				((Circle) (t.getSource())).setTranslateY(newTranslateY);
				if (manual.isSelected()) {
					aileron.setValue(fixedX(newTranslateX));
					elevator.setValue(fixedY(newTranslateY));
					viewModel.setJoystick();
				}
			}
		}
	};

	private double fixedX(double n) {
		double min = border.getCenterX() - (border.getRadius() - joystick.getRadius());

		return ((n - min) / (((border.getRadius() - joystick.getRadius()) + border.getCenterX()) - min) * 2 - 1);
	}

	private double fixedY(double n) {
		double min = (border.getRadius() - joystick.getRadius()) + border.getCenterY();

		return (((n - min) / ((border.getCenterY() - (border.getRadius() - joystick.getRadius())) - min) * 2 - 1));
	}

	private boolean inCircle(double x, double y) {
		return (Math.pow((x - border.getCenterX()), 2) + Math.pow((y - border.getCenterY()), 2)) <= Math
				.pow(border.getRadius() - joystick.getRadius(), 2);
	}

	EventHandler<MouseEvent> onReleaseJoystick = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
			((Circle) (t.getSource())).setTranslateX(translateX);
			((Circle) (t.getSource())).setTranslateY(translateY);
		}
	};

	public void setViewModel(ViewModel viewModel) {
		this.viewModel = viewModel;
		viewModel.script.bindBidirectional(textArea.textProperty());

		throttle.valueProperty().bindBidirectional(viewModel.throttle);
		rudder.valueProperty().bindBidirectional(viewModel.rudder);

		aileron = new SimpleDoubleProperty();
		elevator = new SimpleDoubleProperty();
		aileron.bindBidirectional(viewModel.aileron);
		elevator.bindBidirectional(viewModel.elevator);

		airplaneX = new SimpleDoubleProperty();
		airplaneY = new SimpleDoubleProperty();
		airplaneX.bindBidirectional(viewModel.airplaneX);
		airplaneY.bindBidirectional(viewModel.airplaneY);

		startX = new SimpleDoubleProperty();
		startY = new SimpleDoubleProperty();
		startX.bindBidirectional(viewModel.startX);
		startY.bindBidirectional(viewModel.startY);

		offset = new SimpleDoubleProperty();
		offset.bindBidirectional(viewModel.offset);

		heading = new SimpleDoubleProperty();
		heading.bindBidirectional(viewModel.heading);

		mapSceneX = new SimpleDoubleProperty();
		mapSceneY = new SimpleDoubleProperty();
		mapSceneY.bindBidirectional(viewModel.markSceneY);
		mapSceneX.bindBidirectional(viewModel.markSceneX);

		path = new SimpleBooleanProperty();
		path.bindBidirectional(viewModel.path);
		path.setValue(false);

		plane = new Image[8];

		try {
			for (int i = 0; i <= 315; i += 45) {
				plane[i / 45] = new Image(new FileInputStream(PLANE_IMAGE_PATH_PREFIX + i + IMAGE_EXT));
			}

			markImage = new Image(new FileInputStream(MARK_IMAGE_PATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (location.getPath().contains(Main.FLIGHT_FXML_FILE)) {
			throttle.valueProperty().addListener((obs, oval, nval) -> {
				if (manual.isSelected()) {
					viewModel.setThrottle();
				}
			});

			rudder.valueProperty().addListener((obs, oval, nval) -> {
				if (manual.isSelected()) {
					viewModel.setRudder();
				}
			});

			joystick.setOnMousePressed(onClickJoystick);
			joystick.setOnMouseDragged(onMoveJoystick);
			joystick.setOnMouseReleased(onReleaseJoystick);
			mapMark.setOnMouseClicked(onClickMap);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o != viewModel) {
			return;
		}

		if (arg == null) {
			drawAirplane();
		} else {
			solution = (String[]) arg;
			this.drawLine();
		}
	}
}
