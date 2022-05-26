package chess;
import java.util.*;
import java.util.Scanner;

public class Chess {
    static Scanner keyIn = new Scanner (System.in);
    static int turn = 0;
	static int row, row2;
	static int col, col2;
	static int theRow, theCol;
	static int okingCol = 0;
	static int okingRow = 0;
	static int partOfTurn;
	static int kingRow;
	static int kingCol;
	static int dieEnPesRow = 9, dieEnPesCol = 9;
	static int trueKingRow, trueKingCol;
	static int checkRow, checkCol;
	static int trueCheckRow, trueCheckCol;
	static int totalFlips = 0;
	static int autoFlips = 0;
	static String coordRow, coordCol;
	static String coords;
	static String checkType;
	static String colorToCare;
	static String toKillAPiece = "test";
    static Object currentPiece = null;
    static Object pieceInWay = null;
    static Object savePiece = null;
    static Object saveKing = null;
	static Object saveCurrent;
	static Object savePIW;
	static Object saveEnPes = null;
	static Object capturedPiece = null;
	static Object checkingPiece = null;
	static Object[][] showBoard = new Object[24][8];
	static boolean checkBlocks = true;
	static boolean onAPiece = false;
	static boolean offColor = false;
	static boolean inCheck = false;
	static boolean checkMated = false;
	static boolean canMove = true;
	static boolean trueInCheck = false;
	static boolean canUndo = true;
	static boolean canFlip = true;
	static boolean isFlipped = false;
	static boolean autoFlipped = false;
    static boolean BlackKingHasMoved, WhiteKingHasMoved = false;
    static boolean BlackLeftRookHasMoved, WhiteLeftRookHasMoved = false;
    static boolean BlackRightRookHasMoved, WhiteRightRookHasMoved = false;
    
	static Object[][] board = new Object[8][8];
	public static void setBoard() {
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				board[r][c] = new Empty("__");
			}
		}
		board[7][3] = new King("wK", "white");
		board[0][3] = new King("bK", "black");
		board[7][0] = new Rook("wR","white");
		board[7][7] = new Rook("wR", "white");
		board[0][0] = new Rook("bR", "black");
		board[0][7] = new Rook("bR", "black");
		board[7][1] = new Knight("wN", "white");
		board[7][6] = new Knight("wN", "white");
		board[0][1] = new Knight("bN", "black");
		board[0][6] = new Knight("bN", "black");
		board[7][2] = new Bishop("wB", "white");
		board[7][5] = new Bishop("wB", "white");
		board[0][2] = new Bishop("bB", "black");
		board[0][5] = new Bishop("bB", "black");
		board[7][4] = new Queen("wQ", "white");
		board[0][4] = new Queen("bQ", "black");
		Object obj = new Queen("test", "black");
		for (int c = 0; c < 8; c++) {
			board[6][c] = new Pawn("wP", "white");
		}
		for (int c = 0; c < 8; c++) {
			board[1][c] = new Pawn("bP", "black");
		} 
	}
	public static void display() {
		System.out.print("  ______________________________");
		for (int r = 0; r < 8; r++) {
			
			if (isFlipped == false) {
				System.out.print("\n" + (r+1));
			}
			else {
				System.out.print("\n" + (8-r));
			}
			
			for (int c = 0; c < 8; c++) {
				System.out.print("|");
				System.out.print(board[r][c]);
				System.out.print("|");
			}
		}
		System.out.println();
		System.out.println(" --------------------------------");
		if (isFlipped == false) {
			System.out.println("  A   B   C   D   E   F   G   H");
			System.out.println("  0   1   2   3   4   5   6   7");
		}
		else {
			System.out.println("  H   G   F   E   D   C   B   A");
			System.out.println("  7   6   5   4   3   2   1   0");
		}
	}
	public static boolean checkSame() {
		if (row == row2 && col == col2) {
			System.out.println("You cannot move a piece to the square it is already on. Re-select a piece.");
			display();
			return true;
		}
		else {
			return false;
		}
	}
	public static boolean checkPos(int r, int c) {
		if (r < 0 || c < 0) {
	//		System.out.println("No negative coordinates please.");
		//	startTurn();
			return false;
		}
		Object placeHolder;
		try {
		placeHolder = board[r][c];
		board[r][c] = new Empty("__");
	//	System.out.println("Testing to see if coordinates are on board");
		}
		catch (ArrayIndexOutOfBoundsException j) {
	//		System.out.println("Invalid Coordinates, please retry");
//			board[r][c] = "__";
	//		startTurn();
			return false;
		}
		board[r][c] = placeHolder;
	//	System.out.println("Coordinates exist, continue");
		return true;
	}
	
	public static void movePiece() {
		System.out.println("mvP.running movePiece");
		System.out.println("mvP.Currentpiece class is " + currentPiece.getClass());		
		savePiece = board[row2][col2];
		board[row2][col2] = currentPiece;
		board[row][col] = new Empty("__");
		System.out.println("mvP.displaying current board state (1) .");
		display();
		
		if (autoFlipped == true) {
			System.out.println("mvP.autoflipped is true, autoflipping and setting autoFlipped to false.");
			autoFlip();
			System.out.println("mvP.displaying current board state (2) .");
			display();
			System.out.println("mvP.end of autoflipping sequence.");
			autoFlipped = false;
		}
	}
	
	public static void unMove() {
		System.out.println("running unMove");
		board[row][col] = currentPiece;
		board[row2][col2] = savePiece;
		display();
	}
	public static boolean isEmpty(int r, int c) {
		if (board[r][c].getClass().equals(Empty.class)) {
//			System.out.println("Space is Empty at " + convertNumber(c) + (r+1));
			return true;
		}
		else {
			return false;
		}
	}
	public static boolean sameTeam() {
		if (getColor(row2, col2, turn) == true) {
			System.out.println("You cannot move onto your own piece");
			return true;
		}
		else {
			return false;
		}
	}
		
	public static String getTurn(int turn) {
        if (turn %2 == 0) {
                return "White";
            }
            else {
                return "Black";
            }
    }
	
	public static boolean getColor(int r, int c, int whichTurn) { //returns true if same color as current player's turn

		try {
			if (((Pawn) board[r][c]).getColor().equalsIgnoreCase(getTurn(whichTurn)) ) {
				pieceInWay = (Pawn) board[r][c];
				return true;
			}
		}
		catch (ClassCastException rk) {
			try {
				if (((Rook) board[r][c]).getColor().equalsIgnoreCase(getTurn(whichTurn)) ) {
					pieceInWay = (Rook) board[r][c];
					return true;
				}
			}
			catch (ClassCastException n) {
				try {
					if (((Knight) board[r][c]).getColor().equalsIgnoreCase(getTurn(whichTurn)) ) {
						pieceInWay = (Knight) board[r][c];
						return true;
					}
				}
				catch (ClassCastException b) {
					try {
						if (((Bishop) board[r][c]).getColor().equalsIgnoreCase(getTurn(whichTurn)) ) {
							pieceInWay = (Bishop) board[r][c];
							return true;
						}
					}
					catch (ClassCastException k) {
						try {
							if (((King) board[r][c]).getColor().equalsIgnoreCase(getTurn(whichTurn)) ) {
								pieceInWay = (King) board[r][c];
								return true;
							}
						}
						catch (ClassCastException q){
							try {	
								if (((Queen) board[r][c]).getColor().equalsIgnoreCase(getTurn(whichTurn)) ) {
									pieceInWay = (Queen) board[r][c];
									return true;
								}
							}
							catch (ClassCastException m) {
								pieceInWay = new Empty("__");
	//							System.out.println("That is not a piece, please retry");
	//							startTurn();
								
								return false;
							}
						}
					}
				}
			}
		}
	//	System.out.println("End of getColor");
		return false;
	}
	public static void preset() {
		System.out.println("pre.Which preset do you wish to test? Ex: Stalemate, checkmate, castle, or preventable");
		String input = keyIn.next();
		if (input.equalsIgnoreCase("stalemate")) {
			clearBoard();
			board[7][1] = new King("wK", "white");
			board[6][7] = new Rook("bR", "black");
			board[5][2] = new Queen("bQ", "black");
			board[0][7] = new King("bK", "black");
			display();
//			if (checkStale() == true) {
//				System.out.println("Stale af");
//			}
	//		startTurn();
		}
		else if (input.equalsIgnoreCase("checkMate")) {
			clearBoard();
			board[7][1] = new King("wK", "white");
			board[6][7] = new Rook("bR", "black");
			board[7][7] = new Queen("bQ", "black");
			board[0][7] = new King("bK", "black");
			display();
			if (checkMate() == true) {
				System.out.println("Mate");
			}
	//		startTurn();
		}
		else if (input.equalsIgnoreCase("castle")) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (getColor(i, j, 1) == true && ! pieceInWay.getClass().equals(Empty.class)) {
						System.out.println("pre.Black " + pieceInWay.getClass() + " located at " + convertNumber(j) + (i+1));
						if (pieceInWay.getClass().equals(Bishop.class) || 
								pieceInWay.getClass().equals(Knight.class) || 
								pieceInWay.getClass().equals(Queen.class)) {
							System.out.println("pre.Black " + pieceInWay.getClass() + " located at " + convertNumber(j) + (i+1) + " and has been removed.");
							board[i][j] = new Empty("__");
						}
					}
					else if (getColor(i, j, 2) == true && !pieceInWay.getClass().equals(Empty.class)) {
						System.out.println("pre.White " + pieceInWay.getClass() + " located at " + convertNumber(j) + (i+1));
						if (pieceInWay.getClass().equals(Bishop.class) || 
							pieceInWay.getClass().equals(Knight.class) || 
							pieceInWay.getClass().equals(Queen.class)) {
							System.out.println("pre.White " + pieceInWay.getClass() + "located at " + convertNumber(j) + (i+1) + " and has been removed.");
							board[i][j] = new Empty("__");
						}
					}
				}
			}
			display();
		}
		else {
			System.out.println("Preventable via avoid, capture, or block?");
			String input3 = keyIn.next();
			if (input3.equalsIgnoreCase("avoid")) {
				clearBoard();
				board[7][1] = new King("wK", "white");
	//			board[6][7] = new Rook("bR", "black");
				board[7][7] = new Queen("bQ", "black");
				board[0][7] = new King("bK", "black");
				display();
				if (checkMate() == true) {
					System.out.println("avoid fail Mate");
				}
//				startTurn();
			}
			else if (input3.equalsIgnoreCase("capture")) {
				clearBoard();
				board[7][1] = new King("wK", "white");
				board[6][7] = new Rook("bR", "black");
				board[7][4] = new Queen("bQ", "black");
				board[0][7] = new King("bK", "black");
				System.out.println("Rook, Bishop, Queen, Knight, Pawn?");
				String input4 = keyIn.next();
				if (input4.equalsIgnoreCase("Rook")) {
					board[2][4] = new Rook("wR", "white");
				}
				else if (input4.equalsIgnoreCase("Bishop")) {
					board[4][1] = new Bishop("wB", "white");
				}
				else if (input4.equalsIgnoreCase("Queen")) {
					System.out.println("As a Rook or Bishop");
					String input5 = keyIn.next();
					if (input5.equalsIgnoreCase("Rook")) {
						board[2][4] = new Queen("wQ", "white");
					}
					else {
						board[4][1] = new Queen("wQ", "white");
					}
				}
				else if (input4.equalsIgnoreCase("Knight")) {
					board[5][3] = new Knight("wN", "white");
				}
				else {
					System.out.println("Black or White pawn?");
					String input6 = keyIn.next();
					clearBoard();
					if (input6.equalsIgnoreCase("white")) {
						board[0][0] = new King("wK", "white");
						board[1][7] = new Rook("bR", "black");
						board[7][1] = new Rook ("bR", "black");
						board[3][3] = new Queen("bQ", "black");
						board[4][4] = new Pawn("wP", "white");
						board[7][7] = new King("bK", "black");
					}
					else {
						turn += 1;
						board[7][7] = new King("bK", "black");
						board[6][0] = new Rook("wR", "white");
						board[0][6] = new Rook ("wR", "white");
						board[4][4] = new Queen("wQ", "white");
						board[3][3] = new Pawn("bP", "black");
						board[0][0] = new King("wK", "white");
					}
				}
				display();
				if (checkMate() == true) {
					System.out.println("capture fail mate");
				}
	//			startTurn();
			}
			else {
				clearBoard();
				board[0][0] = new King("wK", "white");
				board[1][7] = new Rook("bR", "black");
				board[7][1] = new Rook ("bR", "black");
				board[3][3] = new Queen("bQ", "black");
				board[7][7] = new King("bK", "black");
				board[3][2] = new Pawn("wP", "white");
				display();
				if (checkMate() == true) {
					System.out.println("block fail mate");
				}
	//			startTurn();
			}
		}
