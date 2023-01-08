import java.awt.*;
import java.awt.event.*;

/** IChess version 1.0 * * Copyright 2006 Stephen Baker (2913895) and Chris Roy (3048899) */
public class BoardActionListener implements ActionListener {
  public synchronized void actionPerformed(ActionEvent e) {
    Point cSquare;

    // Only accept input from a human during the human's turn
    if (IChess.turn % 2 == 0 && IChess.whiteIsComputer == false
        || IChess.turn % 2 == 1 && IChess.blackIsComputer == false) {

      cSquare = translateToPoint(e.getActionCommand());
      IChess.gui.chosenSquare(cSquare);
    }
  }

  private Point translateToPoint(String chessCoord) {
    Point realCoord = new Point();

    realCoord.x = (int) chessCoord.charAt(0) - 'A';
    realCoord.y = (int) chessCoord.charAt(1) - '1';

    return realCoord;
  }
}
