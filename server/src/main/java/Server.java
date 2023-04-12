import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.*;

public class Server{

    int count = 1;
    ArrayList<Integer> card_deck;  // list of ints 0 - 51
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    TheServer server;
    private Consumer<Serializable> callback;
    int port_number;

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
            try(ServerSocket mysocket = new ServerSocket(port_number);){
                System.out.println("Server is waiting for a client!");

                while(true) {

                    ClientThread c = new ClientThread(mysocket.accept(), count);
                    callback.accept("client has connected to server: " + "client #" + count);
                    clients.add(c);
                    c.start();
                    count++;
                }
            }//end of try
            catch(Exception e) {
                callback.accept("Server socket did not launch");
            }
        }//end of while
    }

    class ClientThread extends Thread{
        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;
        PokerInfo clientPokerInfo;

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;
            this.clientPokerInfo = new PokerInfo(0,0);
        }

        public void selectCards() {
            // shuffle deck
            ArrayList<Integer> shuffledCardDeck;
            shuffledCardDeck = card_deck;
            Collections.shuffle(shuffledCardDeck);
            clientPokerInfo.set_shuffledCards(shuffledCardDeck);

            // draw 3 cards for clients
            ArrayList<Integer> cards1 = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                cards1.add(shuffledCardDeck.get(i));
                System.out.println(cards1.get(i).toString());
            }
            clientPokerInfo.set_clientCards(cards1);

            // draw 3 cards for server
            ArrayList<Integer> cards2 = new ArrayList<>();
            for (int i = 3; i < 6; i++) {
                cards2.add(shuffledCardDeck.get(i));
            }
            clientPokerInfo.set_serverCards(cards2);
        }

        // sends info to client
        public void send(PokerInfo instance) {
            try {
                //print statment to check what is being sent
                System.out.println("Sending to client: " + instance.get_clientCards().get(0));

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
                selectCards();

                send(clientPokerInfo);
            }
            catch(Exception e) {
                System.out.println("Streams not open");
            }

            while(true) {
                try {
                    // gets info from client
                    PokerInfo clientData = (PokerInfo) in.readObject();
                    callback.accept("Client's ante wager is " + clientData.get_anteWager() +
                                       "\nClient's pair plus wager is " + clientData.get_paiPlusWager());
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






