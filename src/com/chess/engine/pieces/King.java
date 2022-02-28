package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;



public class King extends Piece {

	// Variable to store possible candidate moves for a king
	private final static int[] CANDIDATE_MOVE_COORDINATE = { -9, -8, -7, -1, 1, 7, 8, 9 };
	
	
	
	// Constructor
	public King(final Alliance pieceAlliance, final int piecePosition) {
		// Call the piece super constructor
		super(PieceType.KING, piecePosition, pieceAlliance, true);
	}
	
	
	
	// Convenience constructor that can set is first move
	public King(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
		// Call the piece super constructor
		super(PieceType.KING, piecePosition, pieceAlliance, isFirstMove);
	}

	
	
	// Override the calculate legal moves method of piece
	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		// Array list to store all possible legal moves that the pawn can make
		final List<Move> legalMoves = new ArrayList<>();
		
		// Loop through all possible legal moves in the array
		for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
			// Variable to store the coordinate of piece after making possible legal move - by applying offset to current position
			final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
			
			// Check if tile is in one of the columns with exceptions and skip to next tile if move is not legal
			if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
					isEightColumnExclusion(this.piecePosition, currentCandidateOffset)) {
				continue;
			}
			
			// Check if the tile is a valid coordinate - not out of bounds
			if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				// Set the candidate destination tile equal to the value of the tile at the given coordinate
				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
				
				// Check if the destination tile is already occupied
				if (!candidateDestinationTile.isTileOccupied()) {
					// If it isn't occupied add the non-attacking move to the legal moves array list
					legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
				} else {
					// Variable to store the piece occupying the destination tile
					final Piece pieceAtDestination = candidateDestinationTile.getPiece();
					// Variable to store the color of the piece
					final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
					
					// Check if the color of the knight is the same as the piece occupying the tile
					if (this.pieceAlliance != pieceAlliance) {
						// Add the major pieces attacking move to the legal moves array list if its occupied by an enemy piece
						legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
					}
				}
			}
		}
		// Return an immutable copy of the legal moves array list
		return ImmutableList.copyOf(legalMoves);
	}
	
	
	
	// Override the move piece method for a king
	@Override
	public King movePiece(final Move move) {
		// Return a new king at the destination coordinate
		return new King(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
	}
	
	
		
	// Override the to string method for a king
	@Override
	public String toString() {
		// Return the string representation of a king
		return PieceType.KING.toString();
	}
	
	
	
	// Method to check for exceptions in the first column
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		// Check if the current position is in the first column and the offset is an illegal move
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 ||
				candidateOffset == 7);
	}
	// Method to check for exceptions in the eight column
	private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
		// Check if the current position is in the first column and the offset is an illegal move
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 ||
				candidateOffset == 9);
	}

}
