import java.awt.Point;
import java.util.Vector;

public class BoardAction {
  public Vector<Board> move(Board board, Point origin, Point destination) {
    Vector<Board> boardVector = new Vector<Board>();

    Board newBoard;
    int player = Board.WHITE;
    int movingPiece = board.theBoard[origin.y][origin.x];

    newBoard = board.clone();

    newBoard.theBoard[origin.y][origin.x] = 0;

    // Kill any black piece here!
    if (board.isBlack(destination.y, destination.x)) {
      newBoard.blackPieces.remove(destination);
    } else if (board.isWhite(destination.y, destination.x)) {
      newBoard.whitePieces.remove(destination);
    }

    if (movingPiece > 0) {
      player = Board.WHITE;
      newBoard.whitePieces.remove(origin);
      newBoard.whitePieces.add(destination);
    } else {
      player = Board.BLACK;
      newBoard.blackPieces.remove(origin);
      newBoard.blackPieces.add(destination);
    }

    // sets castle check to false;
    if (Math.abs(movingPiece) == Board.KING || Math.abs(movingPiece) == Board.ROOK) {
      newBoard.castleCheck[player] = false;
    }

    if (Math.abs(movingPiece) == Board.KING) {
      newBoard.theKings[player] = destination;

      if (destination.x - origin.x == 2) {
        System.out.println("Castle!");
        Point oldRook = new Point(7, destination.y);
        Point newRook = new Point(5, destination.y);

        newBoard.theBoard[destination.y][7] = 0;

        if (player == Board.WHITE) {
          newBoard.whitePieces.remove(oldRook);
          newBoard.whitePieces.add(newRook);
          newBoard.theBoard[destination.y][5] = Board.ROOK;
        } else {
          newBoard.blackPieces.remove(oldRook);
          newBoard.blackPieces.add(newRook);
          newBoard.theBoard[destination.y][5] = Board.ROOK * Board.BLACK_MULTIPLIER;
        }
      }
    }

    // Since pawns can only move forward I only need to check for 0 or 7
    // otherwise that would imply the pawn has moved backwards which isn't
    // possible.

    if (Math.abs(movingPiece) == Board.PAWN && (destination.y == 0 || destination.y == 7)) {
      // temp solution until the vector method is adopted
      if (player == Board.WHITE) {
        movingPiece = Board.QUEEN;
      } else {
        movingPiece = Board.QUEEN * Board.BLACK_MULTIPLIER;
      }
      // only need because i put the regular piece adder in the else below
      newBoard.theBoard[destination.y][destination.x] = movingPiece;

      Board rookBoard = newBoard.clone();
      Board knightBoard = newBoard.clone();
      Board bishopBoard = newBoard.clone();
      Board queenBoard = newBoard.clone();

      if (player == Board.WHITE) {
        rookBoard.theBoard[destination.y][destination.x] = Board.ROOK;
        knightBoard.theBoard[destination.y][destination.x] = Board.KNIGHT;
        bishopBoard.theBoard[destination.y][destination.x] = Board.BISHOP;
        queenBoard.theBoard[destination.y][destination.x] = Board.QUEEN;
      } else {
        rookBoard.theBoard[destination.y][destination.x] = Board.ROOK * Board.BLACK_MULTIPLIER;
        knightBoard.theBoard[destination.y][destination.x] = Board.KNIGHT * Board.BLACK_MULTIPLIER;
        bishopBoard.theBoard[destination.y][destination.x] = Board.BISHOP * Board.BLACK_MULTIPLIER;
        queenBoard.theBoard[destination.y][destination.x] = Board.QUEEN * Board.BLACK_MULTIPLIER;
      }

      boardVector.add(rookBoard);
      boardVector.add(knightBoard);
      boardVector.add(bishopBoard);
      boardVector.add(queenBoard);
    } else {
      newBoard.theBoard[destination.y][destination.x] = movingPiece;
      boardVector.add(newBoard);
    }

    return boardVector;
  }

