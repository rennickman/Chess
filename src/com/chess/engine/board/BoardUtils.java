package com.chess.engine.board;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class BoardUtils {

	// Constants to store booleans for all tiles - where named column is true and all other tiles are false
	public static final boolean[] FIRST_COLUMN = initColumn(0);
	public static final boolean[] SECOND_COLUMN = initColumn(1);
	public static final boolean[] SEVENTH_COLUMN = initColumn(6);
	public static final boolean[] EIGHTH_COLUMN = initColumn(7);
	
	// Constants to store booleans for all rows - where named column is true and all other tiles are false
	public static final boolean[] EIGHTH_RANK = initRow(0);
	public static final boolean[] SEVENTH_RANK = initRow(8);
	public static final boolean[] SIXTH_RANK = initRow(16);
	public static final boolean[] FIFTH_RANK = initRow(24);
	public static final boolean[] FOURTH_RANK = initRow(32);
	public static final boolean[] THIRD_RANK = initRow(40);
	public static final boolean[] SECOND_RANK = initRow(48);
	public static final boolean[] FIRST_RANK = initRow(56);
	
	// String array to hold the algebraic chess style moves made
	public static final String[] ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
	// Map to hold
	public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionCoordinateMap();
	
	// Variables to store the number of tiles on a board and number of tiles in a row
	public static final int NUM_TILES = 64;
	public static final int NUM_TILES_PER_ROW = 8;

	
	
	// Constructor
	private BoardUtils() {
		// Throw runtime exception in case someone tries to instantiate it
		throw new RuntimeException("You cannot instantiate me!");
	}
	
		
		
		
	private static Map<String, Integer> initializePositionCoordinateMap() {
		// Initialize a hash map to store position coordinates
		final Map<String, Integer> positionToCoordinate = new HashMap<>();
		
		// Loop through all the tiles on the game board
		for (int i = 0; i < NUM_TILES; i++) {
			System.out.println(ALGEBRAIC_NOTATION[i]);
			// Add the notation to the position to coordinate hash map
			positionToCoordinate.put(ALGEBRAIC_NOTATION[i], i);
		}
		// Return an immutable copy of the position to coordinate hash map
		return ImmutableMap.copyOf(positionToCoordinate);
	}

	
		
	private static String[] initializeAlgebraicNotation() {
		
		return new String[] {
				"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
				"a7", "b7", "c7", "d7", "e7", "f7", "g8", "h7",
				"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
				"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
				"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
				"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
				"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
				"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1",
		};
	}	
	
		
		
	// Method to initialize columns
	private static boolean[] initColumn(int columnNumber) {
		// An array to store 64 booleans - number of tiles on chess board
		final boolean[] column = new boolean[NUM_TILES];
		
		do {
			// Set boolean at column number to be true
			column[columnNumber] = true;
			// Add 8 to the coordinates of the column number - 8 tiles in a row
			columnNumber += NUM_TILES_PER_ROW;
		} while (columnNumber < NUM_TILES);
		// Return the array of 64 booleans
		return column;
	}



	// Method to initialize rows
	private static boolean[] initRow(int rowNumber) {
		// An array to store 64 booleans - number of tiles on chess board
		final boolean[] row = new boolean[NUM_TILES];
		
		do {
			// Set boolean at row number to be true
			row[rowNumber] = true;
			// Increment row number to loop through the array of tiles
			rowNumber ++;	
		} while (rowNumber % NUM_TILES_PER_ROW != 0);
		// Return the array of 64 booleans
		return row;
	}

	
	
	
	
	
	
	// Method to check if a tile is out of bounds
	public static boolean isValidTileCoordinate(final int coordinate) {
		// Return coordinate if tile isn't less than zero or more than 64 - number of tiles on chess board
		return coordinate >= 0 && coordinate < NUM_TILES;
	}
	
	
	// Method to take in an algebraic chess style tile coordinate and return a numbered tile coordinate
	public static int getCoordinateAtPosition(final String position) {
		return POSITION_TO_COORDINATE.get(position);
	}
	
	// Method to take in numbered tile coordinate and return an algebraic chess style tile coordinate
	public static String getPositionAtCoordinate(final int coordinate) {
		return ALGEBRAIC_NOTATION[coordinate];
	}
}
