package chess;

public class Empty {

	String name;
	int row;
	int col;
	
	public Empty(String theName) {
		name = theName;	
	}
	public Empty(String theName, int therow, int thecol) {
	row = therow;
	col = thecol;
	}
	public String toString() {
		return name;
	}
}