  /**
   * Returns a vector of feasible moves
   *
   * <p>A capture is determined if the current piece value * the square it is going to is <= 0. This
   * is because if they are the same sign the value will be positive. So if the value isn't positive
   * then we know the square can be occupied. After which a check is usually done to see if the last
   * square was empty. If it wasn't than the loop exists as we know we've hit another players piece
   * and therefore should not continue searching in that direction.
   */
  public Vector<Point> getFeasibleMoves(Board board, Point piece) {
    int theCurPiece = board.theBoard[piece.y][piece.x];
    Vector<Point> fMoves = new Vector<Point>();
    int turn;

    if (theCurPiece > 0) {
      turn = Board.WHITE;
    } else {
      turn = Board.BLACK;
    }

    // White Pawn
    if (theCurPiece == Board.PAWN) {
      if (piece.y + 1 < 8 && board.isVacant(piece.y + 1, piece.x)) {
        Point tPoint = new Point(piece.x, piece.y + 1);
        if (!checkChecker(board, piece, tPoint)) {
          fMoves.add(tPoint);
        }
      }

      if (piece.y + 1 < 8 && piece.x + 1 < 8 && board.isBlack(piece.y + 1, piece.x + 1)) {
        Point tPoint = new Point(piece.x + 1, piece.y + 1);
        if (!checkChecker(board, piece, tPoint)) {
          fMoves.add(tPoint);
        }
      }

      if (piece.y + 1 < 8 && piece.x - 1 >= 0 && board.isBlack(piece.y + 1, piece.x - 1)) {
        Point tPoint = new Point(piece.x - 1, piece.y + 1);
        if (!checkChecker(board, piece, tPoint)) {
          fMoves.add(tPoint);
        }
      }

      if (piece.y == 1
          && board.isVacant(piece.y + 1, piece.x)
          && board.isVacant(piece.y + 2, piece.x)) {
        Point tPoint = new Point(piece.x, piece.y + 2);
        if (!checkChecker(board, piece, tPoint)) {
          fMoves.add(tPoint);
        }
      }
    } else if (theCurPiece == Board.BLACK_MULTIPLIER * Board.PAWN) {
      if (piece.y - 1 >= 0 && board.isVacant(piece.y - 1, piece.x)) {
        Point tPoint = new Point(piece.x, piece.y - 1);
        if (!checkChecker(board, piece, tPoint)) {
          fMoves.add(tPoint);
        }
      }

      if (piece.y - 1 >= 0 && piece.x + 1 < 8 && board.isWhite(piece.y - 1, piece.x + 1)) {
        Point tPoint = new Point(piece.x + 1, piece.y - 1);
        if (!checkChecker(board, piece, tPoint)) {
          fMoves.add(tPoint);
        }
      }

      if (piece.y - 1 >= 0 && piece.x - 1 >= 0 && board.isWhite(piece.y - 1, piece.x - 1)) {
        Point tPoint = new Point(piece.x - 1, piece.y - 1);
        if (!checkChecker(board, piece, tPoint)) {
          fMoves.add(tPoint);
        }
      }

      if (piece.y == 6
          && board.isVacant(piece.y - 1, piece.x)
          && board.isVacant(piece.y - 2, piece.x)) {
        Point tPoint = new Point(piece.x, piece.y - 2);
        if (!checkChecker(board, piece, tPoint)) {
          fMoves.add(tPoint);
        }
      }
    }
    // Rook
    else if (Math.abs(theCurPiece) == Board.ROOK) {
      // Forward and Backwards
      fMoves = combineVectors(fMoves, checkVertMoves(board, piece));

      // Right and Left
      fMoves = combineVectors(fMoves, checkHoriMoves(board, piece));

    }
    // Knight
    else if (Math.abs(theCurPiece) == Board.KNIGHT) { // Forward 2 and left / right 1
      if (piece.y + 2 < 8) {
        if (piece.x + 1 < 8) {
          if (theCurPiece * board.theBoard[piece.y + 2][piece.x + 1] <= 0) {
            Point tPoint = new Point(piece.x + 1, piece.y + 2);
            if (!checkChecker(board, piece, tPoint)) {
              fMoves.add(tPoint);
            }
          }
        }

        if (piece.x - 1 >= 0) {
          if (theCurPiece * board.theBoard[piece.y + 2][piece.x - 1] <= 0) {
            Point tPoint = new Point(piece.x - 1, piece.y + 2);
            if (!checkChecker(board, piece, tPoint)) {
              fMoves.add(tPoint);
            }
          }
        }
      }

      // Backward 2 and left / right 1
      if (piece.y - 2 >= 0) {
        if (piece.x + 1 < 8) {
          if (theCurPiece * board.theBoard[piece.y - 2][piece.x + 1] <= 0) {
            Point tPoint = new Point(piece.x + 1, piece.y - 2);
            if (!checkChecker(board, piece, tPoint)) {
              fMoves.add(tPoint);
            }
          }
        }

        if (piece.x - 1 >= 0) {
          if (theCurPiece * board.theBoard[piece.y - 2][piece.x - 1] <= 0) {
            Point tPoint = new Point(piece.x - 1, piece.y - 2);
            if (!checkChecker(board, piece, tPoint)) {
              fMoves.add(tPoint);
            }
          }
        }
      }

      // Forward 1 and left / right 2
      if (piece.y + 1 < 8) {
        if (piece.x + 2 < 8) {
          if (theCurPiece * board.theBoard[piece.y + 1][piece.x + 2] <= 0) {
            Point tPoint = new Point(piece.x + 2, piece.y + 1);
            if (!checkChecker(board, piece, tPoint)) {
              fMoves.add(tPoint);
            }
          }
        }

        if (piece.x - 2 >= 0) {
          if (theCurPiece * board.theBoard[piece.y + 1][piece.x - 2] <= 0) {
            Point tPoint = new Point(piece.x - 2, piece.y + 1);
            if (!checkChecker(board, piece, tPoint)) {
              fMoves.add(tPoint);
            }
          }
        }
      }

      // Backward 1 and left / right 2
      if (piece.y - 1 >= 0) {
        if (piece.x + 2 < 8) {
          if (theCurPiece * board.theBoard[piece.y - 1][piece.x + 2] <= 0) {
            Point tPoint = new Point(piece.x + 2, piece.y - 1);
            if (!checkChecker(board, piece, tPoint)) {
              fMoves.add(tPoint);
            }
          }
        }

        if (piece.x - 2 >= 0) {
          if (theCurPiece * board.theBoard[piece.y - 1][piece.x - 2] <= 0) {
            Point tPoint = new Point(piece.x - 2, piece.y - 1);
            if (!checkChecker(board, piece, tPoint)) {
              fMoves.add(tPoint);
            }
          }
        }
      }

    }
    // Bishop
    else if (Math.abs(theCurPiece) == Board.BISHOP) {
      // Right Up
      int y1 = piece.y + 1;
      int x1 = piece.x + 1;

      while (y1 < 8 && x1 < 8) {
        if (theCurPiece * board.theBoard[y1][x1] <= 0) {
          Point tPoint = new Point(x1, y1);
          if (!checkChecker(board, piece, tPoint)) {
            fMoves.add(tPoint);
          }
        }

        if (!board.isVacant(y1, x1)) {
          break;
        }

        x1++;
        y1++;
      }

      // Left Up
      y1 = piece.y + 1;
      x1 = piece.x - 1;

      while (y1 < 8 && x1 >= 0) {
        if (theCurPiece * board.theBoard[y1][x1] <= 0) {
          Point tPoint = new Point(x1, y1);
          if (!checkChecker(board, piece, tPoint)) {
            fMoves.add(tPoint);
          }
        }

        if (!board.isVacant(y1, x1)) {
          break;
        }

        x1--;
        y1++;
      }

      // Right Down
      y1 = piece.y - 1;
      x1 = piece.x + 1;

      while (y1 >= 0 && x1 < 8) {
        if (theCurPiece * board.theBoard[y1][x1] <= 0) {
          Point tPoint = new Point(x1, y1);
          if (!checkChecker(board, piece, tPoint)) {
            fMoves.add(tPoint);
          }
        }

        if (!board.isVacant(y1, x1)) {
          break;
        }

        x1++;
        y1--;
      }

      // Left Down
      y1 = piece.y - 1;
      x1 = piece.x - 1;

      while (y1 >= 0 && x1 >= 0) {
        if (theCurPiece * board.theBoard[y1][x1] <= 0) {
          Point tPoint = new Point(x1, y1);
          if (!checkChecker(board, piece, tPoint)) {
            fMoves.add(tPoint);
          }
        }

        if (!board.isVacant(y1, x1)) {
          break;
        }

        x1--;
        y1--;
      }

    }
    // The Queen!
    else if (Math.abs(theCurPiece) == Board.QUEEN) {
      // From the Bishop-------------------------------------------------

      // Right Up
      int y1 = piece.y + 1;
      int x1 = piece.x + 1;

      while (y1 < 8 && x1 < 8) {
        if (theCurPiece * board.theBoard[y1][x1] <= 0) {
          Point tPoint = new Point(x1, y1);
          if (!checkChecker(board, piece, tPoint)) {
            fMoves.add(tPoint);
          }
        }

        if (!board.isVacant(y1, x1)) {
          break;
        }

        x1++;
        y1++;
      }

      // Left Up
      y1 = piece.y + 1;
      x1 = piece.x - 1;

      while (y1 < 8 && x1 >= 0) {
        if (theCurPiece * board.theBoard[y1][x1] <= 0) {
          Point tPoint = new Point(x1, y1);
          if (!checkChecker(board, piece, tPoint)) {
            fMoves.add(tPoint);
          }
        }

        if (!board.isVacant(y1, x1)) {
          break;
        }

        x1--;
        y1++;
      }

      // Right Down
      y1 = piece.y - 1;
      x1 = piece.x + 1;

      while (y1 >= 0 && x1 < 8) {
        if (theCurPiece * board.theBoard[y1][x1] <= 0) {
          Point tPoint = new Point(x1, y1);
          if (!checkChecker(board, piece, tPoint)) {
            fMoves.add(tPoint);
          }
        }

        if (!board.isVacant(y1, x1)) {
          break;
        }

        x1++;
        y1--;
      }

      // Left Down
      y1 = piece.y - 1;
      x1 = piece.x - 1;

      while (y1 >= 0 && x1 >= 0) {
        if (theCurPiece * board.theBoard[y1][x1] <= 0) {
          Point tPoint = new Point(x1, y1);
          if (!checkChecker(board, piece, tPoint)) {
            fMoves.add(tPoint);
          }
        }

        if (!board.isVacant(y1, x1)) {
          break;
        }

        x1--;
        y1--;
      }

      // From the Rook---------------------------------------------------

      // Forward and Backward
      fMoves = combineVectors(fMoves, checkVertMoves(board, piece));

      // Right and Left
      fMoves = combineVectors(fMoves, checkHoriMoves(board, piece));

    }
    // The King!
    else if (Math.abs(theCurPiece) == Board.KING) {
      int y1 = piece.y;
      int x1 = piece.x;

      if (canCastle(board, turn)) {
        fMoves.add(new Point(x1 + 2, y1));
      }

      // Forward
      if (y1 + 1 < 8 && (theCurPiece * board.theBoard[y1 + 1][x1] <= 0)) {
        Point tPoint = new Point(x1, y1 + 1);
        if (!checkChecker(board, piece, tPoint)) {
          fMoves.add(tPoint);
        }
      }

      // Backward
      if (y1 - 1 >= 0 && (theCurPiece * board.theBoard[y1 - 1][x1] <= 0)) {
        Point tPoint = new Point(x1, y1 - 1);
        if (!checkChecker(board, piece, tPoint)) {
          fMoves.add(tPoint);
        }
      }

      // Left
      if (x1 - 1 >= 0 && (theCurPiece * board.theBoard[y1][x1 - 1] <= 0)) {
        Point tPoint = new Point(x1 - 1, y1);
        if (!checkChecker(board, piece, tPoint)) {
          fMoves.add(tPoint);
        }
      }

      // Right
      if (x1 + 1 < 8 && (theCurPiece * board.theBoard[y1][x1 + 1] <= 0)) {
        Point tPoint = new Point(x1 + 1, y1);
        if (!checkChecker(board, piece, tPoint)) {
          fMoves.add(tPoint);
        }
      }

      // Forward Left
      if (y1 + 1 < 8 && x1 - 1 >= 0 && (theCurPiece * board.theBoard[y1 + 1][x1 - 1] <= 0)) {
        Point tPoint = new Point(x1 - 1, y1 + 1);
        if (!checkChecker(board, piece, tPoint)) {
          fMoves.add(tPoint);
        }
      }

      // Forward Right
      if (y1 + 1 < 8 && x1 + 1 < 8 && (theCurPiece * board.theBoard[y1 + 1][x1 + 1] <= 0)) {
        Point tPoint = new Point(x1 + 1, y1 + 1);
        if (!checkChecker(board, piece, tPoint)) {
          fMoves.add(tPoint);
        }
      }

      // Backward Left
      if (y1 - 1 >= 0 && x1 - 1 >= 0 && (theCurPiece * board.theBoard[y1 - 1][x1 - 1] <= 0)) {
        Point tPoint = new Point(x1 - 1, y1 - 1);
        if (!checkChecker(board, piece, tPoint)) {
          fMoves.add(tPoint);
        }
      }

      // Backward Right
      if (y1 - 1 >= 0 && x1 + 1 < 8 && (theCurPiece * board.theBoard[y1 - 1][x1 + 1] <= 0)) {
        Point tPoint = new Point(x1 + 1, y1 - 1);
        if (!checkChecker(board, piece, tPoint)) {
          fMoves.add(tPoint);
        }
      }
    }
    return fMoves;
  }

