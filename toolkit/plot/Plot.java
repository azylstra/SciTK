/** A class which uses Java swing to make a generic window for JFreeChart plots.
 * Implement somes generic UI and useful methods. Various types of plots should extend this class
 *
 * @package SciTK
 * @class Plot
 * @brief Implement a generic plot window
 * @author Alex Zylstra
 * @date 2013/04/20
 * @copyright Alex Zylstra
 * @license MIT
 */

package SciTK;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
 
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.PlotOrientation;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.AbstractButton;

import java.io.IOException;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;

import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileWriter;

// For SVG files:
import java.awt.Rectangle;
import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DOMImplementation;
import org.apache.batik.dom.GenericDOMImplementation;
import org.w3c.dom.ElementTraversal;

// for PS/EPS files
import org.apache.xmlgraphics.java2d.ps.AbstractPSDocumentGraphics2D;
import org.apache.xmlgraphics.java2d.ps.PSDocumentGraphics2D;
import org.apache.xmlgraphics.java2d.ps.EPSDocumentGraphics2D;

// for fiddling with axes
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;

// for plot legend
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.block.LineBorder;

// for setting colors of various things
import javax.swing.JColorChooser;

// for keyboard shortcuts
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
//import java.awt.event.KeyListener;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.Paint;

import java.lang.System;
import java.lang.Math;

public abstract class Plot extends SciTK_Frame implements ClipboardOwner 
{
    protected JFreeChart chart; /** The chart displayed in this window */
    protected ChartPanel chart_panel; /** The chart panel containing the plot */
    protected String window_title; /** title of the window */

    private int def_width; /** width of the window */
    private int def_height; /** height of the window */

    protected JMenuBar menubar; /** Menu bar for this window */
    protected JMenu file; /** "File" drop-down menu in the menu bar */
    protected JMenu edit; /** "Edit" drop-down menu in the menu bar */
    protected JMenu plot; /** "Plot" drop-down menu in the menu bar */

    public static String DEFAULT_NAME = ""; /** Default name for a dataset */
    public static String DEFAULT_XLABEL = "x"; /** Default label for the abcissa */
    public static String DEFAULT_YLABEL = "y"; /** Default label for the ordinate */
    public static String DEFAULT_TITLE = ""; /** Default window title */

