package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;



public class Table {

	// Declare an instance of j frame
	private final JFrame gameFrame;
	// Declare an instance of game history panel
	private final GameHistoryPanel gameHistoryPanel;
	// Declare an instance of taken pieces panel
	private final TakenPiecesPanel takenPiecesPanel;
	// Declare an instance of board panel
	private final BoardPanel boardPanel;
	// Declare an instance of move log
	private final MoveLog moveLog;
	
	// Variable to store the chess board
	private Board chessBoard;
	
	// Variable to store the original tile position of the piece
	private Tile sourceTile;
	// Variable to store the destination tile of the piece
	private Tile destinationTile;
	// Variable to store the human piece that is being moved
	private Piece humanMovedPiece;
	// Variable to store the current board direction
	private BoardDirection boardDirection;
	
	// Variable to store whether or not highlight legal moves is selected
	private boolean highlightLegalMoves;
	
	// Variable to store the dimensions of the game frame
	private static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
	// Variable to store the dimensions of the board panel
	private static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
	// Variable to store the dimensions of the board panel
	private static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
	
	// Set the value of the default path for the visual icons
	private static String defaultPieceImagePath = "art/pieces/plain/";
	
	// Variables to store the shades of colors for the tiles
	private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");
	
	
	
	// Constructor
	public Table() {
		// Assign the value of game frame
		this.gameFrame = new JFrame("Chess");
		// Initialize a new border layout for the game frame
		this.gameFrame.setLayout(new BorderLayout());
		
		// Initialize an instance of a j menu bar using the create table menu bar method
		final JMenuBar tableMenuBar = CreateTableMenuBar();
		// Add the populate menu bar to the game frame
		this.gameFrame.setJMenuBar(tableMenuBar);
		// Set the dimensions of the game frame
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		
		// Call the create standard board method to set the value of the chess board
		this.chessBoard = Board.createStandardBoard();
		
		// Assign the value of game history panel
		this.gameHistoryPanel = new GameHistoryPanel();
		// Assign the value of taken pieces panel
		this.takenPiecesPanel = new TakenPiecesPanel();
		// Assign the value of board panel
		this.boardPanel = new BoardPanel();
		// Assign the value of move log
		this.moveLog = new MoveLog();
		
		// Assign the board direction of the board panel
		this.boardDirection = BoardDirection.NORMAL;
		// Assign the value of highlight legal moves
		this.highlightLegalMoves = false;
		
		
		// Add the board panel to the center of the game frame
		this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
		// Add the board panel to the center of the game frame
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		// Add the board panel to the center of the game frame
		this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
		// Set the game frame as visible
		this.gameFrame.setVisible(true);
	}

	
	
