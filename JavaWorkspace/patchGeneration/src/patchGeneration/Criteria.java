package patchGeneration;

/**
 * This class defines criteria for macro or function selection. Criteria are defined as:
 * 		nameComponent:
 * 			The name of the function must either include or not include a particular set of characters
 * 
 * 		mustHave:
 * 			Defines whether the function should or should not contain the phrase "nameComponent"
 */
public class Criteria {
	
	// Private member variables for the criteria components
	private String nameComponent;
	private boolean mustHave;
	
	// Constructor. Pass in criteria components
	public Criteria(String nameComponent, boolean mustHave)
	{
		this.nameComponent = nameComponent;
		this.mustHave = mustHave;
	}
	
	// Get the phrase for this criteria
	public String getNameComponent()
	{
		return nameComponent;
	}
	
	// Get a bool that determines whether the phrase must or must not be included
	public boolean mustHave()
	{
		return mustHave;
	}
}
