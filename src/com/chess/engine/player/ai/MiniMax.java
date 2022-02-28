package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;



public class MiniMax implements MoveStrategy {

	// Declare in instance of board evaluator
	private final BoardEvaluator boardEvaluator;
	// Variable to store the search depth
	private final int searchDepth;
	
	
	// Constructor
	public MiniMax(final int searchDepth) {
		// Assign the value of the board evaluator
		this.boardEvaluator = new StandardBoardEvaluator();
		// Assign the value of search depth
		this.searchDepth = searchDepth;
	}
	
	
	
	// Override the to string method for mini-max
	@Override
	public String toString() {
		// Return a string of mini-max
		return "MiniMax";
	}
	
	
	
	// Override the execute method from move strategy
	@Override
	public Move execute(Board board) {
		
		// Variable to store the current time in milliseconds
		final long startTime = System.currentTimeMillis();
		// Variable to store the best move
		Move bestMove = null;
		// Set the highest seen value to be the lowest number possible
		int highestSeenValue = Integer.MIN_VALUE;
		// Set the lowest seen value to be the highest number possible
		int lowestSeenValue = Integer.MAX_VALUE;
		// Variable to store the current value
		int currentValue;
		
		// Print message to the console
		System.out.println(board.currentPlayer() + " THINKING with depth = " + this.searchDepth);
		// Variable to store the number of possible legal moves
		int numMoves = board.currentPlayer().getLegalMoves().size();
		
		// Loop through all the current player's legal moves
		for (final Move move : board.currentPlayer().getLegalMoves()) {
			// Initialize a move transition to the test the move
			final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
			
			// Check if the move can be completed
			if (moveTransition.getMoveStatus().isDone()) {
				// Set the current value - call the min method for white player and the max method for black player
				currentValue = board.currentPlayer().getAlliance().isWhite() ? 
						min(moveTransition.getTransitionBoard(), this.searchDepth - 1) :
						max(moveTransition.getTransitionBoard(), this.searchDepth - 1);
				
				// Check is the current player is white and the current value is higher than the highest seen value
				if (board.currentPlayer().getAlliance().isWhite() && currentValue >= highestSeenValue) {
					// Set the highest seen value to be the higher current value
					highestSeenValue = currentValue;
					// Assign the value of the best move
					bestMove = move;
					
				// Check is the current player is black and the current value is lower than the lowest seen value
				} else if (board.currentPlayer().getAlliance().isBlack() && currentValue <= lowestSeenValue) {
					// Set the lowest seen value to be the lower current value
					lowestSeenValue = currentValue;
					// Assign the value of the best move
					bestMove = move;
				}
			}
		}
		// Variable to store the time it takes to execute
		final long executionTime = System.currentTimeMillis() - startTime;
		// Return the best move
		return bestMove;
	}
	
	
	
	// Method to return the min
	public int min(final Board board, final int depth) {
		
		// Check if the depth is 0
		if (depth == 0 || isEndGameScenario(board)) {
			// Evaluate the board depth set to 0
			return this.boardEvaluator.evaluate(board, depth);
		}
		// Initialize a variable to store the lowest seen value and set it to the highest number possible
		int lowestSeenValue = Integer.MAX_VALUE;
		
		// Loop through all the current player's legal moves
		for (final Move move : board.currentPlayer().getLegalMoves()) {
			// Initialize a move transition board to test move
			final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
			
			// Check if the move can be completed
			if (moveTransition.getMoveStatus().isDone()) {
				// Initiliaze a variable with the result of calling the max method
				final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1);
				
				// Check if the current value is less than the lowest seen value
				if (currentValue <= lowestSeenValue) {
					// Set the value of the lowest seen value to be the lower current value
					lowestSeenValue = currentValue;
				}
			}
		}
		// Return the lowest seen value
		return lowestSeenValue;
	}

	
	
	// Method to return the max
	public int max(final Board board, final int depth) {
		
		// Check if the depth is 0
		if (depth == 0 || isEndGameScenario(board)) {
			// Evaluate the board depth set to 0
			return this.boardEvaluator.evaluate(board, depth);
		}
		// Initialize a variable to store the highest seen value and set it to the lowest number possible
		int highestSeenValue = Integer.MIN_VALUE;
				
		// Loop through all the current player's legal moves
		for (final Move move : board.currentPlayer().getLegalMoves()) {
			// Initialize a move transition board to test move
			final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
				
			// Check if the move can be completed
			if (moveTransition.getMoveStatus().isDone()) {
				// Initiliaze a variable with the result of calling the min method
				final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1);
						
				// Check if the current value is more than the highest seen value
				if (currentValue >= highestSeenValue) {
					// Set the value of the highest seen value to be the higher current value
					highestSeenValue = currentValue;
				}
			}
		}
		// Return the highest seen value
		return highestSeenValue;
	}



	// Method to calculate if a game is over
	private static boolean isEndGameScenario(final Board board) {
		// Return true if game is over, false if it isn't
		return board.currentPlayer().isInCheckMate() || board.currentPlayer().isInStaleMate();
	}
}
