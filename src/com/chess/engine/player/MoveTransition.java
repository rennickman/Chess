package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;



public class MoveTransition {

	// Variable to store the board in transition
	private final Board transitionBoard;
	// Variable to store the move
	private final Move move;
	// Variable to store whether or not the move can be made
	private final MoveStatus moveStatus;
	
	
	
	// Constructor
	public MoveTransition(final Board transitionBoard, final Move move, final MoveStatus moveStatus) {
		// Assign the value of the transition board
		this.transitionBoard = transitionBoard;
		// Assign the value of the move
		this.move = move;
		// Assign the status of the move - whether or not it can be made
		this.moveStatus = moveStatus;
	}
	
	
	
	// Getter method for move status
	public MoveStatus getMoveStatus() {
		// Return the move status - whether or not it can be made
		return this.moveStatus;
	}
	
	// Getter method for the transition board
	public Board getTransitionBoard() {
		// Return the transition board
		return this.transitionBoard;
	}
}
