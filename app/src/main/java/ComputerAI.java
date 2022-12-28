import java.awt.Point;
import java.util.Vector;

public class ComputerAI extends Thread {
  private Board curBoard;
  private BoardAction action;
  private int localTurn = -1;

  public ComputerAI() {
    action = new BoardAction();
  }

  public void run() {
    Vector<Board> children;
    Board nextBoard;

    // System.out.println("Computer AI Started");
    curBoard = IChess.gui.getBoard();

    while (true) {
      setPriority(MIN_PRIORITY);
      checkSync();

      if (localTurn == IChess.turn - 1
          && (IChess.turn % 2 == 0 && IChess.whiteIsComputer == true
              || IChess.turn % 2 == 1 && IChess.blackIsComputer == true)) {
        setPriority(NORM_PRIORITY);

        // System.out.println("Human has made a move");

        curBoard = IChess.gui.getBoard();
        localTurn = IChess.turn;

        children = getAllChildren(curBoard, localTurn);

        if (!children.isEmpty()) {
          nextBoard = alphaBetaSearch(children);

          if (nextBoard == null) {
            System.err.println("Returned Board is null...");
            // exit(1);
          }

          sendMove(nextBoard);
        }
      }
    }
  }

  /*Makes sure that the computer's board is not different from the one
   *being shown to the user*/
  private void checkSync() {
    if (localTurn != IChess.turn && localTurn != IChess.turn - 1) {
      // System.out.println("Out of sync: lc was " + localTurn);
      setPriority(NORM_PRIORITY);
      localTurn = IChess.turn - 1;
      // curBoard = IChess.gui.getBoard();
    }
  }

  /*Calculates what move the computer made and sends it to the gui*/
  private void sendMove(Board nb) {
    Point origin = null;
    Point destination = null;
    Point point;

    // Move by white
    if (IChess.turn % 2 == 0) {
      // check for origin
      for (int i = 0; i < curBoard.whitePieces.size(); i++) {
        point = curBoard.whitePieces.elementAt(i);
        if (nb.theBoard[point.y][point.x] == 0) {
          origin = point;
          break;
        }
      }

      // check for destination
      for (int i = 0; i < nb.whitePieces.size(); i++) {
        point = nb.whitePieces.elementAt(i);
        if (curBoard.theBoard[point.y][point.x] <= 0) {
          destination = point;
          break;
        }
      }
    }

    // Move by black
    if (IChess.turn % 2 == 1) {
      // check for origin
      for (int i = 0; i < curBoard.blackPieces.size(); i++) {
        point = curBoard.blackPieces.elementAt(i);
        if (nb.theBoard[point.y][point.x] == 0) {
          origin = point;
          break;
        }
      }

      // check for destination
      for (int i = 0; i < nb.blackPieces.size(); i++) {
        point = nb.blackPieces.elementAt(i);
        if (curBoard.theBoard[point.y][point.x] >= 0) {
          destination = point;
          break;
        }
      }
    }

    curBoard = nb;
    IChess.gui.setComputerMove(nb, origin, destination);
  }

  /*Produces all the moves which can be made from a particular board state*/
  private Vector<Board> getAllChildren(Board board, int ft) {
    Vector<Board> children = new Vector<Board>();
    Vector<Point> feasible;
    Vector<Board> moves;
    int i, j, k;

    // Moves for white
    if (ft % 2 == 0) {
      for (i = 0; i < board.whitePieces.size(); i++) {
        feasible = action.getFeasibleMoves(board, board.whitePieces.elementAt(i));
        for (j = 0; j < feasible.size(); j++) {
          moves = action.move(board, board.whitePieces.elementAt(i), feasible.elementAt(j));
          for (k = 0; k < moves.size(); k++) {
            children.add(moves.elementAt(k));
          }
        }
      }
    } else // Moves for black
    {
      for (i = 0; i < board.blackPieces.size(); i++) {
        feasible = action.getFeasibleMoves(board, board.blackPieces.elementAt(i));
        for (j = 0; j < feasible.size(); j++) {
          moves = action.move(board, board.blackPieces.elementAt(i), feasible.elementAt(j));
          for (k = 0; k < moves.size(); k++) {
            children.add(moves.elementAt(k));
          }
        }
      }
    }
    return children;
  }

  /*Calls alpha-beta evaluation on each of the board states in the vector
   *and returns the best of them*/
  private Board alphaBetaSearch(Vector<Board> nodes) {
    int i;
    int min = (int) Integer.MAX_VALUE;
    int max = (int) Integer.MIN_VALUE;
    int temp = 0;
    Board returnBoard = null;

    // System.out.println("..." + localTurn);
    for (i = 0; i < nodes.size(); i++) {
      // Note +1 and -1 for min and max, to avoid problems with finding the min / max later
      temp =
          alphaBetaEval(
              nodes.elementAt(i), Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1, localTurn + 1);
      // System.out.println("temp: " + temp);
      if (localTurn % 2 == 1) {
        // System.out.println("temp: " + temp + "  " + "min: " + min);
        if ((temp
            < min)) // || (temp == min && Math.random() > .5) || (temp < min + 9 && Math.random() >
        // .9 ))
        {
          returnBoard = nodes.elementAt(i);
          min = temp;
          // System.out.println("New Min: " + i+": " + min);
        }
      }

      // System.out.println(temp);
      if (localTurn % 2 == 0) {
        // System.out.println("temp: " + temp + "  " + "max: " + max);
        if ((temp
            > max)) // || (temp == min && Math.random() > .5) || (temp > max - 9 && Math.random() >
        // .9))
        {
          returnBoard = nodes.elementAt(i);
          max = temp;
          // System.out.println("New Max: " + i+": " + max);
        }
      }
    }
    // System.out.println("eval max/min: " + max + "/" + min);
    return returnBoard;
  }

