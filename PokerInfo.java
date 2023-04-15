import java.io.Serializable;

// will pass data like wagers to server
public class PokerInfo implements Serializable {

    private int anteWager;
    private int pairPlusWager;

    public void set_anteWager(int anteWager) { this.anteWager = anteWager; }
    public void set_pairPlusWager(int pairPlusWager) { this.pairPlusWager = pairPlusWager; }

    public int get_anteWager() { return anteWager; }
    public int get_paiPlusWager() { return pairPlusWager; }

    // constructor
    PokerInfo(int anteWager, int pairPlusWager) {
        this.anteWager = anteWager;
        this.pairPlusWager = pairPlusWager;
    }
}
