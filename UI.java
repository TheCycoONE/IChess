import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;


public class UI extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 90L;
	
	private EndGameDialog endGame;
	private ChessMenuBar menu;
	private Container contentPane;
	private BoardPanel bp;
	private Board board;
	private BoardAction action;
	private TurnPanel tp;
	private Point origin;
	private Point destination;
	private Vector<Point> moveList;
	private Timer timer; 
	

	
	static final Color TRANSPARENT = new Color(255,255,255,0);
	
	public UI()
	{
		setTitle("iChess");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		contentPane = getContentPane();
		
		menu = new ChessMenuBar();
		setJMenuBar(menu);
		
	 	tp = new TurnPanel();
	 	tp.setMinimumSize(new Dimension(259,701));
	 	tp.setPreferredSize(new Dimension(259,701));
	 	
	 	moveList = new Vector<Point>();
	 	action = new BoardAction();
		
		bp = new BoardPanel(); 
		board = action.createInitialBoard();
		bp.setBoardSetup(board);
		
		contentPane.add(bp, BorderLayout.CENTER);
		contentPane.add(tp, BorderLayout.EAST);
		 
		pack();
		setVisible(true);
		
		origin = new Point(0,0);

		timer = new Timer(0,this);
		timer.setRepeats(false);
		timer.start();
	}
	
	/*0 = origin slected
	 *1 = move made*/
	public synchronized int chosenSquare(Point cSquare)
	{
		PawnPromoteDialog pawnPromote;
		int i;
		Vector<Board> move;
		
		//Prevent human moves during the computers turn
		if(	IChess.turn % 2 == 0 && IChess.whiteIsComputer ||
			IChess.turn % 2 == 1 && IChess.blackIsComputer)
		{
			return -1;
		}

		destination = null;
		
		// clears the highlight from the previous origin
		if(origin!=null)
		{	
			if((origin.x + origin.y)%2 == 1){bp.highlight(origin,Color.WHITE);}
			else{							 bp.highlight(origin,Color.BLACK);}
		}
		clearFeasibleDisplay();
		
		if(!moveList.isEmpty())
		{
			for(i = 0; i < moveList.size(); i++)
			{
				System.out.println("Should see if it can move from place to place");
				if(cSquare.equals(moveList.elementAt(i)))
				{
					destination = cSquare;
					System.out.println("Should move from place to place");
					//May be moved if a more appropariate place is found
					move = action.move(board, origin, destination);
				
					if(move.size() == 1)
					{
						board = move.elementAt(0);
					}
					else
					{
						pawnPromote = new PawnPromoteDialog(this);
						pawnPromote.setVisible(true);
						if(pawnPromote.getOption() > 0)
						{
							board = move.elementAt(pawnPromote.getOption());
						}
						else
						{
							System.err.println("Invalid selection");
							board = move.elementAt(0);
						}
					}
				
					afterMoveProcessing();
					
					return 1;
				}
			}
		}
		//Make sure that the player is trying to move their own piece
		if(	IChess.turn % 2 == 0 && board.theBoard[cSquare.y][cSquare.x] >= 0 ||
			IChess.turn % 2 == 1 && board.theBoard[cSquare.y][cSquare.x] <= 0)
		{
			origin = cSquare;
			
			if(board.theBoard[cSquare.y][cSquare.x] != 0)
			{
				bp.highlight(origin, Color.BLUE);
			}
			
		}
		
		
		//I Haven't decided yet exactly where I want this next section, but here
		//is good for now.
		moveList = action.getFeasibleMoves(board, origin);
		updateFeasibleDisplay();
		return 0;
	}
	
	public synchronized void setComputerMove(Board board, Point o, Point d)
	{
		this.board = board;
		this.origin = o;
		this.destination = d;
		
		
		afterMoveProcessing();
	}
	
	
	public synchronized Board getBoard()
	{
		return board;
	}
	
	private synchronized void afterMoveProcessing()
	{
		bp.setBoardSetup(board);
		
		moveList = new Vector<Point>();
		IChess.turn++;
		tp.updateTurn(origin, destination);
		
		//A hack if ever I made one
		timer.start();
	}
	
	//TODO: Display Check/Checkmate and handle Checkmate
	private synchronized void preMoveProcessing()
	{
		if(action.isBoardInCheck(board, IChess.turn % 2))
		{
			tp.playerInCheck(IChess.turn % 2);
			
			System.out.println("Board is in Check");
			if(action.isMate(board, IChess.turn % 2))
			{
				endGame("CHECKMATE", (IChess.turn + 1) % 2);
			}
		}
		else
		{
			tp.playerInCheck(-1); // (-1) = No one
			
			if(action.isMate(board, IChess.turn % 2))
			{
				endGame("STALEMATE", 2); //tie
			}
		}
	}
	
	//end of game
	//status: CHECKMATE / STALEMATE / RESIGN
	//winner: 0 - white, 1 - black
	private void endGame(String status, int winner)
	{
		boolean flag;
		flag = true;
		IChess.whiteIsComputer = false;
		IChess.blackIsComputer = false;
		
		System.out.println("END OF GAME!");
		System.out.println("Status: " + status);
		System.out.println("winner: " + winner);
		
		while(flag)
		{
			endGame = new EndGameDialog(this, status, winner);
			endGame.setVisible(true);
			
			switch(endGame.getOption())
			{
				case EndGameDialog.QUIT:
					System.exit(0);
					break;
				
				case EndGameDialog.PLAY_AGAIN:
					flag = false;
					board = action.createInitialBoard();
					bp.setBoardSetup(board);
					IChess.turn = 0;
					tp.reset();
					break;
					
				case EndGameDialog.SAVE:
					tp.saveTurns(this);
					break;
					
				default:
					System.out.println("Unknown");
					break;
			}
		}
	}
	
	//Highlights the board spots that have been selected
	private void updateFeasibleDisplay()
	{
		int i;
		int curPiece = board.theBoard[origin.y][origin.x];
		if(!moveList.isEmpty())
		{
			for(i = 0; i < moveList.size(); i++)
			{
				Point p = moveList.elementAt(i);
				
				if((board.theBoard[p.y][p.x] * curPiece) >=0)
				{
					bp.highlight(p,Color.GREEN);
				}
				else
				{
					bp.highlight(p,Color.RED);
				}
			}
		}
	}
	
	// Should undo highlight, temp solution
	private void clearFeasibleDisplay()
	{
		int i, x1, y1;
		if(!moveList.isEmpty())
		{
			for(i=0 ; i<moveList.size(); i++)
			{
				x1 = moveList.elementAt(i).x;
				y1 = moveList.elementAt(i).y;
				if((x1 + y1) % 2 == 0)
				{
					bp.highlight(moveList.elementAt(i),Color.BLACK);
				}
				else
				{
					bp.highlight(moveList.elementAt(i),Color.WHITE);
				}
			}
		}
	}
	
	public void setFeasibleMoves(Vector<Point> moveList)
	{
		this.moveList = moveList;
	}
	
	public synchronized void setBoard(Board board)
	{
		this.board = board;
		bp.setBoardSetup(board);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		preMoveProcessing();
	}
}
