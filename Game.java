import java.util.LinkedList;
import java.util.Random;

public class Game {

	Board board; //Board for this game
	Player[] players; //Players for this game
	private int turn = -1; //Whose turn it is; initialized at -1 to start the game.
	
	public LinkedList<Integer> spacesMovedTo = new LinkedList<Integer>();
	Card drawnCard;
	
	Player currentPlayer; //The current player in the game
	
	Random generator = new Random(); //For rolling the dices
	
	//Status bits
	private static final int onGo = 10;
	private static final int onJail = 11;
	private static final int onFreeParking = 12;
	private static final int onIncome = 20;
	private static final int onLuxury = 21;
	private static final int drewGoToJail = 220;
	private static final int drewGoToGo = 221;
	private static final int drewGetOutOfJailCard = 222;
	private static final int drewGoBack = 223;
	private static final int drewMoveToSpace = 224;
	private static final int drewMoveToGroup = 225;
	private static final int drewSetSpace = 226;
	private static final int drewReward = 227;
	private static final int canBuy = 30;
	private static final int isMortgaged = 31;
	private static final int paidMoney = 32;
	
	private static final int notEnoughMoney = 1000;
	private static final int boughtProperty = 1001;
	private static final int doesNotOwnProperty = 1002;
	private static final int mortgagedProperty = 1003;
	private static final int alreadyMortgaged = 1004;
	private static final int notMortgaged = 1005;
	private static final int unmortgagedProperty = 1006;
	private static final int doesNotOwnAllPropertiesInGroup = 1007;
	private static final int hotelIsBuilt = 1008;
	private static final int didNotBuildEvenly = 1009;
	private static final int houseBuilt = 1010;
	private static final int hotelBuilt = 1011;
	private static final int mustSellEvenly = 1012;
	private static final int noHousesToSell = 1013;
	private static final int soldHotel = 1014;
	private static final int soldHouse = 1015;
	
	private static final int playerIsBankrupt = -255;
	
	/* ROLL STUFF */
	/**
	 * @return The sum of the dice rolled by the player.
	 * Returns -1 if the current player has rolled three doubles.
	 * Note: What the player rolls is stored in the player's object.
	 */
	public int roll() {
		int roll1 = rollDice();
		int roll2 = rollDice();
		if (roll1 == roll2) { currentPlayer.incrementNumOfDoubles(); currentPlayer.setRolledDoubles(true); }
		else { currentPlayer.setRolledDoubles(false); }
		if (currentPlayer.getNumOfDoubles() == 3) { return -1; }
		return roll1 + roll2;
	}
	
	public int rollDice() {
		return generator.nextInt(6) + 1;
	}
	
	
	/* MOVE STUFF */
	/**
	 * @return The space the player is on after moving one spot.
	 * Moves the player one spot forward and then returns which
	 * spot the player is on. Also puts each space the player is
	 * on into the LinkedList for reference.
	 * Adds 200 to currentPlayer if the current space is 0
	 * ("Go" space).
	 */
	public int movePlayer() {
		currentPlayer.incrementSpace();
		spacesMovedTo.add(currentPlayer.getOnSpace());
		if (currentPlayer.getOnSpace() == 0) { currentPlayer.addMoney(200); }
		return currentPlayer.getOnSpace();
	}
	
	/**
	 * @param space Space to move the player to.
	 * Moves the player to a certain space.
	 */
	public void setPlayerSpace(int space) {
		currentPlayer.setSpace(space);
		spacesMovedTo.add(space);
	}
	
	/**
	 * @param space Space to move the player to.
	 * Moves the player to the space specified by moving the player
	 * one space at a time through the movePlayer() method.
	 */
	public void movePlayerToSpace(int space) {
		while (movePlayer() != space) { /* Dummy stuff. */ }
	}
	
