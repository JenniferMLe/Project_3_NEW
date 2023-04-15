import javafx.application.Application;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.geometry.VPos;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;

//yo what up this is a test
public class GuiClient extends Application {

	ListView<String> game_state2;
	Client clientConn;
	// PokerInfo clientPokerInfo;   // where data is sent and copied
	MenuBar menuBar;
	ArrayList<Image> cardImages;
	HBox dealerCards, dealerCardsHidden;

	//initialize the pokertable image variable as global

	private void loadCardImages() {
		cardImages = new ArrayList<>();
		String[] suits = {"clubs", "spades", "hearts", "diamonds"};
		String[] ranks = {"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};

		for (String suit : suits) {
			for (String rank : ranks) {
				String filename = "/PNG-cards/" + rank + "_of_" + suit + ".png";
				Image image = new Image(getClass().getResourceAsStream(filename));
				cardImages.add(image);
			}
		}
	}

	private ImageView getCardImageView(int number) {
		Image cardImage;
		ImageView imageView;

		if (number == 52) {
			cardImage = new Image("back.png");
			imageView = new ImageView(cardImage);

		} else {
			int suit = number / 13;
			int rank = number % 13;
			cardImage = cardImages.get(suit * 13 + rank);
			imageView = new ImageView(cardImage);
		}
		imageView.setFitWidth(60);
		imageView.setPreserveRatio(true);
		return imageView;

		/*
		int suit = number / 13;
		int rank = number % 13;
		Image cardImage = cardImages.get(suit * 13 + rank);
		ImageView imageView = new ImageView(cardImage);
		imageView.setFitWidth(60);
		imageView.setPreserveRatio(true);
		return imageView;
		 */
	}

	private void create_menu_bar() {
		menuBar = new MenuBar();
		Menu optionsMenu = new Menu("Options");
		MenuItem exitItem = new MenuItem("Exit");
		MenuItem freshStartItem = new MenuItem("Fresh Start");
		MenuItem newLookItem = new MenuItem("NewLook");
		optionsMenu.getItems().addAll(exitItem, freshStartItem, newLookItem);
		menuBar.getMenus().add(optionsMenu);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	void display_results(Stage primaryStage) {
		System.out.println("fold is " + clientConn.info.fold);
		Label result = new Label("This is the results scene");
		result.setAlignment(Pos.CENTER);
		VBox components;

		if (clientConn.info.fold) {
			int totalWager = clientConn.info.get_anteWager() + clientConn.info.get_paiPlusWager();
			Label lost = new Label("You lost $" + totalWager);
			Button play_again = new Button("Play Again");
			components = new VBox(20, lost, play_again);
		}
		else if (clientConn.info.queenHigh) {
			Label won = new Label("You won $" + clientConn.info.winnings);
			Button play_again = new Button("Play Again");
			components = new VBox(20, won, play_again);
		}
		else {
			Label next_hand = new Label("Dealer does not have a queen high or better. " +
					"Click to see dealer's next hand.");
			Button nextHand_button = new Button("See next hand");
			components = new VBox(20, next_hand, nextHand_button);
		}
		components.setAlignment(Pos.CENTER);
		Scene scene = new Scene(components, 700, 700);
		primaryStage.setScene(scene);
		primaryStage.setTitle("This is a Client");
		primaryStage.show();
	}

	void display_cards_scene(Stage primaryStage) {

		create_menu_bar();

		// play wager area
		Label playWager = new Label("Enter your play wager. This must\nbe equal to your ante wager.");
		TextField input = new TextField();
		input.setMaxWidth(100);
		Button continue_ = new Button("Continue");
		HBox playWagerBox = new HBox(10, playWager, input, continue_);
		playWagerBox.setAlignment(Pos.CENTER);
		playWagerBox.setVisible(false);
		Button seeResults = new Button("See Results");
		seeResults.setOnAction(e -> display_results(primaryStage));
		seeResults.setVisible(false);

		continue_.setOnAction(e -> {
			dealerCards.setVisible(true);
			dealerCardsHidden.setVisible(false);
			playWagerBox.setVisible(false);
			seeResults.setVisible(true);
		});

		// Play or fold area
		Label chooseOne = new Label("Choose One");
		Button fold = new Button("FOLD");
		Button play = new Button("PLAY");
		VBox playOrFold = new VBox(10, chooseOne, fold, play);
		playOrFold.setAlignment(Pos.CENTER);

		play.setOnAction(e -> {
			playWagerBox.setVisible(true);
			playOrFold.setVisible(false);
		});

		fold.setOnAction(e -> {
			clientConn.info.fold = true;
			display_results(primaryStage);
		});

		// Load the image
		Image pokerTable = new Image("PokerTable.png");
		ImageView pokerTableImage = new ImageView(pokerTable);
		pokerTableImage.setFitWidth(500);
		pokerTableImage.setPreserveRatio(true);

		// Player and dealer card areas
		Label playerCardsLabel = new Label("Player Cards:");
		playerCardsLabel.setStyle("-fx-text-fill: #FFFFFF");
		Label dealerCardsLabel = new Label("Dealer Cards:");
		dealerCardsLabel.setStyle("-fx-text-fill: #FFFFFF");
		Label cardsHidden = new Label("Dealer Cards: ");

		// clientPokerInfo.set_clientCards(clientConn.clientPokerInfo.get_clientCards());
        // clientPokerInfo.set_serverCards(clientConn.clientPokerInfo.get_serverCards());

		// player cards
		ImageView card1 = getCardImageView(clientConn.info.get_clientCards().get(0));
		ImageView card2 = getCardImageView(clientConn.info.get_clientCards().get(1));
		ImageView card3 = getCardImageView(clientConn.info.get_clientCards().get(2));
		HBox playerCards = new HBox(10, playerCardsLabel, card1, card2, card3);

		// dealer cards hidden
		ImageView hiddenCard1 = getCardImageView(52);
		ImageView hiddenCard2 = getCardImageView(52);
		ImageView hiddenCard3 = getCardImageView(52);
		dealerCardsHidden = new HBox(10, cardsHidden, hiddenCard1, hiddenCard2, hiddenCard3);

		// dealer cards
		ImageView dealerCard1 = getCardImageView(clientConn.info.get_serverCards().get(0));
		ImageView dealerCard2 = getCardImageView(clientConn.info.get_serverCards().get(1));
		ImageView dealerCard3 = getCardImageView(clientConn.info.get_serverCards().get(2));
		dealerCards = new HBox(10, dealerCardsLabel, dealerCard1, dealerCard2, dealerCard3);
		dealerCards.setVisible(false);

		VBox mainBox = new VBox(20, dealerCardsHidden, dealerCards, playerCards);

		GridPane gridPane = new GridPane();
		gridPane.add(mainBox, 0, 0);
		gridPane.setAlignment(Pos.CENTER);

		// Set constraints to control the position of dealer and player cards
		// gridPane.setValignment(mainBox, VPos.TOP);
		// gridPane.setMargin(mainBox, new Insets(-10, 0, 0, 0)); // Adjust margin for dealerCards

		// Wager and winnings areas
		Label anteWagerLabel = new Label("Ante Wager: " + clientConn.info.get_anteWager());
		Label pairPlusWagerLabel = new Label("Pair Plus Wager: " + clientConn.info.get_paiPlusWager());
		Label playerWinningsLabel = new Label("Player Winnings: ");
		VBox wagerAndWinnings = new VBox(10, anteWagerLabel, pairPlusWagerLabel, playerWinningsLabel);
		wagerAndWinnings.setAlignment(Pos.CENTER);

		// Game info area
		Label gameInfoLabel = new Label("Game Info:");
		TextArea gameInfo = new TextArea();
		gameInfo.setEditable(false);
		gameInfo.setPrefSize(50, 120);
		VBox gameInfoArea = new VBox(10, seeResults, playWagerBox, gameInfoLabel, gameInfo);
		gameInfoArea.setAlignment(Pos.CENTER);

		// Layout and scene setup
		StackPane mainStack = new StackPane(pokerTableImage, gridPane);
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(menuBar);
		borderPane.setLeft(wagerAndWinnings);
		borderPane.setCenter(mainStack);
		borderPane.setBottom(gameInfoArea);   // gameInfoArea
		borderPane.setRight(playOrFold);

		// set up scene
		Scene scene = new Scene(borderPane, 700, 700);
		primaryStage.setScene(scene);
		primaryStage.setTitle("This is a Client");
		primaryStage.show();
	}

	void display_wager_scene(Stage primaryStage) {
		Label ante_wager = new Label("Please enter your ante wager in dollars");
		ante_wager.setStyle("-fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;" +
				"-fx-font-size: 16px; -fx-text-fill: #000000;");
		Spinner<Integer> ante_wager_input = new Spinner<>(5, 25, 5);
		ante_wager_input.setMaxWidth(100);


		Label pairPlus_wager = new Label("Please enter your pair plus wager in dollars or a 0 to skip");
		pairPlus_wager.setStyle("-fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;" +
				"-fx-font-size: 16px; -fx-text-fill: #000000;");
		Spinner<Integer> pairPlus_input = new Spinner<>();
		pairPlus_input.setMaxWidth(100);

		Integer[] possibleValues = new Integer[22];
		possibleValues[0] = 0;
		for (int i = 1; i <= 21; i++) {
			possibleValues[i] = i + 4;
		}

		SpinnerValueFactory<Integer> pairPlusValueFactory =
				new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList(possibleValues));
		pairPlusValueFactory.setValue(0);
		pairPlus_input.setValueFactory(pairPlusValueFactory);

		Button draw_cards = new Button("Draw Cards");
		draw_cards.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;" +
				"-fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; -fx-font-size: 16px;");

		// create a new button called "confirm Choice"
		Button confirmChoice = new Button("Confirm Choice");
		confirmChoice.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;" +
				"-fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; -fx-font-size: 16px;");

