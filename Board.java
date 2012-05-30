
public class Board {

	Parser parser;
	
	public final int numOfSpaces = 40;
	public Space[] allSpaces = new Space[40];
	
	public void setSpaces(int space, Space n) {
		allSpaces[space] = n;
	}
	
	public Board(String filename) throws NullBoardException {
		parser = new Parser(filename, this);
	}
		
	public void getInformationOfSpace(int spaceNumber) {
		System.out.println(allSpaces[spaceNumber].toString());
	}
	
}
