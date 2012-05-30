import java.util.HashSet;


public class Player {

	public String name;
	public double money;
	public int onSpace;
	public HashSet<Integer> spacesOwned;
	public boolean inJail; //***TODO: Put this in a better spot.
	
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
	
	public void incrementSpace() {
		this.onSpace++;
	}
	
	public void setSpace(int onSpace) {
		this.onSpace = onSpace;
	}

	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setMoney(double money) {
		this.money = money;
	}	
	
	
	
}
