import java.util.ArrayList;

public class Board {
	boolean DEBUG;
	private Piece[][] boardState;
	private boolean whiteToMove;

	// used for checking if a player can castle
	private boolean whiteKingMove;
	private boolean blackKingMove;

	private boolean whiteQueensideRookMove;
	private boolean whiteKingsideRookMove;

	private boolean blackQueensideRookMove;
	private boolean blackKingsideRookMove;

	public Board(boolean DEBUG) {
		this.DEBUG = DEBUG;
		whiteToMove = true;
		boardState = new Setup().getStartingBoard();
	}

	public Board(Piece[][] boardState, boolean playerMove, boolean whiteKingMove, boolean blackKingMove,
				 boolean whiteQueensideRookMove, boolean whiteKingsideRookMove, boolean blackQueensideRookMove, boolean blackKingsideRookMove) {

		this.boardState = new Piece[8][8];
		for (int a = 0; a < 8; a++) {
			for (int b = 0; b < 8; b++) {
				this.boardState[a][b] = boardState[a][b];
			}
		}
		this.whiteToMove = playerMove;
		this.whiteKingMove = whiteKingMove;
		this.blackKingMove = blackKingMove;
		this.whiteQueensideRookMove = whiteQueensideRookMove;
		this.blackQueensideRookMove = blackQueensideRookMove;
		this.whiteKingsideRookMove = whiteKingsideRookMove;
		this.blackKingsideRookMove = blackKingsideRookMove;
	}

	public Piece[][] getPieces() {
		return boardState;
	}

	public boolean isWhiteToMove() {
		return whiteToMove;
	}

	public boolean isPlayerToMoveInCheck() {
		Point kingLocation = findKing(true);
		debug("KING LOCATION: " + kingLocation);

		boolean attacked = false;
		attacked |= checkAttackingPawns(kingLocation);
		if (attacked) {
			debug("KING ATTACKED BY PAWNS");
		}
		attacked |= checkAttackingKnights(kingLocation);
		if (attacked) {
			debug("KING ATTACKED BY KNIGHT");
		}
		attacked |= checkAttackingBishops(kingLocation);
		if (attacked) {
			debug("KING ATTACKED BY BISHOP");
		}
		attacked |= checkAttackingRooks(kingLocation);
		if (attacked) {
			debug("KING ATTACKED BY ROOK");
		}
		attacked |= checkAttackingQueens(kingLocation);
		if (attacked) {
			debug("KING ATTACKED BY QUEEN");
		}
		attacked |= checkAttackingKings(kingLocation);
		if (attacked) {
			debug("KING ATTACKED BY KINGS");
		}
		return attacked;
	}

	private boolean checkAttackingRooks(Point kingLocation) {
		Piece opposingRook = Piece.EMPTY;
		if (whiteToMove) {
			opposingRook = Piece.BLACK_ROOK;
		} else {
			opposingRook = Piece.WHITE_ROOK;
		}

		ArrayList<Point>[] directionalMoves = new ArrayList[4];
		directionalMoves[0] = getDirectionalMoves(kingLocation, 1, 0);
		directionalMoves[1] = getDirectionalMoves(kingLocation, -1, 0);
		directionalMoves[2] = getDirectionalMoves(kingLocation, 0, 1);
		directionalMoves[3] = getDirectionalMoves(kingLocation, 0, -1);

		for (int x = 0; x < directionalMoves.length; x++) {
			ArrayList<Point> directionList = directionalMoves[x];
			for (Point i : directionList) {
				if (boardState[i.x][i.y] == opposingRook) {
					return true;
				}
				if (boardState[i.x][i.y] != Piece.EMPTY) {
					break;
				}
			}
		}

		return false;
	}

