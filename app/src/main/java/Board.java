import java.awt.Point;
import java.util.Vector;

/*Board class: responsible for storing a representation of a chess board state
 *
 *theBoard -	An array giving a 1 to 1 representation of the squares in a chess
 *			 	board.
 *blackPieces -	A vector of the location of all black pieces
 *whitePieces - A vector of the location of all white pieces
 *theKings -	An array of the location of the kings
 *castleCheck -	boolean values to determine if castling is still possible
 */
public class Board {
  public static Board createInitialBoard() {
    int i;
    Board board = new Board();

    // Set up pawns
    for (i = 0; i < 8; i++) {
      board.theBoard[1][i] = 1;
      board.whitePieces.add(new Point(i, 1));

      board.theBoard[6][i] = -1;
      board.blackPieces.add(new Point(i, 6));
    }

    // Left side + king
    for (i = 0; i <= 4; i++) {
      board.theBoard[0][i] = i + 2;
      board.whitePieces.add(new Point(i, 0));

      board.theBoard[7][i] = -(i + 2);
      board.blackPieces.add(new Point(i, 7));
    }

    // Right side
    for (i = 5; i < 8; i++) {
      board.theBoard[0][i] = 9 - i;
      board.whitePieces.add(new Point(i, 0));

      board.theBoard[7][i] = -(9 - i);
      board.blackPieces.add(new Point(i, 7));
    }

    board.theKings[0] = new Point(4, 0);
    board.theKings[1] = new Point(4, 7);

    board.castleCheck[0] = true;
    board.castleCheck[1] = true;

    return board;
  }

  public int[][] theBoard = new int[8][8];
  public Vector<Point> blackPieces = new Vector<Point>();
  public Vector<Point> whitePieces = new Vector<Point>();

  // 0 is white
  // 1 is black
  public Point[] theKings = new Point[2];
  public boolean[] castleCheck = new boolean[2];

  /*A method to perform a deep copy of the current board state*/
  public Board clone() {
    Board clone = new Board();
    int i, j;

    // Clones the board array
    for (i = 0; i < 8; i++) {
      for (j = 0; j < 8; j++) {
        ((Board) clone).theBoard[i][j] = this.theBoard[i][j];
      }
    }

    // Clones the black piece vector
    for (i = 0; i < this.blackPieces.size(); i++) {
      clone.blackPieces.add(this.blackPieces.elementAt(i));
    }

    // Clones the white piece vector
    for (i = 0; i < this.whitePieces.size(); i++) {
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

  public boolean isBlack(int rank, int file) {
    return theBoard[rank][file] < 0;
  }

  public boolean isWhite(int rank, int file) {
    return theBoard[rank][file] > 0;
  }

  public boolean isVacant(int rank, int file) {
    return theBoard[rank][file] == 0;
  }

  public boolean isPawn(int rank, int file) {
    return theBoard[rank][file] == 1 || theBoard[rank][file] == -1;
  }
}
