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



public class BlackPlayer extends Player{

	// Constructor
	public BlackPlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves ) {
		// Call the player super constructor
		super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
	}

	
	
	// Override the get active pieces method from player
	@Override
	public Collection<Piece> getActivePieces() {
		// Return the collection of black pieces from board
		return this.board.getBlackPieces();
	}


	
	// Override the get alliance method from player
	@Override
	public Alliance getAlliance() {
		// Return the alliance for the black player
		return Alliance.BLACK;
	}



	// Override the get opponent method from player
	@Override
	public Player getOpponent() {
		// Return the opponent by calling the white player method of board
		return this.board.whitePlayer();
	}



	// Override the calculate king castles method for the black player
	@Override
	protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {
		// Initialize an array list to store king castling moves
		final List<Move> kingCastles = new ArrayList<>(); 
				
		// Check if it is the king's first move and that it isn't in check
		if (this.playerKing.isFirstMove() && !this.isInCheck()) {
					
			// Check if the two tiles between the king and rook are empty - black's king side castling
			if (!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
				// Variable to store the tile containing the rook
				final Tile rookTile = this.board.getTile(7);
						
				// Check if the rook tile is occupied and that it is the rook's first move
				if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
							
					// Check that there are no attacks on the tiles between king and rook and rook tile piece is a rook
					if (Player.calculateAttacksOnTile(5, opponentsLegals).isEmpty() &&
						Player.calculateAttacksOnTile(6, opponentsLegals).isEmpty() && rookTile.getPiece().getPieceType().isRook()) {
							// Add the move to the king castles array list
							kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 6,
																		(Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 5));
					}
				}
			}
					
			// Check if the three tiles between the king and rook are empty - black's queen side castling
			if (!this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied() && !this.board.getTile(3).isTileOccupied()) {
				// Variable to store the tile containing the rook
				final Tile rookTile = this.board.getTile(0);
						
				// Check if the rook tile is occupied and that it is the rook's first move
				if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					
					// Check that there are no attacks on the tiles between king and rook and rook tile piece is a rook
					if (Player.calculateAttacksOnTile(2, opponentsLegals).isEmpty() &&
						Player.calculateAttacksOnTile(3, opponentsLegals).isEmpty() && rookTile.getPiece().getPieceType().isRook()) {
							// Add the move to the king castles array list
							kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 2,
																		(Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 3));
					}
				}
			}
		}
		// Return an immutable list of the king side castles array list
		return ImmutableList.copyOf(kingCastles);
	}

}
