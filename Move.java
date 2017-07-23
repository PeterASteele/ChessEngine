import java.util.Scanner;

public class Move{
	Point start;
	Point end;
	
	public Move (Point start, Point end) {
		this.start = start;
		this.end = end;
	}
	
	public Move (Scanner in) {
		int startX = in.nextInt();
		int startY = in.nextInt();
		start = new Point(startX, startY);
		
		int endX = in.nextInt();
		int endY = in.nextInt();
		end = new Point(endX, endY);
	}
	
	public Move (String moveInput) {
		Scanner in = new Scanner(moveInput);
		
		int startX = in.nextInt();
		int startY = in.nextInt();
		start = new Point(startX, startY);
		
		int endX = in.nextInt();
		int endY = in.nextInt();
		end = new Point(endX, endY);
	}
	
	public String toString() {
		return start.x + " " + start.y + " " + end.x + " " + end.y;
	}
}