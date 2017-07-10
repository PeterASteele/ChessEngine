import java.util.ArrayList;

public class Board {
	boolean DEBUG;
	private Piece[][] boardState;
	private boolean whiteToMove;

	public Board(boolean DEBUG) {
		this.DEBUG = DEBUG;
		whiteToMove = true;
		boardState = new Setup().getStartingBoard();
	}

	public Board(Piece[][] boardState, boolean playerMove) {
		this.boardState = (Piece[][]) boardState.clone();
		this.whiteToMove = playerMove;
	}

	public Piece[][] getPieces() {
		return boardState;
	}

	public boolean isWhiteToMove() {
		return whiteToMove;
	}

	public boolean isPlayerToMoveInCheck() {
		Point kingLocation = findKing(true);
		if (DEBUG) {
			System.err.println("KING LOCATION: " + kingLocation);
		}
		boolean attacked = false;
		attacked |= checkAttackingPawns(kingLocation);
		if (DEBUG && attacked) {
			System.err.println("KING ATTACKED BY PAWNS");
		}
		attacked |= checkAttackingKnights(kingLocation);
		if (DEBUG && attacked) {
			System.err.println("KING ATTACKED BY KNIGHT");
		}
		attacked |= checkAttackingBishops(kingLocation);
		if (DEBUG && attacked) {
			System.err.println("KING ATTACKED BY BISHOP");
		}
		attacked |= checkAttackingRooks(kingLocation);
		if (DEBUG && attacked) {
			System.err.println("KING ATTACKED BY ROOK");
		}
		attacked |= checkAttackingQueens(kingLocation);
		if (DEBUG && attacked) {
			System.err.println("KING ATTACKED BY QUEEN");
		}
		attacked |= checkAttackingKings(kingLocation);
		if (DEBUG && attacked) {
			System.err.println("KING ATTACKED BY KINGS");
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

	private boolean checkAttackingKings(Point kingLocation) {
		Piece opposingKing = Piece.EMPTY;

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
		if (DEBUG) {
			System.err.println("opposing queen" + opposingQueen);
		}
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

	public ArrayList<Point> getDirectionalMoves(Point location, int xDiff,
			int yDiff) {
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

	public boolean move(Point start, Point end) {
		// first, check if the piece is empty
		if (boardState[start.x][start.y] == Piece.EMPTY) {
			return false;
		}

		// cannot move in place
		if (start.x == end.x && start.y == end.y) {
			return false;
		}

		// next, check if the piece is the correct color
		boolean pieceColorWhite = isPieceWhite(boardState[start.x][start.y]);
		if (pieceColorWhite != whiteToMove) {
			return false;
		}

		// cannot capture own piece
		if (boardState[end.x][end.y] != Piece.EMPTY) {
			boolean destinationColor = isPieceWhite(boardState[end.x][end.y]);
			if (destinationColor == pieceColorWhite) {
				return false;
			}
		}
		
		// next, confirm if a move is valid
		boolean validMoveForPiece = validMoveForPiece(
				boardState[start.x][start.y], start, end);
		if(validMoveForPiece == false){
			return false;
		}
		// archive board
		Piece rollBack = boardState[end.x][end.y];
		boardState[end.x][end.y] = boardState[start.x][start.y];
		boardState[start.x][start.y] = Piece.EMPTY;

		// check if the new board state is moving into check
		if (isPlayerToMoveInCheck()) {
			boardState[start.x][start.y] = boardState[end.x][end.y];
			boardState[end.x][end.y] = rollBack;
			return false;
		}
		return true;
	}

	private boolean validMoveForPiece(Piece piece, Point start, Point end) {
		switch (piece) {
		case WHITE_KING:
		case BLACK_KING:
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
			xDiff = Math.abs(end.x-start.x);
			yDiff = Math.abs(end.y-start.y);
			if(xDiff + yDiff == 3 && Math.abs(xDiff-yDiff) == 1){
				return true;
			}
			else{
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
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPieceWhite(Piece piece) {
		return piece == Piece.WHITE_PAWN || piece == Piece.WHITE_KNIGHT
				|| piece == Piece.WHITE_BISHOP || piece == Piece.WHITE_ROOK
				|| piece == Piece.WHITE_QUEEN || piece == Piece.WHITE_KING;
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

	public boolean tryMove(Point start, Point end) {
		Piece rollBack = boardState[end.x][end.y];
		boolean testMove = move(start, end);
		if(testMove == true){
			boardState[start.x][start.y] = boardState[end.x][end.y];
			boardState[end.x][end.y] = rollBack;
			return true;
		}
		return false;
	}
}
