import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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
    System.out.println("opening 1");

    cp = this.getContentPane();

    setLocation(300, 300);
    setUndecorated(true);
    setMinimumSize(new Dimension(400, 160));
    setPreferredSize(new Dimension(400, 160));
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
