import java.util.Vector;
import java.awt.Point;
import java.util.Stack;

 /** IChess version 1.0
  **
  ** Copyright 2006 Stephen Baker (2913895) and Chris Roy (3048899)
  **/
  
public class ComputerAI extends Thread
{
	private Board curBoard;			//the current board
	private BoardAction action;		//board manipualation and move functions			
	private int localTurn = -1;		//the turn being or last evaluated by AI

	//Constructor, sets up action for use with this thread
	public ComputerAI()
	{
		action = new BoardAction();
	}
	
	//where thread executes
	public void run()
	{
		Vector<Board> children;		//a vector of board states reached by a move
		Board nextBoard;			//the board chosen by the search to move to

		//syncronize the board in the thread with the board shown to the user
		curBoard = IChess.gui.getBoard();	
		
		while(true)
		{
			//prevent the thread from slowing performance when not doing much
			setPriority(MIN_PRIORITY);
			
			/* Sets the thread turn to one less than the actual turn.  This way
			 * if it is the computers turn the actual board state will be grabed
	 		 * and a move will be made*/
			if(localTurn != IChess.turn && localTurn != IChess.turn - 1)
			{
				localTurn = IChess.turn - 1;
			}
			
			//should the computer make a move now?
			if(	localTurn == IChess.turn - 1 &&
				(IChess.turn % 2 == 0 && IChess.whiteIsComputer == true ||
				IChess.turn % 2 == 1 && IChess.blackIsComputer == true))
			{
				setPriority(NORM_PRIORITY);
				
				//make sure everything is syncronized	
				curBoard = IChess.gui.getBoard();
				localTurn = IChess.turn;
				
				//get all the possible moves
				children = getAllChildren(curBoard, localTurn);
				
				//if there is any moves to make
				if(!children.isEmpty())
				{
					//find the best move
					nextBoard = alphaBetaSearch(children);
				
					//check if no best move was found, should never happen
					if(nextBoard == null)
					{
						System.err.println("Returned Board is null...");
					}
					
					//take care of details handling a move
					sendMove(nextBoard);
				}
			}
			
			//Notify gui that the AI is not determining a move
			IChess.gui.wakeUp();					
		}	
	}
	
	/*Calculates what move the computer made and sends it to the gui*/
	private void sendMove(Board nb)
	{
			Point origin = null;		//location piece moved from
			Point destination = null;	//location piece moved to
			Point point;				//location of a piece
			int i, j;
			
			//Move by white
			if(IChess.turn % 2 == 0)
			{
				//check for origin
				for(i = 0; i < curBoard.whitePieces.size(); i++)
				{
					point = curBoard.whitePieces.elementAt(i);
					if(nb.theBoard[point.y][point.x] == 0)
					{
						origin = point;
						break;
					}
				}
				
				//check for destination
				for(i = 0; i < nb.whitePieces.size(); i++)
				{
					point = nb.whitePieces.elementAt(i);
					if(curBoard.theBoard[point.y][point.x] <= 0)
					{
						destination = point;
						break;
					}
				}
			}
			
			//Move by black
			if(IChess.turn % 2 == 1)
			{
				//check for origin
				for(i = 0; i < curBoard.blackPieces.size(); i++)
				{
					point = curBoard.blackPieces.elementAt(i);
					if(nb.theBoard[point.y][point.x] == 0)
					{
						origin = point;
						break;
					}
				}
				
				//check for destination
				for(i = 0; i < nb.blackPieces.size(); i++)
				{
					point = nb.blackPieces.elementAt(i);
					if(curBoard.theBoard[point.y][point.x] >= 0)
					{
						destination = point;
						break;
					}
				}
			}
			
			//set the current board of the thread to this board
			curBoard = nb;
			
			//send this board to the gui
			IChess.gui.setComputerMove(nb, origin, destination);
	}
	
	
	/*Produces all the moves which can be made from a particular board state*/
	private Vector<Board> getAllChildren(Board board, int ft)
	{
		Vector<Board> children = new Vector<Board>();
		Vector<Point> feasible;
		Vector<Board> moves;
		int i, j, k;
		
		//Moves for white
		if(ft % 2 == 0)
		{
			for(i = 0; i < board.whitePieces.size(); i++)
			{
				feasible = action.getFeasibleMoves
				(	board, 
					board.whitePieces.elementAt(i)
				);
				for(j = 0; j < feasible.size(); j++)
				{
					moves = action.move
					(
						board,
						board.whitePieces.elementAt(i),
						feasible.elementAt(j)
					);
					for(k = 0; k < moves.size(); k++)
					{
						children.add(moves.elementAt(k));
					}
				}
			}
		}
		else //Moves for black
		{
			for(i = 0; i < board.blackPieces.size(); i++)
			{
				feasible = action.getFeasibleMoves
				(
					board,
					board.blackPieces.elementAt(i)
				);
				for(j = 0; j < feasible.size(); j++)
				{
					moves = action.move
					(
						board,
						board.blackPieces.elementAt(i),
						feasible.elementAt(j)
					);
					for(k = 0; k < moves.size(); k++)
					{
						children.add(moves.elementAt(k));
					}
				}
			}
		}
		return children;
	}
	
	
	/*Calls alpha-beta evaluation on each of the board states in the vector
	 *and returns the best of them*/
	private Board alphaBetaSearch(Vector<Board> nodes)
	{
		int i;
		int min = (int) Integer.MAX_VALUE;	//lowest value of a board so far
		int max = (int) Integer.MIN_VALUE;	//highest value of a board so far
		int temp = 0;						//value of current board
		Board returnBoard = null;			//the best board so far
		
		//check every board to find the best
		for(i = 0; i < nodes.size(); i++)
		{
//+1 and -1 for min and max, to avoid problems with finding the min / max later
			temp = alphaBetaEval(	nodes.elementAt(i), 
									Integer.MIN_VALUE + 1, 
									Integer.MAX_VALUE - 1, 
									localTurn + 1			);
			
			//For black find the board with the lowest value				
			if(localTurn % 2 == 1)
			{
				if(temp < min)
				{
					returnBoard = nodes.elementAt(i);
					min = temp;
				}
			}
			
			//For white find the board with the highest value
			if(localTurn % 2 == 0)
			{
				if(temp > max)
				{
					returnBoard = nodes.elementAt(i);
					max = temp;
				}
			}
		}
		
		return returnBoard;
		
	}
	
