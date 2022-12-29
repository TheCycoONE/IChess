# IChess

A computer chess program with a min-max AI, originally written as an undergraduate assignment by Chris Roy and Stephen Baker.

## Running
Navigate to the top directory of the repository.

On Unix like systems: `./gradlew run`

On Windows: `gradlew.bat run`

## Rules of Chess
* [x] Board Setup
    * In the *1* rank from left to right white: Rook, Knight, Bishop, Queen, King, Bishop, Knight, Rook
    * In the *2* rank white pawns in every file
    * In the *7* rank black pawns in every file
    * In the *8* rank from left to right black: Rook, Knight, Bishop, Queen, King, Bishop, Knight, Rook
* [x] White moves first.
* [x] King moves one square in any direction.
* [x] Rook moves any number of squares in it's rank or file but cannot jump.
* [x] Bishop moves any number of squares diagonally but cannot jump.
* [x] Queen moves any number of squares in rank or file or diagonal but cannot jump.
* [x] Pawn moves one square forward as long as space is unoccupied (cannot capture forward).
* [x] Pawn can move two squares forward on it's first move as long as neither square is occupied.
* [x] Pawn can capture on the foward diagonals one square.
* [ ] En Passant: If a pawn moves forward two squares landing beside a pawn of the opposite player. On the next turn the player can move their pawn on the forward diagonal behind the pawn that just moved and capture it.
* [x] Promotion: A pawn on reaching the far rank from where it started, becomes a queen, knight, bishop, or rook at the choice of the player.
* [x] Check: When a king could be captured in the next turn they are in check. The only legal move at that point is to get the king out of check.
* [x] The King cannot move themselves into Check
* [x] Checkmate: If the king is in check and cannot be moved out of check, the opposing player wins.
* [~] Castling:
    * [x] The King moves two squares towards the Rook
    * [x] The Rook moves to the position the King passed over
    * [x] Kingside Castling (O-O) when the King moves towards the closer Rook
    * [ ] Queenside Castling (O-O-O) when the King moves towards the further (queen's side) Rook
    * [x] Cannot Castle when the King is in Check
    * [x] King cannot pass through a square that would be attacked
    * [x] Cannot castle if there are any pieces between the King and the Rook
    * [x] Cannot castle if the King has moved previously
    * [~] Cannot castle if the Rook has moved previously (current implementation bug requires neither rook to have moved)
* [x] Stalemate: If the player has no legal moves and is not in check then the game ends in a tie.
* [ ] Dead position: If there is no sequence of moves left that would result in checkmate the game ends in a tie.
* [ ] Threefold position: If all the pieces end up in the same position three times in a game, a player can call a draw.
* [ ] Fifty Move Rule: If no pawn is advanced and no piece is captured in 50 moves, a player can call a draw.
* [ ] Time control
