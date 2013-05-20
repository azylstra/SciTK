package SciTK;

/** Implement an abstract class for functionality which allows
 * for automatic type and value validation for user input
 * via SciTK dialogs.
 *
 * @package SciTK
 * @interface Prompt_Value
 * @brief Provide input validation for prompts
 * @author Alex Zylstra
 * @date 2013/05/19
 * @copyright Alex Zylstra
 * @license SciTK / MIT License
 */
public abstract class PromptValue
{
	/** a text string entered by the user */
	protected String valueString;
	/** a text description of what the value is */
	protected String description; 

	// ---------------------------------------
	//				Constructors
	// ---------------------------------------
	/** Default constructor, which takes no arguments */
	public PromptValue()
	{
		valueString = "";
		description = "";
	}

	/** 
	 * Constructor with given value string
	 * @param new_value_str a String representing the numerical value
	 */
	public PromptValue(String valueString)
	{
		this.valueString = valueString;
	}

	/** 
	* Constructor with given value string and description
	* @param new_value_str a String representing the numerical value
	* @param new_description a text description of what this value is
	*/
	public PromptValue(String valueString, String description)
	{
		this(valueString);
		this.description = description;
	}

	/**
	 * Check if the current input stored in this class is considered valid.
	 * @return true if the current value is valid, false otherwise
	 */
	abstract public boolean isValid();

	/**
	 * Set the value string.
	 * @param newValueString the new value string to use for this object
	 */
	abstract public void setValueString(String newValueString);

	/** 
	* Get the current string containing this value
	* @return the String representation of this value
	*/
	public String getValueString()
	{
		return valueString;
	}

	/** 
	* Set the description string
	* @param new_description A new description for the value stored in this object.
	*/
	public void setDescription(String newDescription)
	{
		description = newDescription;
	}

	/** 
	* Get a description of this value
	* @return a String containing a text description of this object.
	*/
	public String getDescription()
	{
		return description;
	}
}