package SciTK;

/** Implement a class which wraps some functionality for
 * getting a String from a user via prompt
 *
 * @package SciTK
 * @class PromptValueString
 * @brief Used for getting a String from the user
 * @author Alex Zylstra
 * @date 2013/05/19
 * @copyright Alex Zylstra
 * @license SciTK / MIT License
 */
public class PromptValueString extends PromptValue
{
	private boolean isValid;

	// ---------------------------------------
	//				Constructors
	// ---------------------------------------
	/** Default constructor, which takes no arguments */
	public PromptValueString()
	{
		super();
	}

	/** 
	 * Constructor with given value string
	 * @param new_value_str a String representing the numerical value
	 */
	public PromptValueString(String valueString)
	{
		super(valueString);
	}

	/** 
	* Constructor with given value string and description
	* @param new_value_str a String representing the numerical value
	* @param new_description a text description of what this value is
	*/
	public PromptValueString(String valueString, String description)
	{
		super(valueString,description);
	}

	/**
	 * Check if the current input stored in this class is considered valid.
	 * @return true if the current value is valid, false otherwise
	 */
	public boolean isValid()
	{
		return isValid;
	}
	
	/**
	 * Set the value string.
	 * @param newValueString the new value string to use for this object
	 */
	public void setValueString(String newValueString)
	{
		// sanity check for null values
		if( newValueString == null )
		{
			isValid = false;
		}
		else
		{
			// no type checking (yet?) in PromptValueString:
			isValid = true;

			// set string:
			valueString = newValueString;
		}
	}
}