import java.awt.*;
import javax.swing.*;

/** IChess, an chess game by Chris Roy and Stephen Baker, featuring an
 ** alpha-beta search for computer AI.  The computer is able to play for
 ** white, black, both, or neither side.  At the end of the game move lists can
 ** be saved.  At start the default board is the standard chess board, but any
 ** chess board configuration may be loaded from the menu if the user has
 ** produced the appropriate board representation file.
 **
 ** Following the standard rules of chess: a stalemate is considered a draw,
 ** castling is allowed on both the king and queen side, only if there are no
 ** blocking pieces, if the king is not in check, and if none of the squares the
 ** king would have to travel through would put it in check.  All pieces move as
 ** expected.  Moving into check and not moving out of check when a move out is
 ** available is illegal.  
 **
 ** Copyright 2006 Stephen Baker (2913895) and Chris Roy (3048899)
 */    

public class IChess
{
	public static UI gui;
	public static int turn = 0; //Even = white, Odd = black
	public static boolean whiteIsComputer = false;
	public static boolean blackIsComputer = false;
	public static int ply = 4; //Number of moves ahead to look.
	
	public ComputerAI ai;
		
	public IChess() 
	{
		 gui = new UI(); //sets up the gui and initializes the chess board
		 
		 //starts the computers ai.
		 ai = new ComputerAI();
		 ai.start();
		 	 
	}
	
	/*The main method*/
	public static void main(String args[])
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable() 
		{
            public void run() 
            {
                new IChess();
            }
        });

	}
} 