package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.google.common.primitives.Ints;



public class TakenPiecesPanel extends JPanel{

	// J panel to store top sub panel to store taken pieces
	private final JPanel northPanel;
	// J panel to store bottom sub panel to store taken pieces
	private final JPanel southPanel;
	
	// Variable to store beige color for panels
	private static final Color PANEL_COLOR = Color.decode("0xFDF5E6");
	// Variable to store the dimensions of the taken pieces j panel
	private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40, 80);
	//Initialize an etched border
	private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
	
	
	
	// Constructor
	public TakenPiecesPanel() {
		// Call the border layout super constructor 
		super(new BorderLayout());
		// Set the background to be a beige color
		this.setBackground(PANEL_COLOR);
		// Set the border as an etched border
		this.setBorder(PANEL_BORDER);
		
		// Set the top taken pieces sub panel to accept 16 taken pieces - 8 x 2
		this.northPanel = new JPanel(new GridLayout(8, 2));
		// Set the bottom taken pieces sub panel to accept 16 taken pieces - 8 x 2
		this.southPanel = new JPanel(new GridLayout(8, 2));
		// Set the color of the top taken pieces sub panel to be beige
		this.northPanel.setBackground(PANEL_COLOR);
		// Set the color of the bottom taken pieces sub panel to be beige
		this.southPanel.setBackground(PANEL_COLOR);
		
		// Add the top taken pieces panel
		this.add(this.northPanel, BorderLayout.NORTH);
		// Add the bottom taken pieces panel
		this.add(this.southPanel, BorderLayout.SOUTH);
		// Set the dimensions of the taken pieces j panel
		setPreferredSize(TAKEN_PIECES_DIMENSION);
	}
	
	
	
	// Method to re-do the move log after a move
	public void redo(final Table.MoveLog moveLog) {
		// Remove all the components from the top taken pieces sub panel
		this.northPanel.removeAll();
		// Remove all the components from the bottom taken pieces sub panel
		this.southPanel.removeAll();
		
		// Initialize an array list to store all the white taken pieces
		final List<Piece> whiteTakenPieces = new ArrayList<>();
		// Initialize an array list to store all the black taken pieces
		final List<Piece> blackTakenPieces = new ArrayList<>();
		
		// Loop through all the moves in the move log
		for (final Move move : moveLog.getMoves()) {
			
			// Check if it is an attack move
			if (move.isAttack()) {
				// Variable to store the taken piece
				final Piece takenPiece = move.getAttackedPiece();
				
				// Check if the taken piece is white or black
				if (takenPiece.getPieceAlliance().isWhite()) {
					// Add the taken pieces to the white taken pieces array list
					whiteTakenPieces.add(takenPiece);
				} else if (takenPiece.getPieceAlliance().isBlack()) {
					// Add the taken pieces to the black taken pieces array list
					blackTakenPieces.add(takenPiece);
				} else {
					// Throw a run time exception error if taken piece is not black or white
					throw new RuntimeException("should not reach here");
				}
			}
		}
		
		// Sort the collection of white taken pieces by value
		Collections.sort(whiteTakenPieces, new Comparator<Piece>() {
			// Override the compare method for white taken pieces
			@Override
			public int compare(Piece o1, Piece o2) {
				// Compare the two white pieces and return them in order
				return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
			}	
		});
		
		// Sort the collection of black taken pieces by value
		Collections.sort(blackTakenPieces, new Comparator<Piece>() {
			// Override the compare method for black taken pieces
			@Override
			public int compare(Piece o1, Piece o2) {
				// Compare the two black pieces and return them in order
				return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
			}	
		});
		
		// Loop through all the pieces in the white taken pieces array list
		for (final Piece takenPiece : whiteTakenPieces) {
			try {
				// Initialize a new buffered image using a concatenation of the piece alliance substring and the tiles to string - e.g WB.gif for white bishop
				final BufferedImage image = ImageIO.read(new File("art/pieces/plain/" + 
											takenPiece.getPieceAlliance().toString().substring(0, 1) + "" + takenPiece.toString() + ".gif"));
				// Initialize a new image icon using the buffered image
				final ImageIcon icon = new ImageIcon(image);
				// Initialize a new j label to store the image icon
				final JLabel imageLabel = new JLabel(icon);
				// Add the j label to the bottom take piece sub panel
				this.southPanel.add(imageLabel);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		
		// Loop through all the pieces in the black taken pieces array list
		for (final Piece takenPiece : blackTakenPieces) {
			try {
				// Initialize a new buffered image using a concatenation of the piece alliance substring and the tiles to string - e.g WB.gif for white bishop
				final BufferedImage image = ImageIO.read(new File("art/pieces/plain/" + 
											takenPiece.getPieceAlliance().toString().substring(0, 1) + "" + takenPiece.toString() + ".gif"));
				// Initialize a new image icon using the buffered image
				final ImageIcon icon = new ImageIcon(image);
				// Initialize a new j label to store the image icon
				final JLabel imageLabel = new JLabel(icon);
				// Add the j label to the bottom take piece sub panel
				this.southPanel.add(imageLabel);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		// Layout the taken pieces panel and it's sub components
		validate();
	}
}
