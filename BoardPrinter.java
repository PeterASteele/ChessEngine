
public class BoardPrinter {
	Board boardToPrint;
	
	public BoardPrinter(Board boardToPrint){
		this.boardToPrint = boardToPrint;
	}
	
	public String toString(){
		Piece[][] pieces = boardToPrint.getPieces();
		StringBuilder output = new StringBuilder();
		output.append("      0     1     2     3     4     5     6     7    \n   " + getHorizontalRow() + "   \n");
		for(int a = 7; a >= 0; a--){
			output.append(a + ": | ");
			for(int b = 0; b < 8; b++){
				output.append(pieceToString(pieces[a][b]) + " | ");
			}
			output.append("   \n   " + getHorizontalRow() + "   \n");
		}
		return output.toString();
	}	
	
	public String getHorizontalRow(){
		return "  ---   ---   ---   ---   ---   ---   ---   ---  ";
	}
	
	public String pieceToString(Piece in){
		switch(in){
			case WHITE_PAWN:
				return "WPn";
			case WHITE_ROOK:
				return "WRk";
			case WHITE_BISHOP:
				return "WBp";
			case WHITE_KNIGHT:
				return "WKt";
			case WHITE_QUEEN:
				return "WQn";
			case WHITE_KING:
				return "WKg";
			case BLACK_PAWN:
				return "BPn";
			case BLACK_ROOK:
				return "BRk";
			case BLACK_BISHOP:
				return "BBp";
			case BLACK_KNIGHT:
				return "BKt";
			case BLACK_QUEEN:
				return "BQn";
			case BLACK_KING:
				return "BKg";
			case EMPTY:
				return "   ";
		}
		throw new Error("INVALID_PIECE ERROR");
	}
}
