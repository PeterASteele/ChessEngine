import java.util.Scanner;


public class Game {

	Board boardState;
	
	public Game(boolean dEBUG){
		boardState = new Board(dEBUG);
	}
	
	
	public void printState() {
		// TODO Auto-generated method stub
		BoardPrinter printer = new BoardPrinter(boardState);
		System.out.println(printer);
	}
	public boolean move(Move move) {
		boolean moved = boardState.move(move);
		return moved;		
	}
	
	public boolean tryMove(Move move) {
		return boardState.tryMove(move);
	}

	public boolean isOver() {
		return new GameOverCheck(boardState).isGameOver();
	}


	public void switchTurns() {
		boardState.switchTurns();
	}

	public void printOutcome() {
		if(boardState.isPlayerToMoveInCheck()){
			System.out.println(boardState.isWhiteToMove() == true ? "Black wins" : "White wins");
		}
		else{
			System.out.println("DRAW!");
		}
	}

}
