
public class NotProperty extends Space {

	public Type type;
	public final String followDirections = "and follow the directions on the card";
	
	public NotProperty(String type) throws NullBoardException {
		if (!(type != "Chance" || type != "CommunityChest" || type != "IncomeTax" || type != "LuxuryText")) {
			throw new NullBoardException("This is not a valid type of space. It is " + type + "and is this the same as \"Property\": " + type.equals("Property"));
		}
		if (type.equals("Chance")) { this.setType(Type.Chance); }
		if (type.equals("CommunityChest")) { this.setType(Type.CommunityChest); }
		if (type.equals("IncomeTax")) { this.setType(Type.IncomeTax); }
		if (type.equals("LuxuryTax")) { this.setType(Type.LuxuryTax); }
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
	
	public void setProperties() {
		switch (type) {
			case Chance : this.setName("Chance"); this.setDescription("Draw a card from the chance pile " + followDirections); break;
			case CommunityChest : this.setName("Community Chest"); this.setDescription("Draw a card from the community chest pile " + followDirections); break;
			case IncomeTax : this.setName("Income Tax"); this.setDescription("Pay 10% of your wealth or $200"); break;
			case LuxuryTax : this.setName("Luxury Tax"); this.setDescription("Pay $75 if you land on this space."); break;
		}
	}
	
	public String toString() {
		return super.toString() + " Type: " + type.toString();
	}
	
}
