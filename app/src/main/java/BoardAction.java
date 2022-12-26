import java.awt.Point;
import java.util.Vector;

/** TODO: Castling
 **
 ** Need flags to see if rook and king have moved.
 **		Perhaps 1 flag for white and black.
 **			wCastle, bCastle: boolean values
 **
 **	If xCastle is true check to see if the king is in check.
 **
 **	If castle can take place then make sure the two sqaures are feasible moves
 **	Could reuse the feasible move list or a check checker
 **/
 
public class BoardAction
{
	public Vector<Board> move(Board board, Point origin, Point destination)
	//public Board move(Board board, Point origin, Point destination)
	{
		Vector<Board> boardVector = new Vector<Board>();
		
//		Boolean hasPromoted = false;
		
		Board newBoard;
		Point p;
		int i;
		int player = 0;
		int movingPiece = board.theBoard[origin.y][origin.x];
		
		newBoard = board.clone(); 	
		
		newBoard.theBoard[origin.y][origin.x] = 0;
		
		//Kill any black piece here!
		if(board.theBoard[destination.y][destination.x] < 0)
		{
			newBoard.blackPieces.remove(destination);
		}
		else if(board.theBoard[destination.y][destination.x] > 0)
		{
			newBoard.whitePieces.remove(destination);
		}
		
		if(movingPiece > 0)
		{
			player = 0;
			newBoard.whitePieces.remove(origin);
			newBoard.whitePieces.add(destination);
		}
		else if(movingPiece < 0)
		{
			player = 1;
			newBoard.blackPieces.remove(origin);
			newBoard.blackPieces.add(destination);
		}
		
		// sets castle check to false;
		if(Math.abs(movingPiece) == 6|| Math.abs(movingPiece)==2)
		{
			newBoard.castleCheck[player] = false;
		}
		
		// If the pieces are kings update their positions in the piece list
		/*if(movingPiece == 6){			newBboard.theKings[1] = destination;}
		*/
		
		if(Math.abs(movingPiece) == 6)
		{
			
			newBoard.theKings[player] = destination; 
			
			if(destination.x - origin.x == 2)
			{
				System.out.println("Castle!");
				Point oldRook = new Point(7, destination.y);
				Point newRook = new Point(5, destination.y);
				
				newBoard.theBoard[destination.y][7] = 0;
							
				if(movingPiece > 0)
				{
					newBoard.whitePieces.remove(oldRook);
					newBoard.whitePieces.add(newRook);
					newBoard.theBoard[destination.y][5] = 2;
				}
				else
				{
					newBoard.blackPieces.remove(oldRook);
					newBoard.blackPieces.add(newRook);
					newBoard.theBoard[destination.y][5] = -2;	
				}
				
			}
		}
		
		// Since pawns can only move forward I only need to check for 0 or 7
		// otherwise that would imply the pawn has moved backwards which isn't
		// possible.
		
		if(Math.abs(movingPiece)== 1 && (destination.y==0 || destination.y==7))
		{
			// temp solution until the vector method is adopted
			movingPiece = 5*movingPiece;
			// only need because i put the regular piece adder in the else below
			newBoard.theBoard[destination.y][destination.x] = movingPiece;
			
//			System.out.println("Promotion");
			
//			hasPromoted = true;
			
			Board rookBoard = newBoard.clone();
			Board knightBoard = newBoard.clone();
			Board bishopBoard = newBoard.clone();
			Board queenBoard = newBoard.clone();
			
			if(player == 0)
			{
				rookBoard.theBoard[destination.y][destination.x] = 2;
				knightBoard.theBoard[destination.y][destination.x] = 3;
				bishopBoard.theBoard[destination.y][destination.x] = 4;
				queenBoard.theBoard[destination.y][destination.x] = 5;
			}
			else
			{
				rookBoard.theBoard[destination.y][destination.x] = -2;
				knightBoard.theBoard[destination.y][destination.x] = -3;
				bishopBoard.theBoard[destination.y][destination.x] = -4;
				queenBoard.theBoard[destination.y][destination.x] = -5;
			}
			
			boardVector.add(rookBoard);
			boardVector.add(knightBoard);
			boardVector.add(bishopBoard);
			boardVector.add(queenBoard);
		}
		else
		{
			newBoard.theBoard[destination.y][destination.x] = movingPiece;
			boardVector.add(newBoard);
		}
		
		return boardVector;
		//return newBoard;
	}
	
	//Possibly not necessary...
	public boolean isFeasibleMove(Board theBoard, Point origin, Point destination)
	{
		return false;	
	}
	
