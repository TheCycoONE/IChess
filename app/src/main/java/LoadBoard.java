import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/** IChess version 1.0 * * Copyright 2006 Stephen Baker (2913895) and Chris Roy (3048899) */
public class LoadBoard {
  public Board getBoard() throws IOException {
    Board aBoard = new Board();

    BufferedReader inFile;
    final JFileChooser fc = new JFileChooser();
    int returnVal;

    int thePiece = 0; // holds the value for the piece 1-6
    int theSign = 0; // Sign to determine the colour -1 or 1
    int yValue = 0; // y value of the piece
    int xValue = 0; // x value of the piece

    String inLine; // a combination of colour, piece, and position

    int jfcReturnCode = fc.showOpenDialog(IChess.gui);

    if (jfcReturnCode == fc.APPROVE_OPTION) {
      inFile = new BufferedReader(new FileReader(fc.getSelectedFile()));
    } else {
      throw new IOException();
    }

    while (true) {
      inLine = inFile.readLine();
      if (inLine == null || inLine.equals("EOF")) break;

      if (inLine.charAt(0) == '#') continue;
      //			System.out.println(inLine);

      // determines if the piece is black (-1) or white (1)
      if (inLine.charAt(0) == 'b') {
        theSign = -1;
      } else if (inLine.charAt(0) == 'w') {
        theSign = 1;
      }

      // the next char determines the piece
      char theType = inLine.charAt(1);

      switch (theType) {
        case 'P': // Pawn
          thePiece = 1;
          break;
        case 'R': // Rook
          thePiece = 2;
          break;
        case 'N': // Knight
          thePiece = 3;
          break;
        case 'B': // Bishop
          thePiece = 4;
          break;
        case 'Q': // Queen
          thePiece = 5;
          break;
        case 'K': // King
          thePiece = 6;
          break;
      }

      // X value is stored A-H as its chess notation.
      xValue = ((int) inLine.charAt(2) - 'A');

      // Y value is stored 1-8
      yValue = Integer.parseInt(inLine.charAt(3) + "") - 1;

      // Sets the piece on the new board
      aBoard.theBoard[yValue][xValue] = thePiece * theSign;

      // Creates a point to add to the piece vector
      Point newPoint = new Point(xValue, yValue);

      /**
       * This adds the piece to the appropriate piece vector. * * If the piece is a king its also
       * added to the kings vector. * Furthermore if a king or rook is encounted its checked to see
       * if * its in the default position to determine if castling can happen.
       */
      if (theSign == 1) // White
      {
        aBoard.whitePieces.add(newPoint);
        if (thePiece == 6) // King
        {
          aBoard.theKings[0] = newPoint;
          // Checks to see if the king is in a default position
          if (xValue == 4 && yValue == 0) {
            aBoard.castleCheck[0][1] = true;
          } else {
            aBoard.castleCheck[0][1] = false;
          }
        } else if (thePiece == 2) // Rook
        {
          // Checks to see if both rooks are default
          if ((xValue != 0 || xValue != 7) && yValue != 0) {
            aBoard.castleCheck[0][0] = false;
            aBoard.castleCheck[0][2] = false;
          } else if (xValue == 0 && yValue == 0) {
            aBoard.castleCheck[0][0] = true;
          } else if (xValue == 7 && yValue == 0) {
            aBoard.castleCheck[0][2] = true;
          }
        }

      } else if (theSign == -1) // Black
      {
        aBoard.blackPieces.add(newPoint);
        if (thePiece == 6) // King
        {
          aBoard.theKings[1] = newPoint;
          // Castle
          if (xValue == 4 && yValue == 7) {
            aBoard.castleCheck[1][1] = true;
          } else {
            aBoard.castleCheck[1][1] = false;
          }
        } else if (thePiece == 2) // Rook
        {
          if ((xValue != 0 || xValue != 7) && yValue != 7) {
            aBoard.castleCheck[1][0] = false;
            aBoard.castleCheck[1][2] = false;
          } else if (xValue == 0 && yValue == 7) {
            aBoard.castleCheck[1][0] = true;
          } else if (xValue == 7 && yValue == 7) {
            aBoard.castleCheck[1][2] = true;
          }
        }
      }
    }

    // Last Piece Moved
    aBoard.lastMoved[0] = new Point(-1, -1);
    aBoard.lastMoved[1] = new Point(-1, -1);

    aBoard.lastYDistance[0] = 0;
    aBoard.lastYDistance[1] = 0;

    aBoard.movesUntilDraw = 100;

    return aBoard;
  }
}
