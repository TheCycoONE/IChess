import java.awt.Point;
import java.util.Vector;

 /** IChess version 1.0
  **
  ** Copyright 2006 Stephen Baker (2913895) and Chris Roy (3048899)
  **/
   
public class BoardAction
{
	public Vector<Board> move(Board board, Point origin, Point destination)
	//public Board move(Board board, Point origin, Point destination)
	{
		Vector<Board> boardVector = new Vector<Board>();
		
		Board newBoard;
		Point p;
		int i;
		int player = 0;
		int movingPiece = board.theBoard[origin.y][origin.x];
		
		newBoard = board.clone();
		
		newBoard.theBoard[origin.y][origin.x] = 0;	// clears the origin square
		
		newBoard.movesUntilDraw--;	// Decrease draw counter
		
		// Removed captured black piece
		if(board.theBoard[destination.y][destination.x] < 0)
		{
			newBoard.movesUntilDraw = 100;
			newBoard.blackPieces.remove(destination);
		}
		// Remove captured white piece
		else if(board.theBoard[destination.y][destination.x] > 0) 
		{
			newBoard.movesUntilDraw = 100;
			newBoard.whitePieces.remove(destination);
		}
		
		// If a pawn moves reset the draw counter
		if(Math.abs(movingPiece) == 1)
		{
			newBoard.movesUntilDraw = 100;
		}
		
		// Removes the old piece location and adds the new location
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
		
		// Changes the last moved piece for this player and how far it moved		
		newBoard.lastMoved[player] = destination;
		newBoard.lastYDistance[player] = Math.abs(origin.y - destination.y);

		// Check for en passant for both player colours
		if((movingPiece == 1 && origin.y == 4)			// 5th rank for white
		 ||(movingPiece == -1 && origin.y == 3))		// 5th rank for black
		{
			int oldPlayer = Math.abs(player-1);
			
			Point lMove = board.lastMoved[oldPlayer];
			
			// A Pawn was moved by the opposite player
			if((board.theBoard[lMove.y][lMove.x] == -1 && oldPlayer == 1)
			|| (board.theBoard[lMove.y][lMove.x] == 1 && oldPlayer == 0))
			{
				// Pawn was moved two spaces and both pawns are on the y plane
				if(board.lastYDistance[oldPlayer] == 2 && (lMove.y == origin.y))
				{
					if(Math.abs(lMove.x - origin.x) == 1)// 1 space away
					{
						Point tPoint = new Point();
						
						// Space behind the last moved pawn
						
						if(oldPlayer == 1)		// White is moving
						{
							tPoint = new Point(lMove.x,origin.y+1);
						}
						else if (oldPlayer == 0) // Black is moving
						{
							tPoint = new Point(lMove.x,origin.y-1);
						}
													
						// If the desitnation is that space en passant occured
						if(tPoint.equals(destination))
						{
							newBoard.theBoard[lMove.y][lMove.x] = 0;
							
							if(oldPlayer == 1)
							{
								newBoard.blackPieces.remove(lMove);
							}
							else if(oldPlayer == 0)
							{
								newBoard.whitePieces.remove(lMove);
							}
						}
					}
				}
			}
		}

		if(Math.abs(movingPiece) == 6|| Math.abs(movingPiece)==2)
		{
			if(Math.abs(movingPiece)==6)						// King
			{
				newBoard.castleCheck[player][1] = false;
			}
			else if(Math.abs(movingPiece)==2 && origin.x == 0)	// Left Rook
			{
				newBoard.castleCheck[player][0] = false;
			}
			else if(Math.abs(movingPiece)==2 && origin.x == 7)	// Right Rook
			{
				newBoard.castleCheck[player][2] = false;
			}
		}

		// Sets the king postion and checks for castle
		if(Math.abs(movingPiece) == 6)
		{
			newBoard.theKings[player] = destination; 
			
			// Castle to the right
			if(destination.x - origin.x == 2)
			{
				Point oldRook = new Point(7, destination.y);
				Point newRook = new Point(5, destination.y);
				
				// Removes the rook at the right
				newBoard.theBoard[destination.y][7] = 0;
				
				// Moves rook to the new location			
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
			// Castle to the left
			else if(destination.x - origin.x == -2)
			{
				Point oldRook = new Point(0, destination.y);
				Point newRook = new Point(3, destination.y);
				
				newBoard.theBoard[destination.y][0] = 0;
				
				// Moves rook to the new location		
				if(movingPiece > 0)
				{
					newBoard.whitePieces.remove(oldRook);
					newBoard.whitePieces.add(newRook);
					newBoard.theBoard[destination.y][3] = 2;
				}
				else
				{
					newBoard.blackPieces.remove(oldRook);
					newBoard.blackPieces.add(newRook);
					newBoard.theBoard[destination.y][3] = -2;	
				}
			}
		}
		
		/** Since pawns can only move forward I only need to check for 0 or 7
		 ** otherwise that would imply the pawn has moved backwards which isn't
		 ** possible.
		 */
		
		if(Math.abs(movingPiece)== 1 && (destination.y==0 || destination.y==7))
		{
			Board rookBoard = newBoard.clone();		// Board with a new rook
			Board knightBoard = newBoard.clone();	// Board with a new knight
			Board bishopBoard = newBoard.clone();	// Board with a new bishop
			Board queenBoard = newBoard.clone();	// Board with a new queen
			
			if(player == 0) // White pieces
			{
				rookBoard.theBoard[destination.y][destination.x] = 2;
				knightBoard.theBoard[destination.y][destination.x] = 3;
				bishopBoard.theBoard[destination.y][destination.x] = 4;
				queenBoard.theBoard[destination.y][destination.x] = 5;
			}
			else // Black pieces
			{
				rookBoard.theBoard[destination.y][destination.x] = -2;
				knightBoard.theBoard[destination.y][destination.x] = -3;
				bishopBoard.theBoard[destination.y][destination.x] = -4;
				queenBoard.theBoard[destination.y][destination.x] = -5;
			}
			
			// Adds the new board states to the return vector
			boardVector.add(rookBoard);
			boardVector.add(knightBoard);
			boardVector.add(bishopBoard);
			boardVector.add(queenBoard);
		}
		else
		{
			// If no pieces are being promoted
			newBoard.theBoard[destination.y][destination.x] = movingPiece;
			boardVector.add(newBoard);
		}
		
		return boardVector;
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
			// Forward one square
			if(piece.y+1 < 8 && board.theBoard[piece.y+1][piece.x] == 0)
			{
				Point tPoint = new Point(piece.x, piece.y+1);
				if(!checkChecker(board, piece, tPoint))
				{
					fMoves.add(tPoint);
				}
			}
			// Checks Up Right
			if(piece.y+1 < 8 && piece.x+1 <8
			&& board.theBoard[piece.y+1][piece.x+1] < 0)
			{
				Point tPoint = new Point(piece.x+1, piece.y+1);
				if(!checkChecker(board, piece, tPoint))
				{
					fMoves.add(tPoint);
				}
			}
			// Checks Up Left
			if(piece.y+1 < 8 && piece.x-1>=0
			&& board.theBoard[piece.y+1][piece.x-1] < 0)
			{
				Point tPoint = new Point(piece.x-1, piece.y+1);
				if(!checkChecker(board, piece, tPoint))
				{
					fMoves.add(tPoint);
				}
			}
			
			// Checks forward 2
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
			
			// Start of En Passant!---------------------------------------------
			
			if(piece.y == 4)
			{
				Point lMove = board.lastMoved[1];
				Point tPoint;
				
				int lastYDist =  board.lastYDistance[1];
				
				// Checks if its a black pawn that last moved 2 spaces
				if(board.theBoard[lMove.y][lMove.x] == -1  && lastYDist == 2)
				{
					// Checks to see the pawns are on the same y plane
					if(lMove.y == piece.y)
					{
						// Checks to see its on either side of the pawn
						if(Math.abs(lMove.x - piece.x) == 1)
						{
							tPoint = new Point(lMove.x, piece.y+1);
							
							if(!checkChecker(board, piece, tPoint))
							{
								fMoves.add(tPoint);
							}
						}
					}
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
			
			if(piece.y-1 >=0 && piece.x+1 <8
			&& board.theBoard[piece.y-1][piece.x+1] > 0)
			{
				Point tPoint = new Point(piece.x+1, piece.y-1);
				if(!checkChecker(board, piece, tPoint))
				{
					fMoves.add(tPoint);
				}
			}
			
			if(piece.y-1 >=0 && piece.x-1>=0
			&& board.theBoard[piece.y-1][piece.x-1] > 0)
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
			
			// En Passant
			if(piece.y == 3)
			{
				Point lMove = board.lastMoved[0];
				Point tPoint;
				
				int lastYDist =  board.lastYDistance[0];
				
				// Checks if its a black pawn that last moved 2 spaces
				if(board.theBoard[lMove.y][lMove.x] == 1  && lastYDist == 2)
				{
					// Checks to see the pawns are on the same y plane
					if(lMove.y == piece.y)
					{
						// Checks to see its on either side of the pawn
						if(Math.abs(lMove.x - piece.x) == 1)
						{
							tPoint = new Point(lMove.x, piece.y-1);
							
							if(!checkChecker(board, piece, tPoint))
							{
								fMoves.add(tPoint);
							}
						}
					}
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
			
			// From the Rook----------------------------------------------------
			
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
			
			if(canCastleLeft(board, turn))
			{
				fMoves.add(new Point(x1-2,y1));
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
	
	// Checks the moves forward / backward from the current piece
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
	private Vector<Point> combineVectors(Vector<Point> in1, Vector<Point> in2)
	{
		for(int i=0 ; i<in2.size() ; i++)
		{
			in1.add(in2.elementAt(i));
		}			
		return in1;		
	}
	
	
	// This will evalute a board for check based on 
	private boolean checkChecker(Board board, Point start, Point end)
	{
		Point theKing;
		
		int theCurPiece = board.theBoard[start.y][start.x];
		
		if(theCurPiece<0){	theKing = board.theKings[1];}	// Black King
		else{				theKing = board.theKings[0];}	// White King
		
		// If the piece you selected is a king change the king to be destination
		if(Math.abs(theCurPiece)==6){theKing = end;}
		
		/** Vertical------------------------------------------------------------
		 **
		 ** The pieces that can cause a check or mate on the vertical are
		 ** Rooks, Queens, and Kings. However Kings must be within one square
		 ** to cause a check. However you would never get two kings beside each
		 ** other as that would result in check for both which can't happen.
		 */
		 
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
				break;
			}
			// Attacking
			else if(sign<0)
			{	
				newPiece = Math.abs(newPiece);
				if(newPiece == 6 && Math.abs((tPoint.y-theKing.y))==1)
				{
					return true;
				}
				else if(newPiece == 2 || newPiece == 5)
				{
					return true;
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
				if(newPiece == 6 && Math.abs((tPoint.y-theKing.y))==1)
				{
					return true;
				}
				else if(newPiece == 2 || newPiece == 5)
				{
					return true;
				}else{break;}
			}
		}
		
		/** Horizontal----------------------------------------------------------
		 **
		 ** The pieces that can cause a check or mate on the horizontal are
		 ** Rooks, Queens, and Kings. However Kings must be within one square
		 ** to cause a check. However you would never get two kings beside each
		 ** other as that would result in check for both which can't happen.
		 */
		
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
					return true;
				}
				else if(newPiece == 2 || newPiece == 5)
				{
					return true;
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
					return true;
				}
				else if(newPiece == 2 || newPiece == 5)
				{
					return true;
				}else{break;}
			}
		}
		
		/** Diagonals-----------------------------------------------------------
		 **
		 ** Special cases for the king and the pawn as they must be 1 square
		 ** away diagonally for them to envoke a capture. Because of this there
		 ** is the check of the abs value of (x1 - kings x) * (y1 - kings y) is
		 ** equal to 1 which indicates 1 square away on the diagonals.
		 **
		 ** In addition to this the pawns can only attack in certain directions.
		 ** Because of this when we check up right and up left we are looking
		 ** for black pawns. When we check down right and down left we are
		 ** looking for white pawns.
		 */
		
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
					int savePiece = newPiece;
					newPiece = Math.abs(newPiece);
					if(newPiece == 4 || newPiece == 5
					 ||newPiece == 6 || savePiece == -1)
					{
						if(newPiece == 6 || savePiece == -1)
						{
							if(Math.abs((x1 - theKing.x)*(y1 - theKing.y)) ==1)
							{
								return true;
							}
							else
							{
								break;
							}
						}
						else
						{
							return true;
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
					int savePiece = newPiece;
					newPiece = Math.abs(newPiece);
					if(newPiece == 4 || newPiece == 5
					 ||newPiece == 6 || savePiece == 1)
					{
						if(newPiece == 6 || savePiece == 1)
						{
							if(Math.abs((x1 - theKing.x)*(y1 - theKing.y)) ==1)
							{
								return true;
							}
							else
							{
								break;
							}
						}
						else
						{
							return true;
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
					int savePiece = newPiece;
					newPiece = Math.abs(newPiece);
					if(newPiece == 4 || newPiece == 5
					 ||newPiece == 6 || savePiece == 1)
					{
						if(newPiece == 6 || savePiece == 1)
						{
							if(Math.abs((x1 - theKing.x)*(y1 - theKing.y)) ==1)
							{
								return true;
							}
							else
							{
								break;
							}
						}
						else
						{
							return true;
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
					int savePiece = newPiece;
					newPiece = Math.abs(newPiece);
					if(newPiece == 4 || newPiece == 5
					 ||newPiece == 6 || savePiece == -1)
					{
						if(newPiece == 6 || savePiece == -1)
						{
							if(Math.abs((x1 - theKing.x)*(y1 - theKing.y)) ==1)
							{
								return true;
							}
							else
							{
								break;
							}
						}
						else
						{
							return true;
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
						return true;
					}
				}
				
				if(theKing.x-1>=0)
				{
					int thePiece = board.theBoard[theKing.y+2][theKing.x-1];
					if((!end.equals(new Point(theKing.x-1, theKing.y+2)))
						&&theCurPiece*thePiece<0
						&& Math.abs(thePiece)==3)
					{
						return true;
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
						return true;
					}
				}
				
				if(theKing.x-1>=0)
				{
					int thePiece = board.theBoard[theKing.y-2][theKing.x-1];
					if((!end.equals(new Point(theKing.x-1, theKing.y-2)))
						&&theCurPiece*thePiece<0
						&& Math.abs(thePiece)==3)
					{
						return true;
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
						return true;
					}
				}
				
				if(theKing.x-2>=0)
				{
					int thePiece = board.theBoard[theKing.y+1][theKing.x-2];
					if((!end.equals(new Point(theKing.x-2, theKing.y+1)))
						&&theCurPiece*thePiece<0
						&& Math.abs(thePiece)==3)
					{
						return true;
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
						return true;
					}
				}
				
				if(theKing.x-2>=0)
				{
					int thePiece = board.theBoard[theKing.y-1][theKing.x-2];
										
					if((!end.equals(new Point(theKing.x-2, theKing.y-1)))
						&& theCurPiece*thePiece<0
						&& Math.abs(thePiece)==3)
					{
						return true;
					}
				}
			}		
		return false;
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
				feasible=getFeasibleMoves(board,board.whitePieces.elementAt(i));
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
				feasible=getFeasibleMoves(board,board.blackPieces.elementAt(i));
				if(!feasible.isEmpty())
				{
					isMate = false;
					break;
				}
			}
		}
		
		return isMate;
	}
	
	/** Checks the two spaces between the king and the rook on the right side of
	 ** the board to ensure castling can take place.
	 */
	public boolean canCastle(Board board, int turn)
	{
		Point theKing = board.theKings[turn];
		boolean retVal = false;
		
		if(board.castleCheck[turn][1]		// King
		&& board.castleCheck[turn][2]		// Kings Rook
		&& !isBoardInCheck(board, turn))
		{
			if(board.theBoard[theKing.y][theKing.x+1] == 0
			&& board.theBoard[theKing.y][theKing.x+2] == 0)
			{
				Point tPoint1 = new Point(theKing.x+1,theKing.y);
				Point tPoint2 = new Point(theKing.x+2,theKing.y);
								
				if(!checkChecker(board,theKing,tPoint1))
				{
					if(!checkChecker(board,theKing,tPoint2))
					{
						retVal = true;
					}
				}
			}			
		}
		
		return retVal;
	}
	
	/** Castle to the Left side of board as viewed by white. Needs to check the
	 ** three spaces between the rook and the king in order to ensure castling
	 ** can take place in this direction.
	 */ 
	
	public boolean canCastleLeft(Board board, int turn)
	{
		Point theKing = board.theKings[turn];
		boolean retVal = false;
		
		if(board.castleCheck[turn][0]	// First Rook
		&& board.castleCheck[turn][1]	// King
		&& !isBoardInCheck(board, turn))
		{
			if(board.theBoard[theKing.y][theKing.x-1] == 0
			&& board.theBoard[theKing.y][theKing.x-2] == 0
			&& board.theBoard[theKing.y][theKing.x-3] == 0)
			{
				Point tPoint1 = new Point(theKing.x-1,theKing.y);
				Point tPoint2 = new Point(theKing.x-2,theKing.y);
				Point tPoint3 = new Point(theKing.x-3,theKing.y);
							
				if(!checkChecker(board,theKing,tPoint1))
				{
					if(!checkChecker(board,theKing,tPoint2))
					{
						if(!checkChecker(board,theKing,tPoint3))
						{
							retVal = true;
						}						
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
				
		board.theKings[0] = new Point(4,0);
		board.theKings[1] = new Point(4,7);

		for(i=0 ; i<2 ; i++)
		{
			for(int j=0 ; j<3 ; j++)
			{
				board.castleCheck[i][j] = true;
			}
		}
		
		// Should set the point to be non existant
		board.lastMoved[0] = new Point(-1,-1);
		board.lastMoved[1] = new Point(-1,-1);
		
		board.lastYDistance[0] = 0;
		board.lastYDistance[1] = 0;
		
		board.movesUntilDraw = 100;
		
		return board;
	}
}