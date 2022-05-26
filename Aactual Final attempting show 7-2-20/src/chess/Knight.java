package chess;

public class Knight {
	String name = "Lancelot";
	String color;

	public Knight(String theName, String theColor) {
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
