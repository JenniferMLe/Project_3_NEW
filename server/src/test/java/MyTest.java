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

	@Test
	public void testStraight() {
		assertTrue(compute.straight(new ArrayList<>(Arrays.asList(2, 3, 4))));
		assertFalse(compute.straight(new ArrayList<>(Arrays.asList(2, 4, 5))));
	}

	@Test
	//test straight with different nums
	public void testStraight2() {
		assertTrue(compute.straight(new ArrayList<>(Arrays.asList(8, 9, 10))));
		assertFalse(compute.straight(new ArrayList<>(Arrays.asList(1, 4, 5))));
	}

	@Test
	//test flush
	public void testFlush() {
		assertTrue(compute.flush(new ArrayList<>(Arrays.asList(17, 13, 14))));
		assertFalse(compute.flush(new ArrayList<>(Arrays.asList(0, 13, 26))));
	}

	@Test
	//test flush with different nums
	public void testFlush2() {
		assertTrue(compute.flush(new ArrayList<>(Arrays.asList(17, 13, 14))));
		assertFalse(compute.flush(new ArrayList<>(Arrays.asList(0, 13, 26))));
	}

	@Test
	//test 3 of a kind
	public void testThreeOfAKind() {
		assertTrue(compute.threeOfAKind(new ArrayList<>(Arrays.asList(0, 13, 26))));
		assertFalse(compute.threeOfAKind(new ArrayList<>(Arrays.asList(0, 13, 28))));
	}

	@Test
	//test 3 of a kind with different nums
	public void testThreeOfAKind2() {
		assertTrue(compute.threeOfAKind(new ArrayList<>(Arrays.asList(0, 13, 26))));
		assertFalse(compute.threeOfAKind(new ArrayList<>(Arrays.asList(0, 13, 28))));
	}

















}
