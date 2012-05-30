
public abstract class Space {

	public String name;
	public String description;
	
	public Space() {
		//Does nothing, really. :)
	}
	
	public Space(String name, String description) {
		setName(name);
		setDescription(description);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String toString() {
		return "Name: " + getName() + " Description: " + getDescription();
	}
	
}
