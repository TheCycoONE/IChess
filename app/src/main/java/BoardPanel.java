import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.border.EmptyBorder;

/*BoardPanel class - takes care of representing a board state on the screen*/
public class BoardPanel extends JPanel
{
	private static final long serialVersionUID = 10L;
	
	//The tiles on the board
	private Square[][] square = new Square[8][8];
	
	//All the graphics/icons/etc.
	private Image boardBG;
	private ImageIcon bKing;
	private ImageIcon wKing;
	private ImageIcon bQueen;
	private ImageIcon wQueen;
	private ImageIcon bBishop;
	private ImageIcon wBishop;
	private ImageIcon bKnight;
	private ImageIcon wKnight;
	private ImageIcon bRook;
	private ImageIcon wRook;
	private ImageIcon bPawn;
	private ImageIcon wPawn;
	private ImageIcon hRed;
	private ImageIcon hBlue;
	private ImageIcon hGreen;
	
	
	public BoardPanel()
	{
		setBorder(new EmptyBorder(16,16,16,16));
		setLayout(new GridLayout(8,8));
		
		 try
		 {
			 boardBG = ImageIO.read(getClass().getResource("/images/Board.png"));
			
			 bKing = new ImageIcon(getClass().getResource("/images/Pieces/Black/bKing.png"), "bk");
			 wKing = new ImageIcon(getClass().getResource("/images/Pieces/White/wKing.png"), "wk");
			 bQueen = new ImageIcon(getClass().getResource("/images/Pieces/Black/bQueen.png"), "bq");
			 wQueen = new ImageIcon(getClass().getResource("/images/Pieces/White/wQueen.png"), "wq");
			 bBishop = new ImageIcon(getClass().getResource("/images/Pieces/Black/bBishop.png"), "bb");
			 wBishop = new ImageIcon(getClass().getResource("/images/Pieces/White/wBishop.png"), "wb");
			 bKnight = new ImageIcon(getClass().getResource("/images/Pieces/Black/bKnight.png"), "bn");
			 wKnight = new ImageIcon(getClass().getResource("/images/Pieces/White/wKnight.png"), "wn");
			 bRook = new ImageIcon(getClass().getResource("/images/Pieces/Black/bRook.png"), "br");
			 wRook = new ImageIcon(getClass().getResource("/images/Pieces/White/wRook.png"), "wr");
			 bPawn = new ImageIcon(getClass().getResource("/images/Pieces/Black/bPawn.png"), "bp");
			 wPawn = new ImageIcon(getClass().getResource("/images/Pieces/White/wPawn.png"), "wp");
			 
			 hRed = new ImageIcon(getClass().getResource("/images/Highlights/Red.png"));
			 hBlue = new ImageIcon(getClass().getResource("/images/Highlights/Blue.png"));
			 hGreen = new ImageIcon(getClass().getResource("/images/Highlights/Green.png"));
			 
		}
		catch(IOException e)
		{
			System.err.println("Images have been lost or not properly stored");
		}
		
		
		/*Produce the black&white checkered board, every square set to know it's
		 *identity*/	
		for(int y = 7; y >= 0; y--)
		{
			for(int x = 0; x < 8; x++)
			{
				square[y][x] = new Square();	
				
				if((x+y)%2==1)
				{
					square[y][x].setBackground(Color.WHITE);
					square[y][x].setForeground(Color.BLACK);
				}
				else
				{
					square[y][x].setBackground(Color.BLACK);
					square[y][x].setForeground(Color.WHITE);
				}

				
				square[y][x].setActionCommand((char) (x + 'A') + "" + (y+1));
				square[y][x].addActionListener(new BoardActionListener());
				add(square[y][x]);	
			}
		}
	}
	
	/*Put all the pieces on the board where they are in the board given*/
	public void setBoardSetup(Board board)
	{
		for(int y = 7; y >= 0; y--)
		{
			for(int x = 0; x < 8; x++)
			{
				switch(board.theBoard[y][x])
				{
					case -6: //Black king
						square[y][x].setIcon(bKing);
						break;
					case -5: //Black Queen
						square[y][x].setIcon(bQueen);
						break;
					case -4: //Black Bishop
						square[y][x].setIcon(bBishop);
						break;
					case -3: //Black Knight
						square[y][x].setIcon(bKnight);
						break;
					case -2: //Black Rook
						square[y][x].setIcon(bRook);
						break;
					case -1: //Black Pawn
						square[y][x].setIcon(bPawn);
						break;
					case 0: //Empty square
						square[y][x].setIcon(null);
						break;
					case 1: //White Pawn
						square[y][x].setIcon(wPawn);
						break;
					case 2: //White Rook
						square[y][x].setIcon(wRook);
						break;
					case 3: //White Knight
						square[y][x].setIcon(wKnight);
						break;
					case 4: //WhiteBishop
						square[y][x].setIcon(wBishop);
						break;
					case 5: //WhiteQueen
						square[y][x].setIcon(wQueen);
						break;
					case 6:	//White King
						square[y][x].setIcon(wKing);
						break;
					default:
						square[y][x].setText("" + board.theBoard[y][x]);
						break;
				}
			}
		}
	}
	
	/*Forward highlighting to the appropriate square*/
	public synchronized void highlight(Point hsqr, Color c)
	{
		if (c == Color.BLUE)
		{
			square[hsqr.y][hsqr.x].setHighlight(hBlue);
		}
		else if (c == Color.GREEN)
		{
			square[hsqr.y][hsqr.x].setHighlight(hGreen);
		}
		else if (c == Color.RED)
		{
			square[hsqr.y][hsqr.x].setHighlight(hRed);
		}
		else
		{
			square[hsqr.y][hsqr.x].setHighlight(null);
		}
		
		square[hsqr.y][hsqr.x].repaint();		
	}
	
	/*Paint the background image*/
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(boardBG != null)
		{
			g.drawImage(boardBG,0,0,this);
		}
	}
}
