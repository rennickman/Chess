package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public abstract class Player {

	// Variable to keep track of the board
	protected final Board board;
	// Variable to keep track of the player's king
	protected final King playerKing;
	// A collection of all the possible legal moves a player can make
	protected final Collection<Move> legalMoves;
	
	// Boolean to store whether or not the king is in check
	private final boolean isInCheck;
	
	
	// Constructor
	Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves) {
		// Assign the value of the board
		this.board = board;
		// Use the establish king method to set value of the player's king
		this.playerKing = establishKing();
		// Assign the value of the legal moves as an immutable list of a concatenation of legal moves and king castle moves
		this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponentMoves)));
		// Use the calculate attacks on tile method to check if the king is in check or not
		this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
	}
	
	
	
	// Getter method for each player's king
	public King getPlayerKing() {
		// Return the king
		return this.playerKing;
	}
	
	// Getter method for the legal moves collection
	public Collection<Move> getLegalMoves() {
		// Return the legal moves collection
		return this.legalMoves;
	}



	// Method to calculate potential attacks on a tile
	protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
		// An array list to store the attacking moves on the tile
		final List<Move> attackMoves = new ArrayList<>();
		
		// Loop through all the moves in the collection of moves
		for (Move move : moves) {
			// Check if the position of the piece is equal to the destination coordinate of a move
			if (piecePosition == move.getDestinationCoordinate()) {
				// Add the move to the attack moves array list
				attackMoves.add(move);
			}
		}
		// Return an immutable copy of the attack moves array list
		return ImmutableList.copyOf(attackMoves);
	}



	// Method to set the value of a player's king
	private King establishKing() {
		
		// Loop through all the pieces in the active pieces collection
		for (final Piece piece : getActivePieces()) {
			
			// Check if the piece is a king
			if (piece.getPieceType().isKing()) {
				// Return the piece cast as a king
				return (King) piece;
			}
		}
		// Throw a runtime exception error - no king found on the board
		throw new RuntimeException("Should not reach here! Not a valid board!!");
	}
	
	
	
	// Method to check if a move is legal
	public boolean isMoveLegal(final Move move) {
		// Return true if the move is in the legal moves list
		return this.legalMoves.contains(move);
	}
	
	
	
	// Method to see if king is in check
	public boolean isInCheck() {
		// Return boolean to represent whether or not the king is in check
		return this.isInCheck;
	}
	
	
	
	// Method to see if king is in check mate
	public boolean isInCheckMate() {
		// Return true if the king is in check and has no escape moves
		return this.isInCheck && !hasEscapeMoves();
	}
	
	
	
	// Method to check if a game is in stale mate
	public boolean isInStaleMate() {
		// Return true if the king is NOT in check and has no escape moves
		return !this.isInCheck && !hasEscapeMoves();
	}
	
	
	
	// Method to check whether a king has any escape moves
	protected boolean hasEscapeMoves() {
		// Loop through all the moves in the legal moves collection
		for (final Move move : this.legalMoves) {
			// Use the make move method to create a move transition
			final MoveTransition transition = makeMove(move);
			
			// Check if the move can be complete
			if (transition.getMoveStatus().isDone()) {
				// Return true - king has escape moves
				return true;
			}
		}
		// Return false - king has no escape moves
		return false;
	}

	
	
	// Method to check if a king has been castled
	public boolean isCastled() {
		return false;
	}
	
	
	
	// Method to make a move
	public MoveTransition makeMove(final Move move) {
		
		// Check if the move is legal
		if (!isMoveLegal(move)) {
			// If move is illegal, return the same game board with the illegal move status
			return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
		}
		
		// Create a new transition board after executing the move
		final Board transitionBoard = move.execute();
		// Create a collection of possible attacks on the king after making the move - note that current player will switch after move is made
		final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
				transitionBoard.currentPlayer().getLegalMoves());
		
		// Check if there are attacks on the king
		if (!kingAttacks.isEmpty()) {
			// Return the same game board with the leaves player in check status
			return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
		}
		// Return the new board with the done status if move can be completed
		return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
	}
	
	
	
	// Getter method for the active pieces of each color
	public abstract Collection<Piece> getActivePieces();
	
	// Getter method for the color of a player
	public abstract Alliance getAlliance();
	
	// Getter method for the opponent
	public abstract Player getOpponent();
	
	// Method to calculate castling moves
	protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals);
}
