
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
	public boolean auction = false;
	
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

	public void setIsMortgage(boolean isMortgage) {
		this.isMortgage = isMortgage;
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
	
	public void setNumOfHouse(int numOfHouses) {
		this.numOfHouses = numOfHouses;
	}
	
	public void incrementNumOfHouses() {
		this.numOfHouses = numOfHouses + 1;
		ownedBy.incrementNumberOfHouses();
	}

	public void decrementNumOfHouse() {
		this.numOfHouses = numOfHouses - 1;
		ownedBy.decrementNumberOfHouses();
	}

	public boolean isHotelOwned() {
		return hotelOwned;
	}
	
	public void setHotelOwned(boolean hotelOwned) {
		if (hotelOwned) { ownedBy.incrementNumberOfHotels(); }
		else { ownedBy.decrementNumberOfHotels(); }
		this.hotelOwned = hotelOwned;
	}


	public double getCostOfHouse() {
		return costOfHouse;
	}

	public void setOwned(Player player) {
		ownedBy = player;
		isOwned = true;
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
	
	public double getRentPrice() {
		return rent[this.getNumOfHouses() + (this.hotelOwned ? 1 : 0)];
	}
	
	/**
	 * @param payer Player who is paying the owner.
	 * Payer pays the rent owed to the person who owns this property.
	 */
	public void payRent(Player payer) {
		payer.addMoney(-getRentPrice());
		ownedBy.addMoney(getRentPrice());
	}
	
	public void setAuctionMode() {
		auction = true;
	}
	
	public void doneAuction() {
		auction = false;
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