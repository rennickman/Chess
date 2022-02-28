package com.chess.engine.pieces;

import java.util.Collection;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;



public abstract class Piece {

	// Variable to store the type of each piece
	protected final PieceType pieceType;
	// Variable to store the position of each piece
	protected final int piecePosition; 
	// Variable to store the color of a piece - black or white
	protected final Alliance pieceAlliance;
	// Variable to store whether or not this piece has yet to make it's first move
	protected final boolean isFirstMove;
	
	// Variable to store the cached hash code of a piece
	final private int cachedHashCode;
	
	
	
	// Constructor 
	Piece (final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
		// Assign the type of a piece
		this.pieceType = pieceType;
		// Assign the position of a piece
		this.piecePosition = piecePosition;
		// Assign the color of a piece - black or white
		this.pieceAlliance = pieceAlliance;
		// Set value of is first move
		this.isFirstMove = isFirstMove;
		// Set the value of the cached hash code
		this.cachedHashCode = computeHashCode();
	}
	
	
	
	// Method to create a hash code for each piece
	private int computeHashCode() {
		// Create a hash code using all the attributes of an instance of a piece
		int result = pieceType.hashCode();
		result = 31 * result + pieceAlliance.hashCode();
		result = 31 * result + piecePosition;
		result = 31 * result + (isFirstMove ? 1 : 0);
		// Return the hash code
		return result;
	}



	// Override the equals method for pieces to check if two instances of a piece are the same
	@Override
	public boolean equals(final Object other) {
		// Check if the two instances are the same instance
		if (this == other) {
			// Return true - both instances are the same
			return true;
		}
		
		// Check if the parameter instance is an instance of piece
		if (!(other instanceof Piece)) {
			// Return false if it is not an instance of piece
			return false;
		}
		// Cast other as a piece
		final Piece otherPiece = (Piece) other;
		// Check if the attributes of both piece instances are the same - return true if they are the same, false if they aren't
		return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
				pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
	}
	
	
	
	// Override the hash code method for an instance of a piece
	@Override
	public int hashCode() {
		// Return the cached hash code of a piece
		return this.cachedHashCode;
	}
	
	
	
	// Getter method for the piece position
	public int getPiecePosition() {
		// Return the position of the piece
		return this.piecePosition;
	}
	
	// Getter method for the alliance of a piece
	public Alliance getPieceAlliance() {
		// Return the color of the piece
		return this.pieceAlliance;
	}
	
	// Method to check whether or not it is a piece's first move
	public boolean isFirstMove() {
		// Return a boolean value
		return this.isFirstMove;
	}
	
	// Getter method for the type of a piece
	public PieceType getPieceType() {
		// Return the type of a piece
		return this.pieceType;
	}
	
	// Getter method for the value of a piece
	public int getPieceValue() {
		// Return the result of calling the get piece value method of the piece type
		return this.pieceType.getPieceValue();
	}
	
	
	
	
	// Method to create a collection of possible legal moves - defined in sub classes
	public abstract Collection<Move> calculateLegalMoves (final Board board);
	
	
	// Method to move a piece to a new tile - defined in sub classes
	public abstract Piece movePiece(Move move);
	
	
	
	public enum PieceType {
		
		// Setting the the value and to string representation of each piece
		PAWN(100, "P") {
			
			// Override the is king method for a pawn
			@Override
			public boolean isKing() {
				// Return false - not the king
				return false;
			}
			
			// Override the is rook method for a pawn
			@Override
			public boolean isRook() {
				// Return false - not a rook
				return false;
			}
		},
		KNIGHT(300, "N") {
			
			// Override the is king method for a knight
			@Override
			public boolean isKing() {
				// Return false - not the king
				return false;
			}

			// Override the is rook method for a knight
			@Override
			public boolean isRook() {
				// Return false - not a rook
				return false;
			}
		},
		BISHOP(300, "B") {
			
			// Override the is king method for a bishop
			@Override
			public boolean isKing() {
				// Return false - not the king
				return false;
			}

			// Override the is rook method for a bishop
			@Override
			public boolean isRook() {
				// Return false - not a rook
				return false;
			}
		},
		ROOK(500, "R") {
			
			// Override the is king method for a rook
			@Override
			public boolean isKing() {
				// Return false - not the king
				return false;
			}

			// Override the is rook method for a rook
			@Override
			public boolean isRook() {
				// Return true - it is a rook
				return true;
			}
		},
		QUEEN(900, "Q") {
			
			// Override the is king method for the queen
			@Override
			public boolean isKing() {
				// Return false - not the king
				return false;
			}

			// Override the is rook method for the queen
			@Override
			public boolean isRook() {
				// Return false - not a rook
				return false;
			}
		},
		KING(10000, "K") {
			
			// Override the is king method for the king
			@Override
			public boolean isKing() {
				// Return true - it is the king
				return true;
			}

			// Override the is rook method for the king
			@Override
			public boolean isRook() {
				// Return false - not a rook
				return false;
			}
		};
		
		
		
		// Variable to store the name of a piece
		private String pieceName;
		// Variable to store the value of a piece
		private int pieceValue;
		
		
		
		// Constructor
		PieceType(final int pieceValue, final String pieceName) {
			// Set the name of the piece
			this.pieceName = pieceName;
			// Set the value of the piece
			this.pieceValue = pieceValue;
		}
		
		
		
		//Override the to string method
		@Override
		public String toString() {
			// Return the string representation of each piece
			return this.pieceName;
		}
		
		// Getter method for the value of a piece type
		public int getPieceValue() {
			// Return the value of the piece type
			return this.pieceValue;
		}
		
		// Method to check if a piece is the king
		public abstract boolean isKing();

		// Method to check if a piece is a rook
		public abstract boolean isRook();
	}
}
