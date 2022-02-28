package com.chess.engine.player.ai;

import com.chess.engine.board.Board;



public interface BoardEvaluator {

	// Method to evaluate the board and return an integer - positive number means white is winning, negative number means black is winning
	int evaluate(Board board, int depth);
}
