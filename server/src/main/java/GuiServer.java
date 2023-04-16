import javafx.application.Application;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
//import insets
import javafx.geometry.Insets;



public class GuiServer extends Application {

	ListView<String> game_state;
	Server serverConnection;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	void display_server_scene(Stage primaryStage) {
		Label state = new Label("State of Game");

		// Add radio buttons to turn server on and off
		RadioButton on_button = new RadioButton("Server ON");
		RadioButton off_button = new RadioButton("Server OFF");
		ToggleGroup toggle_group = new ToggleGroup();
		on_button.setToggleGroup(toggle_group);
		off_button.setToggleGroup(toggle_group);
		on_button.setSelected(true);

		toggle_group.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
			if (newVal == on_button) {
				serverConnection.setAllowClients(true);
			} else if (newVal == off_button) {
				serverConnection.setAllowClients(false);
			}
		});

		HBox button_box = new HBox(20, on_button, off_button);
		button_box.setAlignment(Pos.CENTER);

		VBox server_box = new VBox(20, state, button_box, game_state);
		server_box.setAlignment(Pos.CENTER);
		Scene scene = new Scene(server_box, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.setTitle("This is the Server");
		primaryStage.show();
	}

	void display_welcome_scene(Stage primaryStage) {
		Label enter_port = new Label("Please enter the port number to listen to");
		TextField port_input = new TextField();
		port_input.setMaxWidth(200);
		Button start_server = new Button("Start Server");

		start_server.setOnAction(e -> {
			display_server_scene(primaryStage);
			int port_number = Integer.parseInt(port_input.getText());
			System.out.println("Port is " + port_number);

			serverConnection = new Server(data -> {
				Platform.runLater(()->{
					game_state.getItems().add(data.toString());
				});
			}, port_number);
		});

		// create VBox to hold all components
		VBox server = new VBox(30, enter_port, port_input, start_server);
		server.setAlignment(Pos.CENTER);

		// set up scene and display
		Scene scene = new Scene(server, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		game_state = new ListView<String>();
		display_welcome_scene(primaryStage);
	}
}