	private boolean canPlayerCastle(boolean kingside) {
		Piece king = whiteToMove ? Piece.WHITE_KING : Piece.BLACK_KING;
		Piece rook = whiteToMove ? Piece.WHITE_ROOK : Piece.BLACK_ROOK;
		int row = whiteToMove ? 0 : 7;
		boolean kingMove = whiteToMove ? whiteKingMove : blackKingMove;
		boolean queensideRookMove = whiteToMove ? whiteQueensideRookMove : blackQueensideRookMove;
		boolean kingsideRookMove = whiteToMove ? whiteKingsideRookMove : blackKingsideRookMove;

		debug("" + king);
		debug("" + rook);
		debug("" + row);
		debug("" + kingMove);
		debug("" + queensideRookMove);
		debug("" + kingsideRookMove);

		if (kingMove) {
			return false;
		}
		debug("not failed from kingmove");
		if (kingside && kingsideRookMove) {
			return false;
		}
		debug("kingside not failed");
		if ((!kingside) && queensideRookMove) {
			return false;
		}
		debug("queenside not failed");
		if (boardState[row][4] != king) {
			return false;
		}
		debug("king in right spot");
		if (kingside) {
			for (int x = 5; x <= 6; x++){
				if(boardState[row][x] != Piece.EMPTY){
					return false;
				}
			}
		} else {
			for (int x = 3; x >= 1; x--){
				if(boardState[row][x] != Piece.EMPTY){
					return false;
				}
			}
		}


		if (kingside) {
			boardState[row][4] = Piece.EMPTY;
			for (int x = 4; x <= 6; x++) {
				boardState[row][x] = king;
				if (isPlayerToMoveInCheck()) {
					boardState[row][x] = Piece.EMPTY;
					boardState[row][4] = king;
					return false;
				}
				boardState[row][x] = Piece.EMPTY;
			}
			boardState[row][4] = king;
		} else {
			boardState[row][4] = Piece.EMPTY;
			for (int x = 4; x >= 2; x--) {
				boardState[row][x] = king;
				if (isPlayerToMoveInCheck()) {
					boardState[row][x] = Piece.EMPTY;
					boardState[row][4] = king;
					return false;
				}
				boardState[row][x] = Piece.EMPTY;
			}
			boardState[row][4] = king;
		}
		return true;
	}

	private boolean checkAttackingKings(Point kingLocation) {
		Piece opposingKing;

		if (whiteToMove) {
			opposingKing = Piece.BLACK_KING;
		} else {
			opposingKing = Piece.WHITE_KING;
		}

		ArrayList<Point> kingMoves = getKingMoves(kingLocation);
		for (Point i : kingMoves) {
			if (boardState[i.x][i.y] == opposingKing) {
				return true;
			}
		}
		return false;
	}

	private boolean checkAttackingQueens(Point kingLocation) {
		Piece opposingQueen = Piece.EMPTY;
		if (whiteToMove) {
			opposingQueen = Piece.BLACK_QUEEN;
		} else {
			opposingQueen = Piece.WHITE_QUEEN;
		}
		debug("opposing queen" + opposingQueen);

		ArrayList<Point>[] directionalMoves = new ArrayList[8];
		directionalMoves[0] = getDirectionalMoves(kingLocation, 1, -1);
		directionalMoves[1] = getDirectionalMoves(kingLocation, -1, 1);
		directionalMoves[2] = getDirectionalMoves(kingLocation, 1, 1);
		directionalMoves[3] = getDirectionalMoves(kingLocation, -1, -1);
		directionalMoves[4] = getDirectionalMoves(kingLocation, 1, 0);
		directionalMoves[5] = getDirectionalMoves(kingLocation, -1, 0);
		directionalMoves[6] = getDirectionalMoves(kingLocation, 0, 1);
		directionalMoves[7] = getDirectionalMoves(kingLocation, 0, -1);

		for (int x = 0; x < directionalMoves.length; x++) {
			ArrayList<Point> directionList = directionalMoves[x];
			for (Point i : directionList) {
				if (boardState[i.x][i.y] == opposingQueen) {
					return true;
				}
				if (boardState[i.x][i.y] != Piece.EMPTY) {
					break;
				}
			}
		}

		return false;
	}

	private boolean checkAttackingBishops(Point kingLocation) {
		Piece opposingBishop = Piece.EMPTY;
		if (whiteToMove) {
			opposingBishop = Piece.BLACK_BISHOP;
		} else {
			opposingBishop = Piece.WHITE_BISHOP;
		}

		ArrayList<Point>[] directionalMoves = new ArrayList[4];
		directionalMoves[0] = getDirectionalMoves(kingLocation, 1, -1);
		directionalMoves[1] = getDirectionalMoves(kingLocation, -1, 1);
		directionalMoves[2] = getDirectionalMoves(kingLocation, 1, 1);
		directionalMoves[3] = getDirectionalMoves(kingLocation, -1, -1);

		for (int x = 0; x < directionalMoves.length; x++) {
			ArrayList<Point> directionList = directionalMoves[x];
			for (Point i : directionList) {
				if (boardState[i.x][i.y] == opposingBishop) {
					return true;
				}
				if (boardState[i.x][i.y] != Piece.EMPTY) {
					break;
				}
			}
		}

		return false;
	}

