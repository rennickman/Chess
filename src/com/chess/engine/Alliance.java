package com.chess.engine;

import com.chess.engine.board.BoardUtils;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public enum Alliance {
	
	WHITE {
		// Override the get direction method for white pieces
		@Override
		public int getDirection() {
			// Return negative one as direction for white
			return -1;
		}

		// Override the get opposite direction method for white pieces
		@Override
		public int getOppositeDirection() {
			// Return positive one as the opposite direction for white pieces
			return 1;
		}
		
		
		// Override the check color methods for white pieces
		@Override
		public boolean isBlack() {
			return false;
		}

		@Override
		public boolean isWhite() {
			return true;
		}

		
		
		// Override the choose player method for white pieces
		@Override
		public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
			// Return the white player
			return whitePlayer;
		}

		// Override the is promotion square method for white pieces
		@Override
		public boolean isPawnPromotionSquare(int position) {
			// Return the promotion square for white pawns
			return BoardUtils.EIGHTH_RANK[position];
		}
	},
 
	
	
	BLACK {
		// Override the get direction method for black pieces
		@Override
		public int getDirection() {
			// Return positive one as direction for black
			return 1;
		}
		
		// Override the get opposite direction method for black pieces
		@Override
		public int getOppositeDirection() {
			// Return negative one as the opposite direction for black pieces
			return -1;
		}

		
		
		// Override the check color methods for black pieces
		@Override
		public boolean isBlack() {
			return true;
		}

		@Override
		public boolean isWhite() {
			return false;
		}

		
		
		// Override the choose player method for black pieces
		@Override
		public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
			// Return the black player
			return blackPlayer;
		}
		
		// Override the is promotion square method for black pieces
		@Override
		public boolean isPawnPromotionSquare(int position) {
			// Return the promotion square for black pawns
			return BoardUtils.FIRST_RANK[position];
		}
	};	
	
	
	
	// Getter method to return the direction of a piece -  -1 for white and 1 for black
	public abstract int getDirection();
	// Getter method to return the opposite direction
	public abstract int getOppositeDirection();
	
	// Methods to check the color of a piece
	public abstract boolean isBlack();
	public abstract boolean isWhite();

	// Method to return the promotion squares for pawns
	public abstract boolean isPawnPromotionSquare(int position);

	// Method to choose player 
	public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
