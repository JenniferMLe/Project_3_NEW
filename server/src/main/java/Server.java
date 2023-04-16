import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;

//test2
public class Server{

    int count = 1;
    ArrayList<Integer> card_deck;  // list of ints 0 - 51
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    TheServer server;
    private Consumer<Serializable> callback;
    int port_number;

    //boolean property to control client connections
    private volatile boolean allowClients = true;
    // Add a setter method for the boolean property
    public void setAllowClients(boolean allowClients) {
        this.allowClients = allowClients;
    }

    // constructor
    Server(Consumer<Serializable> call, int port_number){
        callback = call;
        this.port_number = port_number;
        server = new TheServer();
        server.start();

        card_deck =  new ArrayList<>();
        // initialize card_deck with integers 0-51
        for (int i = 0; i < 52; i++) {
            card_deck.add(i);
        }
    }

    public class TheServer extends Thread{

        public void run() {
            try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
                serverSocketChannel.bind(new InetSocketAddress(port_number));
                serverSocketChannel.configureBlocking(false);

                while (true) {
                    if (allowClients) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        if (socketChannel != null) {
                            ClientThread c = new ClientThread(socketChannel.socket(), count);
                            callback.accept("client has connected to server: " + "client #" + count);
                            clients.add(c);
                            c.start();
                            count++;
                        } else {
                            Thread.sleep(100);
                        }
                    } else {
                        Thread.sleep(1000);
                    }
                }
            } catch (Exception e) {
                callback.accept("Server socket did not launch");
            }
        }
    }

    class ClientThread extends Thread{
        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;
        PokerInfo info;

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;
            // this.info = new PokerInfo(0,0);
        }

        public ArrayList<Integer> draw_three_cards(int card_index) {
            ArrayList<Integer> three_cards = new ArrayList<>();
            for (int i = card_index; i < card_index + 3; i++) {
                three_cards.add(info.get_shuffledCards().get(i));
                info.cardIndex++;
            }
            return three_cards;
        }

        public void shuffleCards() {
            // shuffle deck
            ArrayList<Integer> shuffledCardDeck;
            shuffledCardDeck = card_deck;
            Collections.shuffle(shuffledCardDeck);
            info.set_shuffledCards(shuffledCardDeck);

            info.set_clientCards(draw_three_cards(info.cardIndex));
            info.set_serverCards(draw_three_cards(info.cardIndex));
        }

        // sends info to client
        public void send(PokerInfo instance) {
            try {
                //print statment to check what is being sent
                System.out.println("Sending to client: " + instance.get_clientCards().get(0));
                //another print statment to check what is being sent to the client from the server
                System.out.println("Sending to client LOL m8 : " + instance.get_serverCards().get(0));
                instance.print_info();
                out.writeObject(instance);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void run(){
            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            }
            catch(Exception e) {
                System.out.println("Streams not open");
            }

            while(true) {
                try {
                    // gets info from client
                    PokerInfo clientData = (PokerInfo) in.readObject();
                    callback.accept("received data");

                    System.out.println("newGame is " + clientData.newGame);
                    if (clientData.newGame) {
                        this.info = new PokerInfo(0,0);
                        shuffleCards();
                        info.set_anteWager(clientData.get_anteWager());
                        info.set_pairPlusWager(clientData.get_paiPlusWager());
                    }
                    info.fold = clientData.fold;
                    if (!compute.queenOrHigher(info.get_serverCards())) {
                        info.set_queenHigh(false);
                    } else {
                        info.winnings = compute.winnings(info.client_cards, info.get_anteWager(), info.get_paiPlusWager());
                    }
                    send(info);
                }
                catch(Exception e) {
                    callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
                    clients.remove(this);
                    break;
                }
            }
        }//end of run
    }//end of client thread
}






