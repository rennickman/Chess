package com.chess;

import com.chess.engine.board.Board;
import com.chess.gui.Table;



public class Chess {

	public static void main(String[] args) {
		// Initialize a new board using the standard board creator
		Board board = Board.createStandardBoard();
		// Print the to string representation of the board
		System.out.println(board );
		
		// Initialize a new table
		Table table = new Table(); 
	}
}
