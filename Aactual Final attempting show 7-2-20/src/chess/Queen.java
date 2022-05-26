package chess;

public class Queen {
	String name = "Vistoria";
	String color;

	public Queen(String theName, String theColor) {
		name = theName;
		color = theColor;
	}
	public String getColor() {
		return color;
	}
	public String toString() {
		return name;
	}
}
