import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PawnPromoteDialog extends JDialog implements ComponentListener {
  private static final long serialVersionUID = 50L;

  protected static final int ROOK = 0;
  protected static final int KNIGHT = 1;
  protected static final int BISHOP = 2;
  protected static final int QUEEN = 3;

  private int status = -1;
  private Container cp;
  private PawnPromotePanel pawnPromotePanel;

  public PawnPromoteDialog(JFrame owner) {
    super(owner, true);

    cp = this.getContentPane();

    setLocation(300, 300);
    setUndecorated(true);
    setMinimumSize(new Dimension(360, 140));
    setPreferredSize(new Dimension(360, 140));
    setBackground(UI.TRANSPARENT);
    cp.setBackground(UI.TRANSPARENT);

    pawnPromotePanel = new PawnPromotePanel();
    pawnPromotePanel.addComponentListener(this);
    cp.add(pawnPromotePanel);

    pack();

    repaint();
  }

  public int getOption() {
    return status;
  }

  public void componentHidden(ComponentEvent e) {
    status = pawnPromotePanel.getStatus();
    this.setVisible(false);
  }

  public void componentMoved(ComponentEvent e) {}

  public void componentResized(ComponentEvent e) {}

  public void componentShown(ComponentEvent e) {}
}
