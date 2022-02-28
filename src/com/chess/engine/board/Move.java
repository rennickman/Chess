package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;



public abstract class Move {

	// Variables to store the board, the piece that is moved and the coordinate of the destination file
	protected final Board board;
	protected final Piece movedPiece;
	protected final int destinationCoordinate;
	
	// Variable to store the is first move value for the moved piece
	protected final boolean isFirstMove;
	
	// Instantiate a null move
	public static final Move NULL_MOVE = new NullMove();
	
	
	
	// Constructor
	private Move (final Board board, final Piece movedPiece, final int destinationCoordinate) {
		// Assign the value of the board
		this.board = board;
		// Assign the value of the piece that is being moved
		this.movedPiece = movedPiece;
		// Assign the value of the destination coordinate
		this.destinationCoordinate = destinationCoordinate;
		// Use the is first move method to assign the value of is first move
		this.isFirstMove = movedPiece.isFirstMove();
	}
	
	
	
	// Convenience constructor to help with null moves
	private Move (final Board board, final int destinationCoordinate) {
		// Assign the value of the board
		this.board = board;
		// Assign the value of the destination coordinate
		this.destinationCoordinate = destinationCoordinate;
		// Set the value of the piece being moved to null
		this.movedPiece = null;
		// Set value of is first move to false
		this.isFirstMove = false;
	}
	
	
	
