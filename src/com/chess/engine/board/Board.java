package com.chess.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Queen;
import com.chess.engine.pieces.Rook;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;



public class Board {

	// List to store all the tiles on a chess game board
	private final List<Tile> gameBoard;
	
	// Collections to store the pieces of each color left on board
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;
	
	// Declare instances of each player
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	
	// Variable to store the current player
	private final Player currentPlayer;
	
	// Variable to store the current en passant pawn
	private final Pawn enPassantPawn;
	
	
	
	// Constructor
	private Board(final Builder builder) {
		// Use the builder to create set the value of the game board list
		this.gameBoard = createGameBoard(builder);
		// Use the calculate active pieces method to assign the collections white and black pieces
		this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
		this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
		
		// Assign the value of the en passant pawn
		this.enPassantPawn = builder.enPassantPawn;
		
		// Collections to store all the standard legal moves for both colors
		final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
		final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
		
		// Create new instances of each player
		this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		
		// Set value of current player
		this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
	}
	
	
	
	// Override the to string method of the board
	@Override
	public String toString() {
		// Initialize a new string builder
		final StringBuilder builder = new StringBuilder();
		
		// Loop through all 64 tiles on a chess board
		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			// Variable to store the to string of each file
			final String tileText = this.gameBoard.get(i).toString();
			// Append it to the builder
			builder.append(String.format("%3s", tileText));
			
			// Check if it is the last tile in a row
			if ((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) {
				// Append a new line to start the next row
				builder.append("\n");
			}
		}
		// Return the new to string value
		return builder.toString();
	}
	
	
	
	// Getter method for the white player
	public Player whitePlayer() {
		// Return the white player
		return this.whitePlayer;
	}
	
	// Getter method for the black player
	public Player blackPlayer() {
		// Return the black player
		return this.blackPlayer;
	}
	
	// Getter method for current player
	public Player currentPlayer() {
		// Return the current player
		return this.currentPlayer;
	}
	
	
	
	// Getter method for the en passant pawn
	public Pawn getEnPassantPawn() {
		// Return the en passant pawn
		return this.enPassantPawn;
	}
	
	// Getter method to return a collection of black pieces
	public Collection<Piece> getBlackPieces() {
		// Return collection of black pieces
		return this.blackPieces;
	}
	
	// Getter method to return a collection of white pieces
	public Collection<Piece> getWhitePieces() {
		// Return collection of white pieces
		return this.whitePieces;
	}
	
	
	
	// Method to calculate all the standard legal moves for a color
	private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
		// Array list to store all the legal moves for a color
		final List<Move> legalMoves = new ArrayList<>();
		
