package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;



public class GameHistoryPanel extends JPanel {

	// Declare a data model
	private final DataModel model;
	// Declare an instance of a scroll pane to use if number of rows exceeds the size of the j table
	private final JScrollPane scrollPane; 
	
	// Variable to store the dimensions of the game history j panel
	private static final Dimension HISTORY_PANEL_DIMENSION = new Dimension(100, 400);
	
	
	
	// Constructor
	GameHistoryPanel() {
		// Set the layout of the game history panel
		this.setLayout(new BorderLayout());
		// Assign the value of the data model
		this.model = new DataModel();
		
		// Initialize a new j table using the data model
		final JTable table = new JTable(model);
		// Set the height of each row in the j table
		table.setRowHeight(15);
		
		// Set the j table to use a scroll pane if number of rows exceeds the size
		this.scrollPane = new JScrollPane(table);
		// Set the column header of the scroll pane
		scrollPane.setColumnHeaderView(table.getTableHeader());
		// Set the dimension of the scroll pane
		scrollPane.setPreferredSize(HISTORY_PANEL_DIMENSION);
		// Add the scroll pane to the game history panel
		this.add(scrollPane, BorderLayout.CENTER);
		
		// Set the game history panel to be visible
		this.setVisible(true);
	}
	
	
	
	// Method to re-do the move log after a move has been made
	void redo(final Board board, final Table.MoveLog moveHistory) {
		
		// Initialize a variable to store the current row
		int currentRow = 0;
		// Clear the components of the game history panel
		this.model.clear();
		
		// Loop through all the made moves in the move log
		for (final Move move : moveHistory.getMoves()) {
			
			// Initialize a variable to store the to string of a move
			final String moveText = move.toString();
			
			// Check if the moved piece is white
			if (move.getMovedPiece().getPieceAlliance().isWhite()) {
				// Set the value of the current row in the first column
				this.model.setValueAt(moveText, currentRow, 0);
			// Check if the moved piece is black
			} else if (move.getMovedPiece().getPieceAlliance().isBlack() ) {
				// Set the value of the current row in the second column
				this.model.setValueAt(moveText, currentRow, 1);
				// Add one the the value of the current row
				currentRow ++;
			}
		}
		
		// Check if the size of the made move log is bigger than 0
		if (moveHistory.getMoves().size() > 0) {
			
			// Variable to store the last move made in the move log
			final Move lastMove = moveHistory.getMoves().get(moveHistory.size() - 1);
			// Variable to store the to string of the last move
			final String moveText = lastMove.toString();
			
			// Check if the last moved piece is white
			if (lastMove.getMovedPiece().getPieceAlliance().isWhite()) {
				// Add if move put the white king in check or check mate
				this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow, 0);
			} else if (lastMove.getMovedPiece().getPieceAlliance().isBlack()) {
				// Add if move put the black king in check or check mate
				this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow -1, 1);
			}
			
		}
		// Initialize a new J scroll bar by using the get vertical scroll bar method
		final JScrollBar vertical = scrollPane.getVerticalScrollBar();
		// Returns the last value of the j scroll bar if the made move log list if bigger than the panel
		vertical.setValue(vertical.getMaximum());
	}
	
	
	
	// Method to calculate hash for check and check mate
	private String calculateCheckAndCheckMateHash(final Board board) {
		
		// Check if the current player is in check mate
		if (board.currentPlayer().isInCheckMate()) {
			// Return # - portable game notation for check mate
			return "#";
		// Check if the current player is in check
		} else if (board.currentPlayer().isInCheck()) {
			// Return + - portable game notation for check
			return "+";
		}
		// Return empty string if current player is not in check or check mate
		return  "";
	}



	private static class DataModel extends DefaultTableModel {
		
		private final List<Row> values;
		private static final String[] NAMES = { "White", "Black" };
		
		
		
		// Constructor
		DataModel() {
			// Set the values array list
			this.values = new ArrayList<>();
		}
		
		
		// Method to clear the data model of all rows
		public void clear() {
			// Clear the values array list
			this.values.clear();
			// Reset the row count to 0
			setRowCount(0);
		}
		
		
		
		//Override the getter method for the number of rows
		@Override
		public int getRowCount() {
			
			// Check if the values array list is equal to null
			if (this.values == null) {
				// Return 0
				return 0;
			}
			// Return the size of the values array list
			return this.values.size();
		}
		
		
		
		// Override the getter method for the number of columns
		@Override
		public int getColumnCount() {
			// Return the length of the names string array
			return NAMES.length;
		}
		
		
		
		// Override the getter method for the value at a row
		@Override
		public Object getValueAt(final int row, final int column) {
			// Variable to store the current row
			final Row currentRow = this.values.get(row);
			
			// Check if it is the first or second column
			if (column == 0) {
				// Return the white move in the current row
				return currentRow.getWhiteMove();
			} else if (column == 1) {
				// Return the black move in the current row
				return currentRow.getBlackMove();
			}
			// Return null if the column isn't the first or second
			return null;
		}
		
		
		
		// Override the setter method for a value at a row
		@Override
		public void setValueAt(final Object aValue, final int row, final int column) {
			// Declare a variable to store the current row
			final Row currentRow;
			
			// Check if the size of the values array list is less than or equal to the row
			if (this.values.size() <= row) {
				// Assign the value of the current row to a new row
				currentRow = new Row();
				// Add the current row to the values array list
				this.values.add(currentRow);
			} else {
				// Set the value of the current row 
				currentRow = this.values.get(row);
				// Update the row
				fireTableRowsInserted(row, row);
			}
			
			// Check if it is the first or second column
			if (column == 0) {
				// Assign the string value of the white move to current row
				currentRow.setWhiteMove((String)aValue);
			} else if (column == 1) {
				// Assign the string value of the black move to current row
				currentRow.setBlackMove((String) aValue);
				// Update the row and column
				fireTableCellUpdated(row, column);
			}
		}
		
		
		
		// Override the getter method for the column class
		@Override
		public Class<?> getColumnClass(final int column) {
			// Return the class of the move
			return Move.class;
		}
		
		// Override the getter method for the column name
		@Override
		public String getColumnName(final int column) {
			// Return the name of the column
			return NAMES[column];
		}
	}
	
	
	
	private static class Row {
		
		// Variable to store a white move string
		private String whiteMove;
		// Variable to store a black move string
		private String blackMove;
		
		
		
		// Constructor
		Row() {
			
		}
		
		
		
		// Getter method for a white move string
		public String getWhiteMove() {
			// Return the white move
			return this.whiteMove;
		}
		
		// Getter method for a black move string
		public String getBlackMove() {
			// Return the black move
			return this.blackMove;
		}
		
		
		
		// Setter method for a white move string
		public void setWhiteMove(final String move) {
			// Set the value of white move
			this.whiteMove = move;
		}
		
		// Setter method for a black move string
		public void setBlackMove(final String move) {
			// Set the value of black move
			this.blackMove = move;
		}
	}
}
