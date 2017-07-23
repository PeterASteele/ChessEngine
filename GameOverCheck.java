public class GameOverCheck {
	Board boardState;

	public GameOverCheck(Board state) {
		boardState = state;
	}

	// TODO: Implement
	public boolean isGameOver() {
		for (int a = 0; a < 8; a++) {
			for (int b = 0; b < 8; b++) {
				for (int c = 0; c < 8; c++) {
					for (int d = 0; d < 8; d++) {
						boolean moveValid = boardState.tryMove(new Move(new Point(a, b),
								new Point(c, d)));
						if (moveValid) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
}
