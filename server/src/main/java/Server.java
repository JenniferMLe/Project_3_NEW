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
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    TheServer server;
    private Consumer<Serializable> callback;
    int port_number;

    int TotalCountClients = 0;

    Set<String> uniqueClients = new HashSet<>();

    //boolean property to control client connections
    private volatile boolean allowClients = true;
    // Add a setter method for the boolean property
    public void setAllowClients(boolean allowClients) {
        this.allowClients = allowClients;
        if (!allowClients) {
            for (ClientThread client : clients) {
                client.setConnected(false);
                try {
                    client.connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // constructor
    Server(Consumer<Serializable> call, int port_number){
        callback = call;
        this.port_number = port_number;
        server = new TheServer();
        server.start();
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
                            TotalCountClients++;
                            callback.accept("Total number of clients: " + TotalCountClients);
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

        String uniqueId;

        private volatile boolean connected;

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;
            // this.info = new PokerInfo(0,0);
            this.uniqueId = s.getInetAddress().toString() + ":" + s.getPort();

            connected = true;

        }

        public void setConnected(boolean connected) {
            this.connected = connected;
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
            ArrayList<Integer> shuffledCardDeck = new ArrayList<>();
            // shuffledCardDeck = card_deck;
            for (int i = 0; i < 52; i++) {
                shuffledCardDeck.add(i);
            }
            Collections.shuffle(shuffledCardDeck);
            info.set_shuffledCards(shuffledCardDeck);
            info.set_clientCards(draw_three_cards(info.cardIndex));
            info.set_serverCards(draw_three_cards(info.cardIndex));
        }

        // sends info to client
        public void send(PokerInfo instance) {
            try {
                //print statment to check what is being sent
                // System.out.println("Sending to client: " + instance.get_clientCards().get(0));
                //another print statment to check what is being sent to the client from the server
                // System.out.println("Sending to client LOL m8 : " + instance.get_serverCards().get(0));
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

            while(connected) {
                try {
                    // gets info from client
                    PokerInfo clientData = (PokerInfo) in.readObject();

                    if (!uniqueClients.contains(uniqueId)) {
                        uniqueClients.add(uniqueId);
                    } else {
                        // Print callback statement if the same client is playing another hand
                        callback.accept("client #" + count + " is playing another hand");
                    }

                    callback.accept("client # " + count + " ante wager: $" +
                            clientData.get_anteWager() + " pair plus wager: $" + clientData.get_paiPlusWager());
                    System.out.println("newGame is " + clientData.newGame);
                    info = clientData;

                    if (clientData.fold) {
                        info.winnings = (info.get_anteWager() + -info.get_paiPlusWager()) * -1;
                        info.setGameMessage("Player has folded.");
                    }
                    else {
                        if (clientData.newGame) {
                            this.info = new PokerInfo(0, 0);
                            shuffleCards();
                            info.set_anteWager(clientData.get_anteWager());
                            info.set_pairPlusWager(clientData.get_paiPlusWager());
                        } else if (clientData.nextHand) {
                            info.server_cards = draw_three_cards(info.cardIndex);
                            info.nextHand = false;
                            info.set_anteWager(clientData.get_anteWager());
                            info.set_pairPlusWager(clientData.get_paiPlusWager());
                        }
                        if (!compute.queenOrHigher(info.get_serverCards())) {
                            info.set_queenHigh(false);
                        } else {
                            info.set_queenHigh(true);
                            info.winnings = compute.winnings(info);
                            info.winningsPair= compute.pairPlusWinnings(info);
                        }
                    }

                    int totalWinnings = info.winnings + info.winningsPair;

                    if(!info.fold) {
                        if (info.winnings > 0) {
                            callback.accept("client # " + count + " has won $" + totalWinnings);
                        } else {
                            callback.accept("client # " + count + " has lost $" + (-totalWinnings));
                        }
                    }
                    else {
                        callback.accept("client # " + count + " has folded and lost $" + (-info.winnings));
                    }



                    send(info);
                }
                catch(Exception e) {
                    callback.accept("client: " + count + " left the game!");
                    TotalCountClients--;
                    clients.remove(this);
                    callback.accept("Total number of clients: " + TotalCountClients);
                    break;
                }
            }
        }//end of run
    }//end of client thread
}






