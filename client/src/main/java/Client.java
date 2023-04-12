import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.ArrayList;

public class Client extends Thread{
    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;

    private Consumer<Serializable> callback;
    int port_number = 0;
    String IP_addr = "";

    ArrayList<Integer> clientCards; //idk if this is needed



    Client(Consumer<Serializable> call, int port_number, String IP_addr){
        callback = call;
        this.port_number = port_number;
        this.IP_addr = IP_addr;
    }

    public void run() {
        try {
            socketClient = new Socket(IP_addr, port_number);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        } catch (Exception e) { }

        while (true) {
            try {
                // gets info from server
                PokerInfo serverData = (PokerInfo) in.readObject();
                callback.accept(serverData);


            } catch (Exception e) { }
        }
    }
    // sends info to server
    public void send(PokerInfo instance) {
        try {
            out.writeObject(instance);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


//    //receives 3 cards from server, return type is PokerInfo object
    public PokerInfo receiveCards() {
        try {
            PokerInfo serverData = (PokerInfo) in.readObject();
            clientCards = serverData.get_clientCards();
            callback.accept("Received client cards from server: " + clientCards.get(0));
            return serverData;
        } catch (Exception e) {
            callback.accept("Error receiving client cards from server: " + e.getMessage());
        }
        return null;
    }

//    public void receiveCards() {
//        try {
//            PokerInfo serverData = (PokerInfo) in.readObject();
//            clientCards = serverData.get_clientCards();
//            System.out.println("Received client cards from server: " + clientCards.get(0));
//        } catch (Exception e) {
//            callback.accept("Error receiving client cards from server: " + e.getMessage());
//        }
//    }



}