		//make the draw cards button invisible
		draw_cards.setVisible(false);
		//create an event handler for the confirm choice button
		confirmChoice.setOnAction(e -> {
			int wager1 = Integer.parseInt(ante_wager_input.getValue().toString());
			int wager2 = Integer.parseInt(pairPlus_input.getValue().toString());
			// clientPokerInfo = new PokerInfo(wager1, wager2);
			clientConn.info.set_anteWager(wager1);
			clientConn.info.set_pairPlusWager(wager2);
			clientConn.send(clientConn.info);
			// clientConn.send(clientPokerInfo);
			//make the confirm choice button invisible
			confirmChoice.setVisible(false);
			//make the draw cards button visible
			draw_cards.setVisible(true);
			//create event handler for the draw cards button
			draw_cards.setOnAction(e1 -> {
				display_cards_scene(primaryStage);
			});

		});
		ImageView spadeImage = new ImageView(new Image(getClass().getResourceAsStream("/spade.png")));
		ImageView clubImage = new ImageView(new Image(getClass().getResourceAsStream("/club.png")));
		ImageView heartImage = new ImageView(new Image(getClass().getResourceAsStream("/heart.png")));
		ImageView diamondImage = new ImageView(new Image(getClass().getResourceAsStream("/diamond.png")));

