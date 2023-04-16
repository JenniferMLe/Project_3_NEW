import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import java.util.ArrayList;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

//yo what up this is a test
public class GuiClient extends Application {

	Client clientConn;
	MenuBar menuBar;
	ArrayList<Image> cardImages;
	HBox dealerCards, dealerCardsHidden;

	//initialize the pokertable image variable as global
	// HERE...

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


		//event handler for menu bar
		exitItem.setOnAction(e -> {
			Platform.exit();
		});

		//event handler for fresh start
		freshStartItem.setOnAction(e -> {
			//set pair plus wager to 0
			//set ante wager to 0
			//set winnings to 0
			//reshuffle deck(new cards for both dealer and player)
			//go back to wager scene
		});

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	void display_results(Stage primaryStage) {
		VBox components;
		Button play_again = new Button("Play Again");
		Button exit_game = new Button("Exit Game");
		HBox playExit = new HBox(20, play_again, exit_game);
		playExit.setAlignment(Pos.CENTER);

		play_again.setOnAction( e -> {
			clientConn.info.reset();
			display_wager_scene(primaryStage);
		});
		exit_game.setOnAction(e -> System.exit(0));

		if (clientConn.info.fold) {
			System.out.println("WINNINGS IS " + clientConn.info.winnings);
			Label lost = new Label("You folded and lost $" + (clientConn.info.winnings * -1));
			components = new VBox(20, lost, playExit);
		}
		else if (clientConn.info.queenHigh) {
			Label label;
			if (clientConn.info.winnings > 0) {
				label = new Label("You beat the dealer. You win $" + clientConn.info.winnings);
			}
			else if (clientConn.info.winnings < 0) {
				label = new Label("The dealer beat you. You lost $" + (clientConn.info.winnings * -1));
			}
			else {
				label = new Label("You tied with the dealer");
			}
			Label pairPlus;
			if (clientConn.info.get_paiPlusWager() > 0) {
				if (clientConn.info.winningsPair > 0) {
					pairPlus = new Label("You won $" + clientConn.info.winningsPair +
							" from your pair plus wager");
				} else {
					pairPlus = new Label("You lost $" + (clientConn.info.winningsPair * -1) +
							" from your pair plus wager");
				}
			} else {
				pairPlus = new Label("");
			}
			components = new VBox(15, label, pairPlus, playExit);
		}
		else {
			Label queenHigh = new Label("Dealer does not have a queen high or better.");
			Label enter =  new Label("Press enter to continue or change your wagers below");

			Label ante_wager = new Label("Ante Wager");
			TextField input = new TextField();
			input.setText(Integer.toString(clientConn.info.get_anteWager()));
			input.setMaxWidth(100);
			HBox wager1 = new HBox(10, ante_wager, input);
			wager1.setAlignment(Pos.CENTER);

			Label pair_plus = new Label("Pair Plus");
			TextField input2 = new TextField();
			input2.setText(Integer.toString(clientConn.info.get_paiPlusWager()));
			input2.setMaxWidth(100);
			HBox wager2 = new HBox(10, pair_plus, input2);
			wager2.setAlignment(Pos.CENTER);

			EventHandler<ActionEvent> myHandler = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					int wager1 = Integer.parseInt(input.getText());
					int wager2 = Integer.parseInt(input2.getText());
					if ((wager1 < 5 || wager1 > 25) || (wager2 != 0 && (wager2 < 5 || wager2 > 25))){
						Alert alert = new Alert(Alert.AlertType.WARNING,
								"Your ante wager and pair plus wager if not $0 cannot" +
										" be less than $5 or greater than $25 ");
						alert.showAndWait();
					}
					else {
						clientConn.info.set_anteWager(wager1);
						clientConn.info.set_pairPlusWager(wager2);
						clientConn.info.nextHand = true;
						clientConn.send(clientConn.info);
						display_cards_scene(primaryStage);
					}
				}
			};
			input.setOnAction(myHandler);
			input2.setOnAction(myHandler);

			components = new VBox(15, queenHigh, enter, wager1, wager2);
		}
		components.setAlignment(Pos.CENTER);
		Scene scene = new Scene(components, 600, 600);
		primaryStage.setScene(scene);
		primaryStage.setTitle("This is a Client");
		primaryStage.show();
	}

	void display_cards_scene(Stage primaryStage) {

		create_menu_bar();

		// play wager area
		Label playWager = new Label("Make your play wager. This must be equal to your ante wager.");
		TextField input = new TextField();
		input.setText(Integer.toString(clientConn.info.get_anteWager()));
		input.setMaxWidth(100);
		Label enter = new Label("Press enter to continue");

		// HBox to hold components
		VBox playWagerBox = new VBox(5, playWager, input, enter);
		playWagerBox.setAlignment(Pos.CENTER);
		playWagerBox.setVisible(false);

		// So users can choose when to see results
		Button seeResults = new Button("See Results");
		seeResults.setOnAction(e -> display_results(primaryStage));
		seeResults.setVisible(false);

		input.setOnAction(e -> {
			if (Integer.parseInt(input.getText()) != clientConn.info.get_anteWager()) {
				Alert alert = new Alert(Alert.AlertType.WARNING,
						"Your play wager must be equal to your ante wager");
				alert.showAndWait();
			} else {
				dealerCards.setVisible(true);
				dealerCardsHidden.setVisible(false);
				playWagerBox.setVisible(false);
				seeResults.setVisible(true);
			}
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
			clientConn.send(clientConn.info);
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
//		gameInfo.setText(clientConn.info.getGameMessage());

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
		borderPane.setBottom(gameInfoArea);
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

			clientConn.info.set_anteWager(wager1);
			clientConn.info.set_pairPlusWager(wager2);
			clientConn.info.newGame = true;
			clientConn.send(clientConn.info);

			confirmChoice.setVisible(false);  //make the confirm choice button invisible
			draw_cards.setVisible(true);      //make the draw cards button visible
			clientConn.info.newGame = false;

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

		Scene scene = new Scene(allComponents, 500, 500);
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
		IP_addr_input.setText("127.0.0.1");
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

		Scene scene = new Scene(components, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		loadCardImages();
		display_welcome_screen(primaryStage);
	}
}