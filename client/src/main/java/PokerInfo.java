import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

//
public class PokerInfo implements Serializable {

    private int anteWager;
    private int pairPlusWager;
    ArrayList<Integer> shuffled_cards;
    ArrayList<Integer> client_cards;
    ArrayList<Integer> server_cards;
    private boolean readyToDraw;

    public void set_anteWager(int anteWager) { this.anteWager = anteWager; }
    public void set_pairPlusWager(int pairPlusWager) { this.pairPlusWager = pairPlusWager; }
    public void set_shuffledCards(ArrayList<Integer> shuffled_cards) { this.shuffled_cards = shuffled_cards; }
    public void set_clientCards(ArrayList<Integer> client_cards) { this.client_cards = client_cards; }
    public void set_serverCards(ArrayList<Integer> server_cards) { this.server_cards = server_cards; }
    public void set_readyToDraw(boolean readyToDraw) { this.readyToDraw = readyToDraw; }

    public int get_anteWager() { return anteWager; }
    public int get_paiPlusWager() { return pairPlusWager; }
    public ArrayList<Integer> get_shuffledCards() { return shuffled_cards; }
    public ArrayList<Integer> get_clientCards() { return client_cards; }
    public ArrayList<Integer> get_serverCards() { return server_cards; }
    public boolean get_readyToDraw() { return readyToDraw; }

    // constructor
    PokerInfo(int anteWager, int pairPlusWager) {
        this.anteWager = anteWager;
        this.pairPlusWager = pairPlusWager;
    }

}