	/** Returns a vector of feasible moves
	 **
	 **	Pieces are the following:
	 ** 	1 Pawn
	 ** 	2 Rook
	 ** 	3 Knight
	 ** 	4 Bishop
	 ** 	5 Queen
	 ** 	6 King
	 **
	 ** Positive values are white
	 ** Negative values are black
	 **
	 **	A Capture is determined if the current piece value * the square it is
	 ** going to is <= 0. This is because if they are the same sign the value
	 ** will be positive. So if the value isn't positive then we know the square
	 ** can be occupied. 
	 **
	 ** After which a check is usually done to see if the last square was empty.
	 ** If it wasn't than the loop exists as we know we've hit another players
	 ** piece and therefore should not continue searching in that direction.
	 */
	
	public Vector<Point> getFeasibleMoves(Board board, Point piece)
	{
		int theCurPiece = board.theBoard[piece.y][piece.x];
		Vector<Point> fMoves = new Vector<Point>();
		int turn;
		
		if(theCurPiece > 0) //White
		{
			turn = 0;
		}
		else //Black
		{
			turn = 1;
		}
		
				
		//White Pawn
		if(theCurPiece == 1)
		{
			if(piece.y+1 < 8 && board.theBoard[piece.y+1][piece.x] == 0)
			{
				Point tPoint = new Point(piece.x, piece.y+1);
				if(!checkChecker(board, piece, tPoint))
				{
					fMoves.add(tPoint);
				}
			}
			
			if(piece.y+1 < 8 && piece.x+1 <8 && board.theBoard[piece.y+1][piece.x+1] < 0)
			{
				Point tPoint = new Point(piece.x+1, piece.y+1);
				if(!checkChecker(board, piece, tPoint))
				{
					fMoves.add(tPoint);
				}
			}
			
			if(piece.y+1 < 8 && piece.x-1>=0 &&board.theBoard[piece.y+1][piece.x-1] < 0)
			{
				Point tPoint = new Point(piece.x-1, piece.y+1);
				if(!checkChecker(board, piece, tPoint))
				{
					fMoves.add(tPoint);
				}
			}
			
			if(piece.y == 1 && 
				board.theBoard[piece.y+1][piece.x] == 0 &&
				board.theBoard[piece.y+2][piece.x] == 0)
			{
				Point tPoint = new Point(piece.x, piece.y+2);
				if(!checkChecker(board, piece, tPoint))
				{
					fMoves.add(tPoint);
				}
			}	
		}
		else if(theCurPiece == -1)
		{
			if(piece.y-1>=0 && board.theBoard[piece.y-1][piece.x] == 0)
			{
				Point tPoint = new Point(piece.x, piece.y-1);
				if(!checkChecker(board, piece, tPoint))
				{
					fMoves.add(tPoint);
				}
			}
			
			if(piece.y-1 >=0 && piece.x+1 <8 && board.theBoard[piece.y-1][piece.x+1] > 0)
			{
				Point tPoint = new Point(piece.x+1, piece.y-1);
				if(!checkChecker(board, piece, tPoint))
				{
					fMoves.add(tPoint);
				}
			}
			
			if(piece.y-1 >=0 && piece.x-1>=0 &&board.theBoard[piece.y-1][piece.x-1] > 0)
			{
				Point tPoint = new Point(piece.x-1, piece.y-1);
				if(!checkChecker(board, piece, tPoint))
				{
					fMoves.add(tPoint);
				}
			}
			
			if(piece.y == 6 && 
				board.theBoard[piece.y-1][piece.x] == 0 &&
				board.theBoard[piece.y-2][piece.x] == 0)
			{
				Point tPoint = new Point(piece.x, piece.y-2);
				if(!checkChecker(board, piece, tPoint))
				{
					fMoves.add(tPoint);
				}
			}	
		}
		// Rook
		else if(Math.abs(theCurPiece) == 2)
		{
			// Forward and Backwards
			fMoves = combineVectors(fMoves, checkVertMoves(board,piece));
			
			// Right and Left
			fMoves = combineVectors(fMoves, checkHoriMoves(board,piece));
			
		}
		// Knight
		else if(Math.abs(theCurPiece) == 3)
		{	// Forward 2 and left / right 1
			if(piece.y+2 <8)
			{
				if(piece.x+1 <8)
				{
					if(theCurPiece * board.theBoard[piece.y+2][piece.x+1]<=0)
					{
						Point tPoint = new Point(piece.x+1, piece.y+2);
						if(!checkChecker(board,piece,tPoint))
						{
							fMoves.add(tPoint);
						}						
					}
				}
				
				if(piece.x-1>=0)
				{
					if(theCurPiece * board.theBoard[piece.y+2][piece.x-1]<=0)
					{
						Point tPoint = new Point(piece.x-1, piece.y+2);
						if(!checkChecker(board,piece,tPoint))
						{
							fMoves.add(tPoint);
						}
					}
				}
			}
			
			// Backward 2 and left / right 1
			if(piece.y-2 >=0)
			{
				if(piece.x+1 <8)
				{
					if(theCurPiece * board.theBoard[piece.y-2][piece.x+1]<=0)
					{
						Point tPoint = new Point(piece.x+1, piece.y-2);
						if(!checkChecker(board,piece,tPoint))
						{
							fMoves.add(tPoint);
						}
					}
				}
				
				if(piece.x-1>=0)
				{
					if(theCurPiece * board.theBoard[piece.y-2][piece.x-1]<=0)
					{
						Point tPoint = new Point(piece.x-1, piece.y-2);
						if(!checkChecker(board,piece,tPoint))
						{
							fMoves.add(tPoint);
						}
					}
				}
			}
			
			// Forward 1 and left / right 2
			if(piece.y+1 <8)
			{
				if(piece.x+2 <8)
				{
					if(theCurPiece * board.theBoard[piece.y+1][piece.x+2]<=0)
					{
						Point tPoint = new Point(piece.x+2, piece.y+1);
						if(!checkChecker(board,piece,tPoint))
						{
							fMoves.add(tPoint);
						}
					}
				}
				
				if(piece.x-2>=0)
				{
					if(theCurPiece * board.theBoard[piece.y+1][piece.x-2]<=0)
					{
						Point tPoint = new Point(piece.x-2, piece.y+1);
						if(!checkChecker(board,piece,tPoint))
						{
							fMoves.add(tPoint);
						}
					}
				}
			}
			
			// Backward 1 and left / right 2
			if(piece.y-1 >=0)
			{
				if(piece.x+2 <8)
				{
					if(theCurPiece * board.theBoard[piece.y-1][piece.x+2]<=0)
					{
						Point tPoint = new Point(piece.x+2, piece.y-1);
						if(!checkChecker(board,piece,tPoint))
						{
							fMoves.add(tPoint);
						}
					}
				}
				
				if(piece.x-2>=0)
				{
					if(theCurPiece * board.theBoard[piece.y-1][piece.x-2]<=0)
					{
						Point tPoint = new Point(piece.x-2, piece.y-1);
						if(!checkChecker(board,piece,tPoint))
						{
							fMoves.add(tPoint);
						}
					}
				}
			}
			
			
		}
		// Bishop
		else if(Math.abs(theCurPiece) == 4)
		{
			// Right Up
			int y1 = piece.y + 1;
			int x1 = piece.x + 1;
						
			while(y1<8 && x1<8)
			{
				if(theCurPiece * board.theBoard[y1][x1] <=0)
				{
					Point tPoint = new Point(x1, y1);
					if(!checkChecker(board, piece, tPoint))
					{
						fMoves.add(tPoint);
					}
				}
				
				if(board.theBoard[y1][x1] !=0){break;}
				
				x1++;
				y1++;
			}
			
			// Left Up
			y1 = piece.y + 1;
			x1 = piece.x - 1;
			
			while(y1<8 && x1>=0)
			{
				if(theCurPiece * board.theBoard[y1][x1] <=0)
				{
				Point tPoint = new Point(x1, y1);
					if(!checkChecker(board, piece, tPoint))
					{
						fMoves.add(tPoint);
					}
				}
				
				if(board.theBoard[y1][x1] !=0){break;}
				
				x1--;
				y1++;
			}
			
			// Right Down
			y1 = piece.y - 1;
			x1 = piece.x + 1;
			
			while(y1>=0 && x1<8)
			{
				if(theCurPiece * board.theBoard[y1][x1] <=0)
				{
					Point tPoint = new Point(x1, y1);
					if(!checkChecker(board, piece, tPoint))
					{
						fMoves.add(tPoint);
					}
				}
				
				if(board.theBoard[y1][x1] !=0){break;}
				
				x1++;
				y1--;
			}
			
			// Left Down
			y1 = piece.y - 1;
			x1 = piece.x - 1;
			
			while(y1>=0 && x1>=0)
			{
				if(theCurPiece * board.theBoard[y1][x1] <=0)
				{
					Point tPoint = new Point(x1, y1);
					if(!checkChecker(board, piece, tPoint))
					{
						fMoves.add(tPoint);
					}
				}
				
				if(board.theBoard[y1][x1] !=0){break;}
				
				x1--;
				y1--;
			}
			
		}
		// The Queen!
		else if(Math.abs(theCurPiece)==5)
		{
			// From the Bishop-------------------------------------------------
			
			// Right Up
			int y1 = piece.y + 1;
			int x1 = piece.x + 1;
						
			while(y1<8 && x1<8)
			{
				if(theCurPiece * board.theBoard[y1][x1] <=0)
				{
					Point tPoint = new Point(x1, y1);
					if(!checkChecker(board, piece, tPoint))
					{
						fMoves.add(tPoint);
					}
				}
				
				if(board.theBoard[y1][x1] !=0){break;}
				
				x1++;
				y1++;
			}
			
			// Left Up
			y1 = piece.y + 1;
			x1 = piece.x - 1;
			
			while(y1<8 && x1>=0)
			{
				if(theCurPiece * board.theBoard[y1][x1] <=0)
				{
					Point tPoint = new Point(x1, y1);
					if(!checkChecker(board, piece, tPoint))
					{
						fMoves.add(tPoint);
					}
				}
				
				if(board.theBoard[y1][x1] !=0){break;}
				
				x1--;
				y1++;
			}
			
			// Right Down
			y1 = piece.y - 1;
			x1 = piece.x + 1;
			
			while(y1>=0 && x1<8)
			{
				if(theCurPiece * board.theBoard[y1][x1] <=0)
				{
					Point tPoint = new Point(x1, y1);
					if(!checkChecker(board, piece, tPoint))
					{
						fMoves.add(tPoint);
					}
				}
				
				if(board.theBoard[y1][x1] !=0){break;}
				
				x1++;
				y1--;
			}
			
			// Left Down
			y1 = piece.y - 1;
			x1 = piece.x - 1;
			
			while(y1>=0 && x1>=0)
			{
				if(theCurPiece * board.theBoard[y1][x1] <=0)
				{
					Point tPoint = new Point(x1, y1);
					if(!checkChecker(board, piece, tPoint))
					{
						fMoves.add(tPoint);
					}
				}
				
				if(board.theBoard[y1][x1] !=0){break;}
				
				x1--;
				y1--;
			}
			
			// From the Rook---------------------------------------------------
			
			// Forward and Backward
			fMoves = combineVectors(fMoves, checkVertMoves(board,piece));
			
			// Right and Left
			fMoves = combineVectors(fMoves, checkHoriMoves(board,piece));

			
		}
		// The King!
		else if(Math.abs(theCurPiece) == 6)
		{	
			int y1 = piece.y;
			int x1 = piece.x;
			
			if(canCastle(board, turn))
			{
				fMoves.add(new Point(x1+2,y1));
			}
			
			// Forward
			if(y1+1<8 && (theCurPiece*board.theBoard[y1+1][x1]<=0))
			{
				Point tPoint = new Point(x1, y1+1);
				if(!checkChecker(board, piece, tPoint)){fMoves.add(tPoint);	}
			}
			
			// Backward
			if(y1-1>=0 && (theCurPiece*board.theBoard[y1-1][x1]<=0))
			{
				Point tPoint = new Point(x1, y1-1);
				if(!checkChecker(board, piece, tPoint)){fMoves.add(tPoint);	}
			}
			
			// Left
			if(x1-1>=0 && (theCurPiece*board.theBoard[y1][x1-1]<=0))
			{
				Point tPoint = new Point(x1-1, y1);
				if(!checkChecker(board, piece, tPoint)){fMoves.add(tPoint);	}
			}
			
			// Right
			if(x1+1<8 && (theCurPiece*board.theBoard[y1][x1+1]<=0))
			{
				Point tPoint = new Point(x1+1, y1);
				if(!checkChecker(board, piece, tPoint)){fMoves.add(tPoint);	}
			}
			
			// Forward Left
			if(y1+1<8 && x1-1>=0 && (theCurPiece*board.theBoard[y1+1][x1-1]<=0))
			{
				Point tPoint = new Point(x1-1, y1+1);
				if(!checkChecker(board, piece, tPoint)){fMoves.add(tPoint);	}
			}
			
			// Forward Right
			if(y1+1<8 && x1+1<8 && (theCurPiece*board.theBoard[y1+1][x1+1]<=0))
			{
				Point tPoint = new Point(x1+1, y1+1);
				if(!checkChecker(board, piece, tPoint)){fMoves.add(tPoint);	}
			}
			
			// Backward Left
			if(y1-1>=0 && x1-1>=0 &&(theCurPiece*board.theBoard[y1-1][x1-1]<=0))
			{
				Point tPoint = new Point(x1-1, y1-1);
				if(!checkChecker(board, piece, tPoint)){fMoves.add(tPoint);	}
			}
			
			// Backward Right
			if(y1-1>=0 && x1+1<8 && (theCurPiece*board.theBoard[y1-1][x1+1]<=0))
			{
				Point tPoint = new Point(x1+1, y1-1);
				if(!checkChecker(board, piece, tPoint)){fMoves.add(tPoint);	}
				
			}
		}
		return fMoves;	
	}
	
