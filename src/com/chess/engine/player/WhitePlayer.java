package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;



public class WhitePlayer extends Player {
	
	// Constructor
	public WhitePlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves) {
		// Call the player super constructor
		super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
	}

	
	
	// Override the get active pieces method from player
	@Override
	public Collection<Piece> getActivePieces() {
		// Return the collection of white pieces from board
		return this.board.getWhitePieces();
	}


	
	// Override the get alliance method from player
	@Override
	public Alliance getAlliance() {
		// Return the alliance for the white player
		return Alliance.WHITE;
	}



	// Override the get opponent method from player
	@Override
	public Player getOpponent() {
		// Return the opponent by calling the black player method of board
		return this.board.blackPlayer();
	}



	// Override the calculate king castles method for the white player
	@Override
	protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {
		// Initialize an array list to store king castling moves
		final List<Move> kingCastles = new ArrayList<>(); 
		
		// Check if it is the king's first move and that it isn't in check
		if (this.playerKing.isFirstMove() && !this.isInCheck()) {
			
			// Check if the two tiles between the king and rook are empty - white's king side castling
			if (!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
				// Variable to store the destination tile for the rook
				final Tile rookTile = this.board.getTile(63);
				
				// Check if the destination rook tile is occupied and that it is the rook's first move
				if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					
					// Check that there are no attacks on the tiles between king and rook and rook tile piece is a rook
					if (Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty() &&
						Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty() && rookTile.getPiece().getPieceType().isRook()) {
							// Add the move to the king castles array list
							kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 62,
																		(Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 61));
					}
				}
			}
			
			// Check if the three tiles between the king and rook are empty - white's queen side castling
			if (!this.board.getTile(59).isTileOccupied() && !this.board.getTile(58).isTileOccupied() && !this.board.getTile(57).isTileOccupied()) {
				// Variable to store the tile containing the rook
				final Tile rookTile = this.board.getTile(56);
				
				// Check if the rook tile is occupied and that it is the rook's first move
				if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					
					// Check that there are no attacks on the tiles between king and rook and rook tile piece is a rook
					if (Player.calculateAttacksOnTile(58, opponentsLegals).isEmpty() &&
						Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty() && rookTile.getPiece().getPieceType().isRook()) {
							// Add the move to the king castles array list
							kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 58,
																		(Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
					}
				}
			}
		}
		// Return an immutable list of the king side castles array list
		return ImmutableList.copyOf(kingCastles);
	}

}