	/**
	 * @param group Group to move the player to.
	 * Moves the player to the group specified by moving the player
	 * one space at a time through the movePlayer() method.
	 */
	public void movePlayerToGroup(int group) {
		boolean onGroup = false;
		while (!onGroup) {
			movePlayer();
			if (board.getSpace(currentPlayer.getOnSpace()) instanceof Property) {
				Property prop = (Property) board.getSpace(currentPlayer.getOnSpace());
				if (prop.getGroup() == group) { onGroup = true; }
			}
		}
	}
	
	/**
	 * @return The space the player is on after moving backwards once.
	 * Moves the player one spot backward and then returns which
	 * spot the player is on. Also puts each space the player is
	 * on into the LinkedList for reference.
	 */
	public int movePlayerBackwards() {
		currentPlayer.decrementSpace();
		spacesMovedTo.add(currentPlayer.getOnSpace());
		return currentPlayer.getOnSpace();
	}
	
	/**
	 * Puts the player in jail.
	 */
	public void putInJail() {
		currentPlayer.setSpace(10);
		currentPlayer.setInJail(true);
	}
	
	
	/* ACTIVATE EFFECT STUFF */
	/**
	 * @return Status bit for what happened from the effect.
	 * Activates the effect based on the player's current space and then
	 * returns the appropriate status bit for what happened from the effect.
	 */
	public int activateEffect() {
		int playerOnSpace = currentPlayer.getOnSpace();
		Space currentSpace = board.getSpace(playerOnSpace);
		if (currentSpace instanceof BigSpace) { return activateEffect((BigSpace) currentSpace); }
		else if (currentSpace instanceof NotProperty) { return activateEffect((NotProperty) currentSpace); }
		else { return activateEffect((Property) currentSpace); }
	}
	
	/**
	 * @param bigSpace The BigSpace the player is on.
	 * @return The appropriate status bit for what happens to the player.
	 */
	public int activateEffect(BigSpace bigSpace) {
		if (bigSpace.getName().equals("Go")) { return Game.onGo; }
		if (bigSpace.getName().equals("Jail")) { return Game.onJail; }
		if (bigSpace.getName().equals("Free Parking")) { return Game.onFreeParking; }
		putInJail(); return 13;
	}
	
	/**
	 * @param notProperty The NotPlayer the player is on.
	 * @return The appropriate status bit for what happens to the player.
	 */
	public int activateEffect(NotProperty notProperty) {
		if (notProperty.getName().equals("Income Tax")) { currentPlayer.addMoney(-75); return Game.onIncome; }
		if (notProperty.getName().equals("Luxury Tax")) { currentPlayer.addMoney(-200); return Game.onLuxury; } //***TODO: Decide which one the player wants to play
		// [ Player landed on Chance or Community Chest ]
		if (notProperty.getName().equals("Chance")) { return drawCard(false); }
		return drawCard(true);
	}