	private boolean checkAttackingKnights(Point kingLocation) {
		Piece opposingKnight = Piece.EMPTY;

		if (whiteToMove) {
			opposingKnight = Piece.BLACK_KNIGHT;
		} else {
			opposingKnight = Piece.WHITE_KNIGHT;
		}

		ArrayList<Point> knightMoves = getKnightMoves(kingLocation);
		for (Point i : knightMoves) {
			if (boardState[i.x][i.y] == opposingKnight) {
				return true;
			}
		}
		return false;
	}

	private boolean checkAttackingPawns(Point location) {
		Piece opposingPawn = Piece.EMPTY;
		if (whiteToMove) {
			opposingPawn = Piece.BLACK_PAWN;
		} else {
			opposingPawn = Piece.WHITE_PAWN;
		}

		ArrayList<Point> pawns = new ArrayList<Point>();
		int direction = whiteToMove ? 1 : -1;
		pawns.add(new Point(location.x + direction, location.y + 1));
		pawns.add(new Point(location.x + direction, location.y - 1));
		pawns = filterInBounds(pawns);

		for (Point i : pawns) {
			if (boardState[i.x][i.y] == opposingPawn) {
				return true;
			}
		}
		return false;
	}

	public Point findPiece(Piece pieceToFind) {
		for (int a = 0; a < 8; a++) {
			for (int b = 0; b < 8; b++) {
				if (boardState[a][b] == pieceToFind) {
					return new Point(a, b);
				}
			}
		}
		return null;
	}

	public Point findKing(boolean toMove) {
		if (whiteToMove ^ toMove ^ true) {
			return findPiece(Piece.WHITE_KING);
		} else {
			return findPiece(Piece.BLACK_KING);
		}
	}

	public ArrayList<Point> getDirectionalMoves(Point location, int xDiff, int yDiff) {
		ArrayList<Point> moves = new ArrayList<Point>();
		for (int a = 1; a < 8; a++) {
			moves.add(new Point(location.x + xDiff * a, location.y + yDiff * a));
		}
		return filterInBounds(moves);
	}

	public ArrayList<Point> getKingMoves(Point location) {
		ArrayList<Point> moves = new ArrayList<Point>();
		for (int a = -1; a <= 1; a++) {
			for (int b = -1; b <= 1; b++) {
				if (a != 0 && b != 0) {
					moves.add(new Point(location.x + a, location.y + b));
				}
			}
		}
		return filterInBounds(moves);
	}

	private ArrayList<Point> getKnightMoves(Point location) {
		ArrayList<Point> moves = new ArrayList<Point>();
		for (int a = -1; a <= 1; a += 2) {
			for (int b = -1; b <= 1; b += 2) {
				moves.add(new Point(location.x + a * 2, location.y + b));
				moves.add(new Point(location.x + a, location.y + b * 2));
			}
		}
		return filterInBounds(moves);
	}