	// Checks the moves upwards from the current piece
	private Vector<Point> checkVertMoves(Board board, Point piece)
	{
		Vector<Point> retVector = new Vector<Point>();
		int theCurPiece = board.theBoard[piece.y][piece.x];
		
		for(int i = piece.y+1 ; i<8 ; i++)
		{
			if(theCurPiece * board.theBoard[i][piece.x]<=0)
			{
				if(!checkChecker(board, piece, new Point(piece.x, i)))
				{
					retVector.add(new Point(piece.x, i));
				}
			}			
			if(board.theBoard[i][piece.x] !=0){break;}
		}
		
		// Backward
		for(int i = piece.y-1 ; i>=0 ; i--)
		{
			if(theCurPiece * board.theBoard[i][piece.x]<=0)
			{
				if(!checkChecker(board, piece, new Point(piece.x, i)))
				{
					retVector.add(new Point(piece.x, i));
				}
			}			
			if(board.theBoard[i][piece.x] !=0){	break;}
		}		
		return retVector;
	}
	
	// Checks the moves left and right from the current piece
	private Vector<Point> checkHoriMoves(Board board, Point piece)
	{
		Vector<Point> retVector = new Vector<Point>();
		int theCurPiece = board.theBoard[piece.y][piece.x];
		
		// Right
		for(int i = piece.x+1 ; i<8 ; i++)
		{
			if(theCurPiece * board.theBoard[piece.y][i]<=0)
			{
				if(!checkChecker(board, piece, new Point(i,piece.y)))
				{
					retVector.add(new Point(i, piece.y));
				}
			}			
			if(board.theBoard[piece.y][i] !=0){	break;}
		}
		
		// Left
		for(int i = piece.x-1 ; i>=0 ; i--)
		{
			if(theCurPiece * board.theBoard[piece.y][i]<=0)
			{
				if(!checkChecker(board, piece, new Point(i,piece.y)))
				{
					retVector.add(new Point(i, piece.y));
				}
			}			
			if(board.theBoard[piece.y][i] !=0){	break;}
		}
		return retVector;
	}
		