  // Checks the moves upwards from the current piece
  private Vector<Point> checkVertMoves(Board board, Point piece) {
    Vector<Point> retVector = new Vector<Point>();
    int theCurPiece = board.theBoard[piece.y][piece.x];

    for (int i = piece.y + 1; i < 8; i++) {
      if (theCurPiece * board.theBoard[i][piece.x] <= 0) {
        if (!checkChecker(board, piece, new Point(piece.x, i))) {
          retVector.add(new Point(piece.x, i));
        }
      }
      if (!board.isVacant(i, piece.x)) {
        break;
      }
    }

    // Backward
    for (int i = piece.y - 1; i >= 0; i--) {
      if (theCurPiece * board.theBoard[i][piece.x] <= 0) {
        if (!checkChecker(board, piece, new Point(piece.x, i))) {
          retVector.add(new Point(piece.x, i));
        }
      }
      if (!board.isVacant(i, piece.x)) {
        break;
      }
    }
    return retVector;
  }

  // Checks the moves left and right from the current piece
  private Vector<Point> checkHoriMoves(Board board, Point piece) {
    Vector<Point> retVector = new Vector<Point>();
    int theCurPiece = board.theBoard[piece.y][piece.x];

    // Right
    for (int i = piece.x + 1; i < 8; i++) {
      if (theCurPiece * board.theBoard[piece.y][i] <= 0) {
        if (!checkChecker(board, piece, new Point(i, piece.y))) {
          retVector.add(new Point(i, piece.y));
        }
      }
      if (!board.isVacant(piece.y, i)) {
        break;
      }
    }

    // Left
    for (int i = piece.x - 1; i >= 0; i--) {
      if (theCurPiece * board.theBoard[piece.y][i] <= 0) {
        if (!checkChecker(board, piece, new Point(i, piece.y))) {
          retVector.add(new Point(i, piece.y));
        }
      }
      if (!board.isVacant(piece.y, i)) {
        break;
      }
    }
    return retVector;
  }