	/*Perform the alpha-beta evaulation on a board state*/
	private int alphaBetaEval(Board node, int alpha, int beta, int ft)
	{
		int iPiece, iFeasible, iMove;	//indexes of valid moves
		Vector<Point> feasible;		//list of feasible moves for a piece
		Vector<Board> moves;		//list of board states resulting from a move
		Board tmpBoard;				//a temporary board
		boolean mate;				//flag to determine if mate
		
		//find out if this is a leaf node and if so evaluate it
		if(terminateCondition(node, ft))
		{
			return evaluationFunction(node, ft);
		}
		
		
		if(ft % 2 == 1) //Minimizing (Black)
		{
			mate = true;
			for(iPiece = 0; iPiece < node.blackPieces.size(); iPiece++)
			{
				feasible = action.getFeasibleMoves
				(
					node,
					node.blackPieces.elementAt(iPiece)
				);
				
				for(iFeasible = 0; iFeasible < feasible.size(); iFeasible++)
				{
					mate = false; //there is at least one feasible move
					moves = action.move
					(
						node,
						node.blackPieces.elementAt(iPiece),
						feasible.elementAt(iFeasible)
					);	
					for(iMove = 0; iMove < moves.size(); iMove++)
					{
						tmpBoard = moves.elementAt(iMove);
						beta = Math.min
						(
							alphaBetaEval(tmpBoard,alpha,beta, ft+1),
							beta
						);
						if(beta <= alpha)
						{
							return beta;
						}
					}
				}
			}
			if(mate)
			{
				//BLACK CHECKMATE
				if(action.isBoardInCheck(node, 1))
				{
					return Integer.MAX_VALUE - 1;
				}
				else //STALEMATE
				{
					return 0;
				}
			}
			return beta;			
		}
		else //Maximizing (White)
		{
			mate = true;
			for(iPiece = 0; iPiece < node.whitePieces.size(); iPiece++)
			{
				feasible = action.getFeasibleMoves
				(
					node,
					node.whitePieces.elementAt(iPiece)
				);
				
				for(iFeasible = 0; iFeasible < feasible.size(); iFeasible++)
				{
					mate = false; //there is at least one feasible move
					moves = action.move
					(
						node, 
						node.whitePieces.elementAt(iPiece), 
						feasible.elementAt(iFeasible)
					);
					for(iMove = 0; iMove < moves.size(); iMove++)
					{
						tmpBoard = moves.elementAt(iMove);
						alpha = Math.max
						(
							alphaBetaEval(tmpBoard,alpha,beta, ft+1),
							alpha
						);
						if(alpha >= beta)
						{
							return alpha;
						}
					}
				}
			}
			if(mate)
			{
				//WHITE CHECKMATE
				if(action.isBoardInCheck(node, 0))
				{
					return Integer.MIN_VALUE + 1;
				}
				else  //STALEMATE
				{ 
					return 0;
				}
			}
			return alpha;			
		}
	}
	

	
	/*Finds the matching child of a root for a human move.  If none is found
	 *a -1 is returned*/  
	private boolean areEqual(Board boardA, Board boardB)
	{
		int i, j;
	
		//same number of pieces
		if(	boardA.whitePieces.size() != boardB.whitePieces.size() ||
			boardA.blackPieces.size() != boardB.blackPieces.size())
		{
			return false;
		}
		
		//each piece is in the same spot
		for(i = 0; i < 8; i++)
		{
			for(j = 0; j < 8; j++)
			{
				if(boardA.theBoard[i][j] != boardB.theBoard[i][j])
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	
	
	/*evaluation function that returns +ve values on boards 
	 *that favour white, and -ve values on boards that favour black */
	private int evaluationFunction(Board aBoard, int ft)
	{
		Point location;	//location of piece being checked
		int piece;				//type of piece being checked
		int eval = 0;			//value of board
		int i;
		
		
		//Avoid repeating moves and conditions that would lead to a draw
		switch(localTurn % 2)
		{
			case 0:	//white
				//cycle
				if(areEqual(aBoard, curBoard))
				{
					return Integer.MIN_VALUE + 5;	
				}
				//close to draw
				if(aBoard.movesUntilDraw < 10)
				{
					eval -= (10 - aBoard.movesUntilDraw) * 50;
				}
				break;
			
			case 1:	//black
				//cycle
				if(areEqual(aBoard, curBoard))
				{
					return Integer.MAX_VALUE - 5;
				}
				//close to draw
				if(aBoard.movesUntilDraw < 10)
				{
					eval += (10 - aBoard.movesUntilDraw) * 50;
				}
				break;
		}
		
		//White (+ve)
		for(i = 0; i < aBoard.whitePieces.size(); i++)
		{
			location = aBoard.whitePieces.elementAt(i);
			piece = aBoard.theBoard[location.y][location.x];
			
			//Center of the board
			if(	(location.y == 3 || location.y == 4) && 
				(location.x == 3 || location.x == 4) && 
				(piece > 1)	) 
			{
				eval += 2;
			}
			
			switch(piece)
			{
				case 1:  //Pawn
					eval += 10;
					eval += location.y; //+1 for every step forward
					
					//Pawns should not block king's movement
					if(	(location.x >= aBoard.theKings[0].x - 1 &&
						 location.x <= aBoard.theKings[0].x + 1) &&
						(location.y >= aBoard.theKings[0].y - 1 &&
						 location.y <= aBoard.theKings[0].y + 1))
					{
						eval -= 1;
					}
					
					break;
				case 2: //Rook
					eval += 100;
					break;
				case 3: //Knight
					eval += 60;
					break;
				case 4: //Bishop
					eval += 50;
					break;
				case 5: //Queen
					eval += 600;
					break;
				case 6: //King
					eval += 10000;
					eval += action.getFeasibleMoves(aBoard, location).size();
					break;
			}
		}
		
		//Black (-ve)
		for(i = 0; i < aBoard.blackPieces.size(); i++)
		{
			location = aBoard.blackPieces.elementAt(i);
			piece = aBoard.theBoard[location.y][location.x];
			
			//Center of the board
			if(	(location.y == 3 || location.y == 4) &&
				(location.x == 3 || location.x == 4) &&
				(piece < 1)	) 
			{
				eval -= 2;
			}
			
			switch(piece)
			{
				case -1:  //Pawn
					eval -= 10;
					eval -= (8-location.y); //-1 for every forward move
					
					//Pawns should not block king's movement
					if(	(location.x >= aBoard.theKings[1].x - 1 && 
						location.x <= aBoard.theKings[1].x + 1) &&
						(location.y >= aBoard.theKings[1].y - 1 && 
						location.y <= aBoard.theKings[1].y + 1))
					{
						eval += 1;
					}
					break;
				case -2:  //Rook
					eval -= 100;
					break;
				case -3:  //Knight
					eval -= 60;
					break;
				case -4:  //Bishop
					eval -= 50;
					break;
				case -5:  //Queen
					eval -= 600;
					break;
				case -6:  //King
					eval -= 10000;
					eval -= action.getFeasibleMoves(aBoard, location).size();
					break;
			}
		}
		
		return eval;
	}
	
	/*Returns whether or not to stop searching down a particular path in the
	 *alpha-beta evaluation*/
	private boolean terminateCondition(Board node, int ft)
	{
		if(ft >= localTurn + IChess.ply || areEqual(node, curBoard))
		{
			return true;
		}
		
		return false;
	}
}