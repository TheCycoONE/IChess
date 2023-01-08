import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/** IChess version 1.0 * * Copyright 2006 Stephen Baker (2913895) and Chris Roy (3048899) */
public class PawnPromotePanel extends JPanel implements ActionListener {
  private static final long serialVersionUID = 60L;

  private JButton queenButton; // The button for a queen
  private JButton knightButton; // The button for a knight
  private JButton rookButton; // The button for a rook
  private JButton bishopButton; // The button for a bishop
  private Image pawnPromoteBG; // The background for the dialog

  private ImageIcon rIcon; // Rook icon
  private ImageIcon rHIcon; // Rook highlight icon
  private ImageIcon bIcon; // Bishop icon
  private ImageIcon bHIcon; // Bishop highlight icon
  private ImageIcon kIcon; // Knight icon
  private ImageIcon kHIcon; // Knight highlight icon
  private ImageIcon qIcon; // Queen icon
  private ImageIcon qHIcon; // Queen highlight icon

  private int status = -1;

  public PawnPromotePanel() {
    try {
      pawnPromoteBG = ImageIO.read(new File("images/piecePromotion.png"));
    } catch (IOException ex) {
      System.err.println("images not found");
    }

    String colour = "a";

    // Based on the turn value this will decide what image set to load
    if (IChess.turn % 2 == 0) {
      colour = "White/w";
    } else {
      colour = "Black/b";
    }

    // Loading the images for all the icons
    qIcon = new ImageIcon("images/Pieces/" + colour + "Queen.png", "Queen");
    qHIcon = new ImageIcon("images/Pieces/" + colour + "hQueen.png", "Queen");
    kIcon = new ImageIcon("images/Pieces/" + colour + "Knight.png", "Knight");
    kHIcon = new ImageIcon("images/Pieces/" + colour + "hKnight.png", "Knight");
    rIcon = new ImageIcon("images/Pieces/" + colour + "Rook.png", "Rook");
    rHIcon = new ImageIcon("images/Pieces/" + colour + "hRook.png", "Rook");
    bIcon = new ImageIcon("images/Pieces/" + colour + "Bishop.png", "Bishop");
    bHIcon = new ImageIcon("images/Pieces/" + colour + "hBishop.png", "Bishop");

    setBorder(new EmptyBorder(32, 0, 0, 0));
    setLayout(new GridLayout(0, 4));
    setBackground(UI.TRANSPARENT);

    // Creates a button for the rook
    rookButton = new JButton(rIcon); // idle state
    rookButton.setRolloverIcon(rHIcon); // Highlight state
    rookButton.setBorderPainted(false); // No border around the icon
    rookButton.setContentAreaFilled(false); // no colour fill
    rookButton.setFocusPainted(false); // Removes focus highlight
    rookButton.setActionCommand("" + PawnPromoteDialog.ROOK);
    rookButton.addActionListener(this);
    add(rookButton);

    // Creates a button for the knight
    knightButton = new JButton(kIcon);
    knightButton.setRolloverIcon(kHIcon);
    knightButton.setBorderPainted(false);
    knightButton.setContentAreaFilled(false);
    knightButton.setFocusPainted(false);
    knightButton.setActionCommand("" + PawnPromoteDialog.KNIGHT);
    knightButton.addActionListener(this);
    add(knightButton);

    // Creates a button for the bishop
    bishopButton = new JButton(bIcon);
    bishopButton.setRolloverIcon(bHIcon);
    bishopButton.setBorderPainted(false);
    bishopButton.setContentAreaFilled(false);
    bishopButton.setFocusPainted(false);
    bishopButton.setActionCommand("" + PawnPromoteDialog.BISHOP);
    bishopButton.addActionListener(this);
    add(bishopButton);

    // Creates a button for the queen
    queenButton = new JButton(qIcon);
    queenButton.setRolloverIcon(qHIcon);
    queenButton.setBorderPainted(false);
    queenButton.setContentAreaFilled(false);
    queenButton.setFocusPainted(false);
    queenButton.setActionCommand("" + PawnPromoteDialog.QUEEN);
    queenButton.addActionListener(this);
    add(queenButton);
  }

  public int getStatus() {
    return status;
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (pawnPromoteBG != null) {
      g.drawImage(pawnPromoteBG, 0, 0, this);
    }
  }

  public void actionPerformed(ActionEvent e) {
    status = Integer.parseInt(e.getActionCommand());
    if (status > -1) {
      this.setVisible(false);
    }
  }
}
