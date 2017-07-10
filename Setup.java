public class Setup {
	private Piece[][] boardState;

	public Setup() {
		boardState = new Piece[8][8];
		whitePieceSetup();
		blackPieceSetup();
		setEmpty();
	}

	public Piece[][] getStartingBoard() {
		return boardState;
	}

	private void whitePieceSetup() {
		boardState[0][0] = Piece.WHITE_ROOK;
		boardState[0][1] = Piece.WHITE_KNIGHT;
		boardState[0][2] = Piece.WHITE_BISHOP;
		boardState[0][3] = Piece.WHITE_QUEEN;
		boardState[0][4] = Piece.WHITE_KING;
		boardState[0][5] = Piece.WHITE_BISHOP;
		boardState[0][6] = Piece.WHITE_KNIGHT;
		boardState[0][7] = Piece.WHITE_ROOK;
		for (int a = 0; a < 8; a++) {
			boardState[1][a] = Piece.WHITE_PAWN;
		}
	}
	
	private void blackPieceSetup() {
		boardState[7][0] = Piece.BLACK_ROOK;
		boardState[7][1] = Piece.BLACK_KNIGHT;
		boardState[7][2] = Piece.BLACK_BISHOP;
		boardState[7][3] = Piece.BLACK_QUEEN;
		boardState[7][4] = Piece.BLACK_KING;
		boardState[7][5] = Piece.BLACK_BISHOP;
		boardState[7][6] = Piece.BLACK_KNIGHT;
		boardState[7][7] = Piece.BLACK_ROOK;
		for (int a = 0; a < 8; a++) {
			boardState[6][a] = Piece.BLACK_PAWN;
		}
	}
	
	private void setEmpty(){
		for(int a = 2; a <= 5; a++){
			for(int b = 0; b < 8; b++){
				boardState[a][b] = Piece.EMPTY;
			}
		}
	}
}
