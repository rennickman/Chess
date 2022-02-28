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



public class Bishop extends Piece {

	// Array to store the possible vector moves of a bishop
	private static final int[] CANDIDATE_MOVE_VECTOR_COORDINATES = { -9, -7, 7, 9 };
	
	
	
	// Constructor
	public Bishop(final Alliance pieceAlliance, final int piecePosition) {
		// Call the piece super constructor
		super(PieceType.BISHOP, piecePosition, pieceAlliance, true);
	}

	
	
	// Convenience constructor that can set is first move
	public Bishop(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
		// Call the piece super constructor
		super(PieceType.BISHOP, piecePosition, pieceAlliance, isFirstMove);
	}
	
	
	
	// Override the calculate legal moves method of piece
	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		// Array list to store all the possible moves a bishop can make
		final List<Move> legalMoves = new ArrayList<>();
		
		// Loop through the array of possible vector moves
		for (final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
			// A variable to store the position of the current piece
			int candidateDestinationCoordinate = this.piecePosition;
			
			// Check that candidate destination is a valid tile - not out of bounds
			while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				
				// Check if tile is in first or eight column with exceptions and break out of loop if it is
				if (isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
						isEightColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
					break;
				}
				// Apply the offset to the candidate destination variable
				candidateDestinationCoordinate += candidateCoordinateOffset;
				
				// Check that the new candidate destination is a valid tile
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
						
						// Check if the color of the bishop is the same as the piece occupying the tile
						if (this.pieceAlliance != pieceAlliance) {
							// Add the major pieces attack move to the legal moves array list if its occupied by an enemy piece
							legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
						}
					// Break out of the loop if there is a piece blocking the path - friendly or enemy
					break;
					}
				}
			}
		}
		// Return an immutable copy of the legal moves array list
		return ImmutableList.copyOf(legalMoves);
	}
	
	
	
	// Override the move piece method for a bishop
	@Override
	public Bishop movePiece(final Move move) {
		// Return a new bishop at the destination coordinate
		return new Bishop(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
	}
	
	
	
	// Override the to string method for a bishop
	@Override
	public String toString() {
		// Return the string representation of a bishop
		return PieceType.BISHOP.toString();
	}
	
	
	
	// Method to check for exceptions in the first column
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		// Check if the current position is in the first column and the offset is an illegal move
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == 7);
	}
	
	// Method to check for exceptions in the eight column
	private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
		// Check if the current position is in the first column and the offset is an illegal move
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 9);
	}	
}