	// Method to create the menu bar
	private JMenuBar CreateTableMenuBar() {
		// Initialize an instance of a menu bar
		final JMenuBar tableMenuBar = new JMenuBar();
		// Call the create file menu method
		tableMenuBar.add(createFileMenu());
		// Call the create preferences menu method
		tableMenuBar.add(createPreferencesMenu());
		// Return the j menu bar
		return tableMenuBar;
	}


	
	// Method to create the file menu
	private JMenu createFileMenu() {
		// Initialize the file J menu bar
		final JMenu fileMenu = new JMenu("File");
		
		// Initialize a j menu item to open portable game notation files - previously played chess game files
		final JMenuItem openPGN = new JMenuItem("Open PGN");
		// Add action listener for the open portable game notation files menu item
		openPGN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("open up that pgn file!!");
			}
		});
		
		// Initialize a j menu item to exit the game
		final JMenuItem exitMenuItem = new JMenuItem("Exit");
		// Add action listener for the exit game menu item
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		// Add the open portable game notation menu item to the file j menu bar
		fileMenu.add(openPGN);
		// Add the exit menu item to the file j menu bar
		fileMenu.add(exitMenuItem);
		
		// Return the file j menu bar
		return fileMenu;
	}
	
	
	
	// Method to create the preference menu
	private JMenu createPreferencesMenu() {
		// Initialize the preferences J menu bar
		final JMenu preferencesMenu = new JMenu("Preferences");
		
		// Initialize a j menu item to flip the board
		final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
		// Add action listener for the flip board menu item
		flipBoardMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Flip the board direction
				boardDirection = boardDirection.opposite();
				// Call the draw board method to redraw the chess board
				boardPanel.drawBoard(chessBoard);
			}
		});
		// Add the flip board menu item to the preferences j menu bar
		preferencesMenu.add(flipBoardMenuItem);
		// Add a separator between the flip board menu item and the legal move highlight check box
		preferencesMenu.addSeparator();
		
		// Initialize a j menu check box item to highlight legal moves with false as default setting
		final JCheckBoxMenuItem legalMoveHighlighterCheckbox = new JCheckBoxMenuItem("Highlight Legal Moves", false);
		
		legalMoveHighlighterCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Assign the value of the check box to the highlight legal moves variable
				highlightLegalMoves = legalMoveHighlighterCheckbox.isSelected();
			}
		});
		// Add the highlight legal moves check box menu item to the preferences j menu bar
		preferencesMenu.add(legalMoveHighlighterCheckbox);
		
		// Return the preferences j menu bar
		return preferencesMenu;
	}
	
	
	
	// Enum to store the board direction
	public enum BoardDirection {
		
		NORMAL {
			
			// Override the traverse method for the normal board direction
			@Override
			List<TilePanel> traverse(List<TilePanel> boardTiles) {
				// Return the list of tiles on the board
				return boardTiles;
			}

			// Override the opposite method for the normal board direction
			@Override
			BoardDirection opposite() {
				// Return the flipped board direction
				return FLIPPED;
			}
		},
		FLIPPED {
			
			// Override the traverse method for the flipped board direction
			@Override
			List<TilePanel> traverse(List<TilePanel> boardTiles) {
				// Return a reversed list of the tiles on the board
				return Lists.reverse(boardTiles);
			}

			// Override the opposite method for the flipped board direction
			@Override
			BoardDirection opposite() {
				// Return the normal board direction
				return NORMAL;
			}
		};
		
		
		
		// Method to return a list of the tiles on the board
		abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
		
		// Method to return the opposite board direction
		abstract BoardDirection opposite();
	}
	
	
	
	// Board panel subclass - represents the game board
	private class BoardPanel extends JPanel {
		
		// List to store all the tiles on the board
		final List<TilePanel> boardTiles;
		
		
		
		// Constructor
		BoardPanel() {
			// Call the grid layout super constructor and set it equal to 8 by 8 - number of rows and columns on a chess board
			super(new GridLayout(8, 8));
			// Initialize the board tiles list
			this.boardTiles = new ArrayList<>();
			
			// Loop through the 64 tiles on a chess board
			for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
				// Initialize a new tile panel
				final TilePanel tilePanel = new TilePanel(this, i);
				// Add the tile panel to the board tiles array list
				this.boardTiles.add(tilePanel);
				// Add the tile panel to the board panel
				add(tilePanel);
			}
			// Set the dimensions of the board panel
			setPreferredSize(BOARD_PANEL_DIMENSION);
			// Layout the sub components of the board panel
			validate();
		}
		
		
		
		// Method to redraw the chess board after a move is made
		public void drawBoard(final Board board) {
			// Remove all the components in the board panel
			removeAll();
			
			// Loop through all the tiles in the board direction's tiles array list
			for (final TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
				// Call each tiles draw tile method
				tilePanel.drawTile(board);
				// Add the tile to the board panel
				add(tilePanel);
			}
			// Layout the board panel and it's sub components
			validate();
			// Repaint the board panel
			repaint();
		}
	}
	
	
	
	// Move log class to store all the moves that have been made
	public static class MoveLog {
		
		// List to store the moves that have been made
		private final List<Move> moves;
		
		
		
		// Constructor
		MoveLog() {
			// Assign the value of the moves list
			this.moves = new ArrayList<>();
		}
		
		
		
		// Getter method for the list of made moves
		public List<Move> getMoves() {
			// Return the list of moves
			return this.moves;
		}
		
		// Method to add a new move to the made moves log list
		public void addMove(final Move move) {
			// Add a move to the moves log list
			this.moves.add(move);
		}
		
		// Method to return the size of the made moves log list
		public int size() {
			// Return the size of the moves log list
			return this.moves.size();
		}
		
		// Method to clear the made moves log list
		public void clear() {
			this.moves.clear();
		}
		
		// Method to accept an index and remove a move from the made moves log list
		public Move removeMove(int index) {
			// Return the removed move
			return this.moves.remove(index);
		}
		
		// Method to accept a move and remove it from the made moves log list
		public boolean removeMove(final Move move) {
			// Return a boolean after removing the move
			return this.moves.remove(move);
		}
		
	}
	
	
	
	// Tile panel subclass - represents the tiles on the board
	private class TilePanel extends JPanel {
		
		// Variable to store the tile ID
		private final int tileId;
		
		
		
		// Constructor
		TilePanel(final BoardPanel boardPanel, final int tileId) {
			// Call the grid bag layout super constructor
			super(new GridBagLayout());
			// Set the value of the tile ID
			this.tileId = tileId;
			// Set the dimensions of a tile panel
			setPreferredSize(TILE_PANEL_DIMENSION);
			// Set the color of the tile using the assign tile color method
			assignTileColor();
			// Set the visual icon of the piece
			assignTilePieceIcon(chessBoard);
			
			// Add a mouse listener to the tile panel
			addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(final MouseEvent e) {
					
					// Check if it is a right mouse click
					if (SwingUtilities.isRightMouseButton(e)) {
						// Reset the source tile variable
						sourceTile = null;
						// Reset the destination tile variable
						destinationTile = null;
						// Reset the human moved piece variable
						humanMovedPiece = null;
						
					// Check if it is a left mouse click
					} else if (SwingUtilities.isLeftMouseButton(e)) {
						
						// Check if the user hasn't clicked on a piece - first click
						if (sourceTile == null) {
							// Assign the tile to be the source tile
							sourceTile = chessBoard.getTile(tileId);
							// Assign the human moved piece to be the piece occupying the source tile
							humanMovedPiece = sourceTile.getPiece();
							
							// Check if the there is a piece stored on the tile
							if (humanMovedPiece == null) {
								// Reset the value of the source tile to null if it is empty
								sourceTile = null;
							}
						// Check if user has already clicked on a piece - second click
						} else {
							// Assign the tile to be the destination tile
							destinationTile = chessBoard.getTile(tileId);
							// Initialize a new move using the source and destination tile
							final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
							// Initialize a new transition board using the move
							final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
							
							// Check if the move can be completed on the transition board
							if (transition.getMoveStatus().isDone()) {
								// Assign the value of the transition board to the chess board
								chessBoard = transition.getTransitionBoard();
								// Add the move to the move log
								moveLog.addMove(move);
							}
							// Reset the source tile variable
							sourceTile = null;
							// Reset the destination tile variable
							destinationTile = null;
							// Reset the human moved piece variable
							humanMovedPiece = null;
						}
						
						// Use the invoke later method to redraw the chess board after making a move
						SwingUtilities.invokeLater(new Runnable( ) {
							
							@Override
							public void run() {
								// Update the game history and taken pieces side panels
								gameHistoryPanel.redo(chessBoard, moveLog);
								takenPiecesPanel.redo(moveLog);
								// Redraw the chess board on the board panel
								boardPanel.drawBoard(chessBoard);
							}
						});
					}
				}

				@Override
				public void mousePressed(final MouseEvent e) {
					
				}

				@Override
				public void mouseReleased(final MouseEvent e) {
					
				}

				@Override
				public void mouseEntered(final MouseEvent e) {
					
				}

				@Override
				public void mouseExited(final MouseEvent e) {
					
				}
				
			});
			// Layout the tile panel and it's sub components
			validate();
		}

		
		
		// Method to draw a tile
		public void drawTile(final Board board) {
			// Assign the tile color
			assignTileColor();
			// Assign the visual icon of the piece on tile
			assignTilePieceIcon(board);
			// Call the highlight legals method to show possible legal moves for each piece
			highlightLegals(chessBoard);
			// Layout the tile panel and it's sub components
			validate();
			// Repaint the tile
			repaint();
		}
		
		
		
		// Method to assign the visual icon for each piece
		private void assignTilePieceIcon(final Board board) {
			// Remove all the components in the tile panel
			this.removeAll();
			
			// Check if the tile is occupied by a piece
			if (board.getTile(this.tileId).isTileOccupied()) {
				// Initialize a new buffered image using a concatenation of the piece alliance substring and the tiles to string - e.g WB.gif for white bishop
				try {
					final BufferedImage image = 
							ImageIO.read(new File(defaultPieceImagePath + board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0, 1) +
									board.getTile(this.tileId).getPiece().toString() + ".gif"));
					// Add the image to the tile board
					add(new JLabel(new ImageIcon(image)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		
		private void highlightLegals(final Board board) {
			// Check if highlight legal moves is selected
			if (highlightLegalMoves) {
				
				// Loop through all the moves in the collection of moves returned from calling the piece legal moves method
				for (final Move move : pieceLegalMoves(board)) {
					
					// Check if the destination tile of the move matches the current tile ID
					if (move.getDestinationCoordinate() == this.tileId) {
						// Initialize a green dot to show that the move is legal
						try {
							add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		
		
		// Method to calculate the legal moves of a piece
		private Collection<Move> pieceLegalMoves(final Board board) {
			
			// Check if the human moved piece variable stores a piece and that it belongs to the current player
			if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
				// Call the calculate legal moves method on the human moved piece
				return humanMovedPiece.calculateLegalMoves(board);
			}
			// Return an empty collection of moves
			return Collections.emptyList();
		}

		
		
		// Method to set the color of a tile
		private void assignTileColor() {
			
			// Check if the tile is in the second, fourth, sixth and eighth ranks
			if (BoardUtils.EIGHTH_RANK[this.tileId] || BoardUtils.SIXTH_RANK[this.tileId] ||
				BoardUtils.FOURTH_RANK[this.tileId] || BoardUtils.SECOND_RANK[this.tileId]) {
					// Set the background color of each tile - light for even numbers and dark for odd numbers
					setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
				
			// Check if the tile is in the first, third, fifth or seventh ranks
			} else if (BoardUtils.SEVENTH_RANK[this.tileId] || BoardUtils.FIFTH_RANK[this.tileId] ||
						BoardUtils.THIRD_RANK[this.tileId] || BoardUtils.FIRST_RANK[this.tileId]) {
				// Set the background color of each tile - light for odd numbers and dark for even numbers
				setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
			}
		}
	}
}
