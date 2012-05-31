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
	 * Note: Spaces goes from 0 to 39 inclusive.
	 */
	public void incrementSpace() {
		onSpace = (onSpace + 1) % Board.numOfSpaces;
	}
	
	public void setSpace(int onSpace) {
		this.onSpace = onSpace;
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
	
}
