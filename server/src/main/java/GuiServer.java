import javafx.application.Application;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.geometry.Pos;

public class GuiServer extends Application {

	ListView<String> game_state;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	void display_server_scene(Stage primaryStage) {
		Label state = new Label("State of Game");
		VBox server_box = new VBox(20, state, game_state);
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
			int port_number = Integer.valueOf(port_input.getText());
			System.out.println("Port is " + port_number);

			Server serverConnection = new Server(data -> {
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
