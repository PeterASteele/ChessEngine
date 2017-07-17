import java.util.Scanner;
import java.io.*;
import java.util.*;

public class Driver {

	static boolean DEBUG = false;
	static StringBuilder gameLog;

	public static void main(String[] args) throws IOException {
		Scanner input = new Scanner(System.in);
		gameLog = new StringBuilder();
		String filename = "log" + System.currentTimeMillis();
		Game game = new Game(DEBUG);
		while (!game.isOver()) {
			game.printState();
			boolean moveMade = false;
			while (!moveMade) {
				String move = "";
				if(game.boardState.isWhiteToMove()) {
					move = input.nextLine();
				}
				else {
					move = game.boardState.getGoodMove();
				}

				gameLog.append(move + "\n");
				FileWriter file = new FileWriter(filename);
				BufferedWriter write = new BufferedWriter(file);
				write.write(gameLog.toString());
				write.close();
				file.close();

				if (game.move(move)) {
					game.switchTurns();
					moveMade = true;
				} else {
					System.out.println("invalid move");
				}
			}
		}
		game.printState();
		game.printOutcome();
	}
}