		//make each image very small
		spadeImage.setFitHeight(40);   spadeImage.setFitWidth(40);
		clubImage.setFitHeight(40);    clubImage.setFitWidth(40);
		heartImage.setFitHeight(40);   heartImage.setFitWidth(40);
		diamondImage.setFitHeight(40); diamondImage.setFitWidth(40);

		//create an hbox to hold these images and center
		HBox imageBox = new HBox(10, spadeImage, clubImage, heartImage, diamondImage);
		imageBox.setAlignment(Pos.CENTER);

		VBox allComponents = new VBox(10, imageBox, ante_wager, ante_wager_input, pairPlus_wager,
				pairPlus_input, draw_cards, confirmChoice);

		allComponents.setAlignment(Pos.CENTER);

		Scene scene = new Scene(allComponents, 600, 600);
		primaryStage.setScene(scene);
		primaryStage.setTitle("This is a Client");
		primaryStage.show();
	}

	void display_welcome_screen(Stage primaryStage) {
		Label welcome = new Label("Welcome to 3 Card Poker");
		welcome.setFont(Font.font("Impact", FontWeight.BOLD, 30));
		welcome.setTextFill(Color.web("#3a3a3a"));

		// Load the image and create an ImageView
		Image welcomeCardsImage = new Image(getClass().getResourceAsStream("/welcomeCards.PNG"));
		ImageView welcomeCardsImageView = new ImageView(welcomeCardsImage);
		welcomeCardsImageView.setPreserveRatio(true);
		welcomeCardsImageView.setFitWidth(200);

		Label enter_portNum = new Label("Enter Port Number");
		enter_portNum.setFont(Font.font("Arial", FontWeight.BOLD, 16));

		TextField portNum_input = new TextField();
		portNum_input.setMaxWidth(200);
		portNum_input.setStyle("-fx-font-size: 14px;");

		Label enter_IP_addr = new Label("Enter IP Address");
		enter_IP_addr.setFont(Font.font("Arial", FontWeight.BOLD, 16));

		TextField IP_addr_input = new TextField();
		IP_addr_input.setMaxWidth(200);
		IP_addr_input.setStyle("-fx-font-size: 14px;");

		Button start_game = new Button("Start Game");
		start_game.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		start_game.setStyle("-fx-base: #3a9ad9;");

		start_game.setOnAction(e -> {
			// Display_wager_scene(primaryStage);
			int port_number = Integer.parseInt(portNum_input.getText());
			String IP_addr = IP_addr_input.getText();
			System.out.println("Port Number is " + port_number);
			System.out.println("IP Address is " + IP_addr);

			clientConn = new Client(data -> {
				Platform.runLater(() -> {

				});
			}, port_number, IP_addr);
			clientConn.start();
			display_wager_scene(primaryStage);
		});

		VBox components = new VBox(20, welcome, welcomeCardsImageView, enter_portNum, portNum_input,
				enter_IP_addr, IP_addr_input, start_game);
		components.setAlignment(Pos.CENTER);
		components.setStyle("-fx-background-color: #f0f0f0;");
		components.setMinWidth(400);
		components.setMinHeight(400);
		components.setFillWidth(true);

		Scene scene = new Scene(components, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		loadCardImages();
		display_welcome_screen(primaryStage);
	}
}