	// Adds vector 2 to vector 1 and returns 
	private Vector<Point> combineVectors(Vector<Point> input1, Vector<Point> input2)
	{
		for(int i=0 ; i<input2.size() ; i++)
		{
			input1.add(input2.elementAt(i));
		}			
		return input1;		
	}
	
	
	// This will evalute a board for check based on 
	private boolean checkChecker(Board board, Point start, Point end)
	{
		boolean theResult = false;
		Point theKing;
		
		int theCurPiece = board.theBoard[start.y][start.x];
//		System.out.println("The Current piece is: "+theCurPiece );
		
		if(theCurPiece<0){	theKing = board.theKings[1];}
		else{				theKing = board.theKings[0];}
		
		// If the piece you selected happens to be a king change to reflect pos 
		if(Math.abs(theCurPiece)==6){theKing = end;}
		
		// Vertical-------------------------------------------------------------
		
		// Forward
		for(int i = theKing.y+1 ; i<8 ; i++)
		{
			int newPiece = board.theBoard[i][theKing.x];
			int sign = theCurPiece * newPiece;
			
			Point tPoint = new Point(theKing.x,i);
			
			// if it runs into where this pieces is going break
			if(tPoint.equals(end))break;
			// if it runs into the space it used to be in continue
			else if(tPoint.equals(start))continue;
			
			if(sign>0)
			{
			//	System.out.println("No Check to the front");
				break;
			}
			// Attacking
			else if(sign<0)
			{	
				newPiece = Math.abs(newPiece);
				if(newPiece == 6 && Math.abs((tPoint.x-theKing.x))==1)
				{
					theResult = true;
				}
				else if(newPiece == 2 || newPiece == 5)
				{
					theResult = true;
				}else{break;}
			}
		}


		// Backward
		for(int i = theKing.y-1 ; i>=0 ; i--)
		{
			int newPiece = board.theBoard[i][theKing.x];
			int sign = theCurPiece * newPiece;
			
			Point tPoint = new Point(theKing.x,i);
			
			// if it runs into where this pieces is going break
			if(tPoint.equals(end))break;
			// if it runs into the space it used to be in continue
			else if(tPoint.equals(start))continue;
			
			if(sign>0)
			{
			//	System.out.println("No Check to the back");
				break;
			}
			else if(sign<0)
			{	
				newPiece = Math.abs(newPiece);
				if(newPiece == 6 && Math.abs((tPoint.x-theKing.x))==1)
				{
					theResult = true;
				}
				else if(newPiece == 2 || newPiece == 5)
				{
					theResult = true;
				}else{break;}
			}
		}
		
		// Horizontal-----------------------------------------------------------
		
		// Left
		for(int i = theKing.x-1 ; i>=0 ; i--)
		{
			int newPiece = board.theBoard[theKing.y][i];
			int sign = theCurPiece * newPiece;
			
			Point tPoint = new Point(i,theKing.y);
			
			// if it runs into where this pieces is going break
			if(tPoint.equals(end))break;
			// if it runs into the space it used to be in continue
			else if(tPoint.equals(start))continue;
			
			if(sign>0)
			{
			//	System.out.println("No Check to the left");
				break;
			}
			else if(sign<0)
			{	
				newPiece = Math.abs(newPiece);
				if(newPiece == 6 && Math.abs((tPoint.x-theKing.x))==1)
				{
					theResult = true;
				}
				else if(newPiece == 2 || newPiece == 5)
				{
					theResult = true;
				}else{break;}
			}
		}
		
		// Right
		for(int i = theKing.x+1 ; i<8 ; i++)
		{
			int newPiece = board.theBoard[theKing.y][i];
			int sign = theCurPiece * newPiece;
			
			Point tPoint = new Point(i,theKing.y);
			
			// if it runs into where this pieces is going break
			if(tPoint.equals(end))break;
			// if it runs into the space it used to be in continue
			else if(tPoint.equals(start))continue;
			
			if(sign>0)
			{
			//	System.out.println("No Check to the right");
				break;
			}
			else if(sign<0)
			{	
				newPiece = Math.abs(newPiece);
				if(newPiece == 6 && Math.abs((tPoint.x-theKing.x))==1)
				{
					theResult = true;
				}
				else if(newPiece == 2 || newPiece == 5)
				{
					theResult = true;
				}else{break;}
			}
		}
		
		// Diagonals------------------------------------------------------------
		
		// Up Right
		
			int y1 = theKing.y + 1;
			int x1 = theKing.x + 1;
						
			while(y1<8 && x1<8)
			{
				Point tPoint = new Point(x1,y1);
				
				// if it runs into where this pieces is going break
				if(tPoint.equals(end))break;
				// if it runs into the space it used to be in continue
				else if(tPoint.equals(start)){ x1++; y1++;continue;}
			
				int newPiece = board.theBoard[y1][x1];
				
				if(theCurPiece * newPiece > 0)
				{
				//	System.out.println("No Check Up and Right");
					break;
				}
				
				if(theCurPiece * newPiece < 0)
				{
					newPiece = Math.abs(newPiece);
					if(newPiece == 4 || newPiece == 5
					 ||newPiece == 6 || newPiece == 1)
					{
						if(newPiece == 6 || newPiece == 1)
						{
							if(Math.abs((x1 - theKing.x)*(y1 - theKing.y)) ==1)
							{
								theResult = true;
							}
							else
							{
								break;
							}
						}
						else
						{
							theResult = true;
						}						
					}
					else{break;}
				}
				
				x1++;
				y1++;
			}
			
			// Down Right
		
			y1 = theKing.y - 1;
			x1 = theKing.x + 1;
						
			while(y1>=0 && x1<8)
			{
				Point tPoint = new Point(x1,y1);
				
				// if it runs into where this pieces is going break
				if(tPoint.equals(end))break;
				// if it runs into the space it used to be in continue
				else if(tPoint.equals(start)){ x1++; y1--;continue;}
			
				int newPiece = board.theBoard[y1][x1];
				
				if(theCurPiece * newPiece > 0)
				{
				//	System.out.println("No Check Down and Right");
					break;
				}
				
				if(theCurPiece * newPiece < 0)
				{
					newPiece = Math.abs(newPiece);
					if(newPiece == 4 || newPiece == 5 
					|| newPiece == 6 || newPiece == 1)
					{
						if(newPiece == 6 || newPiece == 1)
						{
							if(Math.abs((x1 - theKing.x)*(y1 - theKing.y)) ==1)
							{
								theResult = true;
							}
							else
							{
								break;
							}
						}
						else
						{
							theResult = true;
						}						
					}
					else{break;}
				}				
				
				x1++;
				y1--;
			}	
		
			// Down Left
		
			y1 = theKing.y - 1;
			x1 = theKing.x - 1;
						
			while(y1>=0 && x1>=0)
			{
				Point tPoint = new Point(x1,y1);
				
				// if it runs into where this pieces is going break
				if(tPoint.equals(end))break;
				// if it runs into the space it used to be in continue
				else if(tPoint.equals(start)){ x1--; y1--;continue;}
			
				int newPiece = board.theBoard[y1][x1];

				if(theCurPiece * newPiece > 0)
				{
				//	System.out.println("No Check Down and Left");
					break;
				}
								
				if(theCurPiece * newPiece < 0)
				{
					newPiece = Math.abs(newPiece);
					if(newPiece == 4 || newPiece == 5 
					|| newPiece == 6 || newPiece == 1)
					{
						if(newPiece == 6 || newPiece == 1)
						{
							if(Math.abs((x1 - theKing.x)*(y1 - theKing.y)) ==1)
							{
								theResult = true;
							}
							else
							{
								break;
							}
						}
						else
						{
							theResult = true;
						}						
					}
					else{break;}
				}				
				
				x1--;
				y1--;
			}
		
			// Up Left
		
			y1 = theKing.y + 1;
			x1 = theKing.x - 1;
						
			while(y1<8 && x1>=0)
			{
				Point tPoint = new Point(x1,y1);
				
				// if it runs into where this pieces is going break
				if(tPoint.equals(end))break;
				// if it runs into the space it used to be in continue
				else if(tPoint.equals(start)){ x1--; y1++;continue;}
			
				int newPiece = board.theBoard[y1][x1];
				
				if(theCurPiece * newPiece > 0)
				{
					//System.out.println("No Check Up and Left");
					break;
				}
				
				if(theCurPiece * newPiece < 0)
				{
					newPiece = Math.abs(newPiece);
					if(newPiece == 4 || newPiece == 5 
					|| newPiece == 6 || newPiece == 1)
					{
						if(newPiece == 6 || newPiece == 1)
						{
							if(Math.abs((x1 - theKing.x)*(y1 - theKing.y)) ==1)
							{
								theResult = true;
							}
							else
							{
								break;
							}
						}
						else
						{
							theResult = true;
						}						
					}
					else{break;}
				}				
				
				x1--;
				y1++;
			}
		// Knight Moves---------------------------------------------------------
		//
		// These check to see if the piece at each location is a knight only if
		// the end move isn't going to replace that piece.
		 
		// Forward 2 and left / right 1
			if(theKing.y+2 <8)
			{
				if(theKing.x+1 <8)
				{
					int thePiece = board.theBoard[theKing.y+2][theKing.x+1];
					if((!end.equals(new Point(theKing.x+1, theKing.y+2)))
						&&theCurPiece*thePiece<0
						&& Math.abs(thePiece)==3)
					{
						theResult = true;
					}
				}
				
				if(theKing.x-1>=0)
				{
					int thePiece = board.theBoard[theKing.y+2][theKing.x-1];
					if((!end.equals(new Point(theKing.x-1, theKing.y+2)))
						&&theCurPiece*thePiece<0
						&& Math.abs(thePiece)==3)
					{
						theResult = true;
					}
				}
			}
			
			// Backward 2 and left / right 1
			if(theKing.y-2 >=0)
			{
				if(theKing.x+1 <8)
				{
					int thePiece = board.theBoard[theKing.y-2][theKing.x+1];
					if((!end.equals(new Point(theKing.x+1, theKing.y-2)))
						&&theCurPiece*thePiece<0
						&& Math.abs(thePiece)==3)
					{
						theResult = true;
					}
				}
				
				if(theKing.x-1>=0)
				{
					int thePiece = board.theBoard[theKing.y-2][theKing.x-1];
					if((!end.equals(new Point(theKing.x-1, theKing.y-2)))
						&&theCurPiece*thePiece<0
						&& Math.abs(thePiece)==3)
					{
						theResult = true;
					}
				}
			}
			
			// Forward 1 and left / right 2
			if(theKing.y+1 <8)
			{
				if(theKing.x+2 <8)
				{
					int thePiece = board.theBoard[theKing.y+1][theKing.x+2];
					if((!end.equals(new Point(theKing.x+2, theKing.y+1)))
						&&theCurPiece*thePiece<0
						&& Math.abs(thePiece)==3)
					{
						theResult = true;
					}
				}
				
				if(theKing.x-2>=0)
				{
					int thePiece = board.theBoard[theKing.y+1][theKing.x-2];
					if((!end.equals(new Point(theKing.x-2, theKing.y+1)))
						&&theCurPiece*thePiece<0
						&& Math.abs(thePiece)==3)
					{
						theResult = true;
					}
				}
			}
			
			// Backward 1 and left / right 2
			if(theKing.y-1 >=0)
			{
				if(theKing.x+2 <8)
				{
					int thePiece = board.theBoard[theKing.y-1][theKing.x+2];
					if((!end.equals(new Point(theKing.x+2, theKing.y-1)))
						&&theCurPiece*thePiece<0
						&& Math.abs(thePiece)==3)
					{
						theResult = true;
					}
				}
				
				if(theKing.x-2>=0)
				{
					int thePiece = board.theBoard[theKing.y-1][theKing.x-2];
										
					if((!end.equals(new Point(theKing.x-2, theKing.y-1)))
						&& theCurPiece*thePiece<0
						&& Math.abs(thePiece)==3)
					{
						theResult = true;
					}
				}
			}		
//		if(theResult){System.out.println("CHECK!!");}
		
		return theResult;
	}
	
