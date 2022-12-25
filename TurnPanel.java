import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.border.EmptyBorder;

public class TurnPanel extends JPanel
{
	private static final long serialVersionUID = 80L;
	
	private JLabel turnDisplay;
	private JTextArea moveDisplay;
	private JLabel whiteCheckIndicator;
	private JLabel blackCheckIndicator;
	private JScrollPane scrollPane;
	private ImageIcon wCheckOn;
	private ImageIcon wCheckOff;
	private ImageIcon bCheckOn;
	private ImageIcon bCheckOff;
	private Image sideBG;
	
	public TurnPanel()
	{
		try
		{
			sideBG = ImageIO.read(new File("images/SidePanel.png"));
			wCheckOn = new ImageIcon("images/SidePanel/CheckIndicators/wCheckOn.png", "white check");
			wCheckOff = new ImageIcon("images/SidePanel/CheckIndicators/wCheckOff.png");
			bCheckOn = new ImageIcon("images/SidePanel/CheckIndicators/bCheckOn.png", "black check");
			bCheckOff = new ImageIcon("images/SidePanel/CheckIndicators/bCheckOff.png");
		}
		catch(IOException ex)
		{
			System.err.println("Missing images");
		}
		
		setLayout(null);
		
		//setBackground(Color.BLACK);
		turnDisplay = new JLabel("WHITE");
		turnDisplay.setBounds(20,17,200,30);
		add(turnDisplay);
		
		whiteCheckIndicator = new JLabel(wCheckOff);
		whiteCheckIndicator.setBounds(16,50,112,35);
		add(whiteCheckIndicator);
		
		blackCheckIndicator = new JLabel(bCheckOff);
		blackCheckIndicator.setBounds(129,50,112,35);
		add(blackCheckIndicator);
		
		moveDisplay = new JTextArea();
		scrollPane = new JScrollPane(moveDisplay);
		scrollPane.setBounds(40,90,160,590);
		scrollPane.setBorder(new EmptyBorder(0,0,0,0));
		add(scrollPane);	
	}
	
	public void playerInCheck(int player)
	{
		switch(player)
		{	
			case 0: 	//White
				whiteCheckIndicator.setIcon(wCheckOn);
				blackCheckIndicator.setIcon(bCheckOff);
			break;
			case 1: 	//Black
				whiteCheckIndicator.setIcon(wCheckOff);
				blackCheckIndicator.setIcon(bCheckOn);
			break;
			default:
				whiteCheckIndicator.setIcon(wCheckOff);
				blackCheckIndicator.setIcon(bCheckOff);
			break;
		}
	}
	
	public void updateTurn(Point origin, Point destination)
	{
		String chessOrigin;
		String chessDestination;
		
		chessOrigin = translateToChessCoord(origin);
		chessDestination = translateToChessCoord(destination);
		
		
		if(IChess.turn % 2 == 1)
		{
			//Insert newline if not first entry
			if(IChess.turn != 1) 
			{
				moveDisplay.append("\r\n");
			}
			moveDisplay.append(IChess.turn / 2 + 1 + ":");
			turnDisplay.setText("BLACK");
		}
		else
		{
			moveDisplay.append("\t");
			turnDisplay.setText("WHITE");
		}
		moveDisplay.append(chessOrigin + " -> " + chessDestination);	
	}
	
	public void saveTurns(JFrame owner)
	{
		try
		{
			BufferedWriter outFile;
			JFileChooser jfc = new JFileChooser();
			int jfcReturnCode;
			
			/*prompts user for a file to save game too*/
			jfcReturnCode = jfc.showSaveDialog(owner);		
			if (jfcReturnCode == jfc.APPROVE_OPTION)
			{
				outFile = new BufferedWriter(new FileWriter(jfc.getSelectedFile()));	
			}
			else
			{
				throw new IOException();
			}
			
			outFile.write(moveDisplay.getText());
			outFile.close();
			
			JOptionPane.showMessageDialog(this,"Moves have been saved to file","Save complete", JOptionPane.INFORMATION_MESSAGE);
		}
		catch(IOException ex)
		{
			System.err.println("Error writting to file");
		}
		
		
	}
	
	public void reset()
	{
		turnDisplay.setText("WHITE");
		moveDisplay.setText("");
		playerInCheck(-1);
	}
	
	private String translateToChessCoord(Point realCoord)
	{
		String chessCoord;
		char[] cc = new char[2];
		cc[0] = (char) (realCoord.x + 'A');
		cc[1] = (char) (realCoord.y + '1');
		
		chessCoord = new String(cc);
		
		return chessCoord;
	}


	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(sideBG != null)
		{
			g.drawImage(sideBG,0,0,this);
		}
	}
}