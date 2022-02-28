package com.tests.chess.engine.board;

import static org.junit.Assert.*;

import org.junit.Test;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.ai.MiniMax;
import com.chess.engine.player.ai.MoveStrategy;

public class TestBoard {

	@Test
	public void initialBoard() {
		
		// Initialize a new standard board to run tests
		final Board board = Board.createStandardBoard();
		// Check that the white player only has 20 legal moves to start
	    assertEquals(board.currentPlayer().getLegalMoves().size(), 20);
	    // Check that the black player only has 20 legal moves to start
	    assertEquals(board.currentPlayer().getOpponent().getLegalMoves().size(), 20);
	    
	    // Check that the white player is not in check
	    assertFalse(board.currentPlayer().isInCheck());
	    // Check that the white player is not in check mate
	    assertFalse(board.currentPlayer().isInCheckMate());
	    // Check that the white player is not castled
	    assertFalse(board.currentPlayer().isCastled());
	    
//			assertTrue(board.currentPlayer().isKingSideCastleCapable());
//			assertTrue(board.currentPlayer().isQueenSideCastleCapable());
	    
	    // Check that the current player is the white player
	    assertEquals(board.currentPlayer(), board.whitePlayer());
	    // Check that the opponent is the black player
	    assertEquals(board.currentPlayer().getOpponent(), board.blackPlayer());
	    
	    // Check that the black player is not in check
	    assertFalse(board.currentPlayer().getOpponent().isInCheck());
	    // Check that the black player is not in check mate
	    assertFalse(board.currentPlayer().getOpponent().isInCheckMate());
	    // Check that the black player is not castled
	    assertFalse(board.currentPlayer().getOpponent().isCastled());
	    
//	  		assertTrue(board.currentPlayer().getOpponent().isKingSideCastleCapable());
//	  		assertTrue(board.currentPlayer().getOpponent().isQueenSideCastleCapable());
//	  		assertEquals(new StandardBoardEvaluator().evaluate(board, 0), 0);
	}
	
	/*
	@Test
	public void testFoolsMate() {
		
		// Initialize a new standard board
		final Board board = Board.createStandardBoard();
		
		
		
		// Initialize a new move transition to test first move
		final MoveTransition t1 = board.currentPlayer().makeMove(Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("F2"),
																BoardUtils.getCoordinateAtPosition("F3")));
		// Check if the move is completed
		assertTrue(t1.getMoveStatus().isDone());
		
		
		
		// Initialize a new move transition to test second move
		final MoveTransition t2 = t1.getTransitionBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t1.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("e7"), 
																					BoardUtils.getCoordinateAtPosition("e5")));
		// Check if the move is completed
		assertTrue(t2.getMoveStatus().isDone());
		
		
		
		// Initialize a new move transition to test second move
		final MoveTransition t3 = t2.getTransitionBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t2.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("g2"), 
																					BoardUtils.getCoordinateAtPosition("g4")));
		// Check if the move is completed
		assertTrue(t3.getMoveStatus().isDone());
		
		
		// Initialize a new move strategy
		final MoveStrategy strategy = new MiniMax(4);
		// Initialize a new ai move to thest the move strategy
		final Move aiMove = strategy.execute(t3.getTransitionBoard());
		// Assign the value of the best move
		final Move bestMove = Move.MoveFactory.createMove(t3.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d8"), BoardUtils.getCoordinateAtPosition("h4"));
		// Check if the ai move and best move are the same
		assertEquals(aiMove, bestMove);
	}
*/
}