	/**
	 * @param cardType True for Community, false for Chance.
	 * @return The appropriate status bit for what happens to the player.
	 */
	public int drawCard(boolean cardType) {
		drawnCard = board.removeCard(cardType);
		
		if (drawnCard.getName().equals("Go to Jail")) { putInJail(); return Game.drewGoToJail; }
		if (drawnCard.getName().equals("Advance to Go")) { currentPlayer.addMoney(200); currentPlayer.setSpace(0); return Game.drewGoToGo; }
		if (drawnCard.getName().equals("Get Out of Jail Card")) { currentPlayer.incrementNumJailCard(); return Game.drewGetOutOfJailCard; }
		if (drawnCard.getName().equals("Go Back")) { for (int i = 0; i < Math.abs(drawnCard.getSpace()); i = i + 1) { movePlayerBackwards(); } return Game.drewGoBack; }
		
		if (drawnCard.isIncrement()) {
			int spaceOrGroup = drawnCard.getSpace();
			if (drawnCard.getSpace() < 40) { movePlayerToSpace(spaceOrGroup); return Game.drewMoveToSpace; }
			movePlayerToGroup(spaceOrGroup); return Game.drewMoveToGroup;
		}
		
		if (drawnCard.isIncrement() && drawnCard.getSpace() < 40) { this.setPlayerSpace(drawnCard.getSpace()); return Game.drewSetSpace; }
		
		double totalReward =
				drawnCard.getReward() + ((double) players.length) * drawnCard.getCostPerPerson() +
				((double) currentPlayer.getNumberOfHouses()) * drawnCard.getCostPerHouse() +
				((double) currentPlayer.getNumberOfHotels()) * drawnCard.getCostPerHotel();
		//If this affects each player
		if (drawnCard.getCostPerPerson() != 0) {
			double costPerPerson = drawnCard.getCostPerPerson();
			for (int i = 0; i < players.length; i = i + 1) {
				if (costPerPerson < 0 && players[i] != currentPlayer) { players[i].addMoney(costPerPerson); }
				else if (drawnCard.getCostPerPerson() > 0 && players[i] != currentPlayer) { players[i].addMoney(costPerPerson); }
			}
		}
		currentPlayer.addMoney(totalReward); return Game.drewReward;
	}

	
	/**
	 * @param property What property the effect is activated on.
	 * @return The appropriate status bit for what happens to the player.
	 */
	public int activateEffect(Property property) {
		Property theProperty = (Property) board.getSpace(currentPlayer.getOnSpace());
		if (!theProperty.isOwned()) { return Game.canBuy; }
		if (theProperty.isMortgage()) { return Game.isMortgaged; }
		currentPlayer.addMoney(property.getRentPrice()); return Game.paidMoney;
	}
	
	/* PROPERTY STUFF */
	/**
	 * @return The appropriate status bit for if the player bought the property or not.
	 * Precondition: The space the current player is on must be a property.
	 */
	public int buyProprety() {
		int currentSpace = currentPlayer.getOnSpace();
		Property currentProperty = (Property) board.getSpace(currentSpace);
		if (currentPlayer.getMoney() < currentProperty.getCostToBuy()) { return Game.notEnoughMoney; }
		currentPlayer.addMoney(-currentProperty.getCostToBuy());
		currentPlayer.addProperty(currentSpace);
		currentProperty.setOwned(currentPlayer);
		return Game.boughtProperty;
	}
	
	/**
	 * @param propertySpace Property to mortgage.
	 * @return The appropriate status bit for if the player mortgaged the property or not.
	 */
	public int mortgagedProperty(int propertySpace) {
		if (!currentPlayer.ownsProperty(propertySpace)) { return Game.doesNotOwnProperty; }
		Property currentProperty = (Property) board.getSpace(propertySpace);
		if (currentProperty.isMortgage()) { return Game.alreadyMortgaged; }
		currentProperty.setIsMortgage(true);
		currentPlayer.addMoney(currentProperty.getMortgageCost());
		return Game.mortgagedProperty;
	}
	
	/**
	 * @param propertySpace Property to unmortgage.
	 * @return The appropriate status bit for if the player mortgaged the property or not.
	 * ***TODO: Duplicate code as mortgagedProperty!
	 */
	public int unmortgageProperty(int propertySpace) {
		if (!currentPlayer.ownsProperty(propertySpace)) { return Game.doesNotOwnProperty; }
		Property currentProperty = (Property) board.getSpace(propertySpace);
		if (!currentProperty.isMortgage()) { return Game.notMortgaged; }
		double costToUnmortgage = currentProperty.getMortgageCost() * 1.10;
		if (currentPlayer.getMoney() < costToUnmortgage) { return Game.notEnoughMoney; }
		currentPlayer.addMoney(-costToUnmortgage);
		currentProperty.setIsMortgage(false);
		return Game.unmortgagedProperty;
	}
	
