import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

public class PawnPromotePanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 60L;
	
	private JButton queenButton;
	private JButton knightButton;
	private JButton rookButton;
	private JButton bishopButton;
	private Image pawnPromoteBG;
	private ImageIcon rookIcon;
	private ImageIcon bishopIcon;
	private ImageIcon knightIcon;
	private ImageIcon queenIcon;
	
	private int status = -1;
	
	public PawnPromotePanel()
	{
		try
		{
			pawnPromoteBG = ImageIO.read(new File("images/piecePromotion.png"));
		}
		catch(IOException ex)
		{
			System.err.println("images not found");
		}
		
		queenIcon = new ImageIcon("images/Pieces/White/wQueen.png", "Queen");
		knightIcon = new ImageIcon("images/Pieces/White/wKnight.png", "Knight");
		rookIcon = new ImageIcon("images/Pieces/White/wRook.png", "Rook");
		bishopIcon = new ImageIcon("images/Pieces/White/wBishop.png", "Bishop");
		
		setLayout(new GridLayout(0,4));
		setBackground(UI.TRANSPARENT);
		
		queenButton = new JButton(queenIcon);
		queenButton.setRolloverIcon(queenIcon);
		queenButton.setBorderPainted(false);
		queenButton.setContentAreaFilled(false);
		queenButton.setActionCommand("" + PawnPromoteDialog.QUEEN);
		queenButton.addActionListener(this);
		add(queenButton);
		
		rookButton = new JButton(rookIcon);
		rookButton.setRolloverIcon(rookIcon);
		rookButton.setBorderPainted(false);
		rookButton.setContentAreaFilled(false);
		rookButton.setActionCommand("" + PawnPromoteDialog.ROOK);
		rookButton.addActionListener(this);
		add(rookButton);
		
		knightButton = new JButton(knightIcon);
		knightButton.setRolloverIcon(knightIcon);
		knightButton.setBorderPainted(false);
		knightButton.setContentAreaFilled(false);
		knightButton.setActionCommand("" + PawnPromoteDialog.KNIGHT);
		knightButton.addActionListener(this);
		add(knightButton);
		
		bishopButton = new JButton(bishopIcon);
		bishopButton.setRolloverIcon(bishopIcon);
		bishopButton.setBorderPainted(false);
		bishopButton.setContentAreaFilled(false);
		bishopButton.setActionCommand("" + PawnPromoteDialog.BISHOP);
		bishopButton.addActionListener(this);
		add(bishopButton);
		
	}
	
	public int getStatus()
	{
		return status;
	}
		
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);	
		if(pawnPromoteBG != null)
		{
			g.drawImage(pawnPromoteBG,0,0,this);
		}	
	}
	
	public void actionPerformed(ActionEvent e)
	{
		status = Integer.parseInt(e.getActionCommand());
		if(status > -1)
		{
			this.setVisible(false);
		}
	}
	
}