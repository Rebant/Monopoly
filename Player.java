import java.util.HashSet;


public class Player {

	public String name;
	public double money;
	public int onSpace;
	public HashSet<Integer> spacesOwned;
	public boolean inJail; //***TODO: Put this in a better spot.
	public int timeInJail;
	public int numberOfHouses;
	public int numberOfHotels;
	public boolean bankrupt = false;
	public int outOfJailCards = 0;
	public int[] rolls = new int[2];
	public int numOfDoubles;
	public boolean rolledDoubles;
	
	private boolean turn;
	
	public Player(String name, double money, int onSpace) {
		this.name = name;
		this.money = money;
		this.onSpace = onSpace;
	}
	
	/**
	 * @param name
	 * @param money
	 * Default player to add.
	 */
	public Player(String name, double money) {
		this(name, money, 0);
	}
	
	/**
	 * Move one space.
	 * Note: Spaces goes from 0 to to numOfSpaces as defined
	 * by the board the player is on, inclusive.
	 */
	public void incrementSpace() {
		onSpace = (onSpace + 1) % Board.numOfSpaces;
	}
	
	/**
	 * Decrements one space.
	 * Note: Spaces goes from 0 to numOfSpaces as defined
	 * by the board the player is on, inclusive.
	 */
	public void decrementSpace() {
		onSpace = (onSpace - 1) % Board.numOfSpaces;
	}
	
	public void setSpace(int onSpace) {
		this.onSpace = onSpace;
	}
	
	/**
	 * @param property
	 * Adds a property owned to this player.
	 */
	public void addProperty(int prop) {
		spacesOwned.add(prop);
	}
	
	/**
	 * @param property
	 * Removes a property owned from this player.
	 */
	public void removeProperty(int prop) {
		spacesOwned.remove(prop);
	}

	public boolean ownsProperty(int prop) {
		if (spacesOwned.contains(prop)) { return true; }
		return false;
	}
	
	public void addMoney(double money) {
		this.money = money + money;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setMoney(double money) {
		this.money = money;
	}

	public int getOnSpace() {
		return onSpace;
	}

	public void setInJail(boolean inJail) {
		if (inJail) { timeInJail = 0; }
		this.inJail = inJail;
	}

	public boolean getInJail() {
		return inJail;
	}
	
	public void incrementTimeInJail() {
		this.timeInJail = timeInJail + 1;
	}
	public void setTimeInJail(int timeInJail) {
		this.timeInJail = timeInJail;
	}
	
	public int getTimeInJail() {
		return timeInJail;
	}
	
	public void setBankrupt(boolean bankrupt) {
		this.bankrupt = bankrupt;
	}
	
	public boolean getBankrupt() {
		return bankrupt;
	}

	public double getMoney() {
		return money;
	}
	
	public int getNumberOfHouses() {
		return numberOfHouses;
	}
	
	public void incrementNumberOfHouses() {
		numberOfHouses = numberOfHouses + 1;
	}
	
	public void decrementNumberOfHouses() {
		numberOfHouses = numberOfHouses - 1;
	}
	
	public void incrementNumberOfHotels() {
		numberOfHotels = numberOfHotels + 1;
	}
	
	public void decrementNumberOfHotels() {
		numberOfHotels = numberOfHotels - 1;
	}
	public int getNumberOfHotels() {
		return numberOfHotels;
	}

	public void incrementNumJailCard() {
		this.outOfJailCards = outOfJailCards + 1;
	}
	
	public void setTurn(boolean turn) {
		this.turn = turn;
	}
	
	public boolean isTurn() {
		return turn;
	}
	
	public void setRolledDoubles(boolean rolledDoubles) {
		this.rolledDoubles = rolledDoubles;
	}
	
	public boolean isRolledDoubles() {
		return rolledDoubles;
	}
	
	public void incrementNumOfDoubles() {
		numOfDoubles++;
	}
	public void setNumOfDoubles(int numOfDoubles) {
		this.numOfDoubles = numOfDoubles;
	}
	
	public int getNumOfDoubles() {
		return numOfDoubles;
	}
	
}