//		unMove();
		System.out.println("pre.Preset Successfully completed, starting turn.");
		display();
		startTurn();
	}
	public static void clearBoard() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = new Empty("__");
			}
		}
	}
	public static void list() {
		System.out.println("aBC: autoBishopCheck" + '\n' + "aLg: autoLegality" + '\n' + "aRC: autoRookCheck" + '\n' + "bsC: bishopCheck" + '\n' + 
		"chA: checkAvoid" + '\n' + "chB: checkBlock" + '\n' + "chC: checkCapture"  + '\n' + "chM: checkMate" + '\n' +   "chS: checkStale"+ '\n' + 
		"cnT: continueTurn" + '\n' + "csl: castle" + '\n' + "kgF: kingFinder" + '\n' + "kgS: kingSafe" + '\n' + "mvP: movePiece" + '\n' +  
		"pre: preset" + '\n' + "prm: promote" +  '\n' + "rkC: rookCheck" + '\n' + "sCr: safeCrossing" + '\n' + "shw: showMe" + '\n' + "stT: startTurn");
	}
	public static void doFlip() {
		System.out.println("Would you like the board to flip after each player's turn (y/n)? You can also manually flip the board by typing \"flip\" at any time.");
		String toFlip = keyIn.next();
		if (toFlip.equalsIgnoreCase("y") || toFlip.equalsIgnoreCase("yes")) {
			canFlip = true;
		}
		else {
			canFlip = false;
		}
	}
	public static void startTurn() {
		partOfTurn = 1;
	//	System.out.println();
//		System.out.println("Start check for dieEnPes " + dieEnPesRow + ", " + dieEnPesCol);
		System.out.println();
		getTurn(turn);
		System.out.println("stT.Current Player: " + getTurn(turn));
		if (turn %2 == 0 && isFlipped == true) {
            Flip();
        }
		updateShowBoard();
		showDisplay();
		if (kingSafe() == false) {
			System.out.println("stT.You may be in check");
			if (checkMate() == true) {
				mate();
			}
			System.out.println("stT.but not mate");
			display();
			updateShowBoard();
			showDisplay();
		}
		
		System.out.println("stT.Enter a LETTER followed by a NUMBER to indicate a piece on the board you wish to move, say CASTLE to attempt a castle, say SHOW to see the moves of piece, or UNDO to undo the previous move.");
		coords = keyIn.next();
		if (coords.equalsIgnoreCase("SHOW")) {
			System.out.println("stT.Please indicate the board space you wish to see the moves from");
			String checkCoords = keyIn.next();
			row = theRow;
			col = theCol;
			betterCoords(checkCoords);
			showMe();
			startTurn();
		}
		
		if (coords.equalsIgnoreCase("preset")) {
			preset();
		}
		if (coords.equalsIgnoreCase("castle")) {
			castle();
			continueTurn();
		}
		if (coords.equalsIgnoreCase("list")) {
			list();
			display();
			startTurn();
		}
		if (coords.equalsIgnoreCase("undo")) {
			undo();
		}
		if (coords.equalsIgnoreCase("variables")) {
			variableCheck();
			startTurn();
		}
		if (coords.equalsIgnoreCase("flip")) {
			System.out.println("stT.Starting Flip");
			Flip();
			display();
			startTurn();
		}
		betterCoords(coords);
		
		if (isFlipped == false) {
			row = theRow;
			col = theCol;
		}
		else {
			System.out.println("stT.board is flipped, correcting coordinates.");
			row = 7-theRow;
			col = 7-theCol;
			System.out.println("stT.Row: " + row + "\n" + "col: " + col);
			System.out.println("stT." + convertNumber(col) + (row+1));			
		}	
		if (checkPos(row, col) == false) {
			System.out.println("stT.Error, give legal coords");
			startTurn();
		}
		if (isEmpty(row, col) == true) {
			System.out.println("stT.Please select a piece");
			startTurn();
		}
		if (getColor(row, col, turn) == true) {
			currentPiece = pieceInWay;
			System.out.println("stT.Moving a " + currentPiece.getClass());
			
			if (isFlipped == true) {
				System.out.println("stT.row and col position is being corrected to unflipped board positions.");
				row = 7-row;
				col = 7-col;
				System.out.println("stT.Row: " + row + "\n" + "col: " + col);
				System.out.println("stT." + convertNumber(col) + (row+1));
			}
			continueTurn();
		}
		else {
			System.out.println("stT.Not that player's turn.");
			startTurn();
		}
	
	}
	
	public static void continueTurn() {
	/*	if (isFlipped == true) {
			System.out.println("cnT.The board is currently flipped, and has been corrected to its default position.");
			System.out.println("cnT.The default position is: isFlipped: " + isFlipped);		
			autoFlip();
			autoFlipped = false;
		} */
		partOfTurn = 2;
		System.out.println("cnT.Enter a LETTER followed by a NUMBER to indicate the place on the board you wish to move to.");
		coords = keyIn.next();
		if (coords.equalsIgnoreCase("variables")) {
			variableCheck();
			continueTurn();
		}
		else {
			betterCoords(coords);
			row2 = theRow;
			col2 = theCol;
		}
/*		else {
			System.out.println("cnT.board is flipped, correcting coordinates.");
			row2 = 7-theRow;
			col2 = 7-theCol;
			System.out.println("cnT.Row2: " + row2 + "\n" + "col2: " + col2);
			System.out.println("cnT." + convertNumber(col2) + (row2+1));
*/
			if (isFlipped == true) {
				autoFlip();
				autoFlipped = true;
				System.out.println("cnT.Displaying current autoflipped board.");
				display();
				System.out.println("cnT.Done Display.");
			}
			
		if (checkPos(row2, col2) == false) {
			System.out.println("cnT.Invalid Coordinate choice, retry");
			continueTurn();
		}
		if (checkSame() == true) {
			if (checkStale() == true) {
				System.exit(0);
			}
			startTurn();
		}
		if (sameTeam() == true) {
			continueTurn();
		}
		//pawn
		if (currentPiece.getClass().equals(Pawn.class)) {
//			System.out.println("Moving a pawn");
			if (getTurn(turn).equalsIgnoreCase("white")) {
	//			System.out.println("That is white");
					if (row == (row2 + 1) && col == col2) {
		//				System.out.println("One space up");
						if (isEmpty(row2, col2) == true) {
//							dieEnPesRow = 9;	dieEnPesCol = 9;
							movePiece();	
						}
						else {
							System.out.println("cnT.A piece is blocking the pawn, choose a new piece.");
							startTurn();
						}
					}
					else if (row == 6 && (row == (row2 + 2) && col == col2)){
	//					System.out.println("Two spaces up");
						for (int i = (row + 1); i <= row2; i++) {
							if (isEmpty(i, col) == false) {
								System.out.println("cnT.Error, a piece is blocking the Pawn at coordinates: " + i + ", " + col);
			//					System.out.println("This is where the pawn issue is (part 4)");
								continueTurn();
							}
						}
						if (toKillAPiece.equalsIgnoreCase("white")) {
		//					dieEnPesRow = 9;
		//					dieEnPesCol = 9;
						}
						if (checkPos(row2, col2+1) == true && isEmpty(row2, col2+1) == false && getColor(row2, col2+1, turn+1) == true && pieceInWay.getClass().equals(Pawn.class)) {
							System.out.println("cnT.The Piece at " + convertNumber(col2+1) + (row2+1) + " can En Peseant next turn");
							dieEnPesRow = row2+1;
							dieEnPesCol = col2;
							toKillAPiece = "black";
							System.out.println("cnT.dieEnPesRow & col: " + dieEnPesRow + ", " + dieEnPesCol);
							System.out.println("cnT.The Piece at " + convertNumber(col2) + (row2+2) + " would die");
						}
						if (checkPos(row2, col2-1) == true && isEmpty(row2, col2-1) == false && getColor(row2, col2-1, turn+1) == true && pieceInWay.getClass().equals(Pawn.class)) {
							System.out.println("cnT.The Piece at " + convertNumber(col2-1) + (row2+1) + " can En Peseant next turn");
							dieEnPesRow = row2+1;
							dieEnPesCol = col2;
							toKillAPiece = "black";
							System.out.println("cnT.dieEnPesRow & col: " + dieEnPesRow + ", " + dieEnPesCol);
							System.out.println("cnT.The 2Piece at " + convertNumber(col2) + (row2+2) + " would die");
						}
						savePiece = board[row2][col2];
						movePiece();
					}
					else if ((row == row2+1) && Math.abs(col-col2) == 1 && sameTeam() == false && isEmpty(row2, col2) == false) {
						System.out.println("cnT.Pawn capture!");
	//					dieEnPesRow = 9;	dieEnPesCol = 9;
						movePiece();
					}
					else if ((row == row2+1) && Math.abs(col-col2) == 1 && row2 == dieEnPesRow && col2 == dieEnPesCol) {
						System.out.println("cnT.En Peseant, you darn bug tester. There's no way this actually happened in a real game");
						movePiece();
						board[dieEnPesRow+1][dieEnPesCol] = new Empty("__");
					}
					else {
						System.out.println("cnT.Not a legal move for a pawn");
						continueTurn();
					}
			}
			else { //black pawn
				if (row == (row2 - 1) && col == col2) {
					if (isEmpty(row2, col2) == true) {
	//					dieEnPesRow = 9;	dieEnPesCol = 9;
						movePiece();
					}
					else {
						System.out.println("cnT.A piece is blocking the pawn, choose a new piece.");
						startTurn();
					}
				}
				else if (row == 1 && (row == (row2 - 2) && col == col2)){
					for (int i = (row + 1); i <= row2; i++) {
						if (isEmpty(i, col) == false) {
							System.out.println("cnT.Error, a piece is blocking the Pawn at coordinates: " + i + ", " + col);
			//				System.out.println("This is where the pawn issue is (part 4)");
							continueTurn();
						}
					}
					if (toKillAPiece.equalsIgnoreCase("black")) {
	//					dieEnPesRow = 9;
	//					dieEnPesCol = 9;
					}
					if (checkPos(row2, col2+1) == true && isEmpty(row2, col2+1) == false && getColor(row2, col2+1, turn+1) == true && pieceInWay.getClass().equals(Pawn.class)) {
						System.out.println("cnT.The Piece at " + convertNumber(col2+1) + (row2+1) + " can En Peseant next turn");
						dieEnPesRow = row2-1;
						dieEnPesCol = col2;
						toKillAPiece = "white";
						System.out.println("cnT.dieEnPesRow & col: " + dieEnPesRow + ", " + dieEnPesCol);
						System.out.println("cnT.The 3Piece at " + convertNumber(col2) + (row2) + " would die");
					}
					if (checkPos(row2, col2-1) == true && isEmpty(row2, col2+-1) == false && getColor(row2, col2-1, turn+1) == true && pieceInWay.getClass().equals(Pawn.class)) {
						System.out.println("cnT.The Piece at " + convertNumber(col2-1) + (row2+1) + " can En Peseant next turn");
						dieEnPesRow = row2-1;
						dieEnPesCol = col2;
						toKillAPiece = "white";
						System.out.println("cnT.dieEnPesRow & col: " + dieEnPesRow + ", " + dieEnPesCol);
						System.out.println("cnT.The 4Piece at " + convertNumber(col2) + (row2) + " would die");
					}
					savePiece = board[row2][col2];
	//				dieEnPesRow = 9;	dieEnPesCol = 9;

					movePiece();
				}
				else if ((row == row2-1) && Math.abs(col-col2) == 1 && sameTeam() == false && isEmpty(row2, col2) == false) {
//					dieEnPesRow = 9;	dieEnPesCol = 9;
					System.out.println("cnT.Pawn capture!");
					movePiece();
				}
				else if ((row == row2-1) && Math.abs(col-col2) == 1 && row2 == dieEnPesRow && col2 == dieEnPesCol) {
					System.out.println("cnT.En Peseant, you darn bug tester. There's no way this actually happened in a real game");
					movePiece();
					board[dieEnPesRow-1][dieEnPesCol] = new Empty("__");
				}
				else {
					System.out.println("cnT.Not a legal move for a pawn");
					continueTurn();
				}
			}
			if (row2 == 0 || row2 == 7) {
				promote();
			}
		} 
		//knight
		else if (currentPiece.getClass().equals(Knight.class)) {
//			dieEnPesRow = 9;	dieEnPesCol = 9;
			if ( ( Math.abs(row2-row) == 2 && Math.abs(col2-col) == 1 )|| ( Math.abs(row2-row) == 1 && Math.abs(col2-col) == 2 )  && sameTeam() == false) {
				if (sameTeam() == false) {
					movePiece();
				}
				else {
					System.out.println("cnT.Error, same team piece");
					continueTurn();
				}
			}
			else {
				System.out.println("cnT.Not a legal move for a knight, use an L");
				continueTurn();
			}
		} 
		//rook
		else if (currentPiece.getClass().equals(Rook.class)) {		
//			dieEnPesRow = 9;	dieEnPesCol = 9;
			rookCheck();
		} 
		//Bishop
		else if (currentPiece.getClass().equals(Bishop.class)) {
//			dieEnPesRow = 9;	dieEnPesCol = 9;
			bishopCheck();
		} 
		//King
		else if (currentPiece.getClass().equals(King.class)) {
//			dieEnPesRow = 9;	dieEnPesCol = 9;	 
			if ((Math.abs(row-row2) == 1 || Math.abs(col2-col) == 1) && sameTeam() == false) {
				if (getTurn(turn).equalsIgnoreCase("white")) {
					WhiteKingHasMoved = true;
				}
				else {
					BlackKingHasMoved = true;
				}
				movePiece();
			}
			else {
				System.out.println("cnT.Error, illegal move for a King. Retry coordinates.");
				continueTurn();
			}
		}
		else if (currentPiece.getClass().equals(Queen.class)) {
			if (row == row2 || col == col2) {
				rookCheck();
			}
			else {
				bishopCheck();
			}
		}
		if (kingSafe() == false) {
			System.out.println("cnT.Try a different move to prevent the check");
			trueInCheck = true;
			if (getTurn(turn).equalsIgnoreCase("white")) {
				WhiteKingHasMoved = false;
			}
			else {
				BlackKingHasMoved = false;
			}
			unMove();
			display();
			startTurn();
		}	
		if (toKillAPiece.equalsIgnoreCase(getTurn(turn))) {
			dieEnPesRow = 9;	dieEnPesCol = 9;	 
		}
		endTurn();
	}
	public static void kingFinder(int whichTurn) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (getColor(i, j, whichTurn) == true) {
					if (pieceInWay.getClass().equals(King.class)) {
						kingRow = i;
						kingCol = j;
						System.out.println("kgF." + colorToCare + " King is at " + convertNumber(j) + (i+1));
					}
				}
			}
		}
	}
	public static boolean kingSafe() {
		System.out.println("kgS.Checking kingSafe");
		colorToCare = getTurn(turn);
		saveCurrent = currentPiece;
		savePIW = pieceInWay;
		kingRow = 1;
		kingCol = 1;
		checkBlocks = true;
		System.out.println("kgS.CR0: " + checkRow);
		System.out.println("kgS.CR0: " + checkCol);
		System.out.println("kgS.TCR0: " + trueCheckRow);
		System.out.println("kgS.TCC0: " + trueCheckCol);
		kingFinder(turn);
	//rook or queen check from the left
		System.out.println("kgS.Initiating L rook check");
		for (int i = 0; i < kingCol; i++) {
			if (getColor(kingRow, i, turn+1) == true && (pieceInWay.getClass().equals(Rook.class) || pieceInWay.getClass().equals(Queen.class))) {
				checkBlocks = false;
//				System.out.println("A " + getTurn(turn+1) + " rook or queen is to the left of the king");
				for (int j = i+1; j < kingCol; j++) {
					if (isEmpty(kingRow, j) == false) {
						checkBlocks = true;
//						System.out.println("There is not a block at: " + convertNumber(j) + (kingRow+1));
					}	
				}
			}
			if (checkBlocks == false) {
				System.out.println("kgS.Your king would be IN CHECK from a piece on " + convertNumber(i) + (kingRow+1));
				checkType = "Lrook";
				checkRow = kingRow;
				checkCol = i;
				pieceInWay = savePIW;
				currentPiece = saveCurrent;
				trueInCheck = true;
				return false;
			}
		}
	//rook or queen check from the right
		System.out.println("kgS.Initiating R rook check");
		for (int i = 7; i > kingCol; i--) {
			if (getColor(kingRow, i, turn+1) == true && (pieceInWay.getClass().equals(Rook.class) || pieceInWay.getClass().equals(Queen.class))) {
				checkBlocks = false;
//				System.out.println("A " + getTurn(turn+1) + " rook or queen is to the right of the king");
				for (int j = i-1; j > kingCol; j--) {
					if (isEmpty(kingRow, j) == false) {
						checkBlocks = true;
//						System.out.println("There is not a block at: " + convertNumber(j) + (kingRow+1));
					}	
				}
			}
			if (checkBlocks == false) {
				checkType = "Rrook";
				System.out.println("kgS.Your king would be IN CHECK from a piece on " + convertNumber(i) + (kingRow + 1));
				pieceInWay = savePIW;
				checkRow = kingRow;
				checkCol = i;
				currentPiece = saveCurrent;
				return false;
			}
		}
	//rook or queen check from the top
		System.out.println("kgS.Initiating U rook check");
		for (int i = 0; i < kingRow; i++) {
//			System.out.println("Your KingCol is: " + kingCol);
			if (getColor(i, kingCol, turn+1) == true && (pieceInWay.getClass().equals(Rook.class) || pieceInWay.getClass().equals(Queen.class))) {
				checkBlocks = false;
//				System.out.println("A " + getTurn(turn+1) + " rook or queen is above the king");
//				System.out.println("This KingCol is: " + kingCol);

				for (int j = i+1; j < kingRow; j++) {
					if (isEmpty(j, kingCol) == false) {
						checkBlocks = true;
//						System.out.println("There is not a block at: " + convertNumber(kingCol) + j);
					}	
				}
			}
			if (checkBlocks == false) {
				System.out.println("kgS.Your king would be IN CHECK from a piece on " + convertNumber(kingCol) + (i+1));
				checkType = "Urook";
				checkRow = i;
				checkCol = kingCol;
				pieceInWay = savePIW;
				currentPiece = saveCurrent;
				return false;
			}
		}
		//rook or queen check from the bottom
		System.out.println("kgS.Initiating B rook check");
		for (int i = 7; i > kingRow; i--) {

			if (getColor(i, kingCol, turn+1) == true && (pieceInWay.getClass().equals(Rook.class) || pieceInWay.getClass().equals(Queen.class))) {
				checkBlocks = false;
//				System.out.println("A " + getTurn(turn+1) + " rook or queen is below the king");
				for (int j = i-1; j > kingRow; j--) {
					if (isEmpty(j, kingCol) == false) {
						checkBlocks = true;
//						System.out.println("There is not a block at: " + convertNumber(kingCol) + j);
					}				
				}
			}
			if (checkBlocks == false) {
				System.out.println("kgS.Your king would be IN CHECK from a piece on " + convertNumber(kingCol) + (i+1));
				checkType = "Drook";
				pieceInWay = savePIW;
				checkRow = i;
				checkCol = kingCol;
				currentPiece = saveCurrent;
				return false;
			}
		}
	//Bishop or queen check top left
//		System.out.println("Initiating UL Bishop check");
		if (kingCol != 0 && kingRow != 0) {
//			System.out.println("The king is not at 0,0");
//			System.out.println("kingRow = " + kingRow);
//			System.out.println("kingCol = " + kingCol);				
			for (int i = 1; (kingRow+1-i) != 0 && (kingCol+1-i) != 0; i++) {
//				System.out.println("i: " + i);
				if (isEmpty(kingRow-i, kingCol-i) == true) {
//					System.out.println("the location at " + convertNumber(kingCol-i) + (kingRow-i+1) + " is empty");
				}
				else if (getColor(kingRow-i, kingCol-i, turn+1) == true && (pieceInWay.getClass().equals(Bishop.class) || pieceInWay.getClass().equals(Queen.class))) {				
//					System.out.println("From the bottom right");
					System.out.println("kgS.Your king would be IN CHECK from a piece on " + convertNumber(kingCol-i) + (kingRow-i+1));
					pieceInWay = savePIW;
					currentPiece = saveCurrent;
					checkRow = kingRow-i;
					checkCol = kingCol-i;
					return false;
				}
				else {
//					System.out.println("Occupied by a non-concerning piece");
					i = kingRow;
				}	
			}
		}
		else {
//			System.out.println("Not an applicable check");
		}
		//bottom right
//		System.out.println("Initiating BR Bishop check");

		if (kingCol != 7 && kingRow != 7) {
//			System.out.println("The king is not at 7,7");
//			System.out.println("kingRow = " + kingRow);
//			System.out.println("kingCol = " + kingCol);			
			for (int i = 1; (kingRow-1+i) != 7 && (kingCol-1+i) != 7; i++) {
//				System.out.println("i: " + i);
				if (isEmpty(kingRow+i, kingCol+i) == true) {
//					System.out.println("the location at " + convertNumber((kingCol+i)) + (kingRow+i-1) + " is empty");
				}
				else if (getColor(kingRow+i, kingCol+i, turn+1) == true && (pieceInWay.getClass().equals(Bishop.class) || pieceInWay.getClass().equals(Queen.class))) {				
//					System.out.println("From the bottom right");
					System.out.println("kgS.Your king would be IN CHECK from a piece on " + convertNumber(kingCol+i) + (kingRow+i+1));
					pieceInWay = savePIW;
					currentPiece = saveCurrent;
					checkRow = kingRow+i;
					checkCol = kingCol+i;
					return false;
				}
				else {
//					System.out.println("Occupied by a non-concerning piece (white or non-bishop / queen black)");
					i = -1 * kingRow + 7;
				}			
			}
		}
		else {
//			System.out.println("Not an applicable check");
		}
		//bottom left
//		System.out.println("Initiating BL Bishop check");

		if (kingCol != 0 && kingRow != 7) {
//			System.out.println("The king is not at 0,7");
//			System.out.println("kingRow = " + kingRow);
//			System.out.println("kingCol = " + kingCol);
						
			for (int i = 1; (kingRow-1+i) != 7 && (kingCol+1-i) != 0; i++) {
//				System.out.println("i: " + i);
				if (isEmpty(kingRow+i, kingCol-i) == true) {
//					System.out.println("the location at " + convertNumber((kingCol-i)) + (kingRow+i+1) + " is empty");		
				}
				else if (getColor(kingRow+i, kingCol-i, turn+1) == true && (pieceInWay.getClass().equals(Bishop.class) || pieceInWay.getClass().equals(Queen.class))) {				
//					System.out.println("From the bottom left");
				System.out.println("kgS.Your king would be IN CHECK from a piece on " + convertNumber(kingCol-i) + (kingRow+i+1));
					pieceInWay = savePIW;
					currentPiece = saveCurrent;
					checkRow = kingRow+i;
					checkCol = kingCol-i;
					return false;
				}
				else {
//					System.out.println("Occupied by a non-concerning piece (white or non-bishop / queen black)");
					i = -1 * kingRow + 7;
				}
				
			}
		}
		else {
//			System.out.println("Not an applicable check");
		}
		// top right
//	System.out.println("Initiating UR Bishop check");
		if (kingCol != 7 && kingRow != 0) {
//			System.out.println("The king is not at 0,7");
//			System.out.println("kingRow = " + kingRow);
//			System.out.println("kingCol = " + kingCol);
						
			for (int i = 1; (kingRow+1-i) != 0 && (kingCol-1+i) != 7; i++) {
//				System.out.println("i: " + i);
				if (isEmpty(kingRow-i, kingCol+i) == true) {
//					System.out.println("the location at " + convertNumber((kingCol+i)) + (kingRow-i+1) + " is empty");		
				}
				else if (getColor(kingRow-i, kingCol+i, turn+1) == true && (pieceInWay.getClass().equals(Bishop.class) || pieceInWay.getClass().equals(Queen.class))) {				
//					System.out.println("From the top right");
					System.out.println("kgS.Your king would be IN CHECK from a piece on " + convertNumber(kingCol+i) + (kingRow-i+1));
					pieceInWay = savePIW;
					currentPiece = saveCurrent;
					checkRow = kingRow-i;
					checkCol = kingCol+i;
					return false;
				}
				else {
//					System.out.println("Occupied by a non-concerning piece (white or black non-bishop / queen)");
					i = kingRow;
				}		
			}
		}		
		else {
//			System.out.println("Not an applicable check");
		}
		
		
//Knights Check
		for (int i = -2; i <=2; i++) {
			for (int j = -2; j <=2; j++) {
				if ( Math.abs(i) != Math.abs(j) && i != 0 && j!= 0) {
					if (checkPos(kingRow+i, kingCol+j) == true && isEmpty(kingRow+i, kingCol+j) == false) {
						if (getColor(kingRow+i, kingCol+j, turn+1) == true && (pieceInWay.getClass().equals(Knight.class))) {
							System.out.println("kgS.Your King would be IN CHECK from a knight on " + convertNumber(kingCol+j) + (kingRow+1+i));
							checkRow = kingRow+i;
							checkCol = kingCol+j;
							return false;
						}
					}				
				}
			}
		}

		//(black) Pawn checks
		if (colorToCare.equalsIgnoreCase("white") && checkPos(kingRow-1, kingCol-1) == true && isEmpty(kingRow-1, kingCol-1) == false && getColor(kingRow-1, kingCol-1, turn+1) == true && (pieceInWay.getClass().equals(Pawn.class))) {
			System.out.println("kgS.Your King would be IN CHECK from a pawn on " + convertNumber(kingCol-1) + kingRow);
			checkRow = kingRow-1;
			checkCol = kingCol-1;
			return false;
		}
		if (colorToCare.equalsIgnoreCase("white") && checkPos(kingRow-1, kingCol+1) == true && isEmpty(kingRow-1, kingCol+1) == false && getColor(kingRow-1, kingCol+1, turn+1) == true && (pieceInWay.getClass().equals(Pawn.class))) {
			System.out.println("kgS.Your King would be IN CHECK from a pawn on " + convertNumber(kingCol+1) + kingRow);
			checkRow = kingRow-1;
			checkCol = kingCol+1;
			return false;
		}
		//(white) Pawn checks
		if (colorToCare.equalsIgnoreCase("black") && checkPos(kingRow+1, kingCol-1) == true && isEmpty(kingRow+1, kingCol-1) == false && getColor(kingRow+1, kingCol-1, turn+1) == true && (pieceInWay.getClass().equals(Pawn.class))) {
			System.out.println("kgS.Your King would be IN CHECK from a pawn on " + convertNumber(kingCol-1) + (kingRow+2));
			checkRow = kingRow+1;
			checkCol = kingCol-1;
			return false;
		}
		if (colorToCare.equalsIgnoreCase("black") && checkPos(kingRow+1, kingCol+1) == true && isEmpty(kingRow+1, kingCol+1) == false && getColor(kingRow+1, kingCol+1, turn+1) == true && (pieceInWay.getClass().equals(Pawn.class))) {
			System.out.println("kgS.Your King would be IN CHECK from a pawn on " + convertNumber(kingCol+1) + (kingRow+2));
			checkRow = kingRow+1;
			checkCol = kingCol+1;
			return false;
		}
		
		//King vs King Check

		for (int q = 0; q < 8; q++) {
			for (int p = 0; p < 8; p++) {
				if (getColor(q, p, turn+1) == true) {
					if (pieceInWay.getClass().equals(King.class)) {
						okingRow = q;
						okingCol = p;
					}
				}
			}
		}
		System.out.println("kgS.Other king coords ArE: " + convertNumber(okingCol) + (okingRow+1));
		
		 if (Math.abs(kingRow - okingRow) <= 1 && Math.abs(kingCol - okingCol) <= 1) {
		 	System.out.println("kgS.Your King would be IN CHECK from the enemy King!");
			checkRow = okingRow;
			checkCol = okingCol;
			return false;
		 }
		System.out.println("kgS.Concluding kingSafe");
		pieceInWay = savePIW;
		currentPiece = saveCurrent;
		return true;
	}
	
	public static void castle() {
		saveCurrent = currentPiece;
		savePIW = pieceInWay;
		colorToCare = getTurn(turn);
		if (colorToCare.equalsIgnoreCase("white") && WhiteKingHasMoved == true) {
			System.out.println("csl.You have already moved your King and cannot castle");
			startTurn();
		}
		else if (colorToCare.equalsIgnoreCase("black") && BlackKingHasMoved == true) {
			System.out.println("csl.You have already moved your King and cannot castle");
			startTurn();
		}
		System.out.println("csl.What rook do you wish to castle with (enter coords)");
		coords = keyIn.next();
		betterCoords(coords); //theRow and theCol now set
		kingFinder(turn); //kingRow and kingCol now set
	//	if (!(theRow != 0 || theRow != 7) || (theCol!= 0 || theCol != 7)) {
	//			System.out.println("That is not a feasible rook position");
	//			startTurn();
	//	}
		if (isEmpty(theRow, theCol) == true) {
			System.out.println("csl.That rook position attempted is empty");
			startTurn();
		}	
		if (getColor(theRow, theCol, turn) == true && pieceInWay.getClass().equals(Rook.class)) {
			if (theCol < kingCol) {
				if (colorToCare.equalsIgnoreCase("white") && WhiteLeftRookHasMoved == true) {
					System.out.println("csl.You have already moved that Rook");
					startTurn();
				}
				else if (colorToCare.equalsIgnoreCase("black") && BlackLeftRookHasMoved == true) {
					System.out.println("csl.You have already moved that Rook");
					startTurn();
				}
				safeCrossing(theCol+1, kingCol);
			}
			else {
				if (colorToCare.equalsIgnoreCase("white") && WhiteRightRookHasMoved == true) {
					System.out.println("csl.You have already moved that Rook");
					startTurn();
				}
				else if (colorToCare.equalsIgnoreCase("black") && BlackRightRookHasMoved == true) {
					System.out.println("csl.You have already moved that Rook");
					startTurn();
				}
				safeCrossing(kingCol, 7);		
			}
		}
		else {
			System.out.println("csl.The attempted rook is either the wrong color rook or not a rook");
			pieceInWay = savePIW;
			currentPiece = saveCurrent;
			startTurn();
		}
	}	
	public static void safeCrossing(int lower, int higher) {
		saveKing = board[kingRow][kingCol];
		int trueCol = kingCol;
		System.out.println("sCr.Lower bound: " + lower);
		System.out.println("sCr.Upper bound: " + higher);
		System.out.println("sCr.King coords: " + convertNumber(kingCol) + (kingRow+1));
		for (int i = lower; i < higher; i++) {
			System.out.println("the i is: " + i);
			pieceInWay = savePIW;
			currentPiece = saveCurrent;
			getColor(theRow, i+1, turn);
			if (isEmpty(theRow, i+1) == false && !pieceInWay.getClass().equals(King.class)	&& !pieceInWay.getClass().equals(Rook.class)) {
				System.out.println("sCr.There is a piece interfering with your castle on " + convertNumber(i+1) + (kingRow+1));
				display();
				startTurn();
			}
			saveCurrent = board[kingRow][i];
			
			board[kingRow][kingCol] = new Empty("__");
			board[kingRow][i] = new King("kk", colorToCare);
			System.out.println("sCr.Checking kingSafe() at " + convertNumber(i) + (kingRow+1));
			if (kingSafe() == false) {
				System.out.println("i: " + i);
				board[kingRow][i] = saveCurrent;
				board[kingRow][trueCol] = saveKing;
				System.out.println("sCr.King returned to " + convertNumber(trueCol) + (kingRow+1));

				System.out.println("sCr.A castle would put your king out of, through, or into check");
				display();
				startTurn();
			}
			board[kingRow][kingCol] = saveKing;
		}
		System.out.println("sCr.You have successfully castled.");
		if (lower == theCol+1) { //castle king-side
			Object saveRook = board[theRow][theCol];
			board[kingRow][kingCol] = new Empty("__");
//			System.out.println("King pos at " + convertNumber(kingCol+1) + (kingRow+1) + " set to empty");
			board[kingRow][theCol+1] = saveKing;
//			System.out.println("King set to " + convertNumber(theCol+1) + (theRow+1));
//			System.out.println("Rook ID saved at " + convertNumber(theCol) + (theRow+1));
			board[theRow][theCol] = new Empty("__");
//			System.out.println("Rook set to empty");
			board[theRow][theCol+2] = saveRook;
	//		System.out.println("Rook ID set at " + convertNumber(theCol+2) + (theRow+1));
			if (getTurn(turn).equalsIgnoreCase("white")) {
				WhiteKingHasMoved = true;
			}
			else {
				BlackKingHasMoved = true;
			}
			endTurn();

		}
		else { //castle queen-side
			Object saveRook = board[theRow][theCol];
			System.out.println("sCr.Rook ID saved at " + convertNumber(theCol) + (theRow+1));

			board[theRow][theCol] = new Empty("__");
			System.out.println("sCr.Rook set to empty");

			board[theRow][theCol-3] = saveRook;
			System.out.println("sCr.Rook ID set at " + convertNumber(theCol-3) + (theRow+1));

			board[kingRow][kingCol] = new Empty("__");
			System.out.println("sCr.King pos at " + convertNumber(kingCol) + (kingRow+1) + " set to empty");

			board[kingRow][theCol-2] = saveKing;
			System.out.println("sCr.King set to " + convertNumber(theCol-2) + (theRow+1));
			if (getTurn(turn).equalsIgnoreCase("white")) {
				WhiteKingHasMoved = true;
			}
			else {
				BlackKingHasMoved = true;
			}
			endTurn();
		}
	}
	
	public static void promote() {
		
		String toName = null;
		String color = null;
		if (getTurn(turn).equalsIgnoreCase("white")) {
			toName = "w";
			color = "white";
		}
		else {
			toName = "b";
			color = "black";
		}
		System.out.println("prm.What would you like to promote your pawn to? (Knight, Bishop, Rook, Queen)");
		String gimme = keyIn.next().toLowerCase();
		
		switch (gimme) {
		case "knight":
			board[row2][col2] = new Knight(toName + "N", color);
			break;
		case "bishop":
			board[row2][col2] = new Bishop(toName + "B", color);
			break;
		case "rook":
			board[row2][col2] = new Rook(toName + "R", color);
			break;
		case "queen":
			board[row2][col2] = new Queen(toName + "Q", color);
			break;
		default:
			System.out.println("prm.That is not a valid piece name, try again");
			promote();
		}	
	}
	
	
	
	
	
	public static void flipIsFlipped() {
		if (isFlipped == true) {
			isFlipped = false;
		}
		else {
			isFlipped = true;
		}
	}
	
	
	public static void autoFlip() {
		System.out.println("aFp.Starting Flip");
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 4; j++) {
				Object terp = board[i][j];
				board[i][j] = board[7-i][7-j];
				board[7-i][7-j] = terp;
			}
		}
		System.out.println("aFp.Ended Flip");
		flipIsFlipped();
