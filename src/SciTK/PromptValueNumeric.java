package SciTK;

/** Implement a class which wraps some functionality for
 * getting a numeric value from a user via prompt with type and value
 * checking implemented here.
 *
 * @package SciTK
 * @class PromptValueNumeric
 * @brief A value to prompt the user for, with type and limits
 * @author Alex Zylstra
 * @date 2013/05/19
 * @copyright Alex Zylstra
 * @license SciTK / MIT License
 */
public class PromptValueNumeric extends PromptValue
{
	/** if the value set is valid given type, limits */
	private boolean isValid; 
	/** if we should be performing limit checking */
	private boolean useLimits; 
	/** internal representation: value stored as Double */
	private Double value; 
	/** internal representation: maximum value stored as Double */
	private Double max; 
	/** internal representation: minimum value stored as Double */
	private Double min; 

	// ---------------------------------------
	//				Constructors
	// ---------------------------------------
	/** Default constructor, which takes no arguments */
	public PromptValueNumeric()
	{
		super();

		isValid = false;
		useLimits = false;
	}

	/** 
	 * Constructor with given value string
	 * @param valueString a String representing the numerical value
	 */
	public PromptValueNumeric(String valueString)
	{
		this();
		this.valueString = valueString;
	}

	/** 
	* Constructor with given value string and description
	* @param valueString a String representing the numerical value
	* @param description a text description of what this value is
	*/
	public PromptValueNumeric(String valueString, String description)
	{
		this(valueString);
		this.description = description;
	}

	// ---------------------------------------
	//				Data Access
	// ---------------------------------------
	/** Get the value cast as an int.
	 * @return integer representation of this value (0 if not valid)
	 */
	int get_value_int()
	{
		if( isValid )
			return value.intValue();
		else
			return (int)Double.NaN;
	}
	/** Get the value cast as an long.
	 * @return long representation of this value (0 if not valid)
	 */
	long get_value_long()
	{
		if ( isValid )
			return value.longValue();
		else
			return (long)Double.NaN;
	}
	/** Get the value cast as an float.
	 * @return float representation of this value (NaN if not valid)
	 */
	float get_value_float()
	{
		if( isValid )
			return value.floatValue();
		else
			return Float.NaN;
	}
	/** Get the value cast as an double.
	 * @return double representation of this value (NaN if not valid)
	 */
	double get_value_double()
	{
		if( isValid )
			return value.doubleValue();
		else
			return Double.NaN;
	}
	/** Get whether the current stored value is valid
	 * @return true if the value is valid
	 */
	public boolean isValid()
	{
		return isValid;
	}

	// ---------------------------------------
	//	   Value/Description String methods
	// ---------------------------------------
	/** 
	 * Set and parse a new string. 
	 * @param newValueString the String to parse and store in this PromptValueNumeric
	 */
	public void setValueString(String newValueString)
	{
		valueString = newValueString;
		// parse:
		try
		{
			value = new Double(valueString);
			isValid = true;
		}
		catch (NumberFormatException e)
		{
			isValid = false;
		}
		catch (NullPointerException e)
		{
			isValid = false;
		}

		// perform limit checking:
		if( useLimits )
		{
			// sanity check:
			if( value != null && min != null && max != null )
				isValid = isValid && ( value <= max && value >= min );
			else
				isValid = false;
		}
	}

	// ---------------------------------------
	//		Implement max/min functionality
	// ---------------------------------------
	/** Set the maximum acceptable value
	* @param new_max maximum valid value (inclusive)
	*/
	public void setMax(int new_max)
	{
		// set the value:
		setMax((double) new_max);
	}

	/** Get the maximum acceptable value
	* @return max value cast as int
	*/
	public int getMaxInt()
	{
		return max.intValue();
	}

	/** Set the maximum acceptable value
	* @param new_max maximum valid value (inclusive)
	*/
	public void setMax(long new_max)
	{
		// set the value:
		setMax((double) new_max);
	}

	/** Get the maximum acceptable value
	* @return max value cast as long
	*/
	public long getMaxLong()
	{
		return max.longValue();
	}

	/** Set the maximum acceptable value
	* @param new_max maximum valid value (inclusive)
	*/
	public void setMax(float new_max)
	{
		// set the value:
		setMax((double) new_max);
	}

	/** Get the maximum acceptable value
	* @return max value cast as float
	*/
	public float getMaxFloat()
	{
		return max.floatValue();
	}

	/** Set the maximum acceptable value
	* @param new_max maximum valid value (inclusive)
	*/
	public void setMax(double new_max)
	{
		// set the value:
		max = new Double(new_max);
		// turn on limit checking and reparse to include the new limit:
		useLimits = true;
		setValueString(valueString);
	}

	/** Get the maximum acceptable value
	* @return max value cast as double
	*/
	public double getMaxDouble()
	{
		return max.doubleValue();
	}


	
	/** Set the minimum acceptable value
	* @param new_min minimum valid value (inclusive)
	*/
	public void setMin(int new_min)
	{
		// set the value:
		setMin((double) new_min);
	}

	/** Get the minimum acceptable value
	* @return min value cast as int
	*/
	public int getMinInt()
	{
		return min.intValue();
	}

	/** Set the minimum acceptable value
	* @param new_min minimum valid value (inclusive)
	*/
	public void setMin(long new_min)
	{
		// set the value:
		setMin((double) new_min);
	}

	/** Get the minimum acceptable value
	* @return min value cast as long
	*/
	public long getMinLong()
	{
		return min.longValue();
	}

	/** Set the minimum acceptable value
	* @param new_min minimum valid value (inclusive)
	*/
	public void setMin(float new_min)
	{
		// set the value:
		setMin((double) new_min);
	}

	/** Get the minimum acceptable value
	* @return min value cast as float
	*/
	public float getMinFloat()
	{
		return min.floatValue();
	}

	/** Set the minimum acceptable value
	* @param new_min minimum valid value (inclusive)
	*/
	public void setMin(double new_min)
	{
		// set the value:
		min = new Double(new_min);
		// turn on limit checking and reparse to include the new limit:
		useLimits = true;
		setValueString(valueString);
	}

	/** Get the minimum acceptable value
	* @return min value cast as double
	*/
	public double getMinDouble()
	{
		return min.doubleValue();
	}
}
