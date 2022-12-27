import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class EndGamePanel extends JPanel implements ActionListener {
  private static final long serialVersionUID = 40L;

  private JLabel statusLabel;
  private JLabel winnerLabel;
  private JButton quitButton;
  private JButton playButton;
  private JButton saveButton;
  private Image endGameBG;
  private ImageIcon nPlay;
  private ImageIcon nQuit;
  private ImageIcon nSave;
  private ImageIcon rPlay;
  private ImageIcon rQuit;
  private ImageIcon rSave;

  private int status = -1;

  public EndGamePanel(String status, int winner) {
    try {
      endGameBG = ImageIO.read(getClass().getResource("/images/EndGame/endGameDialog.png"));
    } catch (IOException ex) {
      System.err.println("images not found");
    }
    nPlay = new ImageIcon(getClass().getResource("/images/EndGame/playIdle.png"), "play");
    rPlay = new ImageIcon(getClass().getResource("/images/EndGame/playOver.png"), "play");
    nQuit = new ImageIcon(getClass().getResource("/images/EndGame/quitIdle.png"), "quit");
    rQuit = new ImageIcon(getClass().getResource("/images/EndGame/quitOver.png"), "quit");
    nSave = new ImageIcon(getClass().getResource("/images/EndGame/saveIdle.png"), "save");
    rSave = new ImageIcon(getClass().getResource("/images/EndGame/saveOver.png"), "save");

    setLayout(null);
    setBackground(UI.TRANSPARENT);

    statusLabel = new JLabel(status);
    statusLabel.setForeground(Color.BLACK);
    statusLabel.setBackground(Color.WHITE);
    statusLabel.setBounds(60, 30, 150, 20);
    add(statusLabel);

    if (winner == 0) {
      winnerLabel = new JLabel("Victory goes to white!");
    } else if (winner == 1) {
      winnerLabel = new JLabel("Victory goes to black!");
    } else {
      winnerLabel = new JLabel("No winner this match");
    }
    winnerLabel.setBounds(150, 30, 150, 20);
    add(winnerLabel);

    quitButton = new JButton(nQuit);
    quitButton.setRolloverIcon(rQuit);
    quitButton.setBorderPainted(false);
    quitButton.setContentAreaFilled(false);
    quitButton.setActionCommand("" + EndGameDialog.QUIT);
    quitButton.addActionListener(this);
    quitButton.setLocation(100, 50);
    quitButton.setBounds(60, 70, 80, 30);
    add(quitButton);

    playButton = new JButton(nPlay);
    playButton.setRolloverIcon(rPlay);
    playButton.setBorderPainted(false);
    playButton.setContentAreaFilled(false);
    playButton.setActionCommand("" + EndGameDialog.PLAY_AGAIN);
    playButton.addActionListener(this);
    playButton.setLocation(100, 100);
    playButton.setBounds(150, 70, 80, 30);
    add(playButton);

    saveButton = new JButton(nSave);
    saveButton.setRolloverIcon(rSave);
    saveButton.setBorderPainted(false);
    saveButton.setContentAreaFilled(false);
    saveButton.setActionCommand("" + EndGameDialog.SAVE);
    saveButton.addActionListener(this);
    saveButton.setLocation(100, 100);
    saveButton.setBounds(250, 70, 80, 30);
    add(saveButton);
  }

  public int getStatus() {
    return status;
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (endGameBG != null) {
      g.drawImage(endGameBG, 0, 0, this);
    }
  }

  public void actionPerformed(ActionEvent e) {
    status = Integer.parseInt(e.getActionCommand());
    if (status > -1) {
      this.setVisible(false);
    }
  }
}