	// Override the hash code method for an instance of a move
	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + this.destinationCoordinate;
		result = 31 * result + this.movedPiece.hashCode();
		result = 31 * result + this.movedPiece.getPiecePosition();
		// Return the cached hash code of a move
		return result;
	}
	
	
	
	// Override the equals method for moves to check if two instances of a move are the same
	@Override
	public boolean equals(final Object other) {
		
		// Check if the two instances are the same instance
		if (this == other) {
			// Return true - both instances are the same
			return true;
		}
		
		// Check if the parameter instance is an instance of move
		if (!(other instanceof Move)) {
			// Return false if it is not an instance of move
			return false;
		}
		// Cast other as a move
		final Move otherMove = (Move) other;
		// Check if the attributes of both move instances are the same - return true if they are the same, false if they aren't
		return  getCurrentCoordinate() == otherMove.getCurrentCoordinate() && 
				getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
				getMovedPiece().equals(otherMove.getMovedPiece());
	}
	
	
	
	// Getter method for the board
	public Board getBoard() {
		// Return the board
		return this.board;
	}
	
	// Getter method for current tile coordinate
	public int getCurrentCoordinate() {
		// Return the current tile coordinate of the moved piece
		return this.getMovedPiece().getPiecePosition();
	}
	
	// Getter method for the destination coordinate
	public int getDestinationCoordinate() {
		// Return the destination coordinate
		return this.destinationCoordinate;
	}
	
	// Getter method for the moved piece
	public Piece getMovedPiece() {
		// Return the moved piece
		return this.movedPiece;
	}
	
	// Getter method for attacked piece
	public Piece getAttackedPiece() {
		// Return null for the base class
		return null;
	}
	
	
	
	// Method to check if a move is an attack
	public boolean isAttack() {
		// Return false for the base class
		return false;
	}
	
	// Method to check if a move is a castling move
	public boolean isCastlingMove() {
		// Return false for the base class
		return false;
	}
	
	
	
	// Method to execute a move
	public Board execute() {
		// Initialize an instance of the board builder
		final Board.Builder builder = new Board.Builder();
		
		// Loop through the current player's active pieces
		for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
			
			// Check if the piece is not the moved piece
			if (!this.movedPiece.equals(piece)) {
				// Set the unmoved piece in the same position
				builder.setPiece(piece);
			}
		}
		
		// Loop through all of the opponent's active pieces
		for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
			// Set the opponent's pieces in the same position
			builder.setPiece(piece);
		}
		// Set the position of the moved piece
		builder.setPiece(this.movedPiece.movePiece(this));
		// Set the move maker to be opponent
		builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
		// Return the new game board
		return builder.build();
	}

	
	
	// Sub class for major piece attack moves
	public static class MajorAttackMove extends AttackMove{

		// Constructor
		public MajorAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
			// Call the super constructor of attack moves
			super(board, movedPiece, destinationCoordinate, attackedPiece);	
		}
		
		
		
		// Override the equals method for a major piece attack move
		@Override
		public boolean equals(final Object other) {
			// Check if they are an instance of the same major piece attack move and return boolean
			return this == other || other instanceof MajorAttackMove && super.equals(other);
		}
		
		// Override the to string method for a major piece attack move
		public String toString() {
			// Return a concatenation of the piece type and the destination tile coordinate
			return movedPiece.getPieceType() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}
	}
	


	// Sub class for major moves - a major piece moves and no piece attacked
	public static final class MajorMove extends Move {

		// Constructor
		public MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
			// Call the super class constructor of move
			super(board, movedPiece, destinationCoordinate);
		}
		
		
		
		// Override the equals method for a major move
		@Override
		public boolean equals(final Object other) {
			// Check if they are an instance of the same major move and return boolean
			return this == other || other instanceof MajorMove && super.equals(other);
		}
		
		// Override the to string method for a major move - to print completed moves
		@Override
		public String toString() {
			// Return the moved piece's to string method and the destination tile coordinate
			return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}
	}
	
	
	
	// Sub class for attack moves
	public static class AttackMove extends Move {
		
		// Variable to store the piece that is attacked
		final Piece attackedPiece;

		// Constructor
		public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
			// Call the super class constructor of move
			super(board, movedPiece, destinationCoordinate);
			// Assign the value of attacked piece
			this.attackedPiece = attackedPiece;
		}

		
		// Override the hash code method for an attack move
		@Override
		public int hashCode() {
			// Return the cached hash code
			return this.attackedPiece.hashCode() + super.hashCode();
		}
		
		
		
		// Override the equals method of moves to check if two instances of an attack move are the same
		@Override
		public boolean equals(final Object other) {
			
			// Check if the two instances are the same instance
			if (this == other) {
				// Return true - both instances are the same
				return true;
			}
			
			// Check if the parameter instance is an instance of an attack move
			if (!(other instanceof AttackMove)) {
				// Return false if it is not an instance of an attack move
				return false;
			}
			// Cast other as an attack move
			final AttackMove otherAttackMove = (AttackMove) other;
			// Check if the attributes of both attack move instances are the same - return true if they are the same, false if they aren't
			return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
		}
		
		
		// Override the is attack method for an attack move
		@Override
		public boolean isAttack() {
			// Return true - it is an attack move
			return true;
		}
		
		// Override the get attacked piece method for an attack move
		@Override
		public Piece getAttackedPiece() {
			// Return the attacked piece
			return this.attackedPiece;
		}
	}
	
	
	
	// Sub class for pawn moves
	public static final class PawnMove extends Move {

		// Constructor
		public PawnMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
			// Call the super class constructor of move
			super(board, movedPiece, destinationCoordinate);
		}
		
		
		
		// Override the equals method for a pawn move
		@Override
		public boolean equals(final Object other) {
			// Check if they are an instance of the same pawn attack move and return boolean
			return this == other || other instanceof PawnMove && super.equals(other);
		}
		
		@Override
		public String toString() {
			// Return the destination tile coordinate
			return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}
		
	}
	
	
	
	// Sub class for pawn attack moves
	public static class PawnAttackMove extends AttackMove {

		// Constructor
		public PawnAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
			// Call the super class constructor of attack move
			super(board, movedPiece, destinationCoordinate, attackedPiece);
		}
		
		
		
		// Override the equals method for a pawn attack move
		@Override
		public boolean equals(final Object other) {
			// Check if they are an instance of the same pawn attack move and return boolean
			return this == other || other instanceof PawnAttackMove && super.equals(other);
		}
		
		// Override the to string method for a pawn attack move
		@Override
		public String toString() {
			// Return a concatenation of substring, x, and the destination tile coordinate
			return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1) + "x" + 
					BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}
	}
	
	
	
	// Sub class for pawn en passant attack moves
	public static final class PawnEnPassantAttackMove extends PawnAttackMove {

		// Constructor
		public PawnEnPassantAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
			// Call the super class constructor of pawn attack move
			super(board, movedPiece, destinationCoordinate, attackedPiece);
		}
		
		
		
		// Override the equals method for a pawn en passant attack move
		@Override
		public boolean equals(final Object other) {
			// Check if they are an instance of the same pawn attack move and return boolean
			return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other);
		}
		
		
		
		//Override the execute method for a pawn en passant move
		@Override
		public Board execute() {
			
			// Initialize a new instance of a builder
			final Builder builder = new Builder();
			
			// Loop through all the current player's active pieces
			for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
				
				// Check if the piece is different than the moved piece
				if (!this.movedPiece.equals(piece)) {
					// Set all the pieces that are not moved
					builder.setPiece(piece);
				}
			}
			
			// Loop through all the opponent's active pieces
			for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
				
				// Check if the piece is different than the attacked piece
				if (!piece.equals(this.getAttackedPiece())) {
					// Set all the pieces that are not being attacked
					builder.setPiece(piece);
				}
			}
			// Set the moved piece
			builder.setPiece(this.movedPiece.movePiece(this));
			// Set the move maker to be the opponent
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			// Return the new board
			return builder.build();
		}
	}
	
	
	
	// Sub class for pawn promotion
	public static final class PawnPromotion extends Move {
		
		// Variable to store the decorated move
		final Move decoratedMove;
		// Variable to store the pawn that is promoted
		final Pawn promotedPawn;
		
		// Constructor
		public PawnPromotion(final Move decoratedMove) {
			// Call the super constructor of the move class
			super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
			// Assign the value of decorated move
			this.decoratedMove = decoratedMove;
			// Assign the value of the promoted pawn
			this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();	
		}
		
		
		
		// Override the hash code method for a pawn promotion
		@Override
		public int hashCode() {
			// Return the cached hash code
			return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
		}
		
		// Override the equals method for a pawn promotion
		@Override
		public boolean equals(final Object other) {
			// Check if they are an instance of the same pawn promotion move and return boolean
			return this == other || other instanceof PawnPromotion && (super.equals(other));
		}
		
		
		
		// Override the execute method for a pawn promotion
		@Override
		public Board execute() {
			
			// Initialize a pawn moved board
			final Board pawnMovedBoard = this.decoratedMove.execute();
			// Initialize a new builder
			final Board.Builder builder = new Builder();
			
			// Loop through all the current player's pieces on the pawn moved board
			for (final Piece piece : pawnMovedBoard.currentPlayer().getActivePieces()) {
				
				// Check if the piece is different than the promoted pawn
				if (!this.promotedPawn.equals(piece)) {
					// Set the position of all the pieces that are not the promoted pawn
					builder.setPiece(piece);
				}
			}
			
			// Loop through all the opponent's pieces on the pawn moved board
			for (final Piece piece : pawnMovedBoard.currentPlayer().getOpponent().getActivePieces()) {
				// Set the position of all the opponent's pieces
				builder.setPiece(piece);
			}
			// Set the position of the new promoted piece
			builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
			// Set the move maker to be the opponent
			builder.setMoveMaker(pawnMovedBoard.currentPlayer().getAlliance());
			// Return the new board
			return builder.build();
		}
		
		
		
		// Override the is attack method for a pawn promotion
		@Override
		public boolean isAttack() {
			// Return the is attack method of decorated move
			return this.decoratedMove.isAttack();
		}
		
		// Override the get attacked piece method for a pawn promotion
		@Override
		public Piece getAttackedPiece() {
			// Return the attacked piece
			return this.decoratedMove.getAttackedPiece();
		}
		
		// Override the to string method for a pawn promotion
		@Override
		public String toString() {
			return "";
		}
	}
	
	
	
	// Sub class for pawn jump moves
	public static final class PawnJump extends Move {

		// Constructor
		public PawnJump(final Board board, final Piece movedPiece, final int destinationCoordinate) {
			// Call the super class constructor of move
			super(board, movedPiece, destinationCoordinate);
		}
		
		
		
		// Override the execute method for a pawn jump
		@Override
		public Board execute() {
			// Initialize an instance of the board builder
			final Builder builder = new Builder();
			
			// Loop through the current player's active pieces
			for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
				
				// Check if the piece is not the moved piece
				if (!this.movedPiece.equals(piece)) {
					// Set the unmoved piece in the same position
					builder.setPiece(piece);
				}
			}
			
			// Loop through all of the opponent's active pieces
			for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
				// Set the opponent's pieces in the same position
				builder.setPiece(piece);
			}
			// Create a new instance of a pawn with the value of the moved piece
			final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
			// Set the position of the moved pawn
			builder.setPiece(movedPawn);
			// Set the moved pawn as an en passant pawn
			builder.setEnPassantPawn(movedPawn);
			// Set the move maker to be opponent
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			// Return the new game board
			return builder.build();
		}
		
		// Override the to string method for a pawn jump
		@Override
		public String toString() {
			// Return the coordinate as the to string
			return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}
		
	}
	
	
	
	// Sub class for castle moves
	static abstract class CastleMove extends Move {
		
		// Variables to store the rook and it's start tile and destination tile
		protected final Rook castleRook;
		protected final int castleRookStart;
		protected final int castleRookDestination;

		
		
		// Constructor
		public CastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
							final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
			// Call the super class constructor of move
			super(board, movedPiece, destinationCoordinate);
			// Set the variables to store the rook and it's start tile and destination tile
			this.castleRook = castleRook;
			this.castleRookStart = castleRookStart;
			this.castleRookDestination = castleRookDestination;
		}
		
		
		
		// Getter method for the castle rook
		public Rook getCastleRook() {
			return this.castleRook;
		}
		
		// Override the is castling move method for a castling move
		@Override
		public boolean isCastlingMove() {
			// Return true - it is a castling move
			return true;
		}
		
		
		// Override the execute method for a castle move
		@Override
		public Board execute() {
			// Initialize an instance of the board builder
			final Builder builder = new Builder();
						
			// Loop through the current player's active pieces
			for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
							
				// Check if the piece is not the moved piece and it is not the castle rook
				if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
					// Set the unmoved piece in the same position
					builder.setPiece(piece);
				}
			}
						
			// Loop through all of the opponent's active pieces
			for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
				// Set the opponent's pieces in the same position
				builder.setPiece(piece);
			}
			// Set the new position of the king
			builder.setPiece(this.movedPiece.movePiece(this));
			// Create a new rook piece in the castle rook's destination file
			builder.setPiece(new Rook(this.castleRook.getPieceAlliance(), this.castleRookDestination));
			// Set the move maker to be opponent
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			// Return the new game board
			return builder.build();
		}
		
		
		// Override the hash code method for a castling move
		@Override
		public int hashCode() {
			int result = super.hashCode();
			result = 31 * result + this.castleRook.hashCode();
			result = 31 * result + this.castleRookDestination;
			// Return the cached hash code
			return result;
		}
		
		// Override the equals method for a castling move
		@Override
		public boolean equals(final Object other) {
			
			// Check if the two instances are the same instance
			if (this == other) {
				// Return true - both instances are the same
				return true;
			}
			
			// Check if the parameter instance is an instance of castle move
			if (!(other instanceof CastleMove)) {
				// Return false if it is not an instance of a castle move
				return false;
			}
			// Cast other as a castle move
			final CastleMove otherCastleMove = (CastleMove)other;
			// Check if the attributes of both castle move instances are the same - return true if they are the same, false if they aren't
			return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
		}
	}
	
	
	
	// Sub class for king side castle moves
	public static final class KingSideCastleMove extends CastleMove {

		// Constructor
		public KingSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
				final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
			// Call the super class constructor of move
			super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
		}
		
		
		
		// Override the equals method for a king side castle move
		@Override
		public boolean equals(final Object other) {
			// Check if they are an instance of the same king side castle move and return boolean
			return this == other || other instanceof KingSideCastleMove && super.equals(other);
		}
		
		// Override the to string method for king side castle moves
		@Override
		public String toString() {
			// Return the portable game notation for a king side castle move
			return "O-O";
		}
	}
	
	
	
	// Sub class for queen side castle moves
	public static final class QueenSideCastleMove extends CastleMove {

		// Constructor
		public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
				final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
			// Call the super class constructor of move
			super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
		}
		
		
		
		// Override the equals method for a queen side castle move
		@Override
		public boolean equals(final Object other) {
			// Check if they are an instance of the same queen side castle move and return boolean
			return this == other || other instanceof QueenSideCastleMove && super.equals(other);
		}
		
		// Override the to string method for queen side castle moves
		@Override
		public String toString() {
			// Return the portable game notation for a queen side castle move
			return "O-O-O";
		}
	}
	
	
	
	// Sub class for invalid moves
	public static final class NullMove extends Move {

		// Constructor
		public NullMove() {
			// Call the super class constructor of move
			super(null, -1);
		}
		
		
		
		// Override the execute method for a null move
		@Override
		public Board execute() {
			// Throw a run time error exception - null moves cannot be executed
			throw new RuntimeException("Cannot execute the null move!");
		}
		
		// Override the get current coordinate method for a null move
		@Override
		public int getCurrentCoordinate() {
			// Return -1
			return -1;
		}
	}
	
	
	
	// Factory class with a convenience method defined
	public static class MoveFactory {
		
		// Constructor
		private MoveFactory() {
			// Set to not be instantiable - throw a run time exception error
			throw new RuntimeException("Not instantiable!");
		}
		
		
		
		// Method to check if a move is in the board's all legal moves iterable
		public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate) {
			
			// Loop through all the legal moves on the board
			for (final Move move : board.getAllLegalMoves()) {
				
				// Check if the current and destination tile of the move matches a move in the all legal moves iterable
				if (move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate) {
					// Return the move is a match is found
					return move;
				}
			}
			// Return a null move if a match isn't found
			return NULL_MOVE;
		}
	}
}