	public boolean move(Move makeMove) {
		Point start = makeMove.start;
		Point end = makeMove.end;

		debug("Moved called: " + start.x + " " + start.y + " " + end.x + " " + end.y);

		// first, check if the piece is empty
		if (boardState[start.x][start.y] == Piece.EMPTY) {
			return false;
		}

		debug("not empty");
		// cannot move in place
		if (start.x == end.x && start.y == end.y) {
			return false;
		}
		debug("move not in place");

		// next, check if the piece is the correct color
		boolean pieceColorWhite = isPieceWhite(boardState[start.x][start.y]);
		if (pieceColorWhite != whiteToMove) {
			return false;
		}
		debug("Is the piiece the correct color");

		// cannot capture own piece
		if (boardState[end.x][end.y] != Piece.EMPTY) {
			boolean destinationColor = isPieceWhite(boardState[end.x][end.y]);
			if (destinationColor == pieceColorWhite) {
				return false;
			}
		}
		debug("not capture own piece");

		// next, confirm if a move is valid
		boolean validMoveForPiece = validMoveForPiece(boardState[start.x][start.y], start, end);

		if (validMoveForPiece == false) {
			return false;
		}
		debug("Valid move for piece");

		// store some previous state
		boolean whiteKingTMP = whiteKingMove;
		boolean whiteQueensideRookMoveTMP = whiteQueensideRookMove;
		boolean whiteKingsideRookMoveTMP = whiteKingsideRookMove;
		boolean blackKingTMP = blackKingMove;
		boolean blackQueensideRookMoveTMP = blackQueensideRookMove;
		boolean blackKingsideRookMoveTMP = blackKingsideRookMove;

		// side effects?
		if (boardState[start.x][start.y] == Piece.BLACK_ROOK && start.x == 7 && start.y == 0) {
			blackQueensideRookMove = true;
		}
		if (boardState[start.x][start.y] == Piece.BLACK_ROOK && start.x == 7 && start.y == 7) {
			blackKingsideRookMove = true;
		}
		if (boardState[start.x][start.y] == Piece.BLACK_KING && start.x == 7 && start.y == 4) {
			blackKingMove = true;
		}
		if (boardState[start.x][start.y] == Piece.WHITE_ROOK && start.x == 0 && start.y == 0) {
			whiteQueensideRookMove = true;
		}
		if (boardState[start.x][start.y] == Piece.WHITE_ROOK && start.x == 0 && start.y == 7) {
			whiteKingsideRookMove = true;
		}
		if (boardState[start.x][start.y] == Piece.WHITE_KING && start.x == 0 && start.y == 4) {
			whiteKingMove = true;
		}

		// archive board
		Piece rollBack = boardState[end.x][end.y];
		if (boardState[start.x][start.y] == Piece.WHITE_KING && whiteKingTMP == false && end.y == 6) {
			boardState[0][7] = Piece.EMPTY;
			boardState[0][5] = Piece.WHITE_ROOK;
			whiteKingsideRookMove = true;
			whiteQueensideRookMove = true;
			whiteKingMove = true;
		}
		if (boardState[start.x][start.y] == Piece.WHITE_KING && whiteKingTMP == false && end.y == 2) {
			boardState[0][0] = Piece.EMPTY;
			boardState[0][3] = Piece.WHITE_ROOK;
			whiteKingsideRookMove = true;
			whiteQueensideRookMove = true;
			whiteKingMove = true;
		}
		if (boardState[start.x][start.y] == Piece.BLACK_KING && blackKingTMP == false && end.y == 6) {
			boardState[7][7] = Piece.EMPTY;
			boardState[7][5] = Piece.BLACK_ROOK;
			blackKingsideRookMove = true;
			blackQueensideRookMove = true;
			blackKingMove = true;
		}
		if (boardState[start.x][start.y] == Piece.BLACK_KING && blackKingTMP == false && end.y == 2) {
			boardState[7][0] = Piece.EMPTY;
			boardState[7][3] = Piece.BLACK_ROOK;
			blackKingsideRookMove = true;
			blackQueensideRookMove = true;
			blackKingMove = true;
		}
		if (end.x == 7 && end.y == 7) {
			blackKingsideRookMove = true;
		}
		if (end.x == 7 && end.y == 0) {
			blackQueensideRookMove = true;
		}
		if (end.x == 0 && end.y == 7) {
			whiteKingsideRookMove = true;
		}
		if (end.x == 0 && end.y == 0) {
			whiteQueensideRookMove = true;
		}
		boardState[end.x][end.y] = boardState[start.x][start.y];
		boardState[start.x][start.y] = Piece.EMPTY;

		// check if the new board state is moving into check
		if (isPlayerToMoveInCheck()) {
			// store some previous state
			whiteKingMove = whiteKingTMP;
			whiteQueensideRookMove = whiteQueensideRookMoveTMP;
			whiteKingsideRookMove = whiteKingsideRookMoveTMP;
			blackKingMove = blackKingTMP;
			blackQueensideRookMove = blackQueensideRookMoveTMP;
			blackKingsideRookMove = blackKingsideRookMoveTMP;
			boardState[start.x][start.y] = boardState[end.x][end.y];
			boardState[end.x][end.y] = rollBack;
			return false;
		}
		// promote pawn
		for (int x = 0; x < 8; x++) {
			if (boardState[0][x] == Piece.BLACK_PAWN) {
				boardState[0][x] = Piece.BLACK_QUEEN;
			}
			if (boardState[7][x] == Piece.WHITE_PAWN) {
				boardState[7][x] = Piece.WHITE_QUEEN;
			}
		}

		return true;
	}

