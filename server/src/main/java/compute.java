import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class compute {

    static boolean queenOrHigher(ArrayList<Integer> dealerCards) {
        for (int card: dealerCards) {
            if (card % 13 == 11 || card % 13 == 12)  {
                return true;
            }
        }
        return false;
    }

    // consecutive cards
    static boolean straight(ArrayList<Integer> cards) {
        Collections.sort(cards);
        int cardType1 = cards.get(0) % 13;
        int cardType2 = cards.get(1) % 13;
        int cardType3 = cards.get(2) % 13;
        return ((cardType1 == cardType2 - 1) && (cardType2 == cardType3 - 1));
    }

    // cards of the same suit
    static boolean flush(ArrayList<Integer> cards) {
        return (((cards.get(0) / 13) == (cards.get(1) / 13)) &&
                ((cards.get(1) / 13) == (cards.get(2) / 13)));
    }

    // cards of the same type
    static boolean threeOfAKind(ArrayList<Integer> cards) {
        return (((cards.get(0) % 13) == (cards.get(1) % 13)) &&
                ((cards.get(1) % 13) == (cards.get(2) % 13)));
    }

    // two cards of the same type
    static boolean pair(ArrayList<Integer> cards) {
        boolean bool1 = (((cards.get(0) % 13) == (cards.get(1) % 13)) ^
                ((cards.get(1) % 13) == (cards.get(2) % 13)));

        boolean bool2 = (cards.get(0) % 13) == (cards.get(2) % 13);

        return bool1 || bool2;
    }

    static int pairPlusWinnings(PokerInfo info) {
        int wager = info.get_paiPlusWager();

        if (info.get_paiPlusWager() > 0) {
            if (straight(info.client_cards) && flush(info.client_cards)) { return wager * 40; }
            else if (threeOfAKind(info.client_cards)) { return wager * 30; }
            else if (straight(info.client_cards))     { return wager * 6; }
            else if (flush(info.client_cards))        { return wager * 3; }
            else if (pair(info.client_cards))         { return wager; }
            else                                      { return info.get_paiPlusWager() * -1; }
        }
        return 0;
    }

    static int winnings(PokerInfo info) {

        int totalWager = info.get_anteWager() + info.get_anteWager();

        int clientHand = 0;
        int serverHand = 0;

        // rank clientHand
        if (straight(info.client_cards) && flush(info.client_cards)) { clientHand = 5; }
        else if (threeOfAKind(info.client_cards)) { clientHand = 4; }
        else if (straight(info.client_cards))     { clientHand = 3; }
        else if (flush(info.client_cards))        { clientHand = 2; }
        else if (pair(info.client_cards))         { clientHand = 1; }

        // rank serverHand
        if (straight(info.server_cards) && flush(info.server_cards)) { clientHand = 5; }
        else if (threeOfAKind(info.server_cards)) { serverHand = 4; }
        else if (straight(info.server_cards))     { serverHand = 3; }
        else if (flush(info.server_cards))        { serverHand = 2; }
        else if (pair(info.server_cards))         { serverHand = 1; }

        // if server hand ranks higher return negative winnings
        if (serverHand > clientHand) {
            return totalWager * -1;
        }
        // if client hand ranks higher return positive winnings
        else if (serverHand < clientHand) {
            return totalWager;
        }
        else {
            return 0;
        }
    }
}

