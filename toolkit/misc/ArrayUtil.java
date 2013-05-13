/** Implement various functionality for working with arrays and Lists
 *
 * @package SciTK
 * @class ArrayUtil
 * @brief Convert arrays
 * @author Alex Zylstra
 * @date 2013/04/15
 * @copyright Alex Zylstra
 * @license MIT
 */

package SciTK;

import java.util.List;

public class ArrayUtil
{
	public ArrayUtil() {}

	/**
	* Convert a 1-D float array to double []
	* @param in the input array
	* @return input array cast as doubles
	*/
	public static double[] convert_float_double(float[] in)
	{
		// need to explicitly convert each item:
		double[] out = new double[in.length];
		for(int i=0; i < out.length; i++)
		{
			out[i] = (double)in[i];
		}

		return out;
	}

	/**
	* Convert a 2-D float array to double [][]
	* @param in the input array
	* @return input array cast as doubles
	*/
	public static double[][] convert_float_double(float[][] in)
	{
		// need to explicitly convert each item:
		double[][] out = new double[in.length][in[0].length];
		for(int i=0; i < out.length; i++)
		{
			for(int j=0; j < out[i].length; j++)
			{
				out[i][j] = (double)in[i][j];
			}
		}

		return out;
	}

	/**
	* Convert a 3-D float array to double [][]
	* @param in the input array
	* @return input array cast as doubles
	*/
	public static double[][][] convert_float_double(float[][][] in)
	{
		// need to explicitly convert each item:
		double[][][] out = new double[in.length][in[0].length][in[0][0].length];
		for(int i=0; i < out.length; i++)
		{
			for(int j=0; j < out[i].length; j++)
			{
				for(int k=0; k < out[i][j].length; k++)
				{
					out[i][j][k] = (double)in[i][j][k];
				}
			}
		}

		return out;
	}

	/** Convert an integer 2-D array to a double array.
	 * @param in the integer array to convert
	 * @return the input array converted to double type
	 */
	public static double[][] convert_int_double(int[][] in)
	{
		double[][] ret = new double[in.length][in[0].length];
		for(int i=0; i<in.length; i++)
		{
			for(int j=0; j<in.length; j++)
				ret[i][j] = (double)in[i][j];
		}
		return ret;
	}

	/**
	* Convert a 1-D List object to a double []. This is more type-safe than using toArray().
	* Supported types: Java Objects extending from java.lang.Number
	*
	* @param in the input array
	* @return input array cast as doubles
	* @throws Exception
	*/
	public static double[] convert_list_1d(List in) throws Exception
	{
		double [] out = new double[ in.size() ];
		// instead of using toArray, iterate to make sure
		// casting is done correctly:
		for(int i=0; i<in.size(); i++)
		{
			if( in.get(i) instanceof Number )
			{
				out[i] = (Double)in.get(i);
			}
			else
			{
				throw new Exception("Unsupported type in ConvertArray.convert_list");
			}
		}

		return out;
	}

	/**
	* Convert a 2-D List object to a double []. This is more type-safe than using toArray().
	* Supported types: Java Objects extending from java.lang.Number
	*
	* @param in the input array
	* @return input array cast as doubles
	* @throws Exception
	*/
	public static double[][] convert_list_2d(List in) throws Exception
	{
		// sanity check that input is not empty:
		if( in == null || in.size() == 0 )
			return null;
		// sanity check that input is a List of Lists:
		if( !(in.get(0) instanceof List) )
			throw new Exception("Input to convert_list_2d must be List of Lists.");

		// size of array:
		int len_0 = in.size();
		int len_1 = ((List)in.get(0)).size();
		// do conversion, making use of 1-D method:

		double[][] out = new double[ len_0 ][ len_1 ];
		for(int i=0; i < in.size(); i++)
		{
			out[i] = convert_list_1d( (List)in.get(i) );
		}

		return out;
	}

	/**
	* Convert a 3-D List object to a double []. This is more type-safe than using toArray().
	* Supported types: Java Objects extending from java.lang.Number
	*
	* @param in the input array
	* @return input array cast as doubles
	* @throws Exception
	*/
	public static double[][][] convert_list_3d(List in) throws Exception
	{
		// sanity check that input is not empty:
		if( in == null || in.size() == 0 )
			return null;
		// sanity check that input is a List of Lists:
		if( !(in.get(0) instanceof List) )
			throw new Exception("Input to convert_list_2d must be List of Lists of Lists (i.e. 3-D).");
		// further sanity check:
		if( !( ((List)in.get(0)).get(0) instanceof List ) )
			throw new Exception("Input to convert_list_2d must be List of Lists of Lists (i.e. 3-D).");

		// size of array:
		int len_0 = in.size();
		int len_1 = ((List)in.get(0)).size();
		int len_2 = ((List)((List)in.get(0)).get(0)).size();
		// do conversion, making use of 2-D method:
		double[][][] out = new double[ len_0 ][ len_1 ][ len_2 ];
		for(int i=0; i < in.size(); i++)
		{
			out[i] = convert_list_2d( (List)in.get(i) );
		}

		return out;
	}

	/**
	* Get the dimensionality of a List (of Lists of ...)
	* @param l the List to check
	* @return dimensionality of l (i.e. 2-D List returns 2)
	*/
	public static int list_dimensionality(List l)
	{
		if(l.size() == 0)
			return 0;
		if ( l.size() > 0 && l.get(0) instanceof List)
			return 1+list_dimensionality((List)l.get(0));
		return 1;
	}
}