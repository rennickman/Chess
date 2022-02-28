package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;



public final class StandardBoardEvaluator implements BoardEvaluator {

	// Variable to store the check bonus
	private static final int CHECK_BONUS = 50;
	// Variable to store the check bonus
	private static final int CHECK_MATE_BONUS = 10000;
	// Variable to store the depth bonus
	private static final int DEPTH_BONUS = 100;
	// Variable to store the is castled bonus
	private static final int CASTLE_BONUS = 60;
	
	
	
	// Override the evaluate method from board evaluator
	@Override
	public int evaluate(final Board board, final int depth) {
		// Return the result of the white player's score minus the black player's score
		return scorePlayer(board, board.whitePlayer(), depth) - scorePlayer(board, board.blackPlayer(), depth);
	}

	
	
	// Method to calculate the score for a player
	private int scorePlayer(final Board board, final Player player, final int depth) {
		// Return the score for a player
		return pieceValue(player) + mobility(player) + check(player) + checkMate(player, depth) + castled(player);
	}

	

	// Method to calculate if a player is castled
	private static int castled(Player player) {
		// Return the is castled bonus
		return player.isCastled() ? CASTLE_BONUS : 0;
	}



	// Method to calculate if a player puts opponent in check mate
	private static int checkMate(Player player, int depth) {
		// Return the check mate bonus scaled with the depth bonus if player puts opponent in check mate
		return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS * depthBonus(depth) : 0;
	}



	// Method to calculate the depth bonus
	private static int depthBonus(int depth) {
		// Return the depth bonus multiplied by the depth
		return depth == 0 ? 1 : DEPTH_BONUS * depth;
	}



	// Method to calculate if a player puts opponent in check
	private static int check(final Player player) {
		// Return the check bonus if player puts opponent in check
		return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
	}



	// Method to calculate the mobility of a player
	private static int mobility(Player player) {
		// Return the amount of legal moves a player has
		return player.getLegalMoves().size();
	}



	// Method to calculate the total value of all a player's active pieces
	private static int pieceValue(final Player player) {
		// Variable to store the score of all the piece values
		int pieceValueScore = 0;
		
		// Loop through all the player's active pieces
		for (final Piece piece : player.getActivePieces()) {
			// Add the value of the piece to the piece value score
			pieceValueScore += piece.getPieceValue();
		}
		// Return the piece value score
		return pieceValueScore;
	}

}
