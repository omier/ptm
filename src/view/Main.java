package view;

import commands.DisconnectCommand;
import interpreter.AutoPilotParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import viewmodel.ViewModel;

public class Main extends Application {
	public static final String FLIGHT_FXML_FILE = "Flight.fxml";
	public static final String FLIGHT_WINDOW_TITLE = "Flight Gear Joystick";
	
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader fxml = new FXMLLoader(getClass().getResource(FLIGHT_FXML_FILE));
		Parent parent = fxml.load();
		FlightController ctl = fxml.getController();
		Model m = new Model();
		ViewModel vm = new ViewModel();
		
		m.addObserver(vm);
		vm.setModel(m);
		vm.addObserver(ctl);
		ctl.setViewModel(vm);
		stage.setTitle(FLIGHT_WINDOW_TITLE);
		stage.setScene(new Scene(parent));
		stage.show();
		
		stage.setOnCloseRequest(event -> {
			DisconnectCommand cmd = new DisconnectCommand();
			cmd.executeCommand(null);
			AutoPilotParser.thread.interrupt();
			m.stopAll();
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
