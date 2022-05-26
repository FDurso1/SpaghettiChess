package chess;

public class King {

	String name = "Arthur";
	String color;
	
	public King(String theName, String theColor) {
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
