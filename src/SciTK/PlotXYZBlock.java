package SciTK;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;

import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.plot.XYPlot;

import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.chart.axis.SymbolAxis;
import java.text.DecimalFormat;

import org.jfree.chart.plot.PiePlot;

import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

import org.jfree.chart.block.LineBorder;

import java.awt.Color;
import java.awt.Paint;
import java.awt.BasicStroke;

// for adding to menu bar:
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.AbstractButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Implement a class which uses JFreeChart
 * to make a generic XYZ plot using "blocks".
 * This is a 2-D representation of a 3-D dataset using
 * a color scale for the third dimension.
 *
 * @package SciTK
 * @class PlotXYZBlock
 * @brief Implement a 'block' (i.e. color-map) chart.
 * @author Alex Zylstra
 * @date 2013/04/21
 * @copyright Alex Zylstra
 * @license SciTK / MIT License
 */
public class PlotXYZBlock extends Plot
{
	private DefaultXYZDataset data; /** JFreeChart dataset for this plot */
	private PaintScaleLegend psl; /** Scale bar for the color mapping */
    private LookupPaintScale paintScale; /** Lookup table for the color mapping */
    int num_labels; /** Number of labels to use for the scale bar */

    // ---------------------------------------
    //   Constructors taking minimal things
    // ---------------------------------------
    /** Constructor for a single set of data 
     * @param data_in the data to plot (must be an array with length 3, containing three arrays of equal length, the first containing the x-values, the second containing the y-values and the third containing the z-values).
    */
    public PlotXYZBlock(float[][] data_in)
    {
        // call other constructor:
        this( ArrayUtil.convertFloatDouble(data_in) , DEFAULT_NAME, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
    }

    /** Constructor for a single set of data 
     * @param data_in the data to plot (must be an array with length 3, containing three arrays of equal length, the first containing the x-values, the second containing the y-values and the third containing the z-values).
    */
    public PlotXYZBlock(double[][] data_in)
    {
        // call other constructor:
        this(data_in, DEFAULT_NAME, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
    }

    /** Constructor for a single set of data 
     * @param data_in the data to plot (must be an array with length 3, containing three arrays of equal length, the first containing the x-values, the second containing the y-values and the third containing the z-values).
     * @param name the data set's name
    */
    public PlotXYZBlock(float[][] data_in, String name)
    {
        // call other constructor:
        this( ArrayUtil.convertFloatDouble(data_in) , name, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
    }

    /** Constructor for a single set of data 
     * @param data_in the data to plot (must be an array with length 3, containing three arrays of equal length, the first containing the x-values, the second containing the y-values and the third containing the z-values).
     * @param name the data set's name
    */
    public PlotXYZBlock(double[][] data_in, String name)
    {
        // call other constructor:
        this(data_in, name, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
    }

    /** Constructor for multiple sets of data 
     * @param data_in the data to plot, for m series each must be an array with length 3, containing three arrays of equal length, the first containing the x-values, the second containing the y-values and the third containing the z-values).
     * @param name the data set's names in a String array
    */
    public PlotXYZBlock(float[][][] data_in, String name[])
    {
        // call other constructor:
        this( ArrayUtil.convertFloatDouble(data_in) , name, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
    }

    /** Constructor for multiple sets of data 
     * @param data_in the data to plot, for m series each must be an array with length 3, containing three arrays of equal length, the first containing the x-values, the second containing the y-values and the third containing the z-values).
     * @param name the data set's names in a String array
    */
    public PlotXYZBlock(double[][][] data_in, String name[])
    {
        // call other constructor:
        this(data_in, name, DEFAULT_XLABEL, DEFAULT_YLABEL, DEFAULT_TITLE);
    }

    // ---------------------------------------
    //    Constructors taking some things
    // ---------------------------------------
    /** Constructor for a single set of data 
     * @param data_in the data to plot (must be an array with length 3, containing three arrays of equal length, the first containing the x-values, the second containing the y-values and the third containing the z-values).
     * @param name the data set's name
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
    */
    public PlotXYZBlock(float[][] data_in, String name, String x_label, String y_label)
    {
        // call other constructor:
        this( ArrayUtil.convertFloatDouble(data_in) , name, x_label, y_label, DEFAULT_TITLE);
    }

    /** Constructor for a single set of data 
     * @param data_in the data to plot (must be an array with length 3, containing three arrays of equal length, the first containing the x-values, the second containing the y-values and the third containing the z-values).
     * @param name the data set's name
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
    */
    public PlotXYZBlock(double[][] data_in, String name, String x_label, String y_label)
    {
        // call other constructor:
        this(data_in, name, x_label, y_label, DEFAULT_TITLE);
    }

    /** Constructor for multiple sets of data 
     * @param data_in the data to plot, for m series each must be an array with length 3, containing three arrays of equal length, the first containing the x-values, the second containing the y-values and the third containing the z-values).
     * @param name the data set's names in a String array
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
    */
    public PlotXYZBlock(float[][][] data_in, String name[], String x_label, String y_label)
    {
        // call other constructor:
        this( ArrayUtil.convertFloatDouble(data_in) , name, x_label, y_label, DEFAULT_TITLE);
    }

    /** Constructor for multiple sets of data 
     * @param data_in the data to plot, for m series each must be an array with length 3, containing three arrays of equal length, the first containing the x-values, the second containing the y-values and the third containing the z-values).
     * @param name the data set's names in a String array
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
    */
    public PlotXYZBlock(double[][][] data_in, String name[], String x_label, String y_label)
    {
        // call other constructor:
        this(data_in, name, x_label, y_label, DEFAULT_TITLE);
    }

    // ---------------------------------------
    //     Constructors taking everything
    // ---------------------------------------
    /** Constructor for a single set of data 
     * @param data_in the data to plot (must be an array with length 3, containing three arrays of equal length, the first containing the x-values, the second containing the y-values and the third containing the z-values).
     * @param name the data set's name
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
     * @param window_title a label for the window title
    */
    public PlotXYZBlock(float[][] data_in, String name, String x_label, String y_label, String window_title)
    {
        // call other constructor:
        this( ArrayUtil.convertFloatDouble(data_in) , name, x_label, y_label, window_title);
    }

	/** Constructor for a single set of data 
	 * @param data_in the data to plot (must be an array with length 3, containing three arrays of equal length, the first containing the x-values, the second containing the y-values and the third containing the z-values).
	 * @param name the data set's name
	 * @param x_label the label for the abscissa
	 * @param y_label the label for the ordinate
	 * @param window_title a label for the window title
	*/
	public PlotXYZBlock(double[][] data_in, String name, String x_label, String y_label, String window_title)
	{
		data = new DefaultXYZDataset();
		data.addSeries(name,data_in);

		// common routine:
		init(x_label, y_label, window_title);
	}

    /** Constructor for multiple sets of data 
     * @param data_in the data to plot, for m series each must be an array with length 3, containing three arrays of equal length, the first containing the x-values, the second containing the y-values and the third containing the z-values).
     * @param name the data set's names in a String array
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
     * @param window_title a label for the window title
    */
    public PlotXYZBlock(float[][][] data_in, String name[], String x_label, String y_label, String window_title)
    {
        // call other constructor:
        this( ArrayUtil.convertFloatDouble(data_in) , name, x_label, y_label, window_title);
    }

    /** Constructor for multiple sets of data 
     * @param data_in the data to plot, for m series each must be an array with length 3, containing three arrays of equal length, the first containing the x-values, the second containing the y-values and the third containing the z-values).
     * @param name the data set's names in a String array
     * @param x_label the label for the abscissa
     * @param y_label the label for the ordinate
     * @param window_title a label for the window title
    */
    public PlotXYZBlock(double[][][] data_in, String name[], String x_label, String y_label, String window_title)
    {
        data = new DefaultXYZDataset();
        // iterate over input data sets:
        for(int i=0; i<data_in.length; i++)
        {
            data.addSeries(name[i], data_in[i]);
        }

        // common routine:
        init(x_label, y_label, window_title);
    }

    // ---------------------------------------
    //     Actual Routines:
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

    /* Common Initialization routine */
	private void init(String x_label, String y_label, String window_title)
	{
        chart = ChartFactory.createScatterPlot("",
                x_label, y_label, data, PlotOrientation.VERTICAL, false, true,
                false);

        // turn off borders of the plot:
        XYPlot p = chart.getXYPlot();
        p.getDomainAxis().setLowerMargin(0.0);
        p.getDomainAxis().setUpperMargin(0.0);
        p.getRangeAxis().setLowerMargin(0.0);
        p.getRangeAxis().setUpperMargin(0.0);

        // --------------------------------------------
        //          set up a lookup table
        // --------------------------------------------
        // this is how we render the block plots:
        XYBlockRenderer renderer = new XYBlockRenderer();
        // need to find max and min z of the data set:
        double min = 0; double max = 0;
        for(int i=0; i < data.getSeriesCount(); i++) // iterate over data sets
        {
            for(int j=0; j<data.getItemCount(i); j++) // iterate over points in dataset
            {
                if( data.getZValue(i,j) < min )
                    min = data.getZValue(i,j);
                else if( data.getZValue(i,j) > max )
                    max = data.getZValue(i,j);
            }
        }
        // create paint scale using min and max values, default color black:
        LookupPaintScale paintScale = new LookupPaintScale(min, max, Color.black);
        // set up the LUT:
        double step_size = (max-min)/255.; // step size for LUT
        for(int i=0; i < 256; i++)
        {
            paintScale.add( min+i*step_size , new Color(i,i,i,255));
        }
        renderer.setPaintScale(paintScale);
        // set this renderer to the plot:
        p.setRenderer(renderer);

        // --------------------------------------------
        //          set up a color bar
        // --------------------------------------------
        // create an array of display labels:
        num_labels = 10; // default to 10 labels on color bar
        double display_step_size = (max-min)/((double)num_labels);
        String[] scale_bar_labels = new String[num_labels+1];
        // to format numbers in scientific notation:
        DecimalFormat formater = new DecimalFormat("0.#E0");
        // create list of labesl:
        for(int i=0; i <= num_labels; i++)
        {
            scale_bar_labels[i] = formater.format(min+i*display_step_size);
        }
        // create axis:
        SymbolAxis scaleAxis = new SymbolAxis(null, scale_bar_labels);
        scaleAxis.setRange(min, max);
        scaleAxis.setPlot(new PiePlot());
        scaleAxis.setGridBandsVisible(false);
        // set up the paint scale:
        psl = new PaintScaleLegend(paintScale, scaleAxis);
        psl.setBackgroundPaint(new Color(255,255,255,0)); // clear background
        // set up frame with buffer region to allow text display
        psl.setFrame(new LineBorder( (Paint)Color.BLACK, 
            new BasicStroke((float)1.0),
            new RectangleInsets(15, 10, 15, 10)));
        psl.setAxisOffset(5.0);
        // display on right side:
        psl.setPosition(RectangleEdge.RIGHT);
        // margin around color scale:
        psl.setMargin(new RectangleInsets(20, 15, 20, 15));
        // add to the chart so it will be displayed by default:
        chart.addSubtitle(psl); 

        // --------------------------------------------
        //          WINDOW-RELATED UI
        // --------------------------------------------
        // set up the generic plot UI:
        super.window_title = window_title;
		super.initUI();

		// add another menu item
		JMenuBar mb = super.getJMenuBar(); // get the menu bar
        // find menu named "Plot"
        JMenu menu_plot = null;
        for(int i=0; i<mb.getMenuCount(); i++)
        {
            if(mb.getMenu(i).getText() == "Plot")
                menu_plot = mb.getMenu(i);
        }
        // Add a new checkbox for the color scale bar
        JCheckBoxMenuItem menu_plot_scalebar = new JCheckBoxMenuItem("Color Scale");
        menu_plot_scalebar.setToolTipText("Show color scale bar?");
        menu_plot_scalebar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                AbstractButton aButton = (AbstractButton) event.getSource();
                boolean selected = aButton.getModel().isSelected();
                setScaleBar(selected);
            }
        });
        // set appropirate checkbox state:
        menu_plot_scalebar.setState(true);
        if( menu_plot != null ) // sanity check
            menu_plot.add(menu_plot_scalebar);

	}

	/** Set the color bar display  status
     * @param enabled true enables color bar display, false disables
     */
    public void setScaleBar(boolean enabled)
    {
        // action taken depends on enabled boolean:
        if(enabled)
        {
            chart.addSubtitle(psl);
        }
        else
        {
            chart.clearSubtitles();
        }
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
    			s = s.concat(SciTK_Text.TOOLKIT_CSV_DELIM);
    			s = s.concat( Double.toString(data.getZValue(i,j)) );
    			s = s.concat(SciTK_Text.TOOLKIT_NEWLINE);
    		}
	    	// add another line break after series is done:
	    	s = s.concat(SciTK_Text.TOOLKIT_NEWLINE);
    	}
    	return s;
    }
}