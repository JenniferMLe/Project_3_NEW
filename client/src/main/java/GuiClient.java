import javafx.application.Application;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.geometry.Pos;

//yo what up this is a test
public class GuiClient extends Application {

	ListView<String> game_state2;
	Client clientConnection;
	PokerInfo clientPokerInfo;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	void display_cards_scene(Stage primaryStage) {
		Label label = new Label("Your cards are ...");

		//

		Scene scene = new Scene(label, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.setTitle("This is a Client");
		primaryStage.show();
	}

	void display_wager_scene(Stage primaryStage) {
		Label ante_wager = new Label("Please enter your ante wager in dollars");
		TextField ante_wager_input = new TextField();
		ante_wager_input.setMaxWidth(100);

		Label pairPlus_wager = new Label("Please enter your pair plus wager in dollars or a 0 to skip");
		TextField pairPlus_input = new TextField();
		pairPlus_input.setMaxWidth(100);

		Button draw_cards = new Button("Draw Cards");

		// send info to Server and go to
		draw_cards.setOnAction(e -> {
			int wager1 = Integer.parseInt(ante_wager_input.getText());
			int wager2 = Integer.parseInt(pairPlus_input.getText());
			PokerInfo clientInfo = new PokerInfo(wager1, wager2);
			System.out.println("Ante Wager is " + wager1);
			System.out.println("Pair Plus Wager is " + wager2);
			clientConnection.send(clientInfo);
			display_cards_scene(primaryStage);
		});

		VBox allComponents = new VBox(10, ante_wager, ante_wager_input,
									  pairPlus_wager, pairPlus_input, draw_cards);

		allComponents.setAlignment(Pos.CENTER);

		Scene scene = new Scene(allComponents, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.setTitle("This is a Client");
		primaryStage.show();
	}

	void display_welcome_screen(Stage primaryStage) {
		Label welcome = new Label("Welcome to the game!");

		// port number
		Label enter_portNum = new Label("Enter Port Number");
		TextField portNum_input = new TextField();
		portNum_input.setMaxWidth(200);

		// IP address
		Label enter_IP_addr = new Label("Enter IP Address");
		TextField IP_addr_input = new TextField();
		IP_addr_input.setMaxWidth(200);

		// start game button
		Button start_game = new Button("Start Game");

		start_game.setOnAction(e -> {
			display_wager_scene(primaryStage);
			int port_number = Integer.parseInt(portNum_input.getText());
			String IP_addr = IP_addr_input.getText();
			System.out.println("Port Number is " + port_number);
			System.out.println("IP Address is " + IP_addr);

			clientConnection = new Client(data -> {
				Platform.runLater(()->{
					game_state2.getItems().add(data.toString());
				});
			}, port_number, IP_addr);

			clientConnection.start();
		});

		VBox components = new VBox(20, welcome, enter_portNum, portNum_input,
				  					enter_IP_addr, IP_addr_input, start_game);

		components.setAlignment(Pos.CENTER);

		Scene scene = new Scene(components, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		display_welcome_screen(primaryStage);
	}
}
