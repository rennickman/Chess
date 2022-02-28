package com.chess.engine.player;

public enum MoveStatus {
	
	DONE {
		
		// Override the is done method for the done status
		@Override
		public boolean isDone() {
			// Return true - move can be completed
			return true;
		}
	},
	ILLEGAL_MOVE {
		
		// Override the is done method for the illegal move status
		@Override
		public boolean isDone() {
			// Return false - move can't be completed
			return false;
		}
	},
	LEAVES_PLAYER_IN_CHECK {
		
		// Override the is done method for the leaves player in check move status
		@Override
		public boolean isDone() {
			// Return false - move can't be completed
			return false;
		}
	};
	
	
	
	// Method to check if a move is completed
	public abstract boolean isDone();

}