	/**
	 * @param propertySpace Property at which to build the house.
	 * @return The appropriate status bit for what happened while building the house.
	 */
	public int buildHouse(int propertySpace) {
		if (!currentPlayer.ownsProperty(propertySpace)) { return Game.doesNotOwnProperty; }
		Property currentProperty = (Property) board.getSpace(propertySpace);
		int currentGroup = currentProperty.getGroup();
		Property[] allPropertiesInGroup = board.getGroup(currentGroup);
		int numHouses[] = new int[allPropertiesInGroup.length];
		int maxHouses = -1;
		//Check to see that all the properties in the group are owned by the current player
		for (int i = 0; i < allPropertiesInGroup.length; i = i + 1) {
			if (allPropertiesInGroup[i].getOwnedBy() != currentPlayer) { return Game.doesNotOwnAllPropertiesInGroup; }
			numHouses[i] = allPropertiesInGroup[i].getNumOfHouses();
			if (numHouses[i] > maxHouses) { maxHouses = numHouses[i]; }
		}
		if (currentProperty.isHotelOwned()) { return Game.hotelIsBuilt; }
		//Check to see that the house is being built on the property with the least number of houses
		//or that all the houses have the same number of houses
		if (currentProperty.getNumOfHouses() == maxHouses) {
			for (int i = 0; i < numHouses.length; i = i + 1) { if (numHouses[i] != maxHouses) { return Game.didNotBuildEvenly; } }
		}
		double costOfHouse = currentProperty.getCostOfHouse();
		if (currentPlayer.getMoney() < costOfHouse) { return Game.notEnoughMoney; }
		currentPlayer.addMoney(-costOfHouse); 
		//Check to see if building hotel
		if (currentProperty.getNumOfHouses() == Board.maxNumHouses) {
			currentProperty.setHotelOwned(true); return Game.hotelBuilt;
		}
		currentProperty.incrementNumOfHouses(); return Game.houseBuilt;
	}
	
	/**
	 * @param propertySpace Property to sell a house from.
	 * @return The appropriate status bit for what happens when the player tries to sell a house.
	 */
	public int sellHouse(int propertySpace) {
		if (!currentPlayer.ownsProperty(propertySpace)) { return Game.doesNotOwnProperty; }
		Property currentProperty = (Property) board.getSpace(propertySpace);
		int currentGroup = currentProperty.getGroup();
		Property[] allPropertiesInGroup = board.getGroup(currentGroup);
		int numHouses[] = new int[allPropertiesInGroup.length];
		int maxHouses = -1;
		//Check to see that all the properties in the group are owned by the current player
		for (int i = 0; i < allPropertiesInGroup.length; i = i + 1) {
			if (allPropertiesInGroup[i].getOwnedBy() != currentPlayer) { return Game.doesNotOwnAllPropertiesInGroup; }
			numHouses[i] = allPropertiesInGroup[i].getNumOfHouses() + (allPropertiesInGroup[i].isHotelOwned() ? 1 : 0);
			if (numHouses[i] > maxHouses) { maxHouses = numHouses[i]; }
		}
		if (maxHouses == 0) { return Game.noHousesToSell; }
		//Check to see that the house being demolished is from the one with the maximum number
		if (currentProperty.getNumOfHouses() != maxHouses) { return Game.mustSellEvenly; }
		if (currentProperty.isHotelOwned()) {
			currentProperty.setHotelOwned(false); return Game.soldHotel;
		}
		currentProperty.decrementNumOfHouse(); return Game.soldHouse;
	}
	
	
	/* BANKRUPT STUFF */
	public void declareBankrupt() {
		//***TODO: declareBankrupt for this person.
		nextTurn();
	}
	
	/**
	 * @return True if the current player is bankrupt; false otherwise.
	 * Checks to see if the player is bankrupt. If so, then the player will
	 * have this field modified appropriately.
	 * TODO: Check AFTER activate effect
	 */
	public boolean isBankrupt() {
		if (currentPlayer.getMoney() < 0) { currentPlayer.setBankrupt(true); return true; }
		return false;
	}

	public boolean anyBankrupt() {
		//***TODO: This
		return false;
	}
	
	/* TURN STUFF */
	public void nextTurn() {
		do {
			incrementTurn();
		}
		while (!players[getTurn()].getBankrupt());
	}
	
	//***TODO: Add the stuff to figure out whose turn, etc.
	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}
	
	public void incrementTurn() {
		turn = (turn + 1) % players.length;
	}
	
	
}