/*		display();
		if (isFlipped == true) {
			isFlipped = false;
		}
		else {
			isFlipped = true;
		}
		*/
		System.out.println("aFp.All Done");
	}
	public static void Flip() {
		System.out.println("Flp.Starting Flip");
		totalFlips += 1;
		if (totalFlips %2 == 1) {
			isFlipped = true;
		}
		else {
			isFlipped = false;
		}
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 4; j++) {
				Object temp = board[i][j];
		//		System.out.println("Flp.piece at" + convertNumber(j+1) + " " +  i + " has been set to temp");
				board[i][j] = board[7-i][7-j];
				board[7-i][7-j] = temp;
			}
		}
	}
	
	public static void endTurn() {
		partOfTurn = 3;
		turn += 1;
		canUndo = true;
		if (canFlip == true) {
			System.out.println("end.Autoflip enabled, flipping the board");
			Flip();
		}
		display();
		startTurn();
	}
	public static void bishopCheck() {
		if (Math.abs(row2 - row) == Math.abs(col2-col) && sameTeam() == false) {
			//			System.out.println("There is a one-to-one ratio between the new coords and old coords");
						if (row > row2 && col > col2) {
//							System.out.println("Moving up and to the left");
							
							
							for (int i = 1; (row - i) > row2; i++) {
								if (isEmpty(row -i, col-i) == false) {
									System.out.println("bsC.Error, a piece at coordinates: " + (row -i) + ", " + (col-i) + " is blocking this move");
//									System.out.println("This is where the Bishop issue is #1");
									continueTurn();
								}
							}
							movePiece();
						}
						else if (row > row2 && col < col2) {
//							System.out.println("Moving up and to the right");
//7,2 to 4,5
							for (int i = 1; (row - i) > row2; i++) {
								if (isEmpty(row - i, col+i) == false) {
									System.out.println("bsC.Error, a piece at coordinates: " + (row -i) + ", " + (col+i) + " is blocking this move");
//									System.out.println("This is where the Bishop issue is #2");
									continueTurn();
								}
							}
							movePiece();
						}
						else if (row < row2 && col < col2) {
//							System.out.println("Moving down and to the right");
							
							for (int i = 1; row + i < row2; i++) {
								if (isEmpty(row + i, col+i) == false) {
									System.out.println("bsC.Error, a piece at coordinates: " + (row + i) + ", " + (col+i) + " is blocking this move");
	//								System.out.println("This is where the Bishop issue is #3");
									continueTurn();
								}
							}
							movePiece();
						}
						else if (row < row2 && col > col2) {
	//						System.out.println("Moving down and to the left");
							
							for (int i = 1; row + i < row2; i++) {
								if (isEmpty(row + i, col-i) == false) {
									System.out.println("bsC.Error, a piece at coordinates: " + (row + i) + ", " + (col-i) + " is blocking this move");
	//								System.out.println("This is where the Bishop issue is #4");
									continueTurn();
								}
							}
							movePiece();
						}
					}
					else { //not a 1-1 ratio or moving onto same colored piece
						System.out.println("bsC.Not a legal move.");
						continueTurn();
					}
	}
	
	public static void showMe() {
		saveCurrent = currentPiece;
		savePIW = pieceInWay;
		colorToCare = getTurn(turn);
		if (isFlipped == true) {
			System.out.println("shw.Row: " + row + "\n" + "shw.col: " + col);

			System.out.println("shw.row and col position is being corrected to unflipped board positions.");
			theRow = 7-row;
			theCol = 7-col;
			System.out.println("shw.Row: " + row + "\n" + "shw.col: " + col);
		}
		
		checkPos(theRow, theCol);
		getColor(theRow, theCol, turn);
		
		System.out.println("shw.Piece In Way: " + pieceInWay);
		if (isEmpty(theRow, theCol) == true) {
			System.out.println("shw.There is not a piece at that location");
			startTurn();
		}
		if (getColor(theRow, theCol, turn+1) == true) {
			System.out.println("shw.This is a " + getTurn(turn+1) + " " + pieceInWay.getClass() + ", which you can't move. However, these are the places it can go");
			offColor = true;
		}
		else {
			System.out.println("shw.Here are the spaces you can move your " + pieceInWay.getClass());
		}
		if (pieceInWay.getClass().equals(Bishop.class)) { 		//Bishops
			diagonalCheck();
		}
		else if (pieceInWay.getClass().equals(Rook.class)) {		//Rooks
			perpendicularCheck();
		}
		else if (pieceInWay.getClass().equals(Queen.class)) {		//Queens
			diagonalCheck();
			perpendicularCheck();
		}
		else if (pieceInWay.getClass().equals(Knight.class)) {	//Knights
			if (checkPos(theRow+1, theCol + 2) == true && isEmpty(theRow+1, kingCol+2) == true) {
				board[theRow+1][theCol+2] = "XX";
			}
			if (checkPos(theRow-1, theCol -2) == true && isEmpty(theRow-1, theCol-2) == true) {
				board[theRow-1][theCol-2] = "XX";
			}
			if (checkPos(theRow+1, theCol - 2) == true && isEmpty(theRow+1, theCol-2) == true) {
				board[theRow+1][theCol-2] = "XX";
			}
			if (checkPos(theRow-1, theCol + 2) == true && isEmpty(theRow-1, theCol+2) == true) {
				board[theRow-1][theCol+2] = "XX";
			}
			if (checkPos(theRow+2, theCol + 1) == true && isEmpty(theRow+2, theCol+1) == true) {
				board[theRow+2][theCol+1] = "XX";
			}
			if (checkPos(theRow-2, theCol - 1) == true && isEmpty(theRow-2, theCol-1) == true) {
				board[theRow-2][theCol-1] = "XX";
			}
			if (checkPos(theRow+2, theCol - 1) == true && isEmpty(theRow+2, theCol-1) == true) {
				board[theRow+2][theCol-1] = "XX";
			}
			if (checkPos(theRow-2, theCol + 1) == true && isEmpty(theRow-2, theCol+1) == true) {
				board[theRow-2][theCol+1] = "XX";
			}
		}
		else if (pieceInWay.getClass().equals(Pawn.class)) {	//Pawns need work
			if (isFlipped == false) {
				if (getColor(theRow, theCol, turn) == true && getTurn(turn).equalsIgnoreCase("white")) {
					if (isEmpty(theRow-1, theCol)) {
						board[theRow-1][theCol] = "XX";
						if (theRow == 6 && isEmpty(theRow-2, theCol)) {
							board[theRow-2][theCol] = "XX";
						}
					}
				}
				else {
					if (isEmpty(theRow+1, theCol)) {
						board[theRow+1][theCol] = "XX";
						if (theRow == 1 && isEmpty(theRow+2, theCol)) {
							board[theRow+2][theCol] = "XX";
						}
					}
				}
			}
			else {
				if (getColor(theRow, theCol, turn) == true && getTurn(turn).equalsIgnoreCase("white")) {
					if (isEmpty(theRow+1, theCol)) {
						board[theRow+1][theCol] = "XX";
						if (theRow == 6 && isEmpty(theRow+2, theCol)) {
							board[theRow+2][theCol] = "XX";
						}
					}
				}
				else {
					if (isEmpty(theRow-1, theCol)) {
						board[theRow-1][theCol] = "XX";
						if (theRow == 1 && isEmpty(theRow-2, theCol)) {
							board[theRow-2][theCol] = "XX";
						}
					}
				}
			}
		}
		
		
		else if (pieceInWay.getClass().equals(King.class)) { //Kings
			if (getColor(theRow, theCol, turn) == true && getTurn(turn).equalsIgnoreCase("white")) {
				colorToCare = "white";
			}
			else {
				colorToCare = "black";
			}
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (Math.abs(theRow-i) == 1 && Math.abs(theCol-j) <= 1 || (Math.abs(theCol-j) == 1) && Math.abs(theRow-i) <= 1) {
						if (checkPos(i, j) == true && isEmpty(i, j) == true) {
							saveKing = board[theRow][theCol];
							board[theRow][theCol] = new Empty("__");
							board[i][j] = new King("kk", colorToCare);
							if (kingSafe() == true) {
								board[i][j] = "XX";
							}
							else {
								board[i][j] = new Empty("__");
							}
							board[theRow][theCol] = saveKing;
						}
					}
				}
			}
		}
		else {
			System.out.println("shw.Something went horribly wrong, whoops");
		}
		currentPiece = saveCurrent;
		pieceInWay = savePIW;
		display();
		if (isFlipped == true) {
			System.out.println("shw.hmmmm");
		}
		System.out.println("shw.Say 'Done' when you're ready to take your move");
		isDone();
	}
	public static void perpendicularCheck() {
		boolean isDone = false;
		
		for (int i = theRow-1; i >= 0 && isDone == false; i--) {
			if (checkPos(i, theCol)==true && isEmpty(i, theCol)==true) {
					board[i][theCol] = "XX";
			}
			else {
				isDone = true;
			}
		}
		isDone = false;
		for (int i = theRow+1; i <= 7 && isDone == false; i++) {
			if (checkPos(i, theCol)==true && isEmpty(i, theCol)==true) {
				board[i][theCol] = "XX";		
			}
			else {
				isDone = true;
			}
		}
		isDone = false;
		for (int i = theCol-1; i >= 0 && isDone == false; i--) {
			if (checkPos(theRow, i)==true && isEmpty(theRow, i)==true) {
				board[theRow][i] = "XX";
			}
			else {
				isDone = true;
			}
		}
		for (int i = theCol+1; i <= 7 && isDone == false; i++) {
			if (checkPos(theRow, i)==true && isEmpty(theRow, i)==true) {
				board[theRow][i] = "XX";
			}
			else {
				isDone = true;
			}
		}
	}
	public static void diagonalCheck() {
	
		//theRow and theCol
		int diaRow = theRow -1;
		int diaCol = theCol -1;
//		System.out.println("DiaRow and DiaCol are: " + diaRow +" and " + diaCol);
		while (diaRow >= 0 && diaRow < 8 && diaCol >= 0 && diaCol < 8 && isEmpty(diaRow, diaCol) == true && checkPos(diaRow, diaCol) == true) {
			//up and left
			board[diaRow][diaCol] = "\\\\";
			diaRow -= 1;
			diaCol -=1;
		}
		diaRow = theRow +1;
		diaCol = theCol +1;
		while (diaRow >= 0 && diaRow < 8 && diaCol >= 0 && diaCol < 8 && isEmpty(diaRow, diaCol) == true && checkPos(diaRow, diaCol) == true) {
			//down and right
			board[diaRow][diaCol] = "\\\\";
			diaRow += 1;
			diaCol +=1;
		}
		diaRow = theRow -1;
		diaCol = theCol +1;
		while (diaRow >= 0 && diaRow < 8 && diaCol >= 0 && diaCol < 8 && isEmpty(diaRow, diaCol) == true && checkPos(diaRow, diaCol) == true) {
		//up and right
			board[diaRow][diaCol] = "//";
			diaRow -= 1;
			diaCol +=1;
		}
		diaRow = theRow +1;
		diaCol = theCol -1;
		while (diaRow >= 0 && diaRow < 8 && diaCol >= 0 && diaCol < 8 && isEmpty(diaRow, diaCol) == true && checkPos(diaRow, diaCol) == true) {
			//down and left
			board[diaRow][diaCol] = "//";
			diaRow += 1;
			diaCol -=1;
		}
	}
	public static void isDone() {
		String doneNow = keyIn.next();
		if (doneNow.equalsIgnoreCase("done") == true) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (board[i][j].equals("//") || board[i][j].equals("\\\\") || board[i][j].equals("XX")) {
						board[i][j] = new Empty("__");
					}
				}
			}
			display();
		}
		else {
			System.out.println("Say 'done' when you're done");
			isDone();
		}
	}
	public static void rookCheck() {
		if ((row2 == row || col2 == col)  && sameTeam() == false) {
			if (row != row2) {
				if (row < row2) {				
					for (int i = (row + 1); i < row2; i++) {
						if (isEmpty(i, col) == false) {
							System.out.println("rkC.Error, a piece at coordinates: " + i + ", " + col + " is blocking this move");
		//					System.out.println("This is where the issue is (part 4)");
							continueTurn();
						}
					}
				}
				else { //if row > row2
					for (int i = (row - 1); i > row2; i--) {
						if (isEmpty(i, col) == false) {
							System.out.println("rkC.Error, a piece at coordinates: " + i + ", " + col + " is blocking this move");
//							System.out.println("This is where the issue is (part 3)");
							continueTurn();
						}
					}
				}
			}
			else if (col < col2) {
				for (int i = (col + 1); i < col2; i++) {
					if (isEmpty(row, i) == false) {
						System.out.println("rkC.Error, a piece at coordinates: " + row + ", " + i + " is blocking this move");
	//					System.out.println("This is where the issue is (part 2)");
						continueTurn();
					}
				}
			}
			else {
				for (int i = (col - 1); i > col2; i--) {
					if (isEmpty(row, i) == false) {
						System.out.println("rkC.Error, a piece at coordinates: " + row + ", " + i + " is blocking this move");
		//				System.out.println("This is where the issue is");
						continueTurn();
					}
				}
			}
			
		if (getTurn(turn).equalsIgnoreCase("white")) {
			if (row == 7 && col == 0) {
				WhiteLeftRookHasMoved = true;
			}
			else if (row == 7 && col == 7) {
				WhiteRightRookHasMoved = true;
			}
		}
		else {
			if (row == 0 && col == 0) {
				BlackLeftRookHasMoved = true;
			}
			else if (row == 0 && col == 7) {
				BlackRightRookHasMoved = true;
			}
		}
			
			movePiece();
		}
		else { //moving sideways or onto same colored piece
			System.out.println("rkC.Not a legal move.");
			continueTurn();
		}
	}
	
	public static void betterCoords(String coord) {
		if (coord.length() == 2) {
			coordCol = coord.substring(0, 1);
			System.out.println("btC.CoordCol is: " + coordCol);
			coordRow = coord.substring(1,2);
			System.out.println("btC.CoordRow is: " + coordRow);


			try { 
		        theRow = (Integer.parseInt(coordRow) - 1); 
		    } 
			catch(NumberFormatException t) { 
				System.out.println("btC.A coordinate must be a Letter followed by a number, like A1");
				displace();
		    } 
			catch(NullPointerException y) {
				System.out.println("btC.A coordinate must be a Letter followed by a number, like A1");
				displace();
		    }
		}
		else {
			System.out.println("btC.Coordinate pairs consist of just ONE letter followed by ONE number (only A-H and 0-7 applicable");
			displace();
		}
		convertLetter();
//		System.out.println("theRow is " + theRow);
//		System.out.println("theCol is " + theCol);
	}
	public static void convertLetter() {
		String caps = coordCol.toUpperCase();
		switch (caps) {
		case "A":
			theCol = 0;
			break;
		case "B":
			theCol = 1;
			break;
		case "C":
			theCol = 2;
			break;
		case "D":
			theCol = 3;
			break;
		case "E":
			theCol = 4;
			break;
		case "F":
			theCol = 5;
			break;
		case "G":
			theCol = 6;
			break;
		case "H":
			theCol = 7;
			break;
		default:
			System.out.println("cvL.That is not a valid letter imputted, try again");
			displace();
			break;
		}
	}
	public static String convertNumber(int num) {
		switch (num) {
		case 0:
			return "A";
		case 1:
			return "B";
		case 2:
			return "C";
		case 3:
			return "D"; 
		case 4:
			return "E";
		case 5:
			return "F";
		case 6:
			return "G";
		case 7:
			return "H";
		}
		return "You messed up";
	}
	public static void displace() {
		if (partOfTurn == 1) {
			startTurn();
		}
		else if (partOfTurn == 2) {
			continueTurn();
		}
	}
	public static void undo() {
		boolean needToFlip = false;
		if (turn >=1 && canUndo == true) {
			
			if (isFlipped == true) {
				System.out.println("und.it be flipped");
				autoFlip();
				needToFlip = true;
				System.out.println("und.autoflipped");

			}		
			turn -= 1;
			unMove();
			System.out.println("und.move undone");

			canUndo = false;
			if (needToFlip == true) {
				System.out.println("und.goin back");
				autoFlip();
				display();
			}			
			if (canFlip == true) {
				System.out.println("und.since it was flipped on previous turn, flipping now");
				autoFlip();
				display();
			}
			System.out.println("und.done and done");

			display();			
		}
		else {
			System.out.println("You cannot undo a move at this time.");
			display();
		}
		startTurn();
	}
	
	public static boolean checkMate() {
		colorToCare = getTurn(turn);
		checkMated = true;
		trueCheckRow = checkRow;
		trueCheckCol = checkCol;
		if (checkAvoid() == true) {
			System.out.println("chM.King can move away");
			checkMated = false;
			return false;
		}
		else {
			System.out.println("chM.King CANNOT move away");
		}
		System.out.println();  System.out.println();
		display();
		if (checkMated == true && checkCapture() == true) {
			System.out.println("chM.Another piece can capture");
			checkMated = false;
			unMove();
			display();
			return false;
		}
		else if (checkMated == true) {
			System.out.println(); System.out.println("chM.No piece can capture the checking piece");
			System.out.println();
		}
		display();
		if (checkMated == true && checkBlock() == true) {
			System.out.println("chM.Another piece can block!");
			checkMated = false;
//			unMove();
			display();
		}
		return checkMated;
	}
	public static boolean checkAvoid() {
		kingFinder(turn);
		//kingRow and kingCol
		trueKingRow = kingRow;
		trueKingCol = kingCol;
		System.out.println("chA.CR: " + checkRow);
		System.out.println("chA.CC: " + checkCol);
		System.out.println("chA.TKR: " + trueKingRow);
		System.out.println("chA.TKC: " + trueKingCol);
		if (kingSafe() == false) {
			System.out.println("chA.King is in danger");
			//trueCheckRow and trueCheckCol records the position of the checking piece for later reuse.
			trueCheckRow = checkRow;
			System.out.println("chA.trueCheckRow is " + trueCheckRow);
			trueCheckCol = checkCol;
			System.out.println("chA.trueCheckCol is " + trueCheckCol);
			trueInCheck = true;
		}
		int RHcap = 1;
		int RLcap = -1; 
		int CHcap = 1;
		int CLcap = -1;
		if (trueKingRow == 0) {
			RLcap = 0;
			System.out.println("chA.RLcap");
		}
		if (trueKingRow == 7) {
			RHcap = 0;
			System.out.println("chA.RHcap");
		}
		if (trueKingCol == 0) {
			CLcap = 0;
			System.out.println("chA.CLcap");
		}
		if (trueKingCol == 7) {
			CHcap = 0;
			System.out.println("chA.CHcap");
		}
		
		for (int i = (trueKingRow+RLcap); i <= (trueKingRow+RHcap); i++) {
			System.out.println("chA.Row checked is: " + i);
			for (int j = (trueKingCol+CLcap); j <= (trueKingCol+CHcap); j++) { //j >= 0 && j <= 7 && 
				System.out.println("chA.Col checked is: " + j);
				if (!(i == trueKingRow && j == trueKingCol) && checkPos(i, j) == true && !getColor(i, j, turn) == true) {
					System.out.println("chA.Checking the space at " + convertNumber(j) + (i+1));
					saveKing = board[trueKingRow][trueKingCol];
					Object saveMe = board[i][j];
					board[trueKingRow][trueKingCol] = new Empty("__");
					board[i][j] = new King("kk", colorToCare);
					display();
					if (kingSafe() == true) {
						System.out.println("chA.Not mate, open space on " + convertNumber(j) + (i+1));
						board[i][j] = saveMe;
						board[trueKingRow][trueKingCol] = saveKing;
						display();
						return true;
					}
					else {
						board[i][j] = saveMe;
						board[trueKingRow][trueKingCol] = saveKing;
					}
				}
			}
		}
		System.out.println("chA.CR2: " + checkRow);
		System.out.println("chA.CR2: " + checkCol);
		System.out.println("chA.TCR2: " + trueCheckRow);
		System.out.println("chA.TCC2: " + trueCheckCol);
		return false;
	}
	public static boolean checkCapture() {
		System.out.println("chC.CR3: " + checkRow);
		System.out.println("chC.CR3: " + checkCol);
		checkRow = trueCheckRow;
		checkCol = trueCheckCol;
		System.out.println("chC.CR4: " + checkRow);
		System.out.println("chC.CR4: " + checkCol);
		System.out.println("chC.TCR3: " + trueCheckRow);
		System.out.println("chC.TCC3: " + trueCheckCol);
		kingFinder(turn);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (! board[i][j].getClass().equals(Empty.class)) {
					System.out.println("chC.spot at " + convertNumber(j) + (i + 1) + " is not empty");
					if (getColor(i, j, turn) == true && ! pieceInWay.getClass().equals(King.class)) {
						System.out.println("chC." + colorToCare + " piece found at above coords");
							if (autoLegality(i, j, checkRow, checkCol, false) == true) {
								System.out.println("chC.Safe via capture");
								return true;
							}
					}
				}
			}
		}	
		return false;
	}
	public static boolean checkBlock() {
		System.out.println("chB.Initiating checkBlock");
		if (board[checkRow][checkCol].getClass().equals(Knight.class) || board[checkRow][checkCol].getClass().equals(Pawn.class)) {
			System.out.println("chB.You lose");
			return false;
		}
		int top, bottom, left, right;
		kingFinder(turn);
		System.out.println("chB.Determing from a distance");
		if (! (Math.abs(checkCol-kingCol)<= 1) || !(Math.abs(checkRow-kingRow) <=1)) { //if it is a check from a distance
			if (kingRow > checkRow) {
				bottom = kingRow;
				top = checkRow;
			}
			else {
				top = kingRow;
				bottom = checkRow;
			}
			if (kingCol > checkCol) {
				right = kingCol;
				left = checkCol;
			}
			else {
				right = checkCol;
				left = kingCol;
			}
			System.out.println("chB.Left: " + left);
			System.out.println("chB.Right: " + right);
			System.out.println("chB.Top: " + top);
			System.out.println("chB.Bottom: " + bottom);
			System.out.println();
			if (top == bottom || left == right) {
				for (int c = left; c < right; c++) {
					for (int r = top; r < bottom; r++) {
						System.out.println("chB.The location: " + convertNumber(c) + (r+1) + " is an inbetween space");
					}
				}
			}
			else {
				for (int c = 1; c + left < right; c++) {
					System.out.println("chB.Testing: " + convertNumber(c+left) + (c+top+1));
				}
			}			
			if (top == bottom || left == right) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (! board[i][j].getClass().equals(Empty.class)) {
						System.out.println("chB.spot at " + convertNumber(j) + (i + 1) + " is not empty");
						if (getColor(i, j, turn) == true && ! pieceInWay.getClass().equals(King.class)) {
							System.out.println("chB." + colorToCare + " piece found at above coords");
							for (int c = left; c < right; c++) {
								for (int r = top; r < bottom; r++) {
									System.out.println("chB.Testing: " + convertNumber(c) + (r+1));
									if (autoLegality(i, j, r, c, true) == true) {
										System.out.println("chB.Safe via block");
										display();
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
			else { //diagonal
				System.out.println("chB.this is from a diagonal");
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						if (! board[i][j].getClass().equals(Empty.class)) {
							System.out.println("chB.spot at " + convertNumber(j) + (i + 1) + " is not empty");
							if (getColor(i, j, turn) == true && ! pieceInWay.getClass().equals(King.class)) {
								System.out.println("chB." + colorToCare + " piece found at above coords");
								for (int c = 1; c + left < right; c++) {
										System.out.println("chB.Testing: " + convertNumber(c+left) + (c+top+1));
										if (autoLegality(i, j, top+c, left+c, true) == true) {
											System.out.println("chB.Safe via block");
											return true;
										}
								}
							}
						}
					}
				}
			}
		}
		else {
			System.out.println("chB.Check not from a distance");
			mate();
		}
		System.out.println("chB.GAME OVER");
		mate();
		return false;
	}
	public static void mate() {
		System.out.println(getTurn(turn+1) + " has WON THE GAME!");
		System.exit(0);
	}
	public static boolean autoLegality(int r1, int c1, int r2, int c2, boolean avoid) {
		System.out.println("aLg.Starting autoLegality");
		row = r1;
		col = c1;
		//row and col are the coords of the piece that is trying to capture the checking piece
		row2 = r2;
		col2 = c2;
		System.out.println("aLg.ROW2: " + row2 + " COL2: " + col2);
		//row2 and col2 are the coords of the checking piece
		Object piece = board[row][col];
		System.out.println("aLg.The square of interest is located at " + convertNumber(c2) + (r2+1));
		String action;
		currentPiece = board[row][col];
		System.out.println("aLg.Currentpiece class is " + currentPiece.getClass());
		if (avoid == true) {
			action = "block";
		}
		else {
			action = "capture";
		}
		if (piece.getClass().equals(Pawn.class)) {
			System.out.println("aLg.analyzing a pawn");
			if (getTurn(turn).equalsIgnoreCase("white")) {
				System.out.println("aLg.Which is white");
					if (row == (row2 + 1) && col == col2) {
//						System.out.println("This may be legal");
						if (isEmpty(row2, col2) == true) {
//							System.out.println("This is legal");
							if (tryIt() == true) {
//								System.out.println("You are not in check, at the very least a PAWN can " + action + " the checking piece");
								return true;
							}
						}
					}
					else if (row == 6 && (row == (row2 + 2) && col == col2)){
						if (isEmpty(row-1, col) == false || isEmpty(row-2, col) == false) {
							canMove = false;
						}
						if (canMove == true) {
							if (tryIt() == true) {
//								System.out.println("You are not in check, at the very least a PAWN can " + action + " the checking piece");
								return true;
							}
						}
						if (checkPos(row2, col2+1) == true && isEmpty(row2, col2+1) == false && getColor(row2, col2+1, turn+1) == true && pieceInWay.getClass().equals(Pawn.class)) {
							dieEnPesRow = row2+1;
							dieEnPesCol = col2;
							toKillAPiece = "black";
						}
						if (checkPos(row2, col2-1) == true && isEmpty(row2, col2-1) == false && getColor(row2, col2-1, turn+1) == true && pieceInWay.getClass().equals(Pawn.class)) {
							dieEnPesRow = row2+1;
							dieEnPesCol = col2;
							toKillAPiece = "black";
						}
					}
					else if ((row == row2+1) && Math.abs(col-col2) == 1 && sameTeam() == false && isEmpty(row2, col2) == false) {
						currentPiece = board[row][col];
						if (tryIt() == true) {
							System.out.println("aLg.You are not in check, at the very least a PAWN can " + action + " the checking piece");
							return true;
						}						
					}
					else if ((row == row2+1) && Math.abs(col-col2) == 1 && row2 == dieEnPesRow && col2 == dieEnPesCol) {
						saveEnPes = board[dieEnPesRow+1][dieEnPesCol];
						currentPiece = board[row][col];
						board[dieEnPesRow+1][dieEnPesCol] = new Empty("__");
						if (tryIt() == true) {
							System.out.println("aLg.You are not in check, at the very least a PAWN can " + action + " the checking piece");
//							currentPiece = board[row][col];
							System.out.println("aLg.Currentpiece class is " + currentPiece.getClass());
							return true;
						}
					}
			}
			else { //black pawn
				System.out.println("aLg.which is black");
				if (row == (row2 - 1) && col == col2) {
					if (isEmpty(row2, col2) == true) {
						if (tryIt() == true) {
							System.out.println("aLg.You are not in check, at the very least a PAWN can " + action + " the checking piece");
							currentPiece = board[row][col];
							System.out.println("aLg.Currentpiece class is " + currentPiece.getClass());
							return true;
						}
					}
				}
				else if (row == 1 && (row == (row2 - 2) && col == col2)){
					canMove = true;
					if (isEmpty(row+1, col) == false || isEmpty(row+2, col) == false) {
						canMove = false;
					}
					if (canMove == true) {
						currentPiece = board[row][col];
						if (tryIt() == true) {
							System.out.println("aLg.You are not in check, at the very least a PAWN can " + action + " the checking piece");
							currentPiece = board[row][col];
							System.out.println("aLg.Currentpiece class is " + currentPiece.getClass());
							return true;
						}
					}
					if (checkPos(row2, col2+1) == true && isEmpty(row2, col2+1) == false && getColor(row2, col2+1, turn+1) == true && pieceInWay.getClass().equals(Pawn.class)) {
						dieEnPesRow = row2-1;
						dieEnPesCol = col2;
						toKillAPiece = "white";
					}
					if (checkPos(row2, col2-1) == true && isEmpty(row2, col2+-1) == false && getColor(row2, col2-1, turn+1) == true && pieceInWay.getClass().equals(Pawn.class)) {
						dieEnPesRow = row2-1;
						dieEnPesCol = col2;
						toKillAPiece = "white";
					}
				}
				else if ((row == row2-1) && Math.abs(col-col2) == 1 && sameTeam() == false && isEmpty(row2, col2) == false) {
					currentPiece = board[row][col];
					if (tryIt() == true) {
						System.out.println("aLg.You are not in check, at the very least a PAWN can " + action + " the checking piece");
//						currentPiece = board[row][col];
//						System.out.println("Currentpiece class is " + currentPiece.getClass());
						return true;
					}
				}
				else if ((row == row2-1) && Math.abs(col-col2) == 1 && row2 == dieEnPesRow && col2 == dieEnPesCol) {
					saveEnPes = board[dieEnPesRow+1][dieEnPesCol];
					board[dieEnPesRow+1][dieEnPesCol] = new Empty("__");
					currentPiece = board[row][col];
					if (tryIt() == true) {
						System.out.println("aLg.You are not in check, at the very least a PAWN can " + action + " the checking piece");
//						currentPiece = board[row][col];
//						System.out.println("Currentpiece class is " + currentPiece.getClass());
						return true;
					}
				}
			}
		} 
		//knight
		else if (piece.getClass().equals(Knight.class)) {
			System.out.println("aLg.Checking a knight");
			if ( ( Math.abs(row2-row) == 2 && Math.abs(col2-col) == 1 )|| ( Math.abs(row2-row) == 1 && Math.abs(col2-col) == 2 )  && sameTeam() == false) {
				currentPiece = board[row][col];	
				if (tryIt() == true) {
					System.out.println("aLg.You are not in check, at the very least a KNIGHT can " + action + " the checking piece");
	//				currentPiece = board[row][col];
	//				System.out.println("Currentpiece class is " + currentPiece.getClass());
					return true;
				}
			}
		} 
		//rook
		else if (piece.getClass().equals(Rook.class) && autoRookCheck() == true) {		
			System.out.println("aLg.You are not in check, at the very least a ROOK can " + action + " the checking piece");	
			return true;
		} 
		//Bishop
		else if (piece.getClass().equals(Bishop.class) && autoBishopCheck() == true) {
			System.out.println("aLg.You are not in check, at the very least a BISHOP can " + action + " the checking piece");
			return true;
		} 
		else if (piece.getClass().equals(Queen.class)) {
			if ((row == row2 || col == col2) && autoRookCheck() == true) {
				System.out.println("aLg.You are not in check, at the very least a QUEEN can " + action + " the checking piece");
				return true;
			}
			else if (autoBishopCheck() == true){
				System.out.println("aLg.You are not in check, at the very least a QUEEN can " + action + " the checking piece");
				return true;
			}
		}
		return false;	
	}
	public static boolean tryIt() {
		System.out.println("try.Running tryIt");
		movePiece();
		System.out.println("try.Piece moved");
		System.out.println();
		display();
		System.out.println();
		if (kingSafe() == true) {
			System.out.println("try.That move would not endanger your king");
			unMove();
			return true;
		}
		else {
			System.out.println("try.That move WOULD endanger your king");
			unMove();
			return false;
		}
	}
	
	public static boolean autoRookCheck() {
		System.out.println("aRC.initiating autoRookCheck");
		System.out.println("aRC.Row2: " + row2 + " Col2: " + col2);
		if (row2 == row || col2 == col) {
			canMove = true;
			System.out.println("aRC.Which is alligned");
			if (row != row2) {
				if (row < row2) {	
					for (int i = (row + 1); i < row2; i++) {
						if (isEmpty(i, col) == false) {
							canMove = false;
						}
					}
				}
				else { //if row > row2
					for (int i = (row - 1); i > row2; i--) {
						if (isEmpty(i, col) == false) {
							canMove = false;
						}
					}
				}
			}
			else if (col < col2) {
				for (int i = (col + 1); i < col2; i++) {
					if (isEmpty(row, i) == false) {
						canMove = false;
					}
				}
			}
			else {
				for (int i = (col - 1); i > col2; i--) {
					if (isEmpty(row, i) == false) {
						canMove = false;
					}
				}
			}
			System.out.println("aRC.Canmove remained true");
			currentPiece = board[row][col];
			System.out.println("aRC.Currentpiece class is " + currentPiece.getClass());
			if (canMove == true && tryIt() == true) {
					return true;
			}
		}
		System.out.println("aRC.And not blocked");
		return false;
	}
	public static boolean autoBishopCheck() {
		System.out.println("aBC.Initiating autoBishopCheck");
		canMove = true;
		if (Math.abs(row2 - row) == Math.abs(col2-col) && sameTeam() == false) {
			System.out.println("aBC.Legitimate option");
			if (row > row2 && col > col2) { //up and left													
				for (int i = 1; (row - i) > row2; i++) {
					if (isEmpty(row -i, col-i) == false) {
						canMove = false;
					}
				}
			}
			else if (row > row2 && col < col2) { //up and right
				for (int i = 1; (row - i) > row2; i++) {
					if (isEmpty(row - i, col+i) == false) {
						canMove = false;
					}
				}
			}
			else if (row < row2 && col < col2) { //down and right					
				for (int i = 1; row + i < row2; i++) {
					if (isEmpty(row + i, col+i) == false) {
						canMove = false;
					}
				}
			}
			else if (row < row2 && col > col2) { //down and left
				for (int i = 1; row + i < row2; i++) {
					if (isEmpty(row + i, col-i) == false) {
						canMove = false;
					}
				}						
			}
			System.out.println("aBC.Canmove remained true");
			currentPiece = board[row][col];
			System.out.println("aBC.Currentpiece class is " + currentPiece.getClass());
			if (canMove == true && tryIt() == true) {
					return true;
			}
		}
		return false;
	}
	public static void variableCheck() {
		turn = 0;
		System.out.println("vaC.row: " + row);
		System.out.println("vaC.col: " + col);
		System.out.println("vaC.row2: " + row2);
		System.out.println("vaC.col2: " + col2);
		System.out.println("vaC.theRow: " + theRow);
		System.out.println("vaC.theCol: " + theCol);
		System.out.println("vaC.okingRow: " + okingRow);
		System.out.println("vaC.okingCol: " + okingCol);
		System.out.println("vaC.partOfTurn: " + partOfTurn);
		System.out.println("vaC.kingRow: " + kingRow);
		System.out.println("vaC.kingCol: " + kingCol);
		System.out.println("vaC.totalFlips: " + totalFlips);
		System.out.println("vaC.autoFlips: " + autoFlips);
		System.out.println("vaC.canUndo: " + canUndo);
		System.out.println("vaC.canFlip: " + canFlip);
		System.out.println("vaC.isFlipped: " + isFlipped);
		System.out.println("vaC.autoFlipped: " + autoFlipped);
		System.out.println();
	}
	public static boolean checkStale() {
		System.out.println("chS.Checking Stale");
		if (kingSafe() == false) {
			return false;
		}
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (isEmpty(i, j) == false && getColor(i, j, turn) == true && ! board[i][j].getClass().equals(King.class)) {
					currentPiece = board[i][j];
					System.out.println("chS.Checking a " + currentPiece.getClass() + " at " + convertNumber(j) + (i+1));
					for (int r = 0; r < 8; r++) {
						for (int c = 0; c < 8; c++) {
							if (isEmpty(r, c) == true || getColor(r, c, turn+1) == true) {
								if (autoLegality(i, j, r, c, false) == true) {
									display();
									return false;
								}
							}
						}
					}
				}
			}
		}
		System.out.println("chS.It's a STALEMATE!");
		return true;
	}
	public static void showDisplay() {	
		int counter = 0;
		int showRowNumber = 0;
		System.out.print("  _______________________________________________________________________________________________________________");
		for (int r = 0; r < 24; r++) {
			counter = counter+1;
			System.out.print("\n");		
			if (r%3 == 1) {
				showRowNumber += 1;
				System.out.print(showRowNumber);	
			}
			else {
				System.out.print(" ");
			}
				
			for (int c = 0; c < 8; c++) {
					System.out.print("|");
					System.out.print(showBoard[r][c]);
					System.out.print("|");
			}
			if (counter == 3) {
				counter = 0;
				System.out.print("\n");																											   
				System.out.print(" ----------------------------------------------------------------------------------------------------------------");
			}
		}
		System.out.println();
		System.out.println("        A            B             C             D              E             F              G            H");
	}
	public static void updateShowBoard() {
		for (int r = 0; r < 8; r++) {
			int sr = 3*r;
			for (int c = 0; c < 8; c++) {
				if (isEmpty(r, c) == true) {
					showBoard[sr][c] = new Empty("            ");
					showBoard[sr+1][c] = new Empty("            ");
					showBoard[sr+2][c] = new Empty("            ");
				}
				else if (board[r][c].toString().equals("wP")) {
					   showBoard[sr][c] = new showPiece(" |''''''''| ");
					 showBoard[sr+1][c] = new showPiece(" |White Pw| ");
					 showBoard[sr+2][c] = new showPiece(" |~~~~~~~~| ");
				}
				else if (board[r][c].toString().equals("bP")) {
					   showBoard[sr][c] = new showPiece(" |''''''''| ");
					 showBoard[sr+1][c] = new showPiece(" |Black Pw| ");
				     showBoard[sr+2][c] = new showPiece(" |########| ");
				}
				else if (board[r][c].toString().equals("wR")) {
					   showBoard[sr][c] = new showPiece(" |=| || |=| ");
					 showBoard[sr+1][c] = new showPiece(" |White Rk| ");
				     showBoard[sr+2][c] = new showPiece(" |~~~~~~~~| ");
				}
				else if (board[r][c].toString().equals("bR")) {
					showBoard[sr][c] = new showPiece(" |=| || |=| ");
				  showBoard[sr+1][c] = new showPiece(" |Black Rk| ");
				  showBoard[sr+2][c] = new showPiece(" |########| ");
				}
				else if (board[r][c].toString().equals("wN")) {
					showBoard[sr][c] = new showPiece("    /**/    ");
				  showBoard[sr+1][c] = new showPiece(" |White Nt| ");
				  showBoard[sr+2][c] = new showPiece(" |~~~~~~~~| ");
				}
				else if (board[r][c].toString().equals("bN")) {
					    showBoard[sr][c] = new showPiece("    /**/    ");
					  showBoard[sr+1][c] = new showPiece(" |Black Nt| ");
					  showBoard[sr+2][c] = new showPiece(" |########| ");
				}
				else if (board[r][c].toString().equals("wB")) {
					showBoard[sr][c] = new showPiece("     /\\     ");
				  showBoard[sr+1][c] = new showPiece(" |White Bi| ");
				  showBoard[sr+2][c] = new showPiece(" |~~~~~~~~| ");
				}
				else if (board[r][c].toString().equals("bB")) {
					showBoard[sr][c] = new showPiece("     /\\     ");
				  showBoard[sr+1][c] = new showPiece(" |Black Bi| ");
				  showBoard[sr+2][c] = new showPiece(" |########| ");
				}
				else if (board[r][c].toString().equals("wQ")) {
					showBoard[sr][c] = new showPiece("   /\\/\\/\\   ");
				  showBoard[sr+1][c] = new showPiece(" |White Qu| ");
				  showBoard[sr+2][c] = new showPiece(" |~~~~~~~~| ");
				}
				else if (board[r][c].toString().equals("wK")) {
					showBoard[sr][c] = new showPiece("  /\\ || /\\  ");
				  showBoard[sr+1][c] = new showPiece(" |White Kg| ");
				  showBoard[sr+2][c] = new showPiece(" |~~~~~~~~| ");
				}
				else if (board[r][c].toString().equals("bQ")) {
					showBoard[sr][c] = new showPiece("   /\\/\\/\\   ");
				  showBoard[sr+1][c] = new showPiece(" |Black Qu| ");
				  showBoard[sr+2][c] = new showPiece(" |########| ");
				}
				else if (board[r][c].toString().equals("bK")) {
					showBoard[sr][c] = new showPiece("  /\\ || /\\  ");
				  showBoard[sr+1][c] = new showPiece(" |Black Kg| ");
				  showBoard[sr+2][c] = new showPiece(" |########| ");
				}
				else {				
					showBoard[sr+1][c] = new showPiece ("    ERROR   ");
				}
			}
		}
	}
	public static void main(String[] args) {
		setBoard();
		display();
		doFlip();
	//	System.out.println();
		startTurn();
	}
}