	// Should check if a board state is in check.
	// player: 0 - white
	//         1 - black
	public boolean isBoardInCheck(Board board, int player)
	{
		Point tPoint = board.theKings[player];
		return checkChecker(board, tPoint, tPoint);
	}
	
	
	// Steves checkmate method
	public boolean isMate(Board board, int player)
	{
		int i;
		Vector<Point> feasible;
		boolean isMate = true;
		
		if(player == 0) //White
		{
			for(i = 0; i < board.whitePieces.size(); i++)
			{
				feasible = getFeasibleMoves(board, board.whitePieces.elementAt(i));
				if(!feasible.isEmpty())
				{
					isMate = false;
					break;
				}
			}
		}
		else //Black
		{
			for(i = 0; i < board.blackPieces.size(); i++)
			{
				feasible = getFeasibleMoves(board, board.blackPieces.elementAt(i));
				if(!feasible.isEmpty())
				{
					isMate = false;
					break;
				}
			}
		}
		
		return isMate;
	}
	
	// Check if Rook is a valid move
	public boolean canCastle(Board board, int turn)
	{
		Point theKing = board.theKings[turn];
		boolean retVal = false;
		
		if(board.castleCheck[turn] && !isBoardInCheck(board, turn))
		{
//System.out.println("Can Castle Still");
			if(board.theBoard[theKing.y][theKing.x+1] == 0
			&& board.theBoard[theKing.y][theKing.x+2] == 0)
			{
				Point tPoint1 = new Point(theKing.x+1,theKing.y);
				Point tPoint2 = new Point(theKing.x+2,theKing.y);
				
//System.out.println("Squares are free");
				
				if(!checkChecker(board,theKing,tPoint1))
				{
//System.out.println("First Square is not in check");
					if(!checkChecker(board,theKing,tPoint2))
					{
//System.out.println("Second Square is not in check");
						retVal = true;
					}
				}
			}			
		}
		
		return retVal;
	}
	
