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
  public static final int WHITE = 0;
  public static final int BLACK = 1;

  public static final int PAWN = 1;
  public static final int ROOK = 2;
  public static final int KNIGHT = 3;
  public static final int BISHOP = 4;
  public static final int QUEEN = 5;
  public static final int KING = 6;

  public static final int BLACK_MULTIPLIER = -1;

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

    for (i = 0; i < 2; i++) {
      for (int j = 0; j < 3; j++) {
        board.castleCheck[i][j] = true;
      }
    }

    // Should set the point to be non existant
    board.lastMoved[0] = new Point(-1, -1);
    board.lastMoved[1] = new Point(-1, -1);

    board.lastYDistance[0] = 0;
    board.lastYDistance[1] = 0;

    board.movesUntilDraw = 100;

    return board;
  }

  public int[][] theBoard = new int[8][8];
  public Vector<Point> blackPieces = new Vector<Point>();
  public Vector<Point> whitePieces = new Vector<Point>();

  // array to store the location of the kings
  // 0 is white
  // 1 is black
  public Point[] theKings = new Point[2];
  // public boolean[] castleCheck = new boolean[2];

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

    for (i = 0; i < castleCheck.length; i++) {
      for (j = 0; j < castleCheck[i].length; j++) {
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
    return theBoard[rank][file] == PAWN || theBoard[rank][file] == BLACK_MULTIPLIER * PAWN;
  }
}
