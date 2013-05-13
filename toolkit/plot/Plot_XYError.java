/** Implement a class which uses JFreeChart
 * to make a generic XY scatter plot with error bars.
 *
 * @package SciTK
 * @class Plot_XYError
 * @brief Generic XY scatter plot with error bars
 * @author Alex Zylstra
 * @date 2013/04/21
 * @copyright Alex Zylstra
 * @license MIT
 */

package SciTK;

import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.SwingUtilities;
 
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.chart.plot.XYPlot;

import java.awt.Color;

public class Plot_XYError extends Plot
{
	private DefaultIntervalXYDataset data; /** JFreeChart dataset for this plot */

    // ---------------------------------------
    //   Constructors taking minimal things
    // ---------------------------------------
    /** Constructor for a single set of data 
     * @param data_in the data to plot (must be an array with length 6, containing six arrays of equal length, the first three containing the x-values (x, xLow and xHigh) and the last three containing the y-values (y, yLow and yHigh)).
    */
    public Plot_XYError(float[][] data_in)
    {
        // call other constructor:
        this(data_in , DEFAULT_NAME, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
    }

    /** Constructor for a single set of data 
     * @param data_in the data to plot (must be an array with length 6, containing six arrays of equal length, the first three containing the x-values (x, xLow and xHigh) and the last three containing the y-values (y, yLow and yHigh)).
    */
    public Plot_XYError(double[][] data_in)
    {
        // call other constructor:
        this(data_in , DEFAULT_NAME, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
    }

    /** Constructor for a single set of data 
     * @param data_in the data to plot (must be an array with length 6, containing six arrays of equal length, the first three containing the x-values (x, xLow and xHigh) and the last three containing the y-values (y, yLow and yHigh)).
     * @param name the data set's name
    */
    public Plot_XYError(float[][] data_in, String name)
    {
        // call other constructor:
        this(data_in , name, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
    }

    /** Constructor for a single set of data 
     * @param data_in the data to plot (must be an array with length 6, containing six arrays of equal length, the first three containing the x-values (x, xLow and xHigh) and the last three containing the y-values (y, yLow and yHigh)).
     * @param name the data set's name
    */
    public Plot_XYError(double[][] data_in, String name)
    {
        // call other constructor:
        this(data_in , name, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
    }

    /** Constructor for multiple sets of data 
     * @param data_in the data to plot. m x 6 x n, m data sets of n points.
     * @param name the data set's names in a String array
    */
    public Plot_XYError(float[][][] data_in, String name[])
    {
        // call other constructor:
        this(data_in , name, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
    }

    /** Constructor for multiple sets of data 
     * @param data_in the data to plot. m x 6 x n, m data sets of n points.
     * @param name the data set's names in a String array
    */
    public Plot_XYError(double[][][] data_in, String name[])
    {
        // call other constructor:
        this(data_in , name, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
    }

    // ---------------------------------------
    //     Constructors taking some things
    // ---------------------------------------
    /** Constructor for a single set of data 
     * @param data_in the data to plot (must be an array with length 6, containing six arrays of equal length, the first three containing the x-values (x, xLow and xHigh) and the last three containing the y-values (y, yLow and yHigh)).
     * @param name the data set's name
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
    */
    public Plot_XYError(float[][] data_in, String name, String x_label, String y_label)
    {
        // call other constructor:
        this(data_in , name, x_label, y_label, DEFAULT_TITLE);
    }

    /** Constructor for a single set of data 
     * @param data_in the data to plot (must be an array with length 6, containing six arrays of equal length, the first three containing the x-values (x, xLow and xHigh) and the last three containing the y-values (y, yLow and yHigh)).
     * @param name the data set's name
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
    */
    public Plot_XYError(double[][] data_in, String name, String x_label, String y_label)
    {
        // call other constructor:
        this(data_in , name, x_label, y_label, DEFAULT_TITLE);
    }

    /** Constructor for multiple sets of data 
     * @param data_in the data to plot. m x 6 x n, m data sets of n points.
     * @param name the data set's names in a String array
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
    */
    public Plot_XYError(float[][][] data_in, String name[], String x_label, String y_label)
    {
        // call other constructor:
        this(data_in , name, x_label, y_label, DEFAULT_TITLE);
    }

    /** Constructor for multiple sets of data 
     * @param data_in the data to plot. m x 6 x n, m data sets of n points.
     * @param name the data set's names in a String array
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
    */
    public Plot_XYError(double[][][] data_in, String name[], String x_label, String y_label)
    {
        // call other constructor:
        this(data_in , name, x_label, y_label, DEFAULT_TITLE);
    }

    // ---------------------------------------
    //     Constructors taking everything
    // ---------------------------------------
    /** Constructor for a single set of data 
     * @param data_in the data to plot (must be an array with length 6, containing six arrays of equal length, the first three containing the x-values (x, xLow and xHigh) and the last three containing the y-values (y, yLow and yHigh)).
     * @param name the data set's name
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
     * @param window_title a label for the window title
    */
    public Plot_XYError(float[][] data_in, String name, String x_label, String y_label, String window_title)
    {
        // call other constructor:
        this( ArrayUtil.convert_float_double(data_in) , name, x_label, y_label, window_title);
    }

	/** Constructor for a single set of data 
	 * @param data_in the data to plot (must be an array with length 6, containing six arrays of equal length, the first three containing the x-values (x, xLow and xHigh) and the last three containing the y-values (y, yLow and yHigh)).
	 * @param name the data set's name
	 * @param x_label the label for the abscissa
	 * @param y_label the label for the ordinate
	 * @param window_title a label for the window title
	*/
	public Plot_XYError(double[][] data_in, String name, String x_label, String y_label, String window_title)
	{
		data = new DefaultIntervalXYDataset();
		data.addSeries(name,data_in);

		init(x_label, y_label, window_title);
	}

    /** Constructor for multiple sets of data 
     * @param data_in the data to plot. m x 6 x n, m data sets of n points.
     * @param name the data set's names in a String array
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
     * @param window_title a label for the window title
    */
    public Plot_XYError(float[][][] data_in, String name[], String x_label, String y_label, String window_title)
    {
        // call other constructor:
        this( ArrayUtil.convert_float_double(data_in) , name, x_label, y_label, window_title);
    }

	/** Constructor for multiple sets of data 
	 * @param data_in the data to plot. m x 6 x n, m data sets of n points.
	 * @param name the data set's names in a String array
	 * @param x_label the label for the abscissa
	 * @param y_label the label for the ordinate
	 * @param window_title a label for the window title
	*/
	public Plot_XYError(double[][][] data_in, String name[], String x_label, String y_label, String window_title)
	{
		// create dataset:
		data = new DefaultIntervalXYDataset();
		for(int i=0; i < data_in.length; i++)
			data.addSeries(name[i],data_in[i]);

        init(x_label, y_label, window_title);
	}


    // ---------------------------------------
    //          Actual routines:
    // ---------------------------------------
    /**
    * Update a single set of data 
    * @param new_data the data to plot. n x 2 size, eg new_data[i] = [x_i,y_i]
    * @param name the data set's name
    */
    public void update_data(float[][] new_data, String name)
    {
        // convert data to double, and call next function:
        update_data( ArrayUtil.convert_float_double(new_data) , name);
    }

    /**
    * Update a single set of data 
    * @param new_data the data to plot. n x 2 size, eg new_data[i] = [x_i,y_i]
    * @param name the data set's name
    */
    public void update_data(double[][] new_data, String name)
    {
        // add series:
        data.addSeries(name,new_data);
    }

    /**
    * Update multiple sets of data 
    * @param new_data the data to plot. m x n x 2 size, eg new_data[0][i] = [x_i,y_i]; m data sets of n points.
    * @param name the data set's names in a String array of length m
    */
    public void update_data(float[][][] new_data, String[] name)
    {
        // convert data to double, and call next function:
        update_data( ArrayUtil.convert_float_double(new_data) , name);
    }

    /**
    * Update multiple sets of data 
    * @param new_data the data to plot. m x n x 2 size, eg new_data[0][i] = [x_i,y_i]; m data sets of n points.
    * @param name the data set's names in a String array of length m
    */
    public void update_data(double[][][] new_data, String[] name)
    {
        // loop over all data sets given:
        for(int i=0; i < new_data.length; i++)
        {
            // add series:
            data.addSeries(name[i],new_data[i]);  
        }
    }

	/** More (common) initialization routines */
	private void init(String x_label, String y_label, String window_title)
	{	
        chart = ChartFactory.createScatterPlot("",
                x_label, y_label, data, PlotOrientation.VERTICAL, false, true,
                false);

        XYErrorRenderer renderer = new XYErrorRenderer();
        XYPlot plot = chart.getXYPlot(); // the plot itself
        plot.setRenderer(renderer);
        
        // for some reason default is white, change it to black:
        set_gridline_color(Color.BLACK);

        super.window_title = window_title;
		super.initUI();
	}

    /** Get a string representation of the plotted data 
	 * @return the data plotted in CSV format.
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
    			s = s.concat( Double.toString(data.getStartXValue(i,j)) );
    			s = s.concat(SciTK_Text.TOOLKIT_CSV_DELIM);
    			s = s.concat( Double.toString(data.getEndXValue(i,j)) );
    			s = s.concat(SciTK_Text.TOOLKIT_CSV_DELIM);
    			s = s.concat( Double.toString(data.getYValue(i,j)) );
    			s = s.concat(SciTK_Text.TOOLKIT_CSV_DELIM);
    			s = s.concat( Double.toString(data.getStartYValue(i,j)) );
    			s = s.concat(SciTK_Text.TOOLKIT_CSV_DELIM);
    			s = s.concat( Double.toString(data.getEndYValue(i,j)) );
    			s = s.concat(SciTK_Text.TOOLKIT_NEWLINE);
    		}
	    	// add another line break after series is done:
	    	s = s.concat(SciTK_Text.TOOLKIT_NEWLINE);
    	}
    	return s;
    }
}