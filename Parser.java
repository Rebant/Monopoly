import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Parser {

	Board board;
	Scanner scanner;
	public String currentLine;
	public int onSpace;
	public Space currentSpace;
	
	
	public Parser(String filename, Board board) throws NullBoardException {
		this.board = board;
		try {
			scanner = new Scanner(new File(filename));
			currentLine = scanner.nextLine();
		}
		catch (IOException ioe) { throw new NullBoardException("Cannot find the file."); }
		if (!currentLine.equals("Monopoly - Made by Rebant Srivastava")) { throw new NullBoardException("This is not a valid file because it does have the right first line."); }
		onSpace = 0;
		
		while (scanner.hasNextLine()) {
			if (onSpace % 10 == 0 && !(onSpace >= Board.numOfSpaces) ) { makeBigSpace(); }
			else {
				currentLine = scanner.nextLine();
				int k = currentLine.indexOf(":");
				String whatKind = currentLine.substring(0, k);
				if (whatKind.contains("Card")) { makeCard(whatKind, currentLine.substring(k + 2)); continue; }
				else if (!whatKind.equals("Property")) { makeNotProperty(whatKind); }
				else {
					String[] restOfString = currentLine.substring(10).split(",");
					if (restOfString.length < 8) { throw new NullBoardException("This is not a valid file as one of the properties" +
																				" does not have enough information."); }
					makeProperty(restOfString);
				}
			}
			board.setSpaces(onSpace, currentSpace);
			onSpace++;
		}
		
		if (onSpace != 40) { throw new NullBoardException("There are not enough spaces defined."); }
	}
	
	private void makeCard(String whatKind, String cardDescription) throws NullBoardException {
		boolean type;
		String[] description = cardDescription.split(",");
		if (description.length != 10) { System.out.println(whatKind); System.out.println(cardDescription); throw new NullBoardException("This card does not have enough information."); }
		if (whatKind.contains("Community")) { type = true; }
		else if (whatKind.contains("Chance")) { type = false; }
		else { throw new NullBoardException("This is not a valid type of card."); }
		Card toAdd = new Card(type, cardDescription.split(","));
		board.addCard(toAdd);
	}

	public void makeBigSpace() {
		switch(onSpace) {
			case 0 : currentSpace = new BigSpace("Go", "Collect $200 salary as you pass.", "", ""); break;
			case 10 : currentSpace = new BigSpace("Jail", "Pay $50, roll doubles, or use a Get Out of Jail card to get out. " +
					"You can spend a maximum of three turns on this space.", "Just", "Visiting"); break;
			case 20 : currentSpace = new BigSpace("Free Parking", "If you land on this space, collect $20.", "", ""); break;
			case 30 : currentSpace = new BigSpace("Go to Jail", "If you land on this space, you must go directly to Jail.", "", ""); break;
		}
		
	}
	
	public void makeNotProperty(String type) throws NullBoardException {
		currentSpace = new NotProperty(type);
	}
	
	public void makeProperty(String[] allProperties) throws NullBoardException {
		
		//<Name>,<Description>,<Cost>,<Mortgage>,<Group>,<CostOfHouse>,<Rent>,<Rent with 1 House>,<2>,<3>,<4>,<Hotel>
		try {
			String name = allProperties[0];
			String description = allProperties[1];
			int cost = Integer.parseInt(allProperties[2]);
			double mortgage = Double.parseDouble(allProperties[3]);
			int group = Integer.parseInt(allProperties[4]);
			double costOfHouse = Double.parseDouble(allProperties[5]);
			double[] rent = new double[allProperties.length - 6];
			for (int i = 6; i < allProperties.length; i = i + 1) {
				rent[i - 6] = Double.parseDouble(allProperties[i]);
			}
			currentSpace = new Property(name, description, cost, mortgage, group, costOfHouse, rent);
		}
		catch (Exception e) { throw new NullBoardException("One of the piece of data is not right..."); }
		
		
	}
	
}