		// Loop through all the pieces in the collection
		for (final Piece piece : pieces) {
			// Calculate the legal moves for each piece and add to the legal moves array list
			legalMoves.addAll(piece.calculateLegalMoves(this));
		}
		// Return an immutable list of all the legal moves possible for a color
		return ImmutableList.copyOf(legalMoves);
	}



	// Method to calculate the active pieces left on the board
	private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) {
		// Array list to store all the active pieces
		final List<Piece> activePieces = new ArrayList<>();
		
		// Loop through all the tiles on the game board
		for (final Tile tile : gameBoard) {
			
			// Check if the tile is occupied
			if (tile.isTileOccupied()) {
				// Create a piece with the value of the piece occupying the tile
				final Piece piece = tile.getPiece();
				
				// Check if it is the same alliance
				if (piece.getPieceAlliance() == alliance) {
					// Add it to the active pieces array list
					activePieces.add(piece);
				}
			}
		}
		// Return an immutable list of the active pieces
		return ImmutableList.copyOf(activePieces);
	}
	
	
	
	// Getter method for tiles
	public Tile getTile(final int tileCoordinate) {
		// Return a tile from the game board
		return gameBoard.get(tileCoordinate);
	}
	
	
	
	// Method to create a chess game board
	private static List<Tile> createGameBoard(final Builder builder) {
		// Create an array of 64 tiles
		final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
		
		// Loop through all the tiles in the array
		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			// Create a tile with the piece associated with it from the board configuration map
			tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
		}
		// Return an immutable list of the 64 tiles
		return ImmutableList.copyOf(tiles);
	}
	
	
	
	// Method to create a standard chess board with pieces in their starting positions
	public static Board createStandardBoard() {
		
		// Initialize a new builder
		final Builder builder = new Builder();
		
		// Black layout
		builder.setPiece(new Rook(Alliance.BLACK, 0));
		builder.setPiece(new Knight(Alliance.BLACK, 1));
		builder.setPiece(new Bishop(Alliance.BLACK, 2));
		builder.setPiece(new Queen(Alliance.BLACK, 3));
		builder.setPiece(new King(Alliance.BLACK, 4));
		builder.setPiece(new Bishop(Alliance.BLACK, 5));
		builder.setPiece(new Knight(Alliance.BLACK, 6));
		builder.setPiece(new Rook(Alliance.BLACK, 7));
		builder.setPiece(new Pawn(Alliance.BLACK, 8));
		builder.setPiece(new Pawn(Alliance.BLACK, 9));
		builder.setPiece(new Pawn(Alliance.BLACK, 10));
		builder.setPiece(new Pawn(Alliance.BLACK, 11));
		builder.setPiece(new Pawn(Alliance.BLACK, 12));
		builder.setPiece(new Pawn(Alliance.BLACK, 13));
		builder.setPiece(new Pawn(Alliance.BLACK, 14));
		builder.setPiece(new Pawn(Alliance.BLACK, 15));
		
		// White layout
		builder.setPiece(new Pawn(Alliance.WHITE, 48));
		builder.setPiece(new Pawn(Alliance.WHITE, 49));
		builder.setPiece(new Pawn(Alliance.WHITE, 50));
		builder.setPiece(new Pawn(Alliance.WHITE, 51));
		builder.setPiece(new Pawn(Alliance.WHITE, 52));
		builder.setPiece(new Pawn(Alliance.WHITE, 53));
		builder.setPiece(new Pawn(Alliance.WHITE, 54));
		builder.setPiece(new Pawn(Alliance.WHITE, 55));
		builder.setPiece(new Rook(Alliance.WHITE, 56));
		builder.setPiece(new Knight(Alliance.WHITE, 57));
		builder.setPiece(new Bishop(Alliance.WHITE, 58));
		builder.setPiece(new Queen(Alliance.WHITE, 59));
		builder.setPiece(new King(Alliance.WHITE, 60));
		builder.setPiece(new Bishop(Alliance.WHITE, 61));
		builder.setPiece(new Knight(Alliance.WHITE, 62));
		builder.setPiece(new Rook(Alliance.WHITE, 63));
		
		// Set white to move first
		builder.setMoveMaker(Alliance.WHITE);
		
		// Use the builder to return a standard chess board
		return builder.build();
	}
	
	
	
	// Getter method for all the legal moves on a board
	public Iterable<Move> getAllLegalMoves() {
		// Return an iterable of a concatenation white player's legal moves with black player's legal moves 
		return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
	}
	
	
	
	// Builder class to build an instance of a board
	public static class Builder {
		
		// Map tile IDs to a given piece
		Map<Integer, Piece> boardConfig;
		
		// Variable to store who has the next move
		Alliance nextMoveMaker;
		
		// Declare an en passant pawn
		Pawn enPassantPawn;
		
		
		
		// Constructor
		public Builder() {
			// Declare the board configuration variable
			this.boardConfig = new HashMap<>();
		}
		
		
		
		// Setter method for pieces
		public Builder setPiece(final Piece piece) {
			// Add the piece to the board configuration map
			this.boardConfig.put(piece.getPiecePosition(), piece);
			// Return the updated builder
			return this;
		}
		
		// Setter method for who has the next turn
		public Builder setMoveMaker(final Alliance nextMoveMaker) {
			// Set the value of next move maker
			this.nextMoveMaker = nextMoveMaker;
			// Return the update builder
			return this;
		}
		
		
		
		// Method to build a new board
		public Board build() {
			// Return an immutable board
			return new Board(this);
		}


		// Setter method for an en passant pawn
		public void setEnPassantPawn(Pawn enPassantPawn) {
			this.enPassantPawn = enPassantPawn;
			
		}
	}
}
