package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

public class ActivityData {
	
	String name;
	int minAge;
	int maxAge;
	int capacity;
	
	
	
	public String getName() {
		return name;
	}
	public int getMinAge() {
		return minAge;
	}
	public int getMaxAge() {
		return maxAge;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}
	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