	private boolean validMoveForPiece(Piece piece, Point start, Point end) {
		switch (piece) {
		case WHITE_KING:
		case BLACK_KING:
			debug("KING_MOVE");
			if (piece == Piece.BLACK_KING && blackKingMove == false && end.x == 7 && (end.y == 6 || end.y == 2)) {
				debug("black castle");
				return canPlayerCastle(end.y == 6 ? true : false);
			}
			if (piece == Piece.WHITE_KING && whiteKingMove == false && end.x == 0 && (end.y == 6 || end.y == 2)) {
				debug("white castle");
				debug("" + end.y);
				return canPlayerCastle(end.y == 6 ? true : false);
			}
			debug("no castle");
			int val = Math.max(Math.abs(end.x - start.x), Math.abs(end.y - start.y));
			return (val == 1);
		case WHITE_QUEEN:
		case BLACK_QUEEN:
			int xDiff = Math.abs(end.x - start.x);
			int yDiff = Math.abs(end.y - start.y);
			if (!(xDiff == 0 || yDiff == 0 || (xDiff == yDiff))) {
				return false;
			}
			int dist = Math.max(xDiff, yDiff);
			xDiff = (end.x - start.x) / dist;
			yDiff = (end.y - start.y) / dist;
			for (int a = 1; a < dist; a++) {
				if (boardState[start.x + a * xDiff][start.y + a * yDiff] != Piece.EMPTY) {
					return false;
				}
			}
			return true;
		case WHITE_ROOK:
		case BLACK_ROOK:
			xDiff = Math.abs(end.x - start.x);
			yDiff = Math.abs(end.y - start.y);
			if (!(xDiff == 0 || yDiff == 0)) {
				return false;
			}
			dist = Math.max(xDiff, yDiff);
			xDiff = (end.x - start.x) / dist;
			yDiff = (end.y - start.y) / dist;
			for (int a = 1; a < dist; a++) {
				if (boardState[start.x + a * xDiff][start.y + a * yDiff] != Piece.EMPTY) {
					return false;
				}
			}
			return true;
		case WHITE_BISHOP:
		case BLACK_BISHOP:
			xDiff = Math.abs(end.x - start.x);
			yDiff = Math.abs(end.y - start.y);
			if (!(xDiff == yDiff)) {
				return false;
			}
			dist = Math.max(xDiff, yDiff);
			xDiff = (end.x - start.x) / dist;
			yDiff = (end.y - start.y) / dist;
			for (int a = 1; a < dist; a++) {
				if (boardState[start.x + a * xDiff][start.y + a * yDiff] != Piece.EMPTY) {
					return false;
				}
			}
			return true;

		case WHITE_KNIGHT:
		case BLACK_KNIGHT:
			xDiff = Math.abs(end.x - start.x);
			yDiff = Math.abs(end.y - start.y);
			if (xDiff + yDiff == 3 && Math.abs(xDiff - yDiff) == 1) {
				return true;
			} else {
				return false;
			}
		case WHITE_PAWN:
			if (boardState[end.x][end.y] == Piece.EMPTY) {
				if (start.y != end.y) {
					return false;
				}
				if (start.x == 1) {
					if (end.x != 2 && end.x != 3) {
						return false;
					}
				} else {
					if (end.x - 1 != start.x) {
						return false;
					}
				}
			} else {
				if (end.x - start.x != 1 || Math.abs(end.y - start.y) != 1) {
					return false;
				}
			}
			return true;
		case BLACK_PAWN:
			if (boardState[end.x][end.y] == Piece.EMPTY) {
				if (start.y != end.y) {
					return false;
				}
				if (start.x == 6) {
					if (end.x != 5 && end.x != 4) {
						return false;
					}
				} else {
					if (end.x + 1 != start.x) {
						return false;
					}
				}
			} else {
				if (end.x - start.x != -1 || Math.abs(end.y - start.y) != 1) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public boolean isPieceWhite(Piece piece) {
		return piece == Piece.WHITE_PAWN || piece == Piece.WHITE_KNIGHT || piece == Piece.WHITE_BISHOP
				|| piece == Piece.WHITE_ROOK || piece == Piece.WHITE_QUEEN || piece == Piece.WHITE_KING;
	}

	public ArrayList<Point> filterInBounds(ArrayList<Point> listOfPoints) {
		ArrayList<Point> validPoints = new ArrayList<Point>();
		for (Point i : listOfPoints) {
			if (i.x >= 0 && i.x < 8 && i.y >= 0 && i.y < 8) {
				validPoints.add(i);
			}
		}
		return validPoints;
	}

	public void switchTurns() {
		whiteToMove = !whiteToMove;
	}

	public Board cloneBoard() {
		return new Board(boardState, whiteToMove, whiteKingMove, blackKingMove, whiteQueensideRookMove, whiteKingsideRookMove,
				blackQueensideRookMove, blackKingsideRookMove);
	}

	public void setBoard(Board in) {
		for (int a = 0; a < 8; a++) {
			for (int b = 0; b < 8; b++) {
				boardState[a][b] = in.boardState[a][b];
			}
		}
		whiteToMove = in.whiteToMove;
		whiteKingMove = in.whiteKingMove;
		blackKingMove = in.blackKingMove;
		whiteQueensideRookMove = in.whiteQueensideRookMove;
		blackQueensideRookMove = in.blackQueensideRookMove;
		whiteKingsideRookMove = in.whiteKingsideRookMove;
		blackKingsideRookMove = in.blackKingsideRookMove;
	}

	public boolean tryMove(Move makeMove) {
		Board rollBack = cloneBoard();
		boolean testMove = move(makeMove);
		if (testMove == true) {
			setBoard(rollBack);
			return true;
		}
		return false;
	}

	public void debug(String i) {
		if (DEBUG && i.length() < 8) {
			System.err.println(i);
		}
	}

	public double valuation(int depth) {
		if (depth == 0) {
			double total = 0;

			// benefit of having turn
			double tempo = whiteToMove ? .3 : -.3;
			total += tempo;

			// pieces on board
			for (int a = 0; a < 8; a++) {
				for (int b = 0; b < 8; b++) {
					switch (boardState[a][b]) {
					case WHITE_PAWN:
						total += 1;
						break;
					case BLACK_PAWN:
						total -= 1;
						break;
					case WHITE_KNIGHT:
						total += 3;
						break;
					case BLACK_KNIGHT:
						total -= 3;
						break;
					case WHITE_BISHOP:
						total += 3;
						break;
					case BLACK_BISHOP:
						total -= 3;
						break;
					case WHITE_ROOK:
						total += 5;
						break;
					case BLACK_ROOK:
						total -= 5;
						break;
					case WHITE_QUEEN:
						total += 9;
						break;
					case BLACK_QUEEN:
						total -= 9;
						break;
					case WHITE_KING:
						total += 10000;
						break;
					case BLACK_KING:
						total -= 10000;
						break;
					case EMPTY:
						break;
					}
				}
			}
			return total;
		} else {
			whiteToMove ^= true;
			double val =  getGoodDeepMove(depth - 1);
			whiteToMove ^= true;
			return val;
		}
	}

	public double getGoodDeepMove(int depth) {
		// First, get a list of all possible moves
		ArrayList<Move> moves = new ArrayList<Move>();
		for (int a = 0; a < 8; a++) {
			for (int b = 0; b < 8; b++) {
				for (int c = 0; c < 8; c++) {
					for (int d = 0; d < 8; d++) {
						boolean moveValid = tryMove(new Move(new Point(a, b), new Point(c, d)));
						if (moveValid) {
							moves.add(new Move(new Point(a, b), new Point(c, d)));
						}
					}
				}
			}
		}

		double bestValuation = whiteToMove ? -10000000 : 10000000;
		Move bestMove = null;
		for (Move val : moves) {
			double possibility = getValuationOfMove(val, depth);
			if ((possibility < bestValuation) ^ whiteToMove) {
				bestValuation = possibility;
				bestMove = val;
			}
		}

		// TODO Auto-generated method stub
		return bestValuation;
	}

	public Move getGoodMove() {
		// First, get a list of all possible moves
		ArrayList<Move> moves = new ArrayList<Move>();
		for (int a = 0; a < 8; a++) {
			for (int b = 0; b < 8; b++) {
				for (int c = 0; c < 8; c++) {
					for (int d = 0; d < 8; d++) {
						boolean moveValid = tryMove(new Move(new Point(a, b), new Point(c, d)));
						if (moveValid) {
							moves.add(new Move(new Point(a, b), new Point(c, d)));
						}
					}
				}
			}
		}

		double bestValuation = whiteToMove ? -1000000000 : 1000000000;
		Move bestMove = null;
		System.err.println(moves.size());
		for (Move val : moves) {
			double possibility = getValuationOfMove(val, 2);
			if ((possibility < bestValuation) ^ whiteToMove) {
				bestValuation = possibility;
				bestMove = val;
			}
		}

		// TODO Auto-generated method stub
		return bestMove;
	}

	private double getValuationOfMove(Move makeMove, int depth) {
		Board rollBack = cloneBoard();
		move(makeMove);
		double valuation = valuation(depth);
		setBoard(rollBack);
		return valuation;
	}
}
