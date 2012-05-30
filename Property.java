
public class Property extends Space {
	
	public double costToBuy;
	public double mortgageCost;
	public boolean isMortgage;
	public int group;
	public int numOfHouses;
	public boolean hotelOwned;
	public double costOfHouse;
	public boolean isOwned;
	public Player ownedBy;
	
	public double[] rent;
	
	
	//<Name>,<Description>,<Cost>,<Mortgage>,<Group>,<CostOfHouse>,<Rent>,<Rent with 1 House>,<2>,<3>,<4>,<Hotel>	
	public Property(String name, String description, double cost, double mortgage, int group, double costOfHouse, double rent[]) {
		super(name, description);
		this.costToBuy = cost;
		this.mortgageCost = mortgage;
		this.group = group;
		this.costOfHouse = costOfHouse;
		this.rent = rent;
	}
	
	
	public double getCostToBuy() {
		return costToBuy;
	}


	public double getMortgageCost() {
		return mortgageCost;
	}


	public boolean isMortgage() {
		return isMortgage;
	}


	public int getGroup() {
		return group;
	}


	public int getNumOfHouses() {
		return numOfHouses;
	}


	public boolean isHotelOwned() {
		return hotelOwned;
	}


	public double getCostOfHouse() {
		return costOfHouse;
	}


	public boolean isOwned() {
		return isOwned;
	}


	public Player getOwnedBy() {
		return ownedBy;
	}


	public double[] getRent() {
		return rent;
	}


	public String toString() {
		//<Name>,<Description>,<Cost>,<Mortgage>,<Group>,<CostOfHouse>,<Rent>,<Rent with 1 House>,<2>,<3>,<4>,<Hotel>
		String rentString = "";
		for (int i = 0; i < rent.length; i = i + 1) {
			if (i == 0) { rentString = "" + rent[i]; }
			else { rentString = rentString + " " + rent[i]; }
		}
		return super.toString() + " Cost: " + getCostToBuy() + " Mortgage: " + getMortgageCost() +
				" Group: " + getGroup() + " Cost of House: " + getCostOfHouse() + " Rent: " + rentString;
	}
}