import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChessMenuBar extends JMenuBar implements ActionListener, ItemListener
{
	private static final long serialVersionUID = 20L;
	
	private JMenu player;  //A submenu
	private JCheckBoxMenuItem whiteIsComputer; //A checkbox 
	private JCheckBoxMenuItem blackIsComputer; //Another Checkbox
	
	private JMenu difficulty;
	private ButtonGroup difficultyGroup;
	private JRadioButtonMenuItem easy;
	private JRadioButtonMenuItem intermediate;
	private JRadioButtonMenuItem hard;
	private JRadioButtonMenuItem veryhard;
	
	public ChessMenuBar()
	{
		super();
		
		player = new JMenu("Player");
		
		whiteIsComputer = new JCheckBoxMenuItem("White is Computer");
		whiteIsComputer.setSelected(IChess.whiteIsComputer);
		whiteIsComputer.addItemListener(this);
		
		blackIsComputer = new JCheckBoxMenuItem("Black is Computer");
		blackIsComputer.setSelected(IChess.blackIsComputer);
		blackIsComputer.addItemListener(this);
		
		//put these items in the player submenu
		player.add(whiteIsComputer); 
		player.add(blackIsComputer);
		
		//put the player submenu on the menu
		add(player);
		
		difficulty = new JMenu("Difficulty");
		difficultyGroup = new ButtonGroup();
		
		easy = new JRadioButtonMenuItem("Easy");
		easy.setSelected(IChess.ply == 3);
		easy.addItemListener(this);
	
		intermediate = new JRadioButtonMenuItem("Intermediate");
		intermediate.setSelected(IChess.ply == 4);
		intermediate.addItemListener(this);
		
		hard = new JRadioButtonMenuItem("Hard");
		hard.setSelected(IChess.ply == 5);
		hard.addItemListener(this);
		
		veryhard = new JRadioButtonMenuItem("Very Hard (SLOW!)");
		veryhard.setSelected(IChess.ply == 6);
		veryhard.addItemListener(this);
		
		difficultyGroup.add(easy);
		difficultyGroup.add(intermediate);
		difficultyGroup.add(hard);
		difficultyGroup.add(veryhard);
		
		difficulty.add(easy);
		difficulty.add(intermediate);
		difficulty.add(hard);
		difficulty.add(veryhard);
		
		
		add(difficulty);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		//JMenuItem response would go here
		//for an example of actionPerformed see BoardActionListener
	}
	
	public void itemStateChanged(ItemEvent e)
	{
		//JCheckBoxMenuItem and JRadioButtonMenuItem response goes here
		Object source = e.getItemSelectable();
		
		if(source == whiteIsComputer)
		{
			System.out.println("Whis is computer selected");
			IChess.whiteIsComputer = whiteIsComputer.isSelected();
		}
		else if(source == blackIsComputer)
		{
			IChess.blackIsComputer = blackIsComputer.isSelected();
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