	public Board createInitialBoard()
	{
		
		int i;
		Board board = new Board();
		
		//Set up pawns
		for(i = 0; i < 8; i++)
		{
			board.theBoard[1][i] = 1;
			board.whitePieces.add(new Point(i, 1));
			
			board.theBoard[6][i] = -1;
			board.blackPieces.add(new Point(i, 6));
		}
		
		//Left side + king
		for(i = 0; i <= 4; i ++)
		{
			board.theBoard[0][i] = i+2;
			board.whitePieces.add(new Point(i, 0));
			
			board.theBoard[7][i] = -(i+2);
			board.blackPieces.add(new Point(i, 7));
		}
		
		//Right side
		for(i = 5; i < 8; i ++)
		{
			board.theBoard[0][i] = 9-i;
			board.whitePieces.add(new Point(i, 0));
			
			board.theBoard[7][i] = -(9-i);
			board.blackPieces.add(new Point(i, 7));
		}
		
		/*
		board.theBoard[0][4] = 6;
		board.theBoard[7][4] = -6;
		
		board.whitePieces.add(new Point(4,0));
		board.blackPieces.add(new Point(4,7));
		*/
		
		board.theKings[0] = new Point(4,0);
		board.theKings[1] = new Point(4,7);
		
		board.castleCheck[0] = true;
		board.castleCheck[1] = true;
		
		return board;
	}
}