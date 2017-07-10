import java.util.Scanner;

public class Driver {
	
	static boolean DEBUG = false;
	
	public static void main(String[] args){
		Scanner input = new Scanner(System.in);
		Game game = new Game(DEBUG);
		while(!game.isOver()){
			System.out.println(game.boardState.toString());
			game.printState();
			boolean moveMade = false;
			while(!moveMade){
				String move = input.nextLine();
				if(game.move(move)){
					moveMade = true;
				}
				else{
					System.out.println("invalid move");
				}
			}
			game.switchTurns();
		}
		game.printState();
		game.printOutcome();
	}
}
