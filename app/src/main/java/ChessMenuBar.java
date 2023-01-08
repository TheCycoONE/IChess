import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

 /** IChess version 1.0
  **
  ** Copyright 2006 Stephen Baker (2913895) and Chris Roy (3048899)
  **/
  
public class ChessMenuBar extends JMenuBar implements ActionListener, ItemListener
{
	private static final long serialVersionUID = 20L;

	public JMenu file;			// File Menu
	public JMenuItem fNew;		// New board
	public JMenuItem fOpen;		// Open board
	public JMenuItem fQuit;		// Quit app
	
	public JSeparator fSeparate;
		
	public JMenu player;  //A submenu
	public JCheckBoxMenuItem whiteIsComputer; //A checkbox 
	public JCheckBoxMenuItem blackIsComputer; //Another Checkbox
	
	private JMenu difficulty;
	private ButtonGroup difficultyGroup;
	private JRadioButtonMenuItem naive;
	private JRadioButtonMenuItem easy;
	private JRadioButtonMenuItem intermediate;
	private JRadioButtonMenuItem hard;
	private JRadioButtonMenuItem veryhard;
	

	public ChessMenuBar()
	{
		super();
		
		// File menu setup------------------------------------------------------
		file = new JMenu("File");
		
		fNew = new JMenuItem("New Game");
		fNew.addActionListener(this);
				
		fOpen = new JMenuItem("Open Game State");
		fOpen.addActionListener(this);
		
		fQuit = new JMenuItem("Quit");
		fQuit.addActionListener(this);
		
		fSeparate = new JSeparator();		// A Separator
		
		// Adds all the options to the menu
		file.add(fNew);
		file.add(fOpen);
		file.add(fSeparate);
		file.add(fQuit);
		
		add(file);
		
		// Player AI selection--------------------------------------------------
		
		player = new JMenu("Player");
		
		whiteIsComputer = new JCheckBoxMenuItem("White is Computer");
		whiteIsComputer.addItemListener(this);
		
		blackIsComputer = new JCheckBoxMenuItem("Black is Computer");
		blackIsComputer.addItemListener(this);
		
		//put these items in the player submenu
		player.add(whiteIsComputer); 
		player.add(blackIsComputer);
		
		//put the player submenu on the menu
		add(player);
		
		// Difficulty-----------------------------------------------------------
		
		difficulty = new JMenu("Difficulty");
		difficultyGroup = new ButtonGroup();
		
		naive = new JRadioButtonMenuItem("Naive");
		naive.addItemListener(this);
		
		easy = new JRadioButtonMenuItem("Easy");
		easy.addItemListener(this);
	
		intermediate = new JRadioButtonMenuItem("Intermediate");
		intermediate.addItemListener(this);
		
		hard = new JRadioButtonMenuItem("Hard");
		hard.addItemListener(this);
		
		veryhard = new JRadioButtonMenuItem("Very Hard (SLOW!)");
		veryhard.addItemListener(this);
		
		difficultyGroup.add(naive);
		difficultyGroup.add(easy);
		difficultyGroup.add(intermediate);
		difficultyGroup.add(hard);
		difficultyGroup.add(veryhard);
		
		difficulty.add(naive);
		difficulty.add(easy);
		difficulty.add(intermediate);
		difficulty.add(hard);
		difficulty.add(veryhard);
		
		
		add(difficulty);
		
		reset();
		
	}
	
	public void reset()
	{
		//----Player AI Selection
		whiteIsComputer.setSelected(IChess.whiteIsComputer);
		blackIsComputer.setSelected(IChess.blackIsComputer);
		
		//---Difficulty
		naive.setSelected(IChess.ply == 0);
		easy.setSelected(IChess.ply == 3);
		intermediate.setSelected(IChess.ply == 4);
		hard.setSelected(IChess.ply == 5);
		veryhard.setSelected(IChess.ply == 6);
		
	}
	
	public void actionPerformed(ActionEvent e)
	{
		//JMenuItem response would go here
		//for an example of actionPerformed see BoardActionListener
		Object source = e.getSource();
		
		if(source == fNew)
		{
//			System.out.println("New default game");
			IChess.gui.defaultBoard();
		}
		else if(source == fOpen)
		{
//			System.out.println("Open file");
			// Should load a new board
			IChess.gui.loadNewBoard();
		}
		else if(source == fQuit)
		{
//			System.out.println("Quit the application");
			System.exit(0);
		}
	}
	
	public void itemStateChanged(ItemEvent e)
	{
		//JCheckBoxMenuItem and JRadioButtonMenuItem response goes here
		Object source = e.getItemSelectable();
		
		if(source == whiteIsComputer)
		{
			IChess.whiteIsComputer = whiteIsComputer.isSelected();
		}
		else if(source == blackIsComputer)
		{
			IChess.blackIsComputer = blackIsComputer.isSelected();
		}
		else if(source == naive)
		{
			IChess.ply = 0;
		}
		else if(source == easy)
		{
			IChess.ply = 3;
		}
		else if(source == intermediate)
		{
			IChess.ply = 4;
		}
		else if(source == hard)
		{
			IChess.ply = 5;
		}
		else if(source == veryhard)
		{
			IChess.ply = 6;
		}
		
	}
}


