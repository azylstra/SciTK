package SciTK;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

import java.awt.Color;

/** Implement a class which uses JFreeChart
 * to make a generic XY scatter plot.
 *
 * @package SciTK
 * @class PlotXY
 * @brief Generic XY scatter plot
 * @author Alex Zylstra
 * @date 2013/04/21
 * @copyright Alex Zylstra
 * @license SciTK / MIT License
 */
public class PlotXY extends Plot
{
	private DefaultXYDataset data; /** JFreeChart dataset for this plot */

	// ---------------------------------------
	//		Constructors minimal things
	// ---------------------------------------
	/** 
	* Constructor for a single set of data 
	*
	* @param data_in the data to plot. n x 2 size, eg data_in[i] = [x_i,y_i]
	*/
	public PlotXY(float[][] data_in)
	{
		// call other constructor:
		this(data_in, DEFAULT_NAME, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
	}

	/** 
	* Constructor for a single set of data 
	*
	* @param data_in the data to plot. n x 2 size, eg data_in[i] = [x_i,y_i]
	*/
	public PlotXY(double[][] data_in)
	{
		// call other constructor:
		this(data_in, DEFAULT_NAME, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
	}


	// ---------------------------------------
	//		Constructors taking some things
	// ---------------------------------------
	/** 
	* Constructor for a single set of data 
	*
	* @param data_in the data to plot. n x 2 size, eg data_in[i] = [x_i,y_i]
	* @param name the data set's name
	*/
	public PlotXY(float[][] data_in, String name)
	{
		// call other constructor:
		this(data_in, name, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
	}

	/** 
	* Constructor for a single set of data 
	*
	* @param data_in the data to plot. n x 2 size, eg data_in[i] = [x_i,y_i]
	* @param name the data set's name
	*/
	public PlotXY(double[][] data_in, String name)
	{
		// call other constructor:
		this(data_in, name, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
	}

	/** 
	* Constructor for multiple sets of data 
	*
	* @param data_in the data to plot. m x n x 2 size, eg data_in[0][i] = [x_i,y_i]; m data sets of n points.
	* @param name the data set's names in a String array
	*/
	public PlotXY(float[][][] data_in, String name[])
	{
		// call other constructor:
		this(data_in, name, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
	}

	/** 
	* Constructor for multiple sets of data 
	*
	* @param data_in the data to plot. m x n x 2 size, eg data_in[0][i] = [x_i,y_i]; m data sets of n points.
	* @param name the data set's names in a String array
	*/
	public PlotXY(double[][][] data_in, String name[])
	{
		// call other constructor:
		this(data_in, name, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
	}
	/** 
	* Constructor for a single set of data 
	*
	* @param data_in the data to plot. n x 2 size, eg data_in[i] = [x_i,y_i]
	* @param name the data set's name
	* @param x_label the label for the abscissa
	* @param y_label the label for the ordinate
	*/
	public PlotXY(float[][] data_in, String name, String x_label, String y_label)
	{
		// call other constructor:
		this(data_in, name, x_label, y_label, DEFAULT_TITLE);
	}

	/** 
	* Constructor for a single set of data 
	*
	* @param data_in the data to plot. n x 2 size, eg data_in[i] = [x_i,y_i]
	* @param name the data set's name
	* @param x_label the label for the abscissa
	* @param y_label the label for the ordinate
	*/
	public PlotXY(double[][] data_in, String name, String x_label, String y_label)
	{
		// call other constructor:
		this(data_in, name, x_label, y_label, DEFAULT_TITLE);
	}

	/** 
	* Constructor for multiple sets of data 
	*
	* @param data_in the data to plot. m x n x 2 size, eg data_in[0][i] = [x_i,y_i]; m data sets of n points.
	* @param name the data set's names in a String array of length m
	* @param x_label the label for the abscissa
	* @param y_label the label for the ordinate
	*/
	public PlotXY(float[][][] data_in, String name[], String x_label, String y_label)
	{
		// call other constructor:
		this(data_in, name, x_label, y_label, DEFAULT_TITLE);
	}

	/** 
	* Constructor for multiple sets of data 
	*
	* @param data_in the data to plot. m x n x 2 size, eg data_in[0][i] = [x_i,y_i]; m data sets of n points.
	* @param name the data set's names in a String array of length m
	* @param x_label the label for the abscissa
	* @param y_label the label for the ordinate
	*/
	public PlotXY(double[][][] data_in, String name[], String x_label, String y_label)
	{
		// call other constructor:
		this(data_in, name, x_label, y_label, DEFAULT_TITLE);
	}

	// ---------------------------------------
	//		Constructors taking everything
	// ---------------------------------------
	/** 
	* Constructor for a single set of data 
	*
	* @param data_in the data to plot. n x 2 size, eg data_in[i] = [x_i,y_i]
	* @param name the data set's name
	* @param x_label the label for the abscissa
	* @param y_label the label for the ordinate
	* @param window_title a label for the window title
	*/
	public PlotXY(float[][] data_in, String name, String x_label, String y_label, String window_title)
	{
		// call other constructor:
		this( ArrayUtil.convertFloatDouble(data_in) , name, x_label, y_label, window_title);
	}

	/** 
	* Constructor for a single set of data 
	*
	* @param data_in the data to plot. n x 2 size, eg data_in[i] = [x_i,y_i]
	* @param name the data set's name
	* @param x_label the label for the abscissa
	* @param y_label the label for the ordinate
	* @param window_title a label for the window title
	*/
	public PlotXY(double[][] data_in, String name, String x_label, String y_label, String window_title)
	{
		data = new DefaultXYDataset();
		data.addSeries(name,data_in);

		// common routine:
		init(x_label, y_label, window_title);
	}

	/** 
	* Constructor for a multiple sets of data 
	*
	* @param data_in the data to plot. m x n x 2 size, eg data_in[0][i] = [x_i,y_i]; m data sets of n points.
	* @param name the data set's names in a String array of length m
	* @param x_label the label for the abscissa
	* @param y_label the label for the ordinate
	* @param window_title a label for the window title
	*/
	public PlotXY(float[][][] data_in, String name[], String x_label, String y_label, String window_title)
	{
		// call other constructor:
		this( ArrayUtil.convertFloatDouble(data_in) , name, x_label, y_label, window_title);
	}

	/** 
	* Constructor for a multiple sets of data 
	*
	* @param data_in the data to plot. m x n x 2 size, eg data_in[0][i] = [x_i,y_i]; m data sets of n points.
	* @param name the data set's names in a String array of length m
	* @param x_label the label for the abscissa
	* @param y_label the label for the ordinate
	* @param window_title a label for the window title
	*/
	public PlotXY(double[][][] data_in, String name[], String x_label, String y_label, String window_title)
	{
		data = new DefaultXYDataset();
		// iterate over input data sets:
		for(int i=0; i<data_in.length; i++)
		{
			data.addSeries(name[i], data_in[i]);
		}

		// common routine:
		init(x_label, y_label, window_title);
	}


	// ---------------------------------------
	//		Main Functionality
	// ---------------------------------------
	/**
	* Update a single set of data 
	* @param new_data the data to plot. n x 2 size, eg new_data[i] = [x_i,y_i]
	* @param name the data set's name
	*/
	public void updateData(float[][] new_data, String name)
	{
		// convert data to double, and call next function:
		updateData(ArrayUtil.convertFloatDouble(new_data), name);
	}

	/**
	* Update a single set of data 
	* @param new_data the data to plot. n x 2 size, eg new_data[i] = [x_i,y_i]
	* @param name the data set's name
	*/
	public void updateData(double[][] new_data, String name)
	{
		// add series:
		data.addSeries(name,new_data);
	}

	/**
	* Update multiple sets of data 
	* @param new_data the data to plot. m x n x 2 size, eg new_data[0][i] = [x_i,y_i]; m data sets of n points.
	* @param name the data set's names in a String array of length m
	*/
	public void updateData(float[][][] new_data, String[] name)
	{
		// convert data to double, and call next function:
		updateData(ArrayUtil.convertFloatDouble(new_data), name);
	}

	/**
	* Update multiple sets of data 
	* @param new_data the data to plot. m x n x 2 size, eg new_data[0][i] = [x_i,y_i]; m data sets of n points.
	* @param name the data set's names in a String array of length m
	*/
	public void updateData(double[][][] new_data, String[] name)
	{
		// loop over all data sets given:
		for(int i=0; i < new_data.length; i++)
		{
			// add series:
			data.addSeries(name[i],new_data[i]);	
		}
	}

	/**
	* Initialize the chart itself
	*/
	private void init(String x_label, String y_label, String window_title)
	{
        chart = ChartFactory.createScatterPlot("",
                x_label, y_label, data, PlotOrientation.VERTICAL, true, true,
                false);

        // for some reason default is white, change it to black:
        setGridlineColor(Color.BLACK);

        super.window_title = window_title;
		super.initUI();
	}

    /** 
    * Get a string representation of the plotted data 
	* @return the data in CSV format, contained in a String.
    */
    public String toString()
    {	
    	String s = "";
    	// iterate over all data series:
    	for(int i=0; i<data.getSeriesCount(); i++)
    	{
    		// iterate over items in the series:
    		for(int j=0; j<data.getItemCount(i); j++)
    		{
    			// add x,y then new line:
    			s = s.concat( Double.toString(data.getXValue(i,j)) );
    			s = s.concat(SciTK_Text.TOOLKIT_CSV_DELIM);
    			s = s.concat( Double.toString(data.getYValue(i,j)) );
    			s = s.concat(SciTK_Text.TOOLKIT_NEWLINE);
    		}
	    	// add another line break after series is done:
	    	s = s.concat(SciTK_Text.TOOLKIT_NEWLINE);
    	}
    	return s;
    }
}