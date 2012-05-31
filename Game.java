import java.util.Random;

public class Game {

	public Board board;
	public Player[] allPlayers;
	public double startingMoney;
	public int numberOfPlayers;
	int[] rolls = new int[2];
	Random generator = new Random();
	
	
	public Game(String filename, int numberOfPlayers, double startingMoney) throws NullBoardException {
		this(filename);
		this.numberOfPlayers = numberOfPlayers;
		setPlayers();
		setStartingMoney(startingMoney);
		
	}
	
	
	public Game(String filename) throws NullBoardException {
		board = new Board(filename);
		
		
	}
	
	public void turn(Player currentPlayer) {
		if (currentPlayer.getInJail()) { jailMode(currentPlayer); if (currentPlayer.getInJail()) { return; } } //If still in jail, done.
		int steps = roll();
		for (int i = 0; i < steps; i = i + 1) {
			movePlayer(currentPlayer);
		}
		movePlayer(currentPlayer, steps);
		activateEffect(currentPlayer);
	}
	
	/**
	 * @param currentPlayer
	 * Moves 'currentPlayer' one space and checks to see if the player is on the
	 * "Go" space and adds $200 to 'currentPlayer' money
	 */
	public void movePlayer(Player currentPlayer) {
		currentPlayer.incrementSpace();
		if (currentPlayer.getOnSpace() == 0) { currentPlayer.addMoney(200); }
	}
	
	/**
	 * @param currentPlayer
	 * @param toSpace
	 * Moves 'currentPlayer' to the space specified.
	 */
	public void movePlayer(Player currentPlayer, int toSpace) {
		currentPlayer.setSpace(toSpace);
	}
	
	public void movePlayerToSpace(Player currentPlayer, int spaceToLandOn) {
		while (currentPlayer.getOnSpace() != spaceToLandOn) {
			movePlayer(currentPlayer);
		}
	}
	
	public void movePlayerToGroup(Player currentPlayer, int group) {
		int onGroup = -1;
		while (onGroup != group) {
			movePlayer(currentPlayer);
			Space currentSpace = board.getSpace(currentPlayer.getOnSpace());
			if (!(currentSpace instanceof Property)) { onGroup = -1; continue; }
			onGroup = ((Property) currentSpace).getGroup();
		}
	}
	
	/**
	 * @param currentPlayer
	 * Moves the player to jail.
	 */
	public void movePlayerToJail(Player currentPlayer) {
		currentPlayer.setSpace(10);
		currentPlayer.setInJail(true);
		currentPlayer.setTimeInJail(0);
	}
	
	public void activateEffect(Player currentPlayer) {
		Space currentSpace = board.getSpace(currentPlayer.getOnSpace());
		if (currentSpace instanceof BigSpace && currentSpace.getName().equals("Go to Jail")) {
			movePlayerToJail(currentPlayer);
		}
		if (currentSpace instanceof NotProperty) {
			String whatType = ((NotProperty) currentSpace).getType().toString();
			if (whatType.equals("Chance")) { drawCard(currentPlayer, false); }
			if (whatType.equals("CommunityChest")) { drawCard(currentPlayer, true); }
			if (whatType.equals("IncomeTax")) { if (currentPlayer.getMoney() < 200) { currentPlayer.addMoney(-currentPlayer.getMoney() * 0.10); } else { currentPlayer.addMoney(-200); } }
			if (whatType.equals("LuxuryTax")) { currentPlayer.addMoney(-75); }
		}
	}

	public void jailMode(Player currentPlayer) {
		//See if the user has been in jail for three turns
		if (currentPlayer.getTimeInJail() == 3) { currentPlayer.setInJail(false); return; }

		currentPlayer.incrementTimeInJail();
		
		//***TODO: Tell user to roll doubles or pay 50 or use the out of jail card to get out
		
	}
		
	/**
	 * @param pile True for Community Chest, false for Chance
	 * Draw a card from the pile specified by 'pile' and perform the appropriate actions as stated on the card.
	 */
	private void drawCard(Player currentPlayer, boolean pile) {
		Card drawn = board.removeCard(pile);
		double money = drawn.getMoney() + (getNumberOfPlayers() - 1)*drawn.getCostPerPerson() +
				currentPlayer.getNumberOfHouses()*drawn.getCostOfHouseAndHotel()[0] +
				currentPlayer.getNumberOfHotels()*drawn.getCostOfHouseAndHotel()[1];
		currentPlayer.addMoney(money);
		
		//***TODO: What about when the person is supposed to go to a railroad/utility? Cost = 2x or 4x.

		//Subtract cost from each player if needed
		if (drawn.getCostPerPerson() > 0) {
			for (int i = 0; i < allPlayers.length; i = i + 1) { if (allPlayers[i] == currentPlayer) { continue; } allPlayers[i].addMoney(-drawn.getCostPerPerson());	}
		}
		
		if (drawn.getName().equals("Get Out of Jail Card")) { currentPlayer.incrementNumJailCard(); }
		
		//Move the player to the appropriate space according to some cases:
		//Case 1: Increment is true and space is valid - move to a space
		if (drawn.isIncrement() && (drawn.getSpace() < 40 && drawn.getSpace() >= 0)) {
			movePlayerToSpace(currentPlayer, drawn.getSpace());
		}
		//Case 2: Increment is true and space is not valid and group is valid - move to a group
		if (drawn.isIncrement() && drawn.getSpace() > 40 && drawn.getGroup() >= 0) {
			movePlayerToGroup(currentPlayer, drawn.getGroup());
		}
		//Case 3: Increment is false and space is valid - set player to a specific space
		if (!drawn.isIncrement() && drawn.getSpace() >= 0 && drawn.getSpace() < 40) {
			movePlayer(currentPlayer, drawn.getSpace());
			//Check to see if in jail or not:
			if (drawn.getName().equals("Go to Jail")) { movePlayerToJail(currentPlayer); }
		}
		checkToReshuffle();
		
		activateEffect(currentPlayer);
		
	}
	
	/**
	 * Reshuffles cards if there are no more cards in a certain pile.
	 */
	public void checkToReshuffle() {
		if (board.numberOfCommunityCards() == 0) { board.reshuffleCards(true); }
		if (board.numberOfChanceCards() == 0) { board.reshuffleCards(false); }
	}
	
	/**
	 * @return Rolls two dice and stores the result into "rolls"
	 */
	public int roll() {
		rolls[0] = rollDice();
		rolls[1] = rollDice();
		return rolls[0] + rolls[1];
	}
	
	/**
	 * @return One dice roll
	 */
	public int rollDice() {
		return generator.nextInt(6) + 1;
	}
	
	/**
	 * @param n
	 * @param player
	 * Add a player to position n.
	 */
	public void addPlayer(int n, Player player) {
		allPlayers[n] = player;
	}
	
	public void createPlayers(Player[] players) {
		for (int i = 0; i < players.length; i = i + 1) {
			allPlayers[i] = players[i];
		}
	}
	
	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}
	
	public void setPlayers(int numberOfPlayers) {
		allPlayers = new Player[numberOfPlayers];
	}
	
	public void setPlayers() {
		this.setPlayers(numberOfPlayers);
	}
	
	public int getNumberOfPlayers() {
		return allPlayers.length;
	}
	
	public void setStartingMoney(double startingMoney) {
		this.startingMoney = startingMoney;
	}
	
	
	
	
}
