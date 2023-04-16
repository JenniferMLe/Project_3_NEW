import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MyTest {

	@Test
	public void testShuffleCards() {
		ArrayList<Integer> orderedDeck = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
				13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
				38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51));
		ArrayList<Integer> shuffledDeck = compute.shuffleCards(orderedDeck);
		assertEquals(shuffledDeck.size(), orderedDeck.size());
		Collections.sort(shuffledDeck);
		assertEquals(shuffledDeck, orderedDeck);
	}

	@Test
	public void testQueenOrHigher_noQueenOrHigher() {
		ArrayList<Integer> dealerCards = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			dealerCards.add(i);
		}
		boolean expectedOutcome = false;
		assertEquals(expectedOutcome, compute.queenOrHigher(dealerCards));
	}

	@Test
	public void testQueenOrHigher_allAboveQueen() {
		ArrayList<Integer> dealerCards = new ArrayList<>();
		for (int i = 12; i < 17; i++) {
			dealerCards.add(i);
		}
		boolean expectedOutcome = true;
		assertEquals(expectedOutcome, compute.queenOrHigher(dealerCards));
	}

	@Test
	public void testQueenOrHigher_queenPresentNoHigher() {
		ArrayList<Integer> dealerCards = new ArrayList<>();
		dealerCards.add(11);
		for (int i = 0; i < 4; i++) {
			dealerCards.add(i);
		}
		boolean expectedOutcome = true;
		assertEquals(expectedOutcome, compute.queenOrHigher(dealerCards));
	}

	@Test
	public void testQueenOrHigher_multipleQueens() {
		ArrayList<Integer> dealerCards = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			dealerCards.add(11);
		}
		boolean expectedOutcome = true;
		assertEquals(expectedOutcome, compute.queenOrHigher(dealerCards));
	}

	@Test
	public void testQueenOrHigher_noQueenOrHigher2() {
		ArrayList<Integer> dealerCards = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			dealerCards.add(i + 13);
		}
		boolean expectedOutcome = false;
		assertEquals(expectedOutcome, compute.queenOrHigher(dealerCards));
	}

	//test case for winnings 3 of a kind
	@Test
	public void testWinnings_threeOfAKind() {
		ArrayList<Integer> playerCards = new ArrayList<>();
		playerCards.add(0);
		playerCards.add(13);
		playerCards.add(26);
		int anteWager = 10;
		int playWager = 10;
		int expectedOutcome = 620;
		assertEquals(expectedOutcome, compute.winnings(playerCards, anteWager, playWager));
	}

	@Test
	public void testWinnings_threeOfAKindKings() {
		ArrayList<Integer> playerCards = new ArrayList<>(java.util.Arrays.asList(11, 24, 37));
		int anteWager = 5;
		int playWager = 5;
		int expectedOutcome = 310;
		assertEquals(expectedOutcome, compute.winnings(playerCards, anteWager, playWager));
	}

	@Test
	public void testWinnings_threeOfAKindAces() {
		ArrayList<Integer> playerCards = new ArrayList<>(java.util.Arrays.asList(12, 25, 38));
		int anteWager = 15;
		int playWager = 15;
		int expectedOutcome = 930;
		assertEquals(expectedOutcome, compute.winnings(playerCards, anteWager, playWager));
	}

	@Test
	public void testFlush() {
		ArrayList<Integer> playerCards = new ArrayList<Integer>(Arrays.asList(2, 5, 8));
		int anteWager = 10;
		int playWager = 20;
		int expected = 120;
		int result = compute.winnings(playerCards, anteWager, playWager);
		assertEquals(expected, result);
	}

	@Test
	public void testFlush1() {
		ArrayList<Integer> playerCards = new ArrayList<>();
		playerCards.add(1);
		playerCards.add(4);
		playerCards.add(6);
		int anteWager = 5;
		int playWager = 10;
		int expectedOutput = 60;
		assertEquals(expectedOutput, compute.winnings(playerCards, anteWager, playWager));
	}

	@Test
	public void testFlush2() {
		ArrayList<Integer> playerCards = new ArrayList<>();
		playerCards.add(1);
		playerCards.add(4);
		playerCards.add(6);
		int anteWager = 5;
		int playWager = 10;
		int expectedOutput = 60;
		assertEquals(expectedOutput, compute.winnings(playerCards, anteWager, playWager));
	}

	@Test
	public void testStraight() {
		ArrayList<Integer> playerCards = new ArrayList<>();
		//add cards that mod 13 11, 12, 13
		playerCards.add(11);
		playerCards.add(12);
		playerCards.add(13);
		int anteWager = 10;
		int playWager = 20;
		int expected = 210;
		int result = compute.winnings(playerCards, anteWager, playWager);
		assertEquals(expected, result);
	}

	@Test
	public void testStraight1() {
		ArrayList<Integer> playerCards = new ArrayList<>();
		playerCards.add(13);
		playerCards.add(12);
		playerCards.add(11);
		int anteWager = 5;
		int playWager = 10;
		int expectedOutput = 105;
		assertEquals(expectedOutput, compute.winnings(playerCards, anteWager, playWager));
	}

	@Test
	public void testPair() {
		ArrayList<Integer> playerCards = new ArrayList<>();
		playerCards.add(0);
		playerCards.add(13);
		playerCards.add(28);
		int anteWager = 10;
		int playWager = 20;
		int expected = 60;
		int result = compute.winnings(playerCards, anteWager, playWager);
		assertEquals(expected, result);

	}

	@Test
	public void testPair1() {
		ArrayList<Integer> playerCards = new ArrayList<>();
		playerCards.add(14);
		playerCards.add(27);
		playerCards.add(51);
		int anteWager = 10;
		int playWager = 20;
		int expected = 60;
		int result = compute.winnings(playerCards, anteWager, playWager);
		assertEquals(expected, result);

	}

	@Test
	public void testPair2() {
		ArrayList<Integer> playerCards = new ArrayList<>();
		playerCards.add(14);
		playerCards.add(27);
		playerCards.add(51);
		int anteWager = 10;
		int playWager = 20;
		int expected = 60;
		int result = compute.winnings(playerCards, anteWager, playWager);
		assertEquals(expected, result);

	}

	@Test
	public void testPair3() {
		ArrayList<Integer> playerCards = new ArrayList<>();
		playerCards.add(14);
		playerCards.add(27);
		playerCards.add(51);
		int anteWager = 10;
		int playWager = 20;
		int expected = 60;
		int result = compute.winnings(playerCards, anteWager, playWager);
		assertEquals(expected, result);
	}

	@Test
	public void testStraightFlush() {
		ArrayList<Integer> playerCards = new ArrayList<>();
		playerCards.add(0);
		playerCards.add(1);
		playerCards.add(2);
		int anteWager = 10;
		int playWager = 20;
		int expected = 1230;
		int result = compute.winnings(playerCards, anteWager, playWager);
		assertEquals(expected, result);
	}

	@Test
	public void testStraightFlush1() {
		ArrayList<Integer> playerCards = new ArrayList<>();
		playerCards.add(4);
		playerCards.add(5);
		playerCards.add(6);
		int anteWager = 10;
		int playWager = 20;
		int expected = 1230;
		int result = compute.winnings(playerCards, anteWager, playWager);
		assertEquals(expected, result);
	}

	@Test
	public void testStraightFlush2() {
		ArrayList<Integer> playerCards = new ArrayList<>();
		playerCards.add(13);
		playerCards.add(14);
		playerCards.add(15);
		int anteWager = 10;
		int playWager = 20;
		int expected = 1230;
		int result = compute.winnings(playerCards, anteWager, playWager);
		assertEquals(expected, result);
	}

	@Test
	public void testStraightFlush3() {
		ArrayList<Integer> playerCards = new ArrayList<>();
		playerCards.add(26);
		playerCards.add(27);
		playerCards.add(28);
		int anteWager = 10;
		int playWager = 20;
		int expected = 1230;
		int result = compute.winnings(playerCards, anteWager, playWager);
		assertEquals(expected, result);
	}











}
