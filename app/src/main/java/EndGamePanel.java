import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

 /** IChess version 1.0
  **
  ** Copyright 2006 Stephen Baker (2913895) and Chris Roy (3048899)
  **/
  
public class EndGamePanel extends JPanel implements ActionListener
{
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
	
	/** The End Game Dialog
	 **
	 ** The end game dialog will show up once the game is a draw or a mate has
	 ** occured. The dialog has three choices on it, "Play". "Save", and "Quit".
	 ** Play will start a new game with the AI turned off. Save will save the
	 ** list of moves that the game generated. Quit will terminate the app.
	 */
	public EndGamePanel(String status, int winner)
	{
		try
		{
			endGameBG = ImageIO.read
			(
				new File("images/EndGame/endGameDialog.png")
			);
		}
		catch(IOException ex)
		{
			System.err.println("images not found");
		}
		
		// Loads the images for the icons
		nPlay = new ImageIcon("images/EndGame/playIdle.png", "play");
		rPlay = new ImageIcon("images/EndGame/playOver.png", "play");
		nQuit = new ImageIcon("images/EndGame/quitIdle.png", "quit");
		rQuit = new ImageIcon("images/EndGame/quitOver.png", "quit");
		nSave = new ImageIcon("images/EndGame/saveIdle.png", "save");
		rSave = new ImageIcon("images/EndGame/saveOver.png", "save");
		
		setLayout(null);
		setBackground(UI.TRANSPARENT);
		
		statusLabel = new JLabel(status);
		statusLabel.setForeground(Color.BLACK);
		statusLabel.setBackground(Color.WHITE);
		statusLabel.setBounds(60,30, 150, 20);
		add(statusLabel);
		
		if(winner == 0)
		{
			winnerLabel = new JLabel("Victory goes to white!");
		}
		else if(winner == 1)
		{
			winnerLabel = new JLabel("Victory goes to black!");
		}
		else
		{
			winnerLabel = new JLabel("No winner this match");
		}
		winnerLabel.setBounds(150,30,150,20);
		add(winnerLabel);
		
		// Sets up the quit button
		quitButton = new JButton(nQuit);
		quitButton.setRolloverIcon(rQuit);
		quitButton.setBorderPainted(false);
		quitButton.setContentAreaFilled(false);
		quitButton.setFocusPainted(false);
		quitButton.setActionCommand("" + EndGameDialog.QUIT);
		quitButton.addActionListener(this);
		quitButton.setLocation(100,100);
		quitButton.setBounds(50,70,80,30);
		add(quitButton);
		
		// Sets up the play button
		playButton = new JButton(nPlay);
		playButton.setRolloverIcon(rPlay);
		playButton.setBorderPainted(false);
		playButton.setContentAreaFilled(false);
		playButton.setFocusPainted(false);
		playButton.setActionCommand("" + EndGameDialog.PLAY_AGAIN);
		playButton.addActionListener(this);
		playButton.setLocation(100,100);
		playButton.setBounds(150,70,80,30);
		add(playButton);
		
		// Sets up the save button
		saveButton = new JButton(nSave);
		saveButton.setRolloverIcon(rSave);
		saveButton.setBorderPainted(false);
		saveButton.setContentAreaFilled(false);
		saveButton.setFocusPainted(false);
		saveButton.setActionCommand("" + EndGameDialog.SAVE);
		saveButton.addActionListener(this);
		saveButton.setLocation(100,100);
		saveButton.setBounds(250,70,80,30);
		add(saveButton);
	}
	
	public int getStatus()
	{
		return status;
	}
		
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);	
		if(endGameBG != null)
		{
			g.drawImage(endGameBG,0,0,this);
		}	
	}
	
	// will hide the window
	public void actionPerformed(ActionEvent e)
	{
		status = Integer.parseInt(e.getActionCommand());
		if(status > -1)
		{
			this.setVisible(false);
		}
	}
	
}