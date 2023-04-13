import javafx.application.Application;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import javafx.geometry.Insets;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

//yo what up this is a test
public class GuiClient extends Application {

	ListView<String> game_state2;
	Client clientConnection;
	PokerInfo clientPokerInfo;   // where data is sent and copied

	// private int wager1Global;
	// private int wager2Global;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	void display_cards_scene(Stage primaryStage) {

		MenuBar menuBar = new MenuBar();
		Menu optionsMenu = new Menu("Options");
		MenuItem exitItem = new MenuItem("Exit");
		MenuItem freshStartItem = new MenuItem("Fresh Start");
		MenuItem newLookItem = new MenuItem("NewLook");
		optionsMenu.getItems().addAll(exitItem, freshStartItem, newLookItem);
		menuBar.getMenus().add(optionsMenu);

		// Player and dealer card areas
		Label playerCardsLabel = new Label("Player Cards");
		Label dealerCardsLabel = new Label("Dealer Cards");

		// Add the logic and cards
		// this.clientPokerInfo = clientConnection.clientPokerInfo;
		clientPokerInfo.set_clientCards(clientConnection.clientPokerInfo.get_clientCards());
		String Cards = String.valueOf(clientPokerInfo.get_clientCards().get(0)) + "\n" +
				String.valueOf(clientPokerInfo.get_clientCards().get(1)) + "\n" +
				String.valueOf(clientPokerInfo.get_clientCards().get(2));
		Label cards = new Label(Cards);

		VBox playerCards = new VBox(20, playerCardsLabel, cards); // Add player card images here
		playerCards.setStyle("-fx-border-color: black; -fx-border-width: 1;");
		VBox dealerCards = new VBox(10, dealerCardsLabel); // Add dealer card images here

		// Wager and winnings areas
		Label anteWagerLabel = new Label("Ante Wager: " + clientPokerInfo.get_anteWager());
		Label pairPlusWagerLabel = new Label("Pair Plus Wager: " + clientPokerInfo.get_paiPlusWager());
		Label playerWinningsLabel = new Label("Player Winnings: "); // Add player winnings value here
		VBox wagerAndWinnings = new VBox(10, anteWagerLabel, pairPlusWagerLabel, playerWinningsLabel);

		// Game info area
		Label gameInfoLabel = new Label("Game Info:");
		TextArea gameInfo = new TextArea();
		gameInfo.setEditable(false);
		VBox gameInfoArea = new VBox(10, gameInfoLabel, gameInfo);

		// Layout and scene setup
		HBox cardAreas = new HBox(30, playerCards, dealerCards);
		cardAreas.setAlignment(Pos.CENTER);
		VBox mainLayout = new VBox(10, menuBar, cardAreas, wagerAndWinnings, gameInfoArea);
		mainLayout.setPadding(new Insets(10));
		Scene scene = new Scene(mainLayout, 600, 500);

		primaryStage.setScene(scene);
		primaryStage.setTitle("This is a Client");
		primaryStage.show();

		// print the cards one by one to the terminal
		for (int i = 0; i < clientPokerInfo.get_clientCards().size(); i++) {
			System.out.println(clientPokerInfo.get_clientCards().get(i));
		}
	}

	//
	void display_wager_scene(Stage primaryStage) {
		Label ante_wager = new Label("Please enter your ante wager in dollars");
		TextField ante_wager_input = new TextField();
		ante_wager_input.setMaxWidth(100);

		Label pairPlus_wager = new Label("Please enter your pair plus wager in dollars or a 0 to skip");
		TextField pairPlus_input = new TextField();
		pairPlus_input.setMaxWidth(100);

		Label doubleClick = new Label("Please double click button to draw cards");
		Button draw_cards = new Button("Draw Cards");

		draw_cards.setOnAction(e -> {
			int wager1 = Integer.parseInt(ante_wager_input.getText());
			int wager2 = Integer.parseInt(pairPlus_input.getText());
			// wager1Global = wager1;   don't need because we have the PokerInfo class
			// wager2Global = wager2;
			clientPokerInfo = new PokerInfo(wager1, wager2);
			clientConnection.send(clientPokerInfo);
			display_cards_scene(primaryStage);
			/*
			// make the confirmChoice button invisible
			// confirmChoice.setVisible(false);
			// make the text fields uneditable
			// ante_wager_input.setEditable(false);
			// pairPlus_input.setEditable(false);

			//show the draw cards button
			//if the pokerclient is not null, make the draw cards button visible and add event handler
			if(clientPokerInfo != null) {
				draw_cards.setVisible(true);
				draw_cards.setOnAction(e1 -> {
					display_cards_scene(primaryStage);
				});
			}

			 */
		});

		VBox allComponents = new VBox(10, ante_wager, ante_wager_input, pairPlus_wager,
									  pairPlus_input, doubleClick, draw_cards);

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
			// display_wager_scene(primaryStage);
			int port_number = Integer.parseInt(portNum_input.getText());
			String IP_addr = IP_addr_input.getText();
			System.out.println("Port Number is " + port_number);
			System.out.println("IP Address is " + IP_addr);

			clientConnection = new Client(data -> {
				Platform.runLater(()->{

				});
			}, port_number, IP_addr);
			clientConnection.start();
			display_wager_scene(primaryStage);
		});

		// set up VBox with all components
		VBox components = new VBox(20, welcome, enter_portNum, portNum_input,
				  					enter_IP_addr, IP_addr_input, start_game);
		components.setAlignment(Pos.CENTER);

		// set up scene
		Scene scene = new Scene(components, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		display_welcome_screen(primaryStage);
	}
}
