/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. 
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentNMNK.gamma;

import static hanto.common.HantoPieceType.*;
import static hanto.common.MoveResult.*;
import static hanto.common.HantoPlayerColor.*;
import static org.junit.Assert.*;
import hanto.common.*;
import hanto.studentNMNK.HantoGameFactory;

import org.junit.*;

/**
 * Test cases for Beta Hanto.
 * 
 * @version Sep 14, 2014
 */
public class GammaHantoMasterTestOurs {
	/**
	 * Internal class for these test cases.
	 * 
	 * @version Sep 13, 2014
	 */
	class TestHantoCoordinate implements HantoCoordinate {
		private final int x, y;

		public TestHantoCoordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}

		/*
		 * @see hanto.common.HantoCoordinate#getX()
		 */
		@Override
		public int getX() {
			return x;
		}

		/*
		 * @see hanto.common.HantoCoordinate#getY()
		 */
		@Override
		public int getY() {
			return y;
		}

	}

	private static HantoGameFactory factory = null;
	private HantoGame game;

	@BeforeClass
	public static void initializeClass() {
		factory = HantoGameFactory.getInstance();
	}

	@Before
	public void setup() {
		// By default, blue moves first.
		game = factory.makeHantoGame(HantoGameID.GAMMA_HANTO, BLUE);
	}

	@Test // 1
	public void bluePlacesInitialButterflyAtOrigin() throws HantoException {
		final MoveResult mr = game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = game.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}

	@Test(expected = HantoException.class) // 2
	public void bluePlacesInitialPieceNotButterflyOrSparrow() throws HantoException {
		game.makeMove(CRAB, null, makeCoordinate(0, 0));
	}
	
	@Test(expected = HantoException.class)
	public void testWalkingMoreThanOnePiece() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, makeCoordinate(1, 0), makeCoordinate(3, 0));
	}
	
	@Test
	public void pieceMovedNextToOpposingColor() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		final MoveResult mr = game.makeMove(BUTTERFLY, makeCoordinate(0,0), makeCoordinate(1, 0));
		assertEquals(OK, mr);
	}
	
	@Test (expected = HantoException.class)
	public void pieceWalkMakesBoardSeparate() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(-1, 1));
		game.makeMove(BUTTERFLY, makeCoordinate(0,0), makeCoordinate(1, 0));
		game.makeMove(BUTTERFLY, makeCoordinate(-1,1), makeCoordinate(-2, 2));
	}
	
	@Test 
	public void pieceWalkMakesBoardStillContiguous() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(-1, 1));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		MoveResult mr = game.makeMove(SPARROW, makeCoordinate(-1,1), makeCoordinate(0, 1));
		assertEquals(OK, mr);
	}

	@Test (expected= HantoException.class)
	public void playerMovedNonExistentPiece() throws HantoException{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(-1, 1));
		game.makeMove(SPARROW, makeCoordinate(0,0), makeCoordinate(1, 0));
	}
	
	@Test(expected = HantoException.class) // 3
	public void redPlacesPieceNotButterflyOrSparrow() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(CRAB, null, makeCoordinate(0, 1));
	}

	@Test // 4
	public void redMovesSparrow() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		final MoveResult mr = game.makeMove(SPARROW, makeCoordinate(-1, 0), makeCoordinate(-1, 1));
		assertEquals(OK, mr);
	}

	@Test(expected = HantoException.class) // 5
	public void blueFirstMoveInInvalidLocation() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
	}

	@Test(expected = HantoException.class) // 6
	public void redFirstMoveNonAdjacentLocation() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
	}

	@Test(expected = HantoException.class) // 7
	public void redPlacesPieceAtAlreadyTakenLocation() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
	}

	@Test(expected = HantoException.class) // 8
	public void bluePlacesPieceAtAlreadyTakenLocation() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
	}

	@Test // 9
	public void redMakesValidMove() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		final MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		assertEquals(OK, mr);
	}

	@Test // 10
	public void blueMakesValidMove() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		final MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		assertEquals(OK, mr);
	}

	@Test(expected = HantoException.class) // 11
	public void blueMakesNonAdjacentLocation() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		game.makeMove(SPARROW, null, makeCoordinate(2, 2));
	}

	@Test(expected = HantoException.class) // 12
	public void bluePlacesTwoButterflies() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -2));
	}

	@Test(expected = HantoException.class) // 13
	public void blueDoesNotPlaceButteryByFourthTurn() throws HantoException {
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		game.makeMove(SPARROW, null, makeCoordinate(0, -3));
		game.makeMove(SPARROW, null, makeCoordinate(0, -4));
		game.makeMove(SPARROW, null, makeCoordinate(0, -5));
		game.makeMove(SPARROW, null, makeCoordinate(0, -6));
		game.makeMove(SPARROW, null, makeCoordinate(0, -7));
		game.makeMove(SPARROW, null, makeCoordinate(0, -8));
		game.makeMove(SPARROW, null, makeCoordinate(0, -9));
	}

	@Test() // 14
	public void drawAfterTwentyMoves() throws HantoException {
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		game.makeMove(SPARROW, null, makeCoordinate(0, -3));
		game.makeMove(SPARROW, null, makeCoordinate(0, 3));
		game.makeMove(SPARROW, null, makeCoordinate(0, -4));
		game.makeMove(SPARROW, null, makeCoordinate(0, 4));
		game.makeMove(SPARROW, null, makeCoordinate(0, -5));
		game.makeMove(SPARROW, null, makeCoordinate(0, 5));
		game.makeMove(SPARROW, null, makeCoordinate(0, -6));
		game.makeMove(SPARROW, null, makeCoordinate(0, 6));
		game.makeMove(SPARROW, null, makeCoordinate(0, -7));
		game.makeMove(SPARROW, null, makeCoordinate(0, 7));
		game.makeMove(SPARROW, null, makeCoordinate(0, -8));
		game.makeMove(SPARROW, null, makeCoordinate(0, 8));
		game.makeMove(SPARROW, null, makeCoordinate(0, -9));
		game.makeMove(SPARROW, null, makeCoordinate(0, 9));
		final MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(0, -10));
		assertEquals(DRAW, mr);
	}

	@Test() // 15
	public void blueButterflySurrounded() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(1, -1));
		final MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(-1, 1));
		assertEquals(RED_WINS, mr);
	}

	@Test() // 16
	public void redButterflySurrounded() throws HantoException {
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-1, -1));
		game.makeMove(SPARROW, null, makeCoordinate(1, -1));
		final MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(1, -2));
		assertEquals(BLUE_WINS, mr);
	}

	@Test() // 17
	public void blueButterflySurroundedIfRedGoesFirst() throws HantoException {
		HantoGame game2 = factory.getInstance().makeHantoGame(HantoGameID.BETA_HANTO, RED);
		game2.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game2.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		game2.makeMove(SPARROW, null, makeCoordinate(0, -2));
		game2.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game2.makeMove(SPARROW, null, makeCoordinate(-1, -1));
		game2.makeMove(SPARROW, null, makeCoordinate(1, -1));
		final MoveResult mr = game2.makeMove(SPARROW, null, makeCoordinate(1, -2));
		assertEquals(RED_WINS, mr);
	}

	@Test(expected = HantoException.class) // 18
	public void playerMovesAfterGameOver() throws HantoException {
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -2));
		game.makeMove(SPARROW, null, makeCoordinate(0, -3));
		game.makeMove(SPARROW, null, makeCoordinate(0, -4));
		game.makeMove(SPARROW, null, makeCoordinate(0, -5));
		game.makeMove(SPARROW, null, makeCoordinate(0, -6));
		game.makeMove(SPARROW, null, makeCoordinate(0, -7));
		game.makeMove(SPARROW, null, makeCoordinate(0, -8));
		game.makeMove(SPARROW, null, makeCoordinate(0, -9));
		game.makeMove(SPARROW, null, makeCoordinate(0, -10));
		game.makeMove(SPARROW, null, makeCoordinate(0, -11));
		game.makeMove(SPARROW, null, makeCoordinate(0, -12));
		game.makeMove(SPARROW, null, makeCoordinate(0, -13));
		game.makeMove(SPARROW, null, makeCoordinate(0, -14));
		game.makeMove(SPARROW, null, makeCoordinate(0, -15));
		game.makeMove(SPARROW, null, makeCoordinate(0, -16));
		game.makeMove(SPARROW, null, makeCoordinate(0, -17));
		game.makeMove(SPARROW, null, makeCoordinate(0, -18));
		game.makeMove(SPARROW, null, makeCoordinate(0, -19));
		game.makeMove(SPARROW, null, makeCoordinate(0, -20));
	}

	@Test() // 19
	public void printBoardState() throws HantoException {
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		assertNotNull(game.getPrintableBoard());
	}

	// Helper methods
	private HantoCoordinate makeCoordinate(int x, int y) {
		return new TestHantoCoordinate(x, y);
	}

}