	/** 
    * Constructor (automatic window sizing) 
    */
	public Plot()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        def_width = (int) (screenSize.getWidth() / 4);
        def_height = (int) (screenSize.getHeight() / 4);
    }

    /**
    * Construct a new Plot with given window size
    */
    public Plot(int width, int height)
    {
        def_width = width;
        def_height = height;
    }


    // ---------------------------------------
    //      Methods to save/copy plot/data
    // ---------------------------------------
    /** 
    * Save data for generic plot to file 
    */
    public void save_data()
    {
        JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new ExtensionFileFilter("CSV", new String[] {"csv,CSV"}));
        // remove default option:
        fc.setAcceptAllFileFilterUsed(false);

        // Select a file using the JFileChooser dialog:
        int fc_return = fc.showSaveDialog(this);

        if( fc_return == JFileChooser.APPROVE_OPTION ) // user wants to save
        {
            //get the file name from the filter:
            File save_file = fc.getSelectedFile();

            // check to see if the user entered an extension:
            if( !save_file.getName().endsWith(".csv") && !save_file.getName().endsWith(".CSV") )
            {
                save_file = new File(save_file.getAbsolutePath()+".csv");
            }

            // create a string to save
            String s = toString();

            // IO inside a try/catch
            try
            {
                // Create a File Writer
                FileWriter fw = new FileWriter(save_file);
                fw.write(s);
                fw.close();
            }
            // If there was an error, launch an error dialog box:
            catch(IOException e)
            {
                Dialog_Error emsg = new Dialog_Error(" There was an error saving the file." + System.getProperty("line.separator") + e.getMessage());
            }
        }
    }

    /** 
    * Save displayed chart as vector graphics 
    */
    public void save_vector_graphics()
    {
        Rectangle r = chart_panel.getBounds(); // get size of chart

        // file selection:
        JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new ExtensionFileFilter("SVG", new String[] {"svg,SVG"}));
        fc.addChoosableFileFilter(new ExtensionFileFilter("PS", new String[] {"ps,PS"}));
        fc.addChoosableFileFilter(new ExtensionFileFilter("EPS", new String[] {"eps,EPS"}));
        // remove default option:
        fc.setAcceptAllFileFilterUsed(false);

        // Select a file using the JFileChooser dialog:
        int fc_return = fc.showSaveDialog(Plot.this);

        // if the user actually wants to save:
        if( fc_return==JFileChooser.APPROVE_OPTION )
        {
            //get the file:
            File save_file = fc.getSelectedFile();
            // and the type of extension (from file chooser)
            String extension = fc.getFileFilter().getDescription();
            try // try/catch for IO errors
            {
                // For each type of extension, check to see if the name includes the
                // correct extension and then save the image to file.
                // Default extension is EPS
                if( extension=="SVG" )
                {
                    // sanity check extension:
                    if( !save_file.getName().endsWith(".svg") && !save_file.getName().endsWith(".SVG") )
                        save_file = new File(save_file.getAbsolutePath()+".svg");
                    exportChartAsSVG(chart,r,save_file);
                }
                else if( extension=="PS" )
                {
                    // sanity check extension:
                    if( !save_file.getName().endsWith(".ps") && !save_file.getName().endsWith(".PS") )
                        save_file = new File(save_file.getAbsolutePath()+".ps");
                    exportChartAsPS(chart,r,save_file,"ps");
                }
                else
                {
                    // sanity check extension:
                    if( !save_file.getName().endsWith(".eps") && !save_file.getName().endsWith(".EPS") )
                        save_file = new File(save_file.getAbsolutePath()+".eps");
                    exportChartAsPS(chart,r,save_file,"eps");
                }
            }
            catch(IOException e)
            {
                Dialog_Error emsg = new Dialog_Error(" There was an error saving the file." + System.getProperty("line.separator") + e.getMessage());
            }
        }
    }

    /** 
    * Export chart as an image using the built-in JFreeChart utility (including file chooser). 
    */
    public void save_image()
    {
        // Use JFreeChart's save dialog, catch IOException if it occurs
        try
        {
            chart_panel.doSaveAs();
        }
        catch(IOException e)
        {
            Dialog_Error emsg = new Dialog_Error(" There was an error saving the file." + System.getProperty("line.separator") + e.getMessage());
        }

    }

    /** 
    * Print displayed chart using built-in JFreeChart functionality
    */
    public void print_plot()
    {
        chart_panel.createChartPrintJob();
    }

    /** 
    * Copy displayed data to the system clipboard 
    */
    public void copy_data()
    {
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection s2 = new StringSelection( toString() );
        c.setContents(s2,this);
    }

    /** 
    * Copy image of plot to the system clipboard 
    */
    public void copy_image()
    {
        chart_panel.doCopy();
    }

    // ---------------------------------------
    //      Abstract methods
    // ---------------------------------------
    /** Create a string representation of the chart */
    public abstract String toString();
    /** Update the displayed data */
    public abstract void update_data(float[][] new_data, String name);
    /** Update the displayed data */
    public abstract void update_data(double[][] new_data, String name);
    /** Update the displayed data */
    public abstract void update_data(float[][][] new_data, String[] name);
    /** Update the displayed data */
    public abstract void update_data(double[][][] new_data, String[] name);


    // ---------------------------------------
    //              UI methods
    // ---------------------------------------
	/** 
    * Load the initial UI
    */
	protected final void initUI()
	{
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // create a ChartPanel, and make it default to 1/4 of screen size:
        chart_panel = new ChartPanel(chart);
        chart_panel.setPreferredSize(new Dimension(def_width,def_height));

        //add the chart to the window:
        getContentPane().add(chart_panel);

        // create a menu bar:
        menubar = new JMenuBar();
        // ---------------------------------------------------------
        //                 First dropdown menu: "File"
        // ---------------------------------------------------------
        file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        // Set up a save dialog option under the file menu:
        ImageIcon menu_file_save_icon = new ImageIcon(getClass().getResource("/resources/document-save-5.png"));
        JMenuItem menu_file_save = new JMenuItem("Save", menu_file_save_icon);
        menu_file_save.setMnemonic(KeyEvent.VK_S);
        menu_file_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menu_file_save.setToolTipText("Save an image");
        menu_file_save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                save_image();
            }
        });
        file.add(menu_file_save);

        // Set up a save SVG dialog option under the file menu:
        JMenuItem menu_file_save_svg = new JMenuItem("Save VG", menu_file_save_icon);
        menu_file_save_svg.setToolTipText("Save as vector graphics (SVG,PS,EPS)");
        menu_file_save_svg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                save_vector_graphics();
            }
        });
        file.add(menu_file_save_svg);

        // Set up a save data dialog option under the file menu:
        JMenuItem menu_file_save_data = new JMenuItem("Save data", menu_file_save_icon);
        menu_file_save_data.setToolTipText("Save raw data to file");
        menu_file_save_data.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                save_data();
            }
        });
        file.add(menu_file_save_data);

        // Set up a save data dialog option under the file menu:
        ImageIcon menu_file_print_icon = new ImageIcon(getClass().getResource("/resources/document-print-5.png"));
        JMenuItem menu_file_print = new JMenuItem("Print", menu_file_print_icon);
        menu_file_print.setToolTipText("Print image of chart");
        menu_file_print.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                print_plot();
            }
        });
        file.add(menu_file_print);


        ImageIcon menu_file_exit_icon = new ImageIcon(getClass().getResource("/resources/application-exit.png"));
        JMenuItem menu_file_exit = new JMenuItem("Exit", menu_file_exit_icon);
        menu_file_exit.setMnemonic(KeyEvent.VK_X);
        menu_file_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menu_file_exit.setToolTipText("Exit application");
        menu_file_exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dispose();
            }

        });
        file.add(menu_file_exit);


        // ---------------------------------------------------------
        //                 Second dropdown menu: "Edit"
        // ---------------------------------------------------------
        edit = new JMenu("Edit");
        edit.setMnemonic(KeyEvent.VK_E);

        // copy to clipboard
        ImageIcon menu_edit_copy_icon = new ImageIcon(getClass().getResource("/resources/edit-copy-7.png"));
        JMenuItem menu_edit_copy_image = new JMenuItem("Copy", menu_edit_copy_icon);
        menu_edit_copy_image.setMnemonic(KeyEvent.VK_C);
        menu_edit_copy_image.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menu_edit_copy_image.setToolTipText("Copy image to clipboard");
        menu_edit_copy_image.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                copy_image();
            }
        });
        edit.add(menu_edit_copy_image);

        // copy data to clipboard
        JMenuItem menu_edit_copy_data = new JMenuItem("Copy data", menu_edit_copy_icon);
        menu_edit_copy_data.setToolTipText("Copy data to clipboard");
        menu_edit_copy_data.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                copy_data();
            }
        });
        edit.add(menu_edit_copy_data);

        // set background color
        ImageIcon menu_edit_Color_icon = new ImageIcon(getClass().getResource("/resources/color-wheel.png"));
        JMenuItem menu_edit_BackgroundColor = new JMenuItem("Background Color",menu_edit_Color_icon);
        menu_edit_BackgroundColor.setToolTipText("Select background color");
        menu_edit_BackgroundColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Color bgColor = plot_color_chooser("Choose background color",(Color)chart.getBackgroundPaint());
                set_background_color(bgColor);
            }
        });
        edit.add(menu_edit_BackgroundColor);

        // set plot color
        JMenuItem menu_edit_PlotColor = new JMenuItem("Window Color",menu_edit_Color_icon);
        menu_edit_PlotColor.setToolTipText("Select plot window color");
        menu_edit_PlotColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Color plotColor = plot_color_chooser("Choose plot color",(Color)chart.getPlot().getBackgroundPaint());
                set_window_background(plotColor);
            }
        });
        edit.add(menu_edit_PlotColor);

        // set gridline color
        JMenuItem menu_edit_GridlineColor = new JMenuItem("Gridline Color",menu_edit_Color_icon);
        menu_edit_GridlineColor.setToolTipText("Select grid color");
        menu_edit_GridlineColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Color gridColor = plot_color_chooser("Choose grid color",(Color)chart.getPlot().getBackgroundPaint());
                set_gridline_color(gridColor);
            }
        });
        edit.add(menu_edit_GridlineColor);

        // edit chart preferences
        ImageIcon menu_edit_preferences_icon = new ImageIcon(getClass().getResource("/resources/preferences-desktop-3.png"));
        JMenuItem menu_edit_preferences = new JMenuItem("Preferences", menu_edit_preferences_icon);
        menu_edit_preferences.setToolTipText("Edit chart preferences");
        menu_edit_preferences.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                chart_panel.doEditChartProperties();
            }
        });
        edit.add(menu_edit_preferences);

        // ---------------------------------------------------------
        //                 Third dropdown menu: "Plot"
        // ---------------------------------------------------------
        plot = new JMenu("Plot");
        plot.setMnemonic(KeyEvent.VK_P);

        // Options to set log axes
        JCheckBoxMenuItem menu_plot_ylog = new JCheckBoxMenuItem("Log y axis");
        menu_plot_ylog.setToolTipText("Set y axis to logarithmic");
        menu_plot_ylog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                AbstractButton aButton = (AbstractButton) event.getSource();
                boolean selected = aButton.getModel().isSelected();
                set_rangeaxis_log(selected);
            }
        });
        plot.add(menu_plot_ylog);

        JCheckBoxMenuItem menu_plot_xlog = new JCheckBoxMenuItem("Log x axis");
        menu_plot_xlog.setToolTipText("Set x axis to logarithmic");
        menu_plot_xlog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                AbstractButton aButton = (AbstractButton) event.getSource();
                boolean selected = aButton.getModel().isSelected();
                set_domainaxis_log(selected);
            }
        });
        plot.add(menu_plot_xlog);

        // grid line display
        JCheckBoxMenuItem menu_plot_grid = new JCheckBoxMenuItem("Grid lines");
        menu_plot_grid.setToolTipText("Show plot grid lines?");
        menu_plot_grid.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                AbstractButton aButton = (AbstractButton) event.getSource();
                boolean selected = aButton.getModel().isSelected();
                set_gridline_visible(selected);
            }
        });
        // set appropirate checkbox state:
        menu_plot_grid.setState(chart.getXYPlot().isDomainGridlinesVisible());
        plot.add(menu_plot_grid);

        // control for displaying plot legend
        JCheckBoxMenuItem menu_plot_legend = new JCheckBoxMenuItem("Legend");
        menu_plot_legend.setToolTipText("Show plot legend?");
        menu_plot_legend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                AbstractButton aButton = (AbstractButton) event.getSource();
                boolean selected = aButton.getModel().isSelected();
                set_legend(selected);
            }
        });
        // set appropirate checkbox state:
        menu_plot_legend.setState((chart.getLegend() instanceof LegendTitle));
        plot.add(menu_plot_legend);

        // ---------------------------------------------------------
        //                 General UI
        // ---------------------------------------------------------
        // Add menus to the menu bar:
        menubar.add(file);
        menubar.add(edit);
        menubar.add(plot);
        // Set menubar as this JFrame's menu
        setJMenuBar(menubar);

        // set default plot colors:
        chart.setBackgroundPaint( new Color(255,255,255,0) );
        chart.getPlot().setBackgroundPaint( new Color(255,255,255,255) );
        set_background_alpha(0.0f);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setTitle(window_title);
        setLocationRelativeTo(null);
        setVisible(true);
    }   

    // ---------------------------------------------------------
    //                 Plot option functionality
    // ---------------------------------------------------------
    /** 
    * Set the logarithmic state of the domain axis.
    * @param enabled true enables log mode, false disables
    */
    public void set_domainaxis_log(boolean enabled)
    {
        // get current limits of the axis:
        XYPlot p = chart.getXYPlot();
        // get the domain axis for this plot:
        ValueAxis current = p.getDomainAxis();
        // set new axis type based on enabled boolean:
        ValueAxis newAxis;
        if( enabled )
            newAxis = new LogAxis(current.getLabel());
        else
            newAxis = new NumberAxis(current.getLabel());
        // update axis:
        p.setDomainAxis(newAxis);
    }

    /** 
    * Set the logarithmic state of the range axis.
    * @param enabled true enables log mode, false disables
    */
    public void set_rangeaxis_log(boolean enabled)
    {
        // get current limits of the axis:
        XYPlot p = chart.getXYPlot();
        // get the domain axis for this plot:
        ValueAxis current = p.getRangeAxis();
        // set new axis type based on enabled boolean:
        ValueAxis newAxis;
        if( enabled )
            newAxis = new LogAxis(current.getLabel());
        else
            newAxis = new NumberAxis(current.getLabel());
        // update axis:
        p.setRangeAxis(newAxis);
    }

    /** 
     * Set the legend status
     * @param enabled true enables legend display, false disables
     */
    public void set_legend(boolean enabled)
    {
        // action taken depends on enabled boolean:
        if(enabled)
        {
            chart.addLegend(new LegendTitle(chart.getPlot()));
            chart.getLegend().setBackgroundPaint(Color.WHITE);
            chart.getLegend().setFrame(new LineBorder());
        }
        else
        {
            chart.removeLegend();
        }
    }

    /** 
    * Set the plot's background color
    * @param bgColor the Color object you wish to set the background to
    */
    public void set_background_color(Color bgColor)
    {
        if( bgColor != null )
            chart.getPlot().setBackgroundPaint(bgColor);
    }

    /**
    * Set the transparancy of the background
    * @param alpha The transparancy to use (0=transparent, 1=opaque)
    */
    public void set_background_alpha(float alpha)
    {
        chart.getPlot().setBackgroundImageAlpha(alpha);
    }
    
    /**
    * Set the transparancy of the background
    * @param alpha The transparancy to use (0=transparent, 1=opaque)
    */
    public void set_background_alpha(double alpha)
    {
        chart.getPlot().setBackgroundImageAlpha( (float)alpha);
    }

    /**
    * Set the plot gridline color
    * @param gridColor the Color object you wish to set the grid lines to
    */
    public void set_gridline_color(Color gridColor)
    {
        if( gridColor != null )
        {
            chart.getXYPlot().setDomainGridlinePaint(gridColor);
            chart.getXYPlot().setRangeGridlinePaint(gridColor);
        }
    }

    /** 
    * Toggle plot gridline visibility
    * @param showGridlines true to display the gridlines
    */
    public void set_gridline_visible(boolean showGridlines)
    {
        chart.getXYPlot().setDomainGridlinesVisible(showGridlines);
        chart.getXYPlot().setRangeGridlinesVisible(showGridlines);
    }

    /**
    * Set the plot window's background color (ie around the plot)
    * @param windowColor the Color object to set plot background to
    */
    public void set_window_background(Color windowColor)
    {
        if( windowColor != null )
            chart.setBackgroundPaint(windowColor);
    }

    /**
    * Set the plot's title
    * @param title The new title for this plot
    */
    public void set_title(String title)
    {
        chart.setTitle(title);
    }

    /**
    * Set the paint used to draw the plot border.
    * @param borderPaint the Paint object describing the border you want
    */
    public void set_border_paint(Paint borderPaint)
    {
        chart.setBorderPaint(borderPaint);
    }

    /**
    * Set the stroke for drawing the plot border.
    * @param borderStroke the Stroke object describing the border you want
    */
    public void set_border_color(Stroke borderStroke)
    {
        chart.setBorderStroke(borderStroke);
    }

    /**
    * Set whether the border should be displayed
    * @param borderVisible true displays a border
    */
    public void set_border_visible(boolean borderVisible)
    {
        chart.setBorderVisible(borderVisible);
    }

    /**
    * Perform a zoom (in or out) on the plot
    * @param zoom the zoom to use in %
    */
    public void set_zoom(float zoom)
    {
        chart.getPlot().zoom( (double) zoom);
    }

    /**
    * Perform a zoom (in or out) on the plot
    * @param zoom the zoom to use in %
    */
    public void set_zoom(double zoom)
    {
        chart.getPlot().zoom(zoom);
    }

    // ---------------------------------------------------------
    //                 Chart export util
    // ---------------------------------------------------------
    /**
     * Exports a JFreeChart to a SVG file using Apache Batik library.
     * 
     * @param chart JFreeChart to export
     * @param bounds the dimensions of the viewport
     * @param svgFile the output file.
     * @throws IOException if writing the svgFile fails.
     */
    protected void exportChartAsSVG(JFreeChart chart, Rectangle bounds, File svgFile) throws IOException 
    {
        // see http://dolf.trieschnigg.nl/jfreechart/

        // Get a DOMImplementation and create an XML document
        DOMImplementation domImpl =
            GenericDOMImplementation.getDOMImplementation();
        Document document = domImpl.createDocument(null, "svg", null);

        // Create an instance of the SVG Generator
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        // draw the chart in the SVG generator
        chart.draw(svgGenerator, bounds);

        // Write svg file
        OutputStream outputStream = new FileOutputStream(svgFile);
        Writer out = new OutputStreamWriter(outputStream, "UTF-8");
        svgGenerator.stream(out, true /* use css */);    

        // write and close file:                   
        outputStream.flush();
        outputStream.close();
    }

    /**
     * Exports a JFreeChart to a PS file using Adobe XML graphics library.
     * 
     * @param chart JFreeChart to export
     * @param bounds the dimensions of the viewport
     * @param psFile the output file.
     * @param mode the file write mode ("ps","eps")
     * @throws IOException if writing the file fails.
     */
    protected void exportChartAsPS(JFreeChart chart, Rectangle bounds, File psFile, String mode) throws IOException
    {
        // see http://xmlgraphics.apache.org/commons/postscript.html#creating-eps

        // set up file:
        OutputStream outputStream = new FileOutputStream(psFile);

        AbstractPSDocumentGraphics2D g2d;
        if( mode == "ps" )
            g2d = new PSDocumentGraphics2D(false);
        else
            g2d = new EPSDocumentGraphics2D(false);

        g2d.setGraphicContext(new org.apache.xmlgraphics.java2d.GraphicContext());

        //Set up the document size
        g2d.setupDocument(outputStream, (int)bounds.getWidth(), (int)bounds.getHeight());
        //out is the OutputStream to write the EPS to

        // draw the chart to g2d:
        chart.draw(g2d,bounds);

        // write and close file:    
        g2d.finish(); //Wrap up and finalize the EPS file
        outputStream.flush();
        outputStream.close();
    }

    /** Implementation of this is required for clipboard usage */
    public void lostOwnership( Clipboard clip, Transferable trans ) {
        //System.out.println( "Lost Clipboard Ownership" );
    }


    // ---------------------------------------------------------
    //                 Color stuff
    // ---------------------------------------------------------
    /** 
     * Choose a new color using JColorChooser dialog
     * @param message the message to display to user
     * @param init the default color
     * @return the user's selected color
     */
	private Color plot_color_chooser(String message, Color init)
    {
        return JColorChooser.showDialog(this,message,init);
    }
}