  /*Perform the alpha-beta evaulation on a board state*/
  private int alphaBetaEval(Board node, int alpha, int beta, int ft) {
    int pieceIndex, feasibleIndex, moveIndex;
    Vector<Point> feasible;
    Vector<Board> moves;
    Board tmpBoard;

    if (terminateCondition(node, ft)) {
      return evaluationFunction(node);
    }

    if (ft % 2 == 1) // Minimizing (Black)
    {
      for (pieceIndex = 0; pieceIndex < node.blackPieces.size(); pieceIndex++) {
        feasible = action.getFeasibleMoves(node, node.blackPieces.elementAt(pieceIndex));

        for (feasibleIndex = 0; feasibleIndex < feasible.size(); feasibleIndex++) {
          moves =
              action.move(
                  node, node.blackPieces.elementAt(pieceIndex), feasible.elementAt(feasibleIndex));
          for (moveIndex = 0; moveIndex < moves.size(); moveIndex++) {
            tmpBoard = moves.elementAt(moveIndex);
            beta = Math.min(alphaBetaEval(tmpBoard, alpha, beta, ft + 1), beta);
            if (beta <= alpha) {
              return beta;
            }
          }
        }
      }
      return beta;
    } else // Maximizing (White)
    {
      for (pieceIndex = 0; pieceIndex < node.whitePieces.size(); pieceIndex++) {
        feasible = action.getFeasibleMoves(node, node.whitePieces.elementAt(pieceIndex));

        for (feasibleIndex = 0; feasibleIndex < feasible.size(); feasibleIndex++) {
          moves =
              action.move(
                  node, node.whitePieces.elementAt(pieceIndex), feasible.elementAt(feasibleIndex));
          for (moveIndex = 0; moveIndex < moves.size(); moveIndex++) {
            tmpBoard = moves.elementAt(moveIndex);
            alpha = Math.max(alphaBetaEval(tmpBoard, alpha, beta, ft + 1), alpha);
            if (alpha >= beta) {
              return alpha;
            }
          }
        }
      }
      return alpha;
    }
  }

  /*Finds the matching child of a root for a human move.  If none is found
   *a -1 is returned*/
  private int indexOfMatchingChild(Board currentBoard, Vector<Board> children) {
    boolean isChild;
    Point piece;
    int child = -1;
    int i, j;
    for (i = 0; i < children.size(); i++) {
      isChild = true;
      if ((localTurn - 1) % 2 == 0) {
        for (j = 0; j < currentBoard.whitePieces.size(); j++) {
          piece = currentBoard.whitePieces.elementAt(j);
          if (currentBoard.theBoard[piece.y][piece.x]
              != children.elementAt(i).theBoard[piece.y][piece.x]) {
            isChild = false;
            break;
          }
        }
        if (isChild) {
          child = i;
          break;
        }
      } else {
        for (j = 0; j < currentBoard.blackPieces.size(); j++) {
          piece = currentBoard.blackPieces.elementAt(j);
          if (currentBoard.theBoard[piece.y][piece.x]
              != children.elementAt(i).theBoard[piece.y][piece.x]) {
            isChild = false;
            break;
          }
        }
        if (isChild) {
          child = i;
          break;
        }
      }
    }

    return child;
  }

  /*evaluation function that returns +ve values on boards
   *that favour white, and -ve values on boards that favour black */
  private int evaluationFunction(Board aBoard) {
    Point pieceLocation;
    int piece;

    int eval = 0;
    int i;

    // White (+ve)
    for (i = 0; i < aBoard.whitePieces.size(); i++) {
      pieceLocation = aBoard.whitePieces.elementAt(i);
      piece = aBoard.theBoard[pieceLocation.y][pieceLocation.x];
      // Center of the board
      if ((pieceLocation.y == 3 || pieceLocation.y == 4)
          && (pieceLocation.x == 3 || pieceLocation.x == 4)
          && piece > 1) {
        eval += 2;
      }

      switch (piece) {
        case 1: // Pawn
          eval += 10;
          eval += pieceLocation.y; // +1 for every step forward
          break;
        case 2: // Rook
          eval += 50;
          break;
        case 3: // Knight
          eval += 100;
          break;
        case 4: // Bishop
          eval += 50;
          break;
        case 5: // Queen
          eval += 200;
          break;
        case 6: // King
          eval += 10000;
          break;
      }
    }

    // Black (-ve)
    for (i = 0; i < aBoard.blackPieces.size(); i++) {
      pieceLocation = aBoard.blackPieces.elementAt(i);
      piece = aBoard.theBoard[pieceLocation.y][pieceLocation.x];

      // Center of the board
      if ((pieceLocation.y == 3 || pieceLocation.y == 4)
          && (pieceLocation.x == 3 || pieceLocation.x == 4)
          && piece < 1) {
        eval -= 2;
      }

      switch (piece) {
        case -1: // Pawn
          eval -= 10;
          eval -= (8 - pieceLocation.y); // -1 for every forward move
          break;
        case -2: // Rook
          eval -= 50;
          break;
        case -3: // Knight
          eval -= 100;
          break;
        case -4: // Bishop
          eval -= 50;
          break;
        case -5: // Queen
          eval -= 200;
          break;
        case -6: // King
          eval -= 10000;
          break;
      }
    }

    return eval;
  }

  /*Returns whether or not to stop searching down a particular path in the
   *alpha-beta evaluation*/
  private boolean terminateCondition(Board node, int ft) {
    // System.out.println("ft: " + ft + " local: " + localTurn);
    if (ft >= localTurn + IChess.ply) {
      return true;
    }

    return false;
  }
}
