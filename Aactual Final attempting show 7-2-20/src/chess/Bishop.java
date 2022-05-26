package chess;

public class Bishop {
	String name = "John";
	String color;

	public Bishop(String theName, String theColor) {
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
