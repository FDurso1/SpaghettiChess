package chess;

public class Pawn {
	String name = "Steve";
	String color;

	public Pawn(String theName, String theColor) {
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
