import java.util.Vector;
import java.awt.Point;

 /** IChess version 1.0
  **
  ** Copyright 2006 Stephen Baker (2913895) and Chris Roy (3048899)
  **/
  
public class Board
{
	public int[][] theBoard = new int[8][8];
	public Vector<Point> blackPieces = new Vector<Point>();
	public Vector<Point> whitePieces = new Vector<Point>();
	
	// array to store the location of the kings
	// 0 is white
	// 1 is black
	public Point[] theKings = new Point[2];
	//public boolean[] castleCheck = new boolean[2];
	
	// 0 for white
	// 1 for black
	// [][0] for first rook
	// [][1] for king
	// [][2] for second rook
	public boolean[][] castleCheck = new boolean[2][3];	
	
	// Last Piece Moved: Stores the last point moved for each side
	// 0 is white
	// 1 is black
	public Point[] lastMoved = new Point[2];
	
	// records the last y distance. Needed only for en passant
	public int[] lastYDistance = new int[2];
	
	public int movesUntilDraw;
	
	public Board clone()
	{
		Board clone = new Board();
		int i, j;
		
	    for(i = 0; i < 8; i++)
	    {
	    	for(j = 0; j < 8; j++)
	    	{
	    		((Board) clone).theBoard[i][j] = this.theBoard[i][j];
	    	}
	    }
	    
	    for(i = 0; i < this.blackPieces.size(); i++)
	    {
	    	clone.blackPieces.add(this.blackPieces.elementAt(i));
	    }
	    
	    for(i = 0; i < this.whitePieces.size(); i++)
	    {
	    	clone.whitePieces.add(this.whitePieces.elementAt(i));
	    }
	    
	    // Clones the Points which indicate where the king is.
	    clone.theKings[0] = this.theKings[0];
	    clone.theKings[1] = this.theKings[1];
	    
		for(i=0 ; i<castleCheck.length ; i++)
		{
			for(j=0 ; j<castleCheck[i].length ; j++)
			{
				clone.castleCheck[i][j] = this.castleCheck[i][j];
			}
		}
		
		// Last Piece Moved
		clone.lastMoved[0] = this.lastMoved[0];
		clone.lastMoved[1] = this.lastMoved[1];
		
		clone.lastYDistance[0] = this.lastYDistance[0];
		clone.lastYDistance[1] = this.lastYDistance[1];
		
		clone.movesUntilDraw = this.movesUntilDraw;
		
	    return clone;
	}
}