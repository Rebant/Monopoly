
public class Card {

	public boolean isCommunity;

	public String name;
	public String description;
	public double reward;
	public int space; //Over 39 means not moving specifically to a space
	public boolean isGood;
	public boolean increment; //True means going to a particular spot
	public double costPerPerson;
	public double[] costOfHouseAndHotel;
	public int group; //-1 means not moving to a group
	
	
	public Card(boolean isCommunity, String name, String description, double reward, int space, boolean isGood, boolean increment, double costPerPerson, double[] costOfHouseAndHotel, int group) {
		this.name = name;
		this.description = description;
		this.reward = reward;
		this.space = space;
		this.isCommunity = isCommunity;
		this.isGood = isGood;
		this.increment = increment;
		this.costPerPerson = costPerPerson;
		this.costOfHouseAndHotel = costOfHouseAndHotel;
		this.group = group;
	}
	
	public Card(boolean isCommunity, String[] cardDescription) throws NullBoardException {
		this(isCommunity, cardDescription[0], cardDescription[1], Double.parseDouble(cardDescription[2]), Integer.parseInt(cardDescription[3]),
				Boolean.parseBoolean(cardDescription[4]), Boolean.parseBoolean(cardDescription[5]), Double.parseDouble(cardDescription[6]),
				new double[] { Double.parseDouble(cardDescription[7]), Double.parseDouble(cardDescription[8]) }, Integer.parseInt(cardDescription[9]));
	}

	public boolean isCommunity() {
		return isCommunity;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public double getReward() {
		return reward;
	}

	public int getSpace() {
		return space;
	}

	public boolean isGood() {
		return isGood;
	}

	public boolean isIncrement() {
		return increment;
	}

	public double getCostPerPerson() {
		return costPerPerson;
	}

	public double[] getCostOfHouseAndHotel() {
		return costOfHouseAndHotel;
	}
	
	public double getCostPerHouse() {
		return costOfHouseAndHotel[0];
	}
	
	public double getCostPerHotel() {
		return costOfHouseAndHotel[1];
	}

	public int getGroup() {
		return group;
	}
	
	/**
	 * @return The same card except with a different reference. :p
	 */
	public Card createShadowCard() {
		boolean a = this.isCommunity();
		String b = this.getName();
		String c = this.getDescription();
		double d = this.getReward();
		int e = this.getSpace();
		boolean f = this.isGood();
		boolean g = this.isIncrement();
		double h = this.getCostPerPerson();
		double[] i = this.getCostOfHouseAndHotel();
		int j = this.getGroup();
		return new Card(a, b, c, d, e, f, g, h, i, j);
	}
	
}
