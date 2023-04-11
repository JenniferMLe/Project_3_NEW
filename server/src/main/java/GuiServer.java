import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.geometry.Pos;

public class GuiServer extends Application {

	int port_number = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	void welcome_scene(Stage primaryStage) {
		Label enter_port = new Label("Please enter the port number to listen to");
		TextField port_input = new TextField();
		port_input.setMaxWidth(200);
		Button start_server = new Button("Start Server");

		start_server.setOnAction(e -> {
			port_number = Integer.valueOf(port_input.getText());
			System.out.println("Port is " + port_number);

		});

		VBox server = new VBox(30, enter_port, port_input, start_server);
		server.setAlignment(Pos.CENTER);
		Scene scene = new Scene(server, 700, 700);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		welcome_scene(primaryStage);
	}
}
