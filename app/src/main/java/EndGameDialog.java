import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** IChess version 1.0 * * Copyright 2006 Stephen Baker (2913895) and Chris Roy (3048899) */
public class EndGameDialog extends JDialog implements ComponentListener {
  private static final long serialVersionUID = 30L;

  protected static final int QUIT = 0;
  protected static final int PLAY_AGAIN = 1;
  protected static final int SAVE = 2;

  private int status = -1;
  private Container cp;
  private EndGamePanel endGamePanel;

  public EndGameDialog(JFrame owner, String status, int winner) {
    super(owner, true);

    cp = this.getContentPane();

    // Makes the location relative to the IChess main window
    setLocation(IChess.gui.getX() + 200, IChess.gui.getY() + 300);

    setUndecorated(true);
    setMinimumSize(new Dimension(372, 143));
    setPreferredSize(new Dimension(372, 143));

    setBackground(UI.TRANSPARENT);
    cp.setBackground(UI.TRANSPARENT);

    endGamePanel = new EndGamePanel(status, winner);
    endGamePanel.addComponentListener(this);
    cp.add(endGamePanel);

    pack();
  }

  public int getOption() {
    return status;
  }

  public void componentHidden(ComponentEvent e) {
    status = endGamePanel.getStatus();
    this.setVisible(false);
  }

  public void componentMoved(ComponentEvent e) {}

  public void componentResized(ComponentEvent e) {}

  public void componentShown(ComponentEvent e) {}
}
