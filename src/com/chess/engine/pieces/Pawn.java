package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece {

	// Variable to store possible candidate moves for a pawn piece - 8 and 16 are non attacking, 7 and 9 are attacking
	private final static int[] CANDIDATE_MOVE_COORDINATE = { 8, 7, 9, 16 };
	
	
	
	// Constructor
	public Pawn(final Alliance pieceAlliance, final int piecePosition) {
		// Call the piece super constructor
		super(PieceType.PAWN, piecePosition, pieceAlliance, true);
	}
	
	
	
	// Convenience constructor that can set is first move
	public Pawn(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
		// Call the piece super constructor
		super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
	}

	
	
	// Override the calculate legal moves method of Piece
	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		// Array list to store all possible legal moves that the pawn can make
		final List<Move> legalMoves = new ArrayList<>();
		
		// Loop through all possible legal moves in the array
		for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
			// Variable to store the coordinate of piece after making possible legal move - by applying offset multiplied by the direction
			final int  candidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() *  currentCandidateOffset);
			
			// Check if the tile is a valid coordinate - not out of bounds
			if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				// Skip to next tile if move is illegal
				continue;
			}
			
			// Check if it is a normal pawn move and destination tile isn't occupied
			if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
				
				// Check if the destination file is a pawn promotion tile
				if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
					// If tile is unoccupied and it is a pawn promotion tile, add the pawn promotion move to the legal moves array list
					legalMoves.add(new Move.PawnPromotion(new Move.PawnMove(board, this, candidateDestinationCoordinate)));
				} else {
					// If the tile is unoccupied and not a pawn promotion tile, add the pawn move to the legal moves array list
					legalMoves.add(new Move.PawnMove(board, this, candidateDestinationCoordinate));
				}
				
				// Check if it is a double jump pawn move, the pawns first move and it is in the starting row for it's color
			} else if (currentCandidateOffset == 16 && this.isFirstMove() && 
					((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack()) || 
					(BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite()))) {
				// Variable to store the tile between current tile and destination tile
				final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8); 
				
				// Check if both the destination tile and the tile behind it are not occupied
				if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() && 
						!board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					// Add the non-attacking move to the legal moves array list
					legalMoves.add(new Move.PawnJump(board, this, candidateDestinationCoordinate));
				}
				
			// Check if it is an attacking diagonal pawn move offset by 7 and doesn't go out of bounds - side pawns
			} else if (currentCandidateOffset == 7 && 
					!((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
					 (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) {
				
				// Check if destination tile is occupied
				if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					// Variable to store the value of the piece in the occupied tile
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					
					// Check if the piece in occupied tile is an enemy
					if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
						
						// Check if the destination file is a pawn promotion tile
						if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
							// Add the pawn promotion attack move to the legal moves array list
							legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
						} else {
							// Add the attacking move to the legal moves array list
							legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
						}
					}
					
				// Check if there is an en passant pawn on the board
				} else if (board.getEnPassantPawn() != null) {
					
					// Check if the en passant pawn tile is next to the current pawn tile
					if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))) {
						// Variable to store the pawn on the candidate destination tile
						final Piece pieceOnCandidate = board.getEnPassantPawn();
						
						// Check if the piece alliances of the two pawns are different
						if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
							// Add the pawn en passant attack move to the legal moves collection
							legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
						}
					}
				}
				
			// Check if it is an attacking diagonal pawn move offset by 9 and doesn't go out of bounds - side pawns	
			} else if (currentCandidateOffset == 9 &&
					!((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
					 (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) {
				
				// Check if destination tile is occupied
				if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					// Variable to store the value of the piece in the occupied tile
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					
					// Check if the piece in occupied tile is an enemy
					if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
						
						// Check if the destination file is a pawn promotion tile
						if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
							// Add the pawn promotion attack move to the legal moves array list
							legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
						} else {
							// Add the attacking move to the legal moves array list
							legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));	
						}	
					}
				
				// Check if there is an en passant pawn on the board
				} else if (board.getEnPassantPawn() != null) {
					
					// Check if the en passant pawn tile is next to the current pawn tile
					if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))) {
						// Variable to store the pawn on the candidate destination tile
						final Piece pieceOnCandidate = board.getEnPassantPawn();
						
						// Check if the piece alliances of the two pawns are different
						if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
							// Add the pawn en passant attack move to the legal moves collection
							legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
						}
					}
				}
			}
		}
		// Return an immutable copy of the legal moves array list
		return ImmutableList.copyOf(legalMoves);
	}
	
	
	
	// Override the move piece method for a pawn
	@Override
	public Pawn movePiece(final Move move) {
		// Return a new pawn at the destination coordinate
		return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
	}
	
	
	
	// Override the to string method for a pawn
	@Override
	public String toString() {
		// Return the string representation of a pawn
		return PieceType.PAWN.toString();
	}
	
	
	
	// Getter method for the new promotion piece
	public Piece getPromotionPiece() {
		// Return a new queen to replace the promoted pawn
		return new Queen(this.pieceAlliance, this.piecePosition, false);
	}
}
