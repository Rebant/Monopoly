
public class BigSpace extends Space {

	public String topDescription;
	public String bottomDescription;
	
	public BigSpace(String name, String description, String topDescription, String bottomDescription) {
		super(name, description);
		this.topDescription = topDescription;
		this.bottomDescription = bottomDescription;
	}
	
	public String getTopDescription() {
		return topDescription;
	}

	public String getBottomDescription() {
		return bottomDescription;
	}

	public String toString() {
		return super.toString() + " Top Description: " + getTopDescription() + " Bottom Description: " + getBottomDescription();
	}

}
