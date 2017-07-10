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
	public boolean move(String move) {
		Scanner in = new Scanner(move);
		
		int startX = in.nextInt();
		int startY = in.nextInt();
		Point startPoint = new Point(startX, startY);
		
		int endX = in.nextInt();
		int endY = in.nextInt();
		Point endPoint = new Point(endX, endY);
		
		boolean moved = boardState.move(startPoint, endPoint);
		return moved;		
	}
	public boolean tryMove(String move) {
		Scanner in = new Scanner(move);
		
		int startX = in.nextInt();
		int startY = in.nextInt();
		Point startPoint = new Point(startX, startY);
		
		int endX = in.nextInt();
		int endY = in.nextInt();
		Point endPoint = new Point(endX, endY);
		
		boolean moved = boardState.tryMove(startPoint, endPoint);
		return moved;
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
