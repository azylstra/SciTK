/** Implement a class which uses JFreeChart
 * to make a generic XY step plot. A step plot is
 * one in which the points are connected via purely
 * vertical and horizontal lines, i.e. appropriate for
 * a spectrum.
 *
 * @package SciTK
 * @class Plot_XYStep
 * @brief Plot a step plot, i.e. appropriate for spectra
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


import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
 
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.chart.plot.XYPlot;

import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.MultipleXYSeriesLabelGenerator;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.NumberTickUnit;

import java.awt.Color;
import java.awt.BasicStroke;

public class Plot_XYStep extends Plot
{
	private DefaultXYDataset data; /** JFreeChart dataset for this plot */

    // ---------------------------------------
    //   Constructors taking minimal things
    // ---------------------------------------
    /** Constructor for a single set of data 
     * @param data_in the data to plot. n x 2 size, eg data_in[i] = [x_i,y_i]
    */
    public Plot_XYStep(float[][] data_in)
    {
        // call other constructor:
        this( ArrayUtil.convert_float_double(data_in) , DEFAULT_NAME, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
    }

    /** Constructor for a single set of data 
     * @param data_in the data to plot. n x 2 size, eg data_in[i] = [x_i,y_i]
    */
    public Plot_XYStep(double[][] data_in)
    {
        // call other constructor:
        this(data_in , DEFAULT_NAME, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
    }

    /** Constructor for a single set of data 
     * @param data_in the data to plot. n x 2 size, eg data_in[i] = [x_i,y_i]
     * @param name the data set's name
    */
    public Plot_XYStep(float[][] data_in, String name)
    {
        // call other constructor:
        this( ArrayUtil.convert_float_double(data_in) , name, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
    }

    /** Constructor for a single set of data 
     * @param data_in the data to plot. n x 2 size, eg data_in[i] = [x_i,y_i]
     * @param name the data set's name
    */
    public Plot_XYStep(double[][] data_in, String name)
    {
        // call other constructor:
        this(data_in , name, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
    }

    /** Constructor for multipe sets of data 
     * @param data_in the data to plot. m x n x 2 size, eg data_in[0][i] = [x_i,y_i]; m data sets of n points.
     * @param name the data set's names in a String array
    */
    public Plot_XYStep(float[][][] data_in, String name[])
    {
        // call other constructor:
        this( ArrayUtil.convert_float_double(data_in) , name, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
    }

    /** Constructor for multipe sets of data 
     * @param data_in the data to plot. m x n x 2 size, eg data_in[0][i] = [x_i,y_i]; m data sets of n points.
     * @param name the data set's names in a String array
    */
    public Plot_XYStep(double[][][] data_in, String name[])
    {
        // call other constructor:
        this(data_in, name, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
    }

    // ---------------------------------------
    //    Constructors taking some thigns
    // ---------------------------------------
    /** Constructor for a single set of data 
     * @param data_in the data to plot. n x 2 size, eg data_in[i] = [x_i,y_i]
     * @param name the data set's name
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
    */
    public Plot_XYStep(float[][] data_in, String name, String x_label, String y_label)
    {
        // call other constructor:
        this( ArrayUtil.convert_float_double(data_in) , name, x_label, y_label, DEFAULT_TITLE);
    }

    /** Constructor for a single set of data 
     * @param data_in the data to plot. n x 2 size, eg data_in[i] = [x_i,y_i]
     * @param name the data set's name
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
    */
    public Plot_XYStep(double[][] data_in, String name, String x_label, String y_label)
    {
        // call other constructor:
        this(data_in , name, x_label, y_label, DEFAULT_TITLE);
    }

    /** Constructor for multipe sets of data 
     * @param data_in the data to plot. m x n x 2 size, eg data_in[0][i] = [x_i,y_i]; m data sets of n points.
     * @param name the data set's names in a String array
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
    */
    public Plot_XYStep(float[][][] data_in, String name[], String x_label, String y_label)
    {
        // call other constructor:
        this( ArrayUtil.convert_float_double(data_in) , name, x_label, y_label, DEFAULT_TITLE);
    }

    /** Constructor for multipe sets of data 
     * @param data_in the data to plot. m x n x 2 size, eg data_in[0][i] = [x_i,y_i]; m data sets of n points.
     * @param name the data set's names in a String array
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
    */
    public Plot_XYStep(double[][][] data_in, String name[], String x_label, String y_label)
    {
        // call other constructor:
        this(data_in, name, x_label, y_label, DEFAULT_TITLE);
    }

    // ---------------------------------------
    //     Constructors taking everything
    // ---------------------------------------
    /** Constructor for a single set of data 
     * @param data_in the data to plot. n x 2 size, eg data_in[i] = [x_i,y_i]
     * @param name the data set's name
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
     * @param window_title a label for the window title
    */
    public Plot_XYStep(float[][] data_in, String name, String x_label, String y_label, String window_title)
    {
        // call other constructor:
        this( ArrayUtil.convert_float_double(data_in) , name, x_label, y_label, window_title);
    }

    /** Constructor for a single set of data 
     * @param data_in the data to plot. n x 2 size, eg data_in[i] = [x_i,y_i]
     * @param name the data set's name
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
     * @param window_title a label for the window title
    */
    public Plot_XYStep(double[][] data_in, String name, String x_label, String y_label, String window_title)
    {
		data = new DefaultXYDataset();
		data.addSeries(name,data_in);

        // Step Chart requires some hand-holding for the horizontal (domain) axis
        double x_min = 0;
        double x_max = 0;
        for(int i=0; i<data_in.length; i++)
        {
        	if( data_in[i][0] < x_min )
        		x_min = data_in[i][0];
        	else if( data_in[i][0] > x_max )
        		x_max = data_in[i][0];
        }

        // call common initialization routine:
        init(x_label, y_label, window_title, x_min, x_max);
	}

    /** Constructor for multipe sets of data 
     * @param data_in the data to plot. m x n x 2 size, eg data_in[0][i] = [x_i,y_i]; m data sets of n points.
     * @param name the data set's names in a String array
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
     * @param window_title a label for the window title
    */
    public Plot_XYStep(float[][][] data_in, String name[], String x_label, String y_label, String window_title)
    {
        // call other constructor:
        this( ArrayUtil.convert_float_double(data_in) , name, x_label, y_label, window_title);
    }

	/** Constructor for multipe sets of data 
	 * @param data_in the data to plot. m x n x 2 size, eg data_in[0][i] = [x_i,y_i]; m data sets of n points.
	 * @param name the data set's names in a String array
	 * @param x_label the label for the abscissa
	 * @param y_label the label for the ordinate
	 * @param window_title a label for the window title
	*/
	public Plot_XYStep(double[][][] data_in, String name[], String x_label, String y_label, String window_title)
	{
		data = new DefaultXYDataset();
		// iterate over input data sets:
		for(int i=0; i<data_in.length; i++)
		{
			data.addSeries(name[i], data_in[i]);
		}
        

        // Step Chart requires some hand-holding for the horizontal (domain) axis
        double x_min = 0;
        double x_max = 0;
        for(int i=0; i<data_in.length; i++)
        {
        	for(int j=0; j<data_in[i].length; j++)
        	{
	        	if( data_in[i][j][0] < x_min )
	        		x_min = data_in[i][j][0];
	        	else if( data_in[i][j][0] > x_max )
	        		x_max = data_in[i][j][0];
        	}
        }

        // call common init routine:
        init(x_label, y_label, window_title, x_min, x_max);
	}


    // ---------------------------------------
    //     Actual Routines:
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

    /** Initialization routine (common to both constructors) */
    private void init(String x_label, String y_label, String window_title, double x_min, double x_max)
    {
        chart = ChartFactory.createXYStepChart("",
                x_label, y_label, data, PlotOrientation.VERTICAL, false, true,
                false);

        chart.setBackgroundPaint(Color.white);
        XYPlot plot = chart.getXYPlot(); // the plot itself

        // Use a step renderer for this type of chart:
        XYStepRenderer renderer = new XYStepRenderer();
        renderer.setBaseStroke(new BasicStroke(2.0f));
        renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
        renderer.setDefaultEntityRadius(6);
        renderer.setLegendItemLabelGenerator(new MultipleXYSeriesLabelGenerator());
        // need to tell the plot to use this renderer
        plot.setRenderer(renderer);

        // create new axis with range set by dataset max/min:
        NumberAxis domainAxis = new NumberAxis(x_label);
        domainAxis.setRange(x_min,x_max);
        plot.setDomainAxis(domainAxis);

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
    			s = s.concat( Double.toString(data.getYValue(i,j)) );
    			s = s.concat(SciTK_Text.TOOLKIT_NEWLINE);
    		}
	    	// add another line break after series is done:
	    	s = s.concat(SciTK_Text.TOOLKIT_NEWLINE);
    	}
    	return s;
    }
}