  // Adds vector 2 to vector 1 and returns
  private Vector<Point> combineVectors(Vector<Point> input1, Vector<Point> input2) {
    for (int i = 0; i < input2.size(); i++) {
      input1.add(input2.elementAt(i));
    }
    return input1;
  }

  // This will evalute a board for check based on
  private boolean checkChecker(Board board, Point start, Point end) {
    boolean theResult = false;
    Point theKing;

    int theCurPiece = board.theBoard[start.y][start.x];
    //		System.out.println("The Current piece is: "+theCurPiece );

    if (theCurPiece < 0) {
      theKing = board.theKings[Board.BLACK];
    } else {
      theKing = board.theKings[Board.WHITE];
    }

    // If the piece you selected happens to be a king change to reflect pos
    if (Math.abs(theCurPiece) == Board.KING) {
      theKing = end;
    }

    // Vertical-------------------------------------------------------------

    // Forward
    for (int i = theKing.y + 1; i < 8; i++) {
      int newPiece = board.theBoard[i][theKing.x];
      int sign = theCurPiece * newPiece;

      Point tPoint = new Point(theKing.x, i);

      // if it runs into where this pieces is going break
      if (tPoint.equals(end)) break;
      // if it runs into the space it used to be in continue
      else if (tPoint.equals(start)) continue;

      if (sign > 0) {
        //	System.out.println("No Check to the front");
        break;
      }
      // Attacking
      else if (sign < 0) {
        newPiece = Math.abs(newPiece);
        if (newPiece == Board.KING && Math.abs((tPoint.x - theKing.x)) == 1) {
          theResult = true;
        } else if (newPiece == Board.ROOK || newPiece == Board.QUEEN) {
          theResult = true;
        } else {
          break;
        }
      }
    }

    // Backward
    for (int i = theKing.y - 1; i >= 0; i--) {
      int newPiece = board.theBoard[i][theKing.x];
      int sign = theCurPiece * newPiece;

      Point tPoint = new Point(theKing.x, i);

      // if it runs into where this pieces is going break
      if (tPoint.equals(end)) break;
      // if it runs into the space it used to be in continue
      else if (tPoint.equals(start)) continue;

      if (sign > 0) {
        //	System.out.println("No Check to the back");
        break;
      } else if (sign < 0) {
        newPiece = Math.abs(newPiece);
        if (newPiece == Board.KING && Math.abs((tPoint.x - theKing.x)) == 1) {
          theResult = true;
        } else if (newPiece == Board.ROOK || newPiece == Board.QUEEN) {
          theResult = true;
        } else {
          break;
        }
      }
    }

    // Horizontal-----------------------------------------------------------

    // Left
    for (int i = theKing.x - 1; i >= 0; i--) {
      int newPiece = board.theBoard[theKing.y][i];
      int sign = theCurPiece * newPiece;

      Point tPoint = new Point(i, theKing.y);

      // if it runs into where this pieces is going break
      if (tPoint.equals(end)) break;
      // if it runs into the space it used to be in continue
      else if (tPoint.equals(start)) continue;

      if (sign > 0) {
        //	System.out.println("No Check to the left");
        break;
      } else if (sign < 0) {
        newPiece = Math.abs(newPiece);
        if (newPiece == Board.KING && Math.abs((tPoint.x - theKing.x)) == 1) {
          theResult = true;
        } else if (newPiece == Board.ROOK || newPiece == Board.QUEEN) {
          theResult = true;
        } else {
          break;
        }
      }
    }

    // Right
    for (int i = theKing.x + 1; i < 8; i++) {
      int newPiece = board.theBoard[theKing.y][i];
      int sign = theCurPiece * newPiece;

      Point tPoint = new Point(i, theKing.y);

      // if it runs into where this pieces is going break
      if (tPoint.equals(end)) break;
      // if it runs into the space it used to be in continue
      else if (tPoint.equals(start)) continue;

      if (sign > 0) {
        //	System.out.println("No Check to the right");
        break;
      } else if (sign < 0) {
        newPiece = Math.abs(newPiece);
        if (newPiece == Board.KING && Math.abs((tPoint.x - theKing.x)) == 1) {
          theResult = true;
        } else if (newPiece == Board.ROOK || newPiece == Board.QUEEN) {
          theResult = true;
        } else {
          break;
        }
      }
    }

    // Diagonals------------------------------------------------------------

    // Up Right

    int y1 = theKing.y + 1;
    int x1 = theKing.x + 1;

    while (y1 < 8 && x1 < 8) {
      Point tPoint = new Point(x1, y1);

      // if it runs into where this pieces is going break
      if (tPoint.equals(end)) break;
      // if it runs into the space it used to be in continue
      else if (tPoint.equals(start)) {
        x1++;
        y1++;
        continue;
      }

      int newPiece = board.theBoard[y1][x1];

      if (theCurPiece * newPiece > 0) {
        //	System.out.println("No Check Up and Right");
        break;
      }

      if (theCurPiece * newPiece < 0) {
        newPiece = Math.abs(newPiece);
        if (newPiece == Board.BISHOP
            || newPiece == Board.QUEEN
            || newPiece == Board.KING
            || newPiece == Board.PAWN) {
          if (newPiece == Board.KING || newPiece == Board.PAWN) {
            if (Math.abs((x1 - theKing.x) * (y1 - theKing.y)) == 1) {
              theResult = true;
            } else {
              break;
            }
          } else {
            theResult = true;
          }
        } else {
          break;
        }
      }

      x1++;
      y1++;
    }

    // Down Right

    y1 = theKing.y - 1;
    x1 = theKing.x + 1;

    while (y1 >= 0 && x1 < 8) {
      Point tPoint = new Point(x1, y1);

      // if it runs into where this pieces is going break
      if (tPoint.equals(end)) break;
      // if it runs into the space it used to be in continue
      else if (tPoint.equals(start)) {
        x1++;
        y1--;
        continue;
      }

      int newPiece = board.theBoard[y1][x1];

      if (theCurPiece * newPiece > 0) {
        //	System.out.println("No Check Down and Right");
        break;
      }

      if (theCurPiece * newPiece < 0) {
        newPiece = Math.abs(newPiece);
        if (newPiece == Board.BISHOP
            || newPiece == Board.QUEEN
            || newPiece == Board.KING
            || newPiece == Board.PAWN) {
          if (newPiece == Board.KING || newPiece == Board.PAWN) {
            if (Math.abs((x1 - theKing.x) * (y1 - theKing.y)) == 1) {
              theResult = true;
            } else {
              break;
            }
          } else {
            theResult = true;
          }
        } else {
          break;
        }
      }

      x1++;
      y1--;
    }

    // Down Left

    y1 = theKing.y - 1;
    x1 = theKing.x - 1;

    while (y1 >= 0 && x1 >= 0) {
      Point tPoint = new Point(x1, y1);

      // if it runs into where this pieces is going break
      if (tPoint.equals(end)) break;
      // if it runs into the space it used to be in continue
      else if (tPoint.equals(start)) {
        x1--;
        y1--;
        continue;
      }

      int newPiece = board.theBoard[y1][x1];

      if (theCurPiece * newPiece > 0) {
        //	System.out.println("No Check Down and Left");
        break;
      }

      if (theCurPiece * newPiece < 0) {
        newPiece = Math.abs(newPiece);
        if (newPiece == Board.BISHOP
            || newPiece == Board.QUEEN
            || newPiece == Board.KING
            || newPiece == Board.PAWN) {
          if (newPiece == Board.KING || newPiece == Board.PAWN) {
            if (Math.abs((x1 - theKing.x) * (y1 - theKing.y)) == 1) {
              theResult = true;
            } else {
              break;
            }
          } else {
            theResult = true;
          }
        } else {
          break;
        }
      }

      x1--;
      y1--;
    }

    // Up Left

    y1 = theKing.y + 1;
    x1 = theKing.x - 1;

    while (y1 < 8 && x1 >= 0) {
      Point tPoint = new Point(x1, y1);

      // if it runs into where this pieces is going break
      if (tPoint.equals(end)) break;
      // if it runs into the space it used to be in continue
      else if (tPoint.equals(start)) {
        x1--;
        y1++;
        continue;
      }

      int newPiece = board.theBoard[y1][x1];

      if (theCurPiece * newPiece > 0) {
        // System.out.println("No Check Up and Left");
        break;
      }

      if (theCurPiece * newPiece < 0) {
        newPiece = Math.abs(newPiece);
        if (newPiece == Board.BISHOP
            || newPiece == Board.QUEEN
            || newPiece == Board.KING
            || newPiece == Board.PAWN) {
          if (newPiece == Board.KING || newPiece == Board.PAWN) {
            if (Math.abs((x1 - theKing.x) * (y1 - theKing.y)) == 1) {
              theResult = true;
            } else {
              break;
            }
          } else {
            theResult = true;
          }
        } else {
          break;
        }
      }

      x1--;
      y1++;
    }
    // Knight Moves---------------------------------------------------------
    //
    // These check to see if the piece at each location is a knight only if
    // the end move isn't going to replace that piece.

    // Forward 2 and left / right 1
    if (theKing.y + 2 < 8) {
      if (theKing.x + 1 < 8) {
        int thePiece = board.theBoard[theKing.y + 2][theKing.x + 1];
        if ((!end.equals(new Point(theKing.x + 1, theKing.y + 2)))
            && theCurPiece * thePiece < 0
            && Math.abs(thePiece) == Board.KNIGHT) {
          theResult = true;
        }
      }

      if (theKing.x - 1 >= 0) {
        int thePiece = board.theBoard[theKing.y + 2][theKing.x - 1];
        if ((!end.equals(new Point(theKing.x - 1, theKing.y + 2)))
            && theCurPiece * thePiece < 0
            && Math.abs(thePiece) == Board.KNIGHT) {
          theResult = true;
        }
      }
    }

    // Backward 2 and left / right 1
    if (theKing.y - 2 >= 0) {
      if (theKing.x + 1 < 8) {
        int thePiece = board.theBoard[theKing.y - 2][theKing.x + 1];
        if ((!end.equals(new Point(theKing.x + 1, theKing.y - 2)))
            && theCurPiece * thePiece < 0
            && Math.abs(thePiece) == Board.KNIGHT) {
          theResult = true;
        }
      }

      if (theKing.x - 1 >= 0) {
        int thePiece = board.theBoard[theKing.y - 2][theKing.x - 1];
        if ((!end.equals(new Point(theKing.x - 1, theKing.y - 2)))
            && theCurPiece * thePiece < 0
            && Math.abs(thePiece) == Board.KNIGHT) {
          theResult = true;
        }
      }
    }

    // Forward 1 and left / right 2
    if (theKing.y + 1 < 8) {
      if (theKing.x + 2 < 8) {
        int thePiece = board.theBoard[theKing.y + 1][theKing.x + 2];
        if ((!end.equals(new Point(theKing.x + 2, theKing.y + 1)))
            && theCurPiece * thePiece < 0
            && Math.abs(thePiece) == Board.KNIGHT) {
          theResult = true;
        }
      }

      if (theKing.x - 2 >= 0) {
        int thePiece = board.theBoard[theKing.y + 1][theKing.x - 2];
        if ((!end.equals(new Point(theKing.x - 2, theKing.y + 1)))
            && theCurPiece * thePiece < 0
            && Math.abs(thePiece) == Board.KNIGHT) {
          theResult = true;
        }
      }
    }

    // Backward 1 and left / right 2
    if (theKing.y - 1 >= 0) {
      if (theKing.x + 2 < 8) {
        int thePiece = board.theBoard[theKing.y - 1][theKing.x + 2];
        if ((!end.equals(new Point(theKing.x + 2, theKing.y - 1)))
            && theCurPiece * thePiece < 0
            && Math.abs(thePiece) == Board.KNIGHT) {
          theResult = true;
        }
      }

      if (theKing.x - 2 >= 0) {
        int thePiece = board.theBoard[theKing.y - 1][theKing.x - 2];

        if ((!end.equals(new Point(theKing.x - 2, theKing.y - 1)))
            && theCurPiece * thePiece < 0
            && Math.abs(thePiece) == Board.KNIGHT) {
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
  public boolean isBoardInCheck(Board board, int player) {
    Point tPoint = board.theKings[player];
    return checkChecker(board, tPoint, tPoint);
  }

  // Steves checkmate method
  public boolean isMate(Board board, int player) {
    int i;
    Vector<Point> feasible;
    boolean isMate = true;

    if (player == Board.WHITE) {
      for (i = 0; i < board.whitePieces.size(); i++) {
        feasible = getFeasibleMoves(board, board.whitePieces.elementAt(i));
        if (!feasible.isEmpty()) {
          isMate = false;
          break;
        }
      }
    } else {
      for (i = 0; i < board.blackPieces.size(); i++) {
        feasible = getFeasibleMoves(board, board.blackPieces.elementAt(i));
        if (!feasible.isEmpty()) {
          isMate = false;
          break;
        }
      }
    }

    return isMate;
  }

  // Check if Rook is a valid move
  public boolean canCastle(Board board, int turn) {
    Point theKing = board.theKings[turn];
    boolean retVal = false;

    if (board.castleCheck[turn] && !isBoardInCheck(board, turn)) {
      // System.out.println("Can Castle Still");
      if (board.isVacant(theKing.y, theKing.x + 1) && board.isVacant(theKing.y, theKing.x + 2)) {
        Point tPoint1 = new Point(theKing.x + 1, theKing.y);
        Point tPoint2 = new Point(theKing.x + 2, theKing.y);

        // System.out.println("Squares are free");

        if (!checkChecker(board, theKing, tPoint1)) {
          if (!checkChecker(board, theKing, tPoint2)) {
            retVal = true;
          }
        }
      }
    }

    return retVal;
  }
}
