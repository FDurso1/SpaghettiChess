package chess;

public class Square {
int row;
int col;
String piece;
String color;

	public Square(int r, int c, String thePiece, String theColor) {
		row = r;
		col = c;
		piece = thePiece;
		color = theColor;
	}
}
