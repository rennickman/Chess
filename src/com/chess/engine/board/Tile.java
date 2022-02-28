package com.chess.engine.board;

import java.util.HashMap;
import java.util.Map;

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;



public abstract class Tile {

	// Variable to store coordinates of a tile - can only be accessed by sub classes and value can't be changed after its assigned during construction
	protected final int tileCoordinate;
	
	// Declare a new member field to hold all the possible empty tiles up front so that they never have to be created again
	private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();
	
	
	
	// Method to store all possible empty tiles in a hash map
	private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
		// Declare a hash map to store all empty tiles
		final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
		
		// Loop through all 64 tiles
		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			// Add a new empty tile to hash map
			emptyTileMap.put(i, new EmptyTile(i));
		}
		// Declare and return an immutable map with the value of the hash map so that its values can't be changed or deleted.
		return ImmutableMap.copyOf(emptyTileMap);
	}
	
	
	
	// Method for creating tiles as constructors are set as private - only method of this class that is accessible
	public static Tile createTile(final int tileCoordinate, final Piece piece) {
		// Return a new occupied tile if it contains a piece, if not get one of the empty tiles from our hash map
		return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
	}
	
	
	
	// Constructor
	private Tile (final int tileCoordinate) {
		// Assign the coordinates to a tile
		this.tileCoordinate = tileCoordinate;
	}
	
	

	// Abstract method to check if tile is occupied or not - defined in a sub class
	public abstract boolean isTileOccupied();
	
	// Method to return a piece from an occupied tile or null from an empty tile - defined in a sub class
	public abstract Piece getPiece();
	
	
	
	// Getter method for the tile coordinate
	public int getTileCoordinate() {
		// Return the coordinate of the tile
		return this.tileCoordinate;
	}
	
	
	
	// Sub class for empty tiles - set as static so they are not inner classes and can be used outside of tile
	public static final class EmptyTile extends Tile {
		
		// Constructor
		private EmptyTile(final int coordinate) {
			// Call the super class constructor with the tile coordinate
			super(coordinate);
		}
		
		
		
		// Override the to string method of an empty tile
		@Override
		public String toString() {
			// Return a dash to represent an empty tile
			return "-";
		}
		
		// Override the abstract methods from Tile class
		@Override
		public boolean isTileOccupied() {
			// Return false as tile is empty
			return false;
		}
		
		@Override
		public Piece getPiece() {
			// Return null as empty tile contains no pieces
			return null;
		}
	}
	
	
	
	// Sub class for occupied tiles - set as static so they are not inner classes and can be used outside of tile
	public static final class OccupiedTile extends Tile {
		
		// Declare a variable to store the piece that is on the file - set as private to the class and value can't be changed after assigned
		private final Piece pieceOnTile;
		
		private OccupiedTile(int tileCoordinate, final Piece pieceOnTile) {
			// Call the super class constructor with the tile coordinate
			super(tileCoordinate);
			// Assign the the piece on the tile
			this.pieceOnTile = pieceOnTile;
		}
		
		
		
		// Override the to string method of an occupied tile
		@Override
		public String toString() {
			// Check the color of the piece occupying the tile and print it's to string - black is set to lower case, white is set to upper case
			return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() :
					getPiece().toString();
		}
		
		// Override the abstract methods from Tile class
		@Override
		public boolean isTileOccupied() {
			// Return true as tile contains a piece
			return true;
		}
				
		@Override
		public Piece getPiece() {
			// Return the value of the piece occupying the tile
			return this.pieceOnTile;
		 }
		
	}
}
