/** Implement a class which wraps some functionality for
 * getting a value from a user via prompt with type and value
 * checking implemented here.
 *
 * @package SciTK
 * @class Prompt_Value
 * @brief A value to prompt the user for, with type and limits
 * @author Alex Zylstra
 * @date 2013/04/10
 * @copyright Alex Zylstra
 * @license MIT
 */

package SciTK;

public class Prompt_Value
{
	protected String value_str; /* a text string entered by the user */
	protected String description; /* a text description of what the value is */
	protected boolean is_valid; /* if the value set is valid given type, limits */
	protected boolean use_limits; /* if we should be performing limit checking */
	private Double value; /* internal representation: value stored as Double */
	private Double max; /* internal representation: maximum value stored as Double */
	private Double min; /* internal representation: minimum value stored as Double */

	// ---------------------------------------
	//				Constructors
	// ---------------------------------------
	/** Default constructor, which takes no arguments */
	public Prompt_Value()
	{
		// use defaults:
		value_str = "";
		description = "";
		is_valid = false;
		use_limits = false;
	}

	/** Constructor with given value string
	 * @param new_value_str a String representing the numerical value
	 */
	public Prompt_Value(String new_value_str)
	{
		this();
		value_str = new_value_str;
	}

	/** Constructor with given value string and description
	* @param new_value_str a String representing the numerical value
	* @param new_description a text description of what this value is
	*/
	public Prompt_Value(String new_value_str, String new_description)
	{
		this(new_value_str);
		description = new_description;
	}

	// ---------------------------------------
	//				Data Access
	// ---------------------------------------
	/** Get the value cast as an int.
	 * @return integer representation of this value (0 if not valid)
	 */
	int get_value_int()
	{
		if( is_valid )
			return value.intValue();
		else
			return (int)Double.NaN;
	}
	/** Get the value cast as an long.
	 * @return long representation of this value (0 if not valid)
	 */
	long get_value_long()
	{
		if ( is_valid )
			return value.longValue();
		else
			return (long)Double.NaN;
	}
	/** Get the value cast as an float.
	 * @return float representation of this value (NaN if not valid)
	 */
	float get_value_float()
	{
		if( is_valid )
			return value.floatValue();
		else
			return Float.NaN;
	}
	/** Get the value cast as an double.
	 * @return double representation of this value (NaN if not valid)
	 */
	double get_value_double()
	{
		if( is_valid )
			return value.doubleValue();
		else
			return Double.NaN;
	}
	/** Get whether the current stored value is valid
	 * @return true if the value is valid
	 */
	public boolean is_valid()
	{
		return is_valid;
	}

	// ---------------------------------------
	//	   Value/Description String methods
	// ---------------------------------------
	/** Set and parse a new string. */
	public void set_value_str(String new_value_str)
	{
		value_str = new_value_str;
		// parse:
		try
		{
			value = new Double(value_str);
			is_valid = true;
		}
		catch (NumberFormatException e)
		{
			is_valid = false;
		}

		// perform limit checking:
		if( use_limits )
		{
			is_valid = is_valid && ( value <= max && value >= min );
		}
	}
	/** Get the current string containing this value
	* @return the String representation of this value
	*/
	public String get_value_str()
	{
		return value_str;
	}
	/** Set the description string
	* @param new_description A new description for the value stored in this object.
	*/
	public void set_description(String new_description)
	{
		description = new_description;
	}
	/** Get a description of this value
	* @return a String containing a text description of this object.
	*/
	public String get_description()
	{
		return description;
	}


	// ---------------------------------------
	//		Implement max/min functionality
	// ---------------------------------------
	/** Set the maximum acceptable value
	* @param new_max maximum valid value (inclusive)
	*/
	public void set_max(int new_max)
	{
		// set the value:
		set_max( (double)new_max );
	}

	/** Get the maximum acceptable value
	* @return max value cast as int
	*/
	public int get_max_int()
	{
		return max.intValue();
	}

	/** Set the maximum acceptable value
	* @param new_max maximum valid value (inclusive)
	*/
	public void set_max(long new_max)
	{
		// set the value:
		set_max( (double)new_max );
	}

	/** Get the maximum acceptable value
	* @return max value cast as long
	*/
	public long get_max_long()
	{
		return max.longValue();
	}

	/** Set the maximum acceptable value
	* @param new_max maximum valid value (inclusive)
	*/
	public void set_max(float new_max)
	{
		// set the value:
		set_max( (double)new_max );
	}

	/** Get the maximum acceptable value
	* @return max value cast as float
	*/
	public float get_max_float()
	{
		return max.floatValue();
	}

	/** Set the maximum acceptable value
	* @param new_max maximum valid value (inclusive)
	*/
	public void set_max(double new_max)
	{
		// set the value:
		max = new Double(new_max);
		// turn on limit checking and reparse to include the new limit:
		use_limits = true;
		set_value_str(value_str);
	}

	/** Get the maximum acceptable value
	* @return max value cast as double
	*/
	public double get_max_double()
	{
		return max.doubleValue();
	}


	
	/** Set the minimum acceptable value
	* @param new_min minimum valid value (inclusive)
	*/
	public void set_min(int new_min)
	{
		// set the value:
		set_min( (double)new_min );
	}

	/** Get the minimum acceptable value
	* @return min value cast as int
	*/
	public int get_min_int()
	{
		return min.intValue();
	}

	/** Set the minimum acceptable value
	* @param new_min minimum valid value (inclusive)
	*/
	public void set_min(long new_min)
	{
		// set the value:
		set_min( (double)new_min );
	}

	/** Get the minimum acceptable value
	* @return min value cast as long
	*/
	public long get_min_long()
	{
		return min.longValue();
	}

	/** Set the minimum acceptable value
	* @param new_min minimum valid value (inclusive)
	*/
	public void set_min(float new_min)
	{
		// set the value:
		set_min( (double)new_min );
	}

	/** Get the minimum acceptable value
	* @return min value cast as float
	*/
	public float get_min_float()
	{
		return min.floatValue();
	}

	/** Set the minimum acceptable value
	* @param new_min minimum valid value (inclusive)
	*/
	public void set_min(double new_min)
	{
		// set the value:
		min = new Double(new_min);
		// turn on limit checking and reparse to include the new limit:
		use_limits = true;
		set_value_str(value_str);
	}

	/** Get the minimum acceptable value
	* @return min value cast as double
	*/
	public double get_min_double()
	{
		return min.doubleValue();
	}
}
