package chess;

public class Rook {

	String name = "theSteadFast";
	String color;
	
	public Rook(String theName, String theColor) {
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
