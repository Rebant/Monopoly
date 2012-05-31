
public class NotProperty extends Space {

	public Type type;
	public final String followDirections = "and follow the directions on the card.";
	
	public NotProperty(String type) throws NullBoardException {
		if (!(type != "Chance" || type != "Community Chest" || type != "Income Tax" || type != "Luxury Text")) {
			throw new NullBoardException("This is not a valid type of space.");
		}
		if (type.equals("Chance")) { this.setType(Type.Chance); }
		if (type.equals("Community Chest")) { this.setType(Type.CommunityChest); }
		if (type.equals("Income Tax")) { this.setType(Type.IncomeTax); }
		if (type.equals("Luxury Tax")) { this.setType(Type.LuxuryTax); }
		this.setProperties();
	}
	
	public NotProperty(Type type) {
		this.setType(type);
		this.setProperties();
	}
	
	public enum Type {
		Chance, CommunityChest, IncomeTax, LuxuryTax;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setProperties() {
		switch (type) {
			case Chance : this.setName("Chance"); this.setDescription("Draw a card from the chance pile " + followDirections); break;
			case CommunityChest : this.setName("Community Chest"); this.setDescription("Draw a card from the community chest pile " + followDirections); break;
			case IncomeTax : this.setName("Income Tax"); this.setDescription("Pay whichever is lower: 10% of your wealth or $200."); break;
			case LuxuryTax : this.setName("Luxury Tax"); this.setDescription("Pay $75 if you land on this space."); break;
		}
	}
	
	public String toString() {
		return super.toString() + " Type: " + type.toString();
	}
	
}
