# Intelligent Chess

A computer chess program with a min-max AI, originally written as an undergraduate assignment by Chris Roy and Stephen Baker.

## Running
Navigate to the top directory of the repository.

On Unix like systems: `./gradlew run`

On Windows: `gradlew.bat run`


## HOW TO USE ICHESS

Once IChess is started you will be presented with a window which contains the board, a side panel, and a menu bar. On the menu bar you will see the following options **File**, **Player**, and **Difficulty**.

In File you have the following options **New Game**, **Open Game State** and **Quit**. New game will reset the board to a default layout which is the same as when you started the game. Open Game State will load a text file to set the initial positions for each piece. Each piece in that file is represented by a four character string. The first character represents the colour (w or b). The second character represents the piece, P (Pawn), R (Rook), N (Knight), B (Bishop), Q (Queen), and K (King). The last two characters are the piece location in the form XY, where X is A-H and Y is 1-8. These correspond to the X and Y values on the board starting from the bottom left corner. The last option is Quit which will shut down the program.

In Player you have two options **white is computer** and **black is computer**. These options will set which colour is using the AI and which is a human. By default both are not checked which indicates that both black and white are human players. You can set either colour to be human or computer at any point during the game by clicking on the corresponding option.

The last menu item is Difficulty. In this menu you can set the strength of the AI from the following options **Naïve**, **Easy**, **Intermediate**, **Hard**, or **Very Hard**. By default the strength is set to intermediate. Like the Player options you can change the strength of the AI during game play.

### Playing a Game
To move a piece you click on it and if it has any valid moves they will be highlighted in green or red. Green indicates the square is currently empty and Red indicates you will make a capture if you move there. The current piece you have selected will display as blue. If no moves are valid the piece will just be highlighted blue.

### Special Moves
En Passant is represented by a red highlight behind the pawn you would be capturing en passant. This is the only time you will see a red highlight over top of an empty square.

Castling can be done as per the rules of chess to either the left or the right of the king. If castling is possible the square will be highlighted in purple and once clicked both the king and the appropriate rook will move to complete the castle.

### Check
If your king is put into check an indicator in the side bar will light up to indicate which colour is currently in check. If you are in check the only valid moves that will show up when you click on a piece would be those that would get the king out of check.

### Checkmate or Stalemate
If either checkmate or stalemate occurs the end game dialog will be displayed which will give you the option to **Play** again, **Save** the list of moves, or **Quit** the program.

### 50 Move Rule
The program also adheres to the 50 move rule in chess in which if no irreversible move is made in 50 consecutive moves the game is considered a tie. An irreversible move is one in which a pawn is moved, or a piece is captured.

### Unimplemented Rules
* Dead position: If there is no sequence of moves left that would result in checkmate the game ends in a tie.
* Threefold position: If all the pieces end up in the same position three times in a game, a player can call a draw.
* Time control

## ARTIFICIAL INTELLIGENCE

### Alpha-Beta
A list of all the legal moves which can be made from the current board state is made. Each of these is then evaluated using an alpha-beta search to see which of the moves is best for the computer. Alpha-beta does a depth first search from the board state, keeping at each node an alpha (smallest) and beta (largest) value. If at a minimizing step a child has a lower value than its alpha this value is returned to it and no more of its children are evaluated, otherwise the minimum of its child's values are returned. Likewise for a maximizing node if a child has a higher value then its beta this value is returned, and otherwise the maximum of its child's values are returned. The alpha's and beta's at every step are inherited from their parents and the bounds between them are shrunk as children are evaluated to eliminate evaluating nodes in the tree that will be irrelevant because the move would not be chosen by an optimal player.

### Terminating Condition
The explicit terminating conditions are when the future turn value (the turn of the move it is looking ahead to) is greater than the current real turn plus the set ply (dependent upon difficulty), and when the board state its looking ahead to is the same as the current board state. Other implicit terminating conditions are when a player is in mate; that is that there are no legal moves left for them.

### Board Evaluation
If there are no valid moves which can be made from a board state which is not already a leaf node in the search tree then a check is made to see if the player who cannot move in this board state is in check. If so and it is white who cannot make a move the board is given the highest possible evaluation score (`Integer.MAX_VALUE - 1`). Likewise if it is black that cannot move and is in check that board is given the lowest possible evaluation score (`Integer.MIN_VALUE + 1`). If on the other hand the player is not in check (it is a stalemate) the board is evaluated to 0, a value not particularly good for either player. If the look-ahead board is the same as the current board: ie. All the pieces are in the same place, for example, moving the knight out and then back to its original location at the beginning of the game then this board is evaluated poorly for the player making the move. If it is getting close to the no capture/no pawn move draw condition this too evaluates poorly for whichever player's turn it currently is. Those being the special conditions for board evaluation, there are a number of other things which are taken into account. Each white piece is given a weight: pawn, bishop, knight, rook, queen, king respectively, and each of these pieces on the board increase the evaluated value by the weight of the piece. Similarly, each black piece is subtracted from the evaluation value by the same weights. Controlling the center of the board, moving pawns forward, and having a larger number of possible moves for the king, especially by not having pawns around it, are also favourable for the player, though to a lesser degree than the weights of the pieces themselves. 
