import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class compute {

    static ArrayList<Integer> shuffleCards(ArrayList<Integer> orderedDeck) {
        ArrayList<Integer> shuffledDeck = new ArrayList<>();
        shuffledDeck = orderedDeck;
        Collections.shuffle(shuffledDeck);
        return shuffledDeck;
    }

    static boolean queenOrHigher(ArrayList<Integer> dealerCards) {
        for (int card: dealerCards) {
            if (card % 13 == 11 || card % 13 == 12 || card % 13 == 0)  {
                return true;
            }
        }
        return false;
    }

    static int winnings(ArrayList<Integer> playerCards,
                        int anteWager, int playWager) {

        int totalWager = anteWager + playWager;

        int num1 = playerCards.get(0);
        int num2 = playerCards.get(1);
        int num3 = playerCards.get(2);

        boolean straight = (num1 == num2 + 1 && num2 == num3 + 1) || (num1 == num2 - 1 && num2 == num3 - 1);
        boolean flush = ((num1 / 13) == (num2 / 13)) && ((num2 / 13) == (num3 / 13));
        boolean threeOfAKind = ((num1 % 13) == (num2 % 13)) && ((num2 % 13) == (num3 % 13));
        boolean pair = ((num1 % 13) == (num2 % 13)) ^ ((num2 % 13) == (num3 % 13));

        if (straight && flush) { return totalWager + (totalWager * 40); }
        else if (threeOfAKind) { return totalWager + (totalWager * 30); }
        else if (straight)     { return totalWager + (totalWager * 6); }
        else if (flush)        { return totalWager + (totalWager * 3); }
        else if (pair)         { return totalWager + totalWager; }
        else { return 0; }
    }
}

