import java.util.Vector;
import java.awt.Point;

/*Board class: responsible for storing a representation of a chess board state
 *
 *theBoard -	An array giving a 1 to 1 representation of the squares in a chess
 *			 	board.
 *blackPieces -	A vector of the location of all black pieces
 *whitePieces - A vector of the location of all white pieces
 *theKings -	An array of the location of the kings
 *castleCheck -	boolean values to determine if castling is still possible
 */
public class Board
{
	public int[][] theBoard = new int[8][8];
	public Vector<Point> blackPieces = new Vector<Point>();
	public Vector<Point> whitePieces = new Vector<Point>();
	
	// 0 is white
	// 1 is black
	public Point[] theKings = new Point[2];
	public boolean[] castleCheck = new boolean[2];
	
	/*A method to perform a deep copy of the current board state*/
	public Board clone()
	{
		Board clone = new Board();
		int i, j;
		
		//Clones the board array
	    for(i = 0; i < 8; i++)
	    {
	    	for(j = 0; j < 8; j++)
	    	{
	    		((Board) clone).theBoard[i][j] = this.theBoard[i][j];
	    	}
	    }
	    
	    //Clones the black piece vector
	    for(i = 0; i < this.blackPieces.size(); i++)
	    {
	    	clone.blackPieces.add(this.blackPieces.elementAt(i));
	    }
	    
	    //Clones the white piece vector
	    for(i = 0; i < this.whitePieces.size(); i++)
	    {
	    	clone.whitePieces.add(this.whitePieces.elementAt(i));
	    }
	    
	    // Clones the Points which indicate where the king is.
	    clone.theKings[0] = this.theKings[0];
	    clone.theKings[1] = this.theKings[1];
	    
	    // Clones the castle check condition. True if rook and king havn't moved
	    clone.castleCheck[0] = this.castleCheck[0];
	    clone.castleCheck[1] = this.castleCheck[1];
	    
	    return clone;
	}
}