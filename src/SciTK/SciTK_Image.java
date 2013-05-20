package SciTK;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.RenderingHints;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import java.awt.Container;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.io.IOException;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileWriter;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import java.awt.BasicStroke;
import java.awt.Color;

// for PS/EPS files
import org.apache.xmlgraphics.java2d.ps.AbstractPSDocumentGraphics2D;
import org.apache.xmlgraphics.java2d.ps.PSDocumentGraphics2D;
import org.apache.xmlgraphics.java2d.ps.EPSDocumentGraphics2D;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.awt.image.RescaleOp;

// For SVG files:

// for keyboard shortcuts
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

import java.lang.Math;

/** Implement a class which takes raw
 * 3-d data on a rectangular grid. and makes a grayscale image
 *
 * @package SciTK
 * @class SciTK_Image
 * @brief Plot data as an image
 * @author Alex Zylstra
 * @date 2013/05/19
 * @copyright Alex Zylstra
 * @license SciTK / MIT License
 */
public class SciTK_Image extends SciTK_Frame implements ClipboardOwner
{
	// -----------------------------------------------------
	// 					CLASS VARIABLES
	// -----------------------------------------------------
	// For storing the raw data in this plot:
	protected double[][] raw_data; /** The raw data passed to SciTK_Image */
	protected int raw_width; /** Intrinsic width of the data */
	protected int raw_height; /** Intrinsic height of the data */

	// for storing scaled image data:
	protected int[] image_data; /** Scaled data */
	protected int[] image_data_overlay; /** For displaying overlays */
	// dimensions of the scaled data:
	protected int width; /** Width of the displayed image */
	protected int height; /** Height of the displayed image */
	protected double scale; /** Scale factor for the image */

	// For displaying the image in a JFrame:
	protected BufferedImage image; /** Pure image */
	protected BufferedImage image_overlay; /** Image with overlays */
	protected ImageIcon icon; /** ImageIcon - convenient for displaying the BufferedImage */
	protected JLabel icon_label; /** JLabel - convenient for displaying the BufferedImage */

	// title for the window:
	protected String title;
	protected String title_root;

	// FileChooser dialog and filter:
	protected JFileChooser fc;
	protected ExtensionFileFilter fc_filter;

	// Class variable to hold the working file:
	protected File save_file;

	// Class variable for selecting points, rectangle, circle
	private Point p_image; // point coordinates in image itself
	private boolean p_selected; // if user has selected p
	private JLabel select_instruction; // JLabel for writing instructions to screen

	private Rectangle r_image; // rect coordinates in image itself
	// rectangles are defined by two corners in both image and frame:
	private Point r_corner1_image; 
	private Point r_corner2_image;
	private boolean r_selected; // if r has been selected

	private Ellipse2D c_image;
	// circles are defined by center and a second point on the radius
	private Point c_point1_image; 
	private Point c_point2_image;
	private boolean c_selected; // if circle has been selected


	// -----------------------------------------------------
	// 					CONSTRUCTORS
	// -----------------------------------------------------
	/** 
	* Constructor using integer data
	* @param image_data_in int data representing the image
	* @param title_in the title of this plot
	*/
	public SciTK_Image(int[][] image_data_in, String title_in)
	{
		this( ArrayUtil.convertIntDouble(image_data_in), title_in);
	}

	/**
	* Constructor using unscaled data 
	* @param raw_data_in the data in an i x j double array, e.g. raw_data_in[i][j] = pixel_ij
	* @param title_in the title of this plot
	*/
	public SciTK_Image(float[][] raw_data_in, String title_in)
	{
		this( ArrayUtil.convertFloatDouble(raw_data_in), title_in);
	}

	/** 
	* Constructor using unscaled data 
	* @param raw_data_in the data in an i x j double array, e.g. raw_data_in[i][j] = pixel_ij
	* @param title_in the title of this plot
	*/
	public SciTK_Image(double[][] raw_data_in, String title_in)
	{
		// set the data:
		updateData(raw_data_in);
		
		// Set the window title
		title_root = title_in;
		title = title_in + " (" + Integer.toString((int)(scale*100.0)) + "%)";

		// create the UI:
		initUI();
	}


	// -----------------------------------------------------
	// 					Update intrinsic data
	// -----------------------------------------------------
	/**
	 * Update data displayed in the image 
	 * @param raw_data_in the data in an i x j int array, e.g. raw_data_in[i][j] = pixel_ij
	 */
	public void updateData(int[][] raw_data_in)
	{
		updateData(ArrayUtil.convertIntDouble(raw_data_in));
	}

	/**
	 * Update data displayed in the image 
	 * @param raw_data_in the data in an i x j float array, e.g. raw_data_in[i][j] = pixel_ij
	 */
	public void updateData(float[][] raw_data_in)
	{
		updateData(ArrayUtil.convertFloatDouble(raw_data_in));
	}

	/**
	 * Update data displayed in the image 
	 * @param raw_data_in the data in an i x j double array, e.g. raw_data_in[i][j] = pixel_ij
	 */
	public void updateData(double[][] raw_data_in)
	{
		// copy raw data to class variable
		raw_data = raw_data_in;
		// set widths and heights:
		raw_width = raw_data[0].length;
		raw_height = raw_data.length;
		width = raw_width;
		height = raw_height;

		// allocate array for the image data:
		image_data = new int[(raw_width*raw_height)];

		// Find the max and min values:
		double max = raw_data[0][0];
		double min = raw_data[0][0];
		for(int i=0; i<raw_height;i++)
		{
			for(int j=0;j<raw_width;j++)
			{
				if( max < raw_data[i][j])
					max = raw_data[i][j];
				else if (min > raw_data[i][j])
					min = raw_data[i][j];
			}
		}

		// if max == min:
		if( max == min )
			min = 0;

		// Create image data, which is auto scaled so that
		// max -> white
		// min -> black
		for (int i=0; i<raw_height; i++) 
		{
			for(int j=0; j<raw_width; j++)
			{
				image_data[i*raw_width+j] = (int)( (double)(255) * ( raw_data[i][j] - min )/(max-min));
			}	
		}
		// Images are generally byte grayscale.
		// For overlays, we convert to a RGB array
		image_data_overlay = new int[3*(width*height)];
		for(int i=0; i<height; i++)
		{
			for(int j=0; j<width; j++)
			{
				int temp = image_data[i*width+j];
				image_data_overlay[3*(i*width+j)] = temp;
				image_data_overlay[3*(i*width+j)+1] = temp;
				image_data_overlay[3*(i*width+j)+2] = temp;
			}
		} 

		// pick a scale factor
		scale = (double)1.0;
		if( width > 200 || height > 200 )
		{
			while( scale*width > 200 )
			{
				scale -= 0.1;
			}
		}

		// update the UI if the image is already displayed:
		if( this.isVisible() )
			updateUI();
	}


	// -----------------------------------------------------
	// 					IMAGE GENERATION
	// -----------------------------------------------------
	/** Generate the image to be displayed, with scale factor */
	public void generateImage()
	{
		if( icon instanceof ImageIcon) 
			icon.getImage().flush();
		// Set up temp image at native resolution
		BufferedImage temp;
		// needs to be in color if overlays will be displayed:
		if( p_selected || r_selected || c_selected )
		{
			temp = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			temp.getRaster().setPixels(0,0,width,height,image_data_overlay);
		}
		else
		{
			temp = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
			temp.getRaster().setPixels(0,0,width,height,image_data);
		}
		
		// calculated scaled dimensions:
		int disp_width = (int) (scale*(float)width);
		int disp_height = (int) (scale*(float)height);

		// set class image variable to a BufferedImage with the scaled dimensions:
		image = new BufferedImage(disp_width, disp_height, temp.getType());  

		// Use Graphics2D to re-render temp into image:
		Graphics2D g = image.createGraphics();  
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
		g.drawImage(temp, 0, 0, disp_width, disp_height, 0, 0, width, height, null);  

		// If a point/rectangle is selected, draw them:
			g.setStroke(new BasicStroke((float)1.0));
		if( p_selected )
		{
			Rectangle to_draw = new Rectangle((int)(p_image.x*scale), (int)(p_image.y*scale), 
				(int)(1*scale), (int)(1*scale));
			g.setColor(Color.BLUE);
			g.draw(to_draw);
			g.fill(to_draw);
		}
		if( r_selected )
		{
			// calculate scaled rectangle:
			Rectangle r_scaled = new Rectangle((int)(r_image.x*scale), (int)(r_image.y*scale), 
				(int)(r_image.width*scale), (int)(r_image.height*scale));
			// draw it on the image:
			g.setColor(Color.RED);
			g.draw(r_scaled);
			g.setColor(new Color(255,0,0,50));
			g.fill(r_scaled);
		}
		if( c_selected )
		{
			// Calculate a scaled circle:
			Ellipse2D.Float c_scaled = new Ellipse2D.Float( (float)(c_image.getX()*scale), (float)(c_image.getY()*scale), 
				(float)(c_image.getWidth()*scale), (float)(c_image.getHeight()*scale));
			// draw it on the image:
			g.setColor(Color.GREEN);
			g.draw(c_scaled);
			g.setColor(new Color(0,255,0,50));
			g.fill(c_scaled);
		}

		// done drawing
		g.dispose();  

		// generate a new ImageIcon from image:
		icon = new ImageIcon(image);
		icon_label.setIcon(icon); 
	}

	// -----------------------------------------------------
	// 				COPY / SAVE FUNCTIONALITY
	// -----------------------------------------------------
	/** Save the displayed image to file using a dialog */
	public void saveImage()
	{
		fc = new JFileChooser();
		fc.addChoosableFileFilter(new ExtensionFileFilter("BMP", new String[] {"bmp,BMP"}));
		fc.addChoosableFileFilter(new ExtensionFileFilter("JPG", new String[] {"jpg,JPG,jpeg,JPEG"}));
		fc.addChoosableFileFilter(new ExtensionFileFilter("GIF", new String[] {"gif,GIF"}));
		fc.addChoosableFileFilter(new ExtensionFileFilter("EPS", new String[] {"eps,EPS"}));
		fc.addChoosableFileFilter(new ExtensionFileFilter("PS", new String[] {"ps,PS"}));
		fc.addChoosableFileFilter(new ExtensionFileFilter("PNG", new String[] {"png,PNG"}));
		// remove default option:
		fc.setAcceptAllFileFilterUsed(false);

		// Select a file using the JFileChooser dialog:
		int fc_return = fc.showSaveDialog(SciTK_Image.this);
		if( fc_return == JFileChooser.APPROVE_OPTION ) // user wants to save
		{
			//get the file extension from the filter:
			save_file = fc.getSelectedFile();
			String extension = fc.getFileFilter().getDescription();
			try // try/catch for IO errors
			{
				// For each type of extension, check to see if the name includes the
				// correct extension and then save the image to file.
				// Default extension is PNG
				if( extension=="JPG" )
				{
					if( !save_file.getName().endsWith(".jpg") && !save_file.getName().endsWith(".JPG") &&
						!save_file.getName().endsWith(".jpeg") && !save_file.getName().endsWith(".JPEG") )
						save_file = new File(save_file.getAbsolutePath()+".jpg");
					ImageIO.write(image, "jpg", save_file);
				}
				else if( extension=="BMP" )
				{
					if( !save_file.getName().endsWith(".bmp") && !save_file.getName().endsWith(".BMP") )
						save_file = new File(save_file.getAbsolutePath()+".bmp");
					ImageIO.write(image, "bmp", save_file);
				}	
				else if( extension=="GIF" )
				{
					if( !save_file.getName().endsWith(".gif") && !save_file.getName().endsWith(".GIF") )
						save_file = new File(save_file.getAbsolutePath()+".gif");
					ImageIO.write(image, "gif", save_file);
				}
				else if( extension=="EPS" )
				{
					if( !save_file.getName().endsWith(".eps") && !save_file.getName().endsWith(".EPS") )
						save_file = new File(save_file.getAbsolutePath()+".eps");
					exportImageAsPS(save_file,"eps");
				}
				else if( extension=="PS" )
				{
					if( !save_file.getName().endsWith(".ps") && !save_file.getName().endsWith(".PS") )
						save_file = new File(save_file.getAbsolutePath()+".ps");
					exportImageAsPS(save_file,"ps");
				}
				else // ( extension=="PNG" )
				{
					if( !save_file.getName().endsWith(".png") && !save_file.getName().endsWith(".PNG") )
						save_file = new File(save_file.getAbsolutePath()+".png");
					ImageIO.write(image, "png", save_file);
				}
			}
			// If there was an error, launch an error dialog box:
			catch(IOException e)
			{
				DialogError emsg = new DialogError(this," There was an error saving the file." + System.getProperty("line.separator") + e.getMessage());
			}
		}
	}

	/** Save the raw data to a file using a dialog */
	public void saveData()
	{
		fc = new JFileChooser();
		fc.addChoosableFileFilter(new ExtensionFileFilter("CSV", new String[] {"csv,CSV"}));

		// Select a file using the JFileChooser dialog:
		int fc_return = fc.showSaveDialog(SciTK_Image.this);

		if( fc_return == JFileChooser.APPROVE_OPTION ) // user wants to save
		{
			//get the file name from the filter:
			save_file = fc.getSelectedFile();

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
				DialogError emsg = new DialogError(this," There was an error saving the file. \n" + e.getMessage());
			}
		}
	}

	/** Copy the displayed image to the clipboard */
	public void copyImage()
	{
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		TransferableImage to_copy = new TransferableImage(image);
		c.setContents(to_copy,this);
	}

	/** Copy the raw data to the clipboard */
	public void copyData()
	{
		// Get the system clipboard:
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		// Java requires that clipboard gets this StringSelection thing:
		StringSelection s2 = new StringSelection( toString() );
		// set the clipboard contents:
		c.setContents(s2,this);
	}

	/** 
	* Get a string representation of the raw data displayed
	* @return A CSV representation of the displayed data.
	*/
	public String toString()
	{
		String s = new String(""); // build a new string
		//iterate over all elements in 2D array:
		for(int i=0; i<raw_height; i++)
		{
			for(int j=0; j<raw_width; j++)
			{
				// add the number at [i][j] to s, then a delim char:
				s = s.concat( Double.toString(raw_data[i][j]) );
				s = s.concat(SciTK_Text.TOOLKIT_CSV_DELIM);
			}
			// line break:
			s = s.concat(SciTK_Text.TOOLKIT_NEWLINE);
		}
		return s;
	}


	// -----------------------------------------------------
	// 					MISC UI
	// -----------------------------------------------------
	/** Set the image zoom (or scale) using a prompt */
	public void setZoom()
	{
		// prompt the user:
		DialogPrompt zoom_prompt = new DialogPrompt(new PromptValueNumeric("","Zoom %"),"Set Zoom %");
		if(zoom_prompt.getAccepted() == false) // user cancelled
			return;
		try
		{
			scale = (float)0.01*((PromptValueNumeric)zoom_prompt.getValue()).get_value_float();
		}
		catch(Exception e)
		{
			DialogError emsg = new DialogError(this," Unable to set zoom, try again. " + '\n' + e.getMessage());
			setZoom();
		}

		updateUI();
	}

	/** Update the displayed image */
	public void updateUI()
	{
		// Update the image:
		generateImage();

		// update the title (if zoom change):
		title = title_root + " (" + Integer.toString((int)(scale*100.0)) + "%)";
		setTitle(title);

		// automatically resize:
		pack();

		// update:
		this.repaint();
	}


	// -----------------------------------------------------
	// 				SELECT PARTS OF IMAGE
	// -----------------------------------------------------
	/** Enter mode to select a point on the image (for lineout, etc) */
	public void selectPoint()
	{
		p_image = new Point(); // initialize
		p_selected = false;

		// remove an old instruction, if it exists
		if( select_instruction instanceof JLabel )
			remove(select_instruction);
		// remove any existing mouse listeners:
		removeAllListeners();

		// Make a label to instruct the user
		select_instruction = new JLabel("Select a point");
		add(select_instruction,BorderLayout.SOUTH);
		pack();
		this.repaint();

		// add a listener to the image
		icon_label.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent me) {
				if(onImage(me.getPoint()))
				{
					// set point to coordinates ON THE IMAGE
					// (some fudge factor required)
					p_image = pointTransformToImage(me.getPoint());
					p_image.x = p_image.x - 1;
					p_image.y = p_image.y + 2;
					// correct for scale factor:
					p_image.x = (int) (p_image.x / scale);
					p_image.y = (int) (p_image.y / scale);
					// update text displayed at bottom of the window:
					select_instruction.setText("Point: (" + p_image.x + "," + p_image.y + ")");
				}
				else
				{
					select_instruction.setText("Point: ");
				}
				select_instruction.repaint();
			}
		});
		// Add another mouse listener to detect clicks:
		icon_label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				// Only perform action if the user clicks on the image:
				if( onImage(me.getPoint()) )
				{
					//when a user clicks, the point is selected:
					p_selected = true;

					// remove all mouse listeners:
					removeAllListeners();

					// remove the JLael and repaint the frame:
					remove(select_instruction);
					select_instruction = null;

					pack();
					updateUI();
				}
			}
		});
	}

	/** Enter mode to select a rectangle on the image (for lineout, etc) */
	public void selectRectangle()
	{
		r_image = new Rectangle(); // init
		r_selected = false;

		// remove an old instruction, if it exists
		if( select_instruction instanceof JLabel )
			remove(select_instruction);
		// remove any existing mouse listeners:
		removeAllListeners();

		// Make a label to instruct the user
		select_instruction = new JLabel("Drag to select rectangle");
		add(select_instruction,BorderLayout.SOUTH);
		pack();
		this.repaint();

		// add a listener to the image
		icon_label.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent me) {
				if(onImage(me.getPoint()))
				{
					// Define the start of the rectangle:
					// set point to coordinates ON THE IMAGE
					// (some fudge factor required)
					r_corner1_image = pointTransformToImage(me.getPoint());
					r_corner1_image.x = r_corner1_image.x - 1;
					r_corner1_image.y = r_corner1_image.y + 2;
					// correct for scale factor:
					r_corner1_image.x = (int) (r_corner1_image.x / scale);
					r_corner1_image.y = (int) (r_corner1_image.y / scale);
					// update instruction text:
					select_instruction.setText("Start: (" + r_corner1_image.x + "," + r_corner1_image.y + ")");
				}
				else
				{
					select_instruction.setText("Start: ");
				}
				select_instruction.repaint();
			}

			@Override
			public void mouseDragged(MouseEvent me) {
				// only perform rectangle calculations if on point:
				if( onImage(me.getPoint()) )
				{
					// set second corner to define the rectangle:
					r_corner2_image = pointTransformToImage(me.getPoint());
					r_corner2_image.x = r_corner2_image.x - 1;
					r_corner2_image.y = r_corner2_image.y + 2;
					// correct for scale factor:
					r_corner2_image.x = (int) (r_corner2_image.x / scale);
					r_corner2_image.y = (int) (r_corner2_image.y / scale);

					// To allow for rectangles to be drawn multiple directions,
					// and prevent negative width, height:
					int x_min = Math.min( r_corner1_image.x , r_corner2_image.x );
					int x_max = Math.max( r_corner1_image.x , r_corner2_image.x );
					int y_min = Math.min( r_corner1_image.y , r_corner2_image.y );
					int y_max = Math.max( r_corner1_image.y , r_corner2_image.y );

					// set rectangle based x_min, y_min and calculated width/height:
					r_image = new Rectangle(x_min, y_min, x_max-x_min, y_max-y_min);

					//when a user drags, the point is selected:
					r_selected = true;

					select_instruction.setText("Width: " + r_image.getWidth() + ", Height: " + r_image.getHeight());

					updateUI();
				}
				else
				{
					r_selected = false;
				}
			}            
		});
		icon_label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent me) {
				// if the mouse is released then rectangle selection is done
				// remove all mouse listeners:
				removeAllListeners();

				// remove the JLael and repaint the frame:
				remove(select_instruction);
				select_instruction = null;
				pack();

				updateUI();
			}
		});
	}

	/** Enter mode to select a circle on the image (for lineout, etc) */
	public void selectCircle()
	{
		c_image = new Ellipse2D.Float(); // init
		c_selected = false;

		// remove an old instruction, if it exists
		if( select_instruction instanceof JLabel )
			remove(select_instruction);
		// remove any existing mouse listeners:
		removeAllListeners();

		// Make a label to instruct the user
		select_instruction = new JLabel("Start by selecting center");
		add(select_instruction,BorderLayout.SOUTH);
		pack();
		this.repaint();

		// add a listener to the image
		icon_label.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent me) {
				if(onImage(me.getPoint()))
				{
					// Define the start of the circle:
					// set point to coordinates ON THE IMAGE
					// (some fudge factor required)
					c_point1_image = pointTransformToImage(me.getPoint());
					c_point1_image.x = c_point1_image.x - 1;
					c_point1_image.y = c_point1_image.y + 2;
					// correct for scale factor:
					c_point1_image.x = (int) (c_point1_image.x / scale);
					c_point1_image.y = (int) (c_point1_image.y / scale);
					select_instruction.setText("Center: (" + c_point1_image.x + "," + c_point1_image.y + ")");
				}
				else
				{
					select_instruction.setText("Center: ");
				}
				select_instruction.repaint();
			}

			@Override
			public void mouseDragged(MouseEvent me) {
				// only perform rectangle calculations if on point:
				if( onImage(me.getPoint()) )
				{
					// set second corner to define the rectangle:
					c_point2_image = pointTransformToImage(me.getPoint());
					c_point2_image.x = c_point2_image.x - 1;
					c_point2_image.y = c_point2_image.y + 2;
					// correct for scale factor:
					c_point2_image.x = (int) (c_point2_image.x / scale);
					c_point2_image.y = (int) (c_point2_image.y / scale);

					// Calculate the corner of the circle and it's total width
					// to construct an Ellipse2D.Float
					// First, in image system:
					int r = (int)Math.sqrt( Math.pow(c_point1_image.x - c_point2_image.x , 2) 
						+ Math.pow(c_point1_image.y - c_point2_image.y , 2) );
					int x = c_point1_image.x;
					int y = c_point1_image.x;
					// Create new Ellipse2D:
					c_image = new Ellipse2D.Float( x-r, y-r, 2*r, 2*r );

					//when a user clicks, the point is selected:
					c_selected = true;

					// Update JLabel:
					select_instruction.setText("Radius: " + r);

					updateUI();
				}
				else
				{
					c_selected = false;
				}
			}            
		});
		icon_label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent me) {
				// if the mouse is released after dragging,
				// then selection is done
				// remove all mouse listeners:
				removeAllListeners();

				// remove the JLael and repaint the frame:
				remove(select_instruction);
				select_instruction = null;
				pack();

				updateUI();

				System.out.println(c_image);
			}
		});
	}

	/** Remove all selections from the image */
	public void clearSelections()
	{
		// clear all point info:
		p_image = null;
		//p_frame = null;
		p_selected = false;

		// clear all rectangle info:
		r_image = null;
		//r_frame = null;
		r_corner1_image = null; r_corner2_image = null;
		//r_corner1_frame = null; r_corner2_frame = null;
		r_selected = false;

		// clear all circle info:
		c_image = null;
		//c_frame = null;
		c_point1_image = null; c_point2_image = null;
		//c_point1_frame = null; c_point2_frame = null;
		c_selected = false;

		// if necessary, remove instruction JLabel from frame:
		if( select_instruction instanceof JLabel )
			remove(select_instruction);
		select_instruction = null;

		// we also need to make sure that all mouse listeners
		// are removed from the image:
		removeAllListeners();

		updateUI();
	}

	/** 
	* Transform coordinates from JFrame land to the image as displayed
	* @param p the Point to transform
	* @return the point after transformation
	*/
	private Point pointTransformToImage(Point p)
	{
		// get a rectangle containing coordinates of the displayed image
		Rectangle image_bounds = icon_label.getBounds();
		// The frame's panes:
		final Container cp = this.getContentPane();
		// get size of the frame:
		Dimension frame_size = cp.getSize();
		// menu bar for this frame:
		final JMenuBar mb = this.getJMenuBar();
		// get size of the menu bar
		Dimension menu_size = mb.getSize();

		// get heights of various labels if they exist (ie are displayed):
		int label_height = 0;
		if( select_instruction instanceof JLabel )
			label_height += select_instruction.getHeight();

		// do coordinate transformation:
		int x = (int) (frame_size.getWidth()/2 - scale*width/2);
		int y = (int) (frame_size.getHeight()/2 - menu_size.getHeight()/2 
			+ label_height/2 - scale*height/2);
		return new Point(p.x-x,p.y-y);
	}

	/** 
	* Transform coordinates from image land to the JFrame as displayed
	* @param p the Point to transform
	* @return the point after transformation
	*/
	private Point pointTransformToFrame(Point p)
	{
		// get a rectangle containing coordinates of the displayed image
		Rectangle image_bounds = icon_label.getBounds();
		// The frame's panes:
		final Container cp = this.getContentPane();
		// get size of the frame:
		Dimension frame_size = cp.getSize();
		// menu bar for this frame:
		final JMenuBar mb = this.getJMenuBar();
		// get size of the menu bar
		Dimension menu_size = mb.getSize();

		// get heights of various labels if they exist (ie are displayed):
		int label_height = 0;
		if( select_instruction instanceof JLabel )
			label_height += select_instruction.getHeight();

		// do coordinate transformation:
		int x = (int) (frame_size.getWidth()/2 - scale*width/2);
		int y = (int) (frame_size.getHeight()/2 - menu_size.getHeight()/2 
			+ label_height/2 - scale*height/2);
		return new Point(p.x+x,p.y+y);
	}

	/** 
	* Detect if a point is on the image as displayed (for use with MouseListeners)
	* @param p the Point to check
	* @return true if p is over the image
	*/
	private boolean onImage(Point p)
	{
		// use pointTransformToImage to get the coordinates of the image corner
		Point p2 = pointTransformToImage(new Point(0, 0)); // in image
		Rectangle image_bounds = new Rectangle();

		// create a new rectangle describing extent of the image in JFrame:
		image_bounds.setRect(-p2.x+1,-p2.y-2,scale*width,scale*height);
		
		// check to see if p is over the image:
		return image_bounds.contains(p);
	}

	/** Remove all mouse listeners from the image */
	private void removeAllListeners()
	{
		// remove MouseMotionListeners
		MouseMotionListener[] all_listeners = icon_label.getMouseMotionListeners();
		for(int i=0; i<all_listeners.length; i++)
			icon_label.removeMouseMotionListener( all_listeners[i] );
		// remove MouseListeners
		MouseListener[] all_listeners2 = icon_label.getMouseListeners();
		for(int i=0; i<all_listeners2.length; i++)
			icon_label.removeMouseListener( all_listeners2[i] );
	}


	// -----------------------------------------------------
	// 					MAIN UI
	// -----------------------------------------------------
	/** Load the initial UI */
	private final void initUI()
	{
		// initialize a layout manager:
		this.setLayout(new BorderLayout());

		// create a menu bar:
		JMenuBar menubar = new JMenuBar();
		// ---------------------------------------------------------
		//                 First dropdown menu: "File"
		// ---------------------------------------------------------
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);

		// Set up a save dialog option under the file menu:
		JMenuItem menu_file_save;
		ImageIcon menu_file_save_icon = null;
		try
		{
			menu_file_save_icon = new ImageIcon(getClass().getResource("/SciTK/resources/document-save-5.png"));
			menu_file_save = new JMenuItem("Save", menu_file_save_icon);
		}
		catch(Exception e)
		{
			menu_file_save = new JMenuItem("Save");
		}
		menu_file_save.setMnemonic(KeyEvent.VK_S);
		menu_file_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu_file_save.setToolTipText("Save an image");
		menu_file_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				saveImage();
			}
		});
		file.add(menu_file_save);

		// Set up a save data dialog option under the file menu:
		JMenuItem menu_file_save_data;
		try
		{
			menu_file_save_data = new JMenuItem("Save data", menu_file_save_icon);
		}
		catch(Exception e)
		{
			menu_file_save_data = new JMenuItem("Save data");
		}
		menu_file_save_data.setToolTipText("Save raw data to file");
		menu_file_save_data.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				saveData();
			}
		});
		file.add(menu_file_save_data);

		// add a menu item to close the window
		JMenuItem menu_file_exit;
		try
		{
			ImageIcon menu_file_exit_icon = new ImageIcon(getClass().getResource("/SciTK/resources/application-exit.png"));
			menu_file_exit = new JMenuItem("Exit", menu_file_exit_icon);
		}
		catch(Exception e)
		{
			menu_file_exit = new JMenuItem("Exit");
		}
		menu_file_exit.setMnemonic(KeyEvent.VK_X);
		menu_file_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu_file_exit.setToolTipText("Close window");
		menu_file_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dispose();
			}

		});
		file.add(menu_file_exit);


		// ---------------------------------------------------------
		//                 Second dropdown menu: "Edit"
		// ---------------------------------------------------------
		JMenu edit = new JMenu("Edit");
		edit.setMnemonic(KeyEvent.VK_E);

		// copy to clipboard
		JMenuItem menu_edit_copy_image;
		ImageIcon menu_edit_copy_icon = null;
		try
		{
			menu_edit_copy_icon = new ImageIcon(getClass().getResource("/SciTK/resources/edit-copy-7.png"));
			menu_edit_copy_image = new JMenuItem("Copy", menu_edit_copy_icon);
		}
		catch(Exception e)
		{
			menu_edit_copy_image = new JMenuItem("Copy");
		}
		menu_edit_copy_image.setMnemonic(KeyEvent.VK_C);
		menu_edit_copy_image.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu_edit_copy_image.setToolTipText("Copy image to clipboard");
		menu_edit_copy_image.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				copyImage();
			}
		});
		edit.add(menu_edit_copy_image);

		// copy data to clipboard
		JMenuItem menu_edit_copy_data;
		try
		{
			menu_edit_copy_data = new JMenuItem("Copy data", menu_edit_copy_icon);
		}
		catch(Exception e)
		{
			menu_edit_copy_data = new JMenuItem("Copy data");
		}
		menu_edit_copy_data.setToolTipText("Copy data to clipboard");
		menu_edit_copy_data.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				copyData();
			}
		});
		edit.add(menu_edit_copy_data);

		// zoom:
		JMenuItem menu_edit_zoom;
		try
		{
			ImageIcon menu_edit_zoom_icon = new ImageIcon(getClass().getResource("/SciTK/resources/zoom-3.png"));
			menu_edit_zoom = new JMenuItem("Zoom",menu_edit_zoom_icon);
		}
		catch(Exception e)
		{
			menu_edit_zoom = new JMenuItem("Zoom");
		}
		menu_edit_zoom.setMnemonic(KeyEvent.VK_Z);
		menu_edit_zoom.setToolTipText("Set image zoom");
		menu_edit_zoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setZoom();
			}
		});
		edit.add(menu_edit_zoom);


		// ---------------------------------------------------------
		//                 Third dropdown menu: "Analysis"
		// ---------------------------------------------------------
		JMenu analysis = new JMenu("Analysis");
		analysis.setMnemonic(KeyEvent.VK_A);

		// select a point
		JMenuItem menu_analysis_point = new JMenuItem("Select point");
		menu_analysis_point.setToolTipText("Select a point in the image");
		menu_analysis_point.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				selectPoint();
			}
		});
		analysis.add(menu_analysis_point);

		// select a rectangle
		JMenuItem menu_analysis_rectangle = new JMenuItem("Select rectangle");
		menu_analysis_rectangle.setToolTipText("Select a rectangle in the image");
		menu_analysis_rectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				selectRectangle();
			}
		});
		analysis.add(menu_analysis_rectangle);

		// select a circle
		JMenuItem menu_analysis_circle = new JMenuItem("Select circle");
		menu_analysis_circle.setToolTipText("Select a circle in the image");
		menu_analysis_circle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				selectCircle();
			}
		});
		analysis.add(menu_analysis_circle);

		// Clear all selections
		JMenuItem menu_analysis_clear = new JMenuItem("Clear all");
		menu_analysis_clear.setToolTipText("Clear all selections from image");
		menu_analysis_clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				clearSelections();
			}
		});
		analysis.add(menu_analysis_clear);



		// ---------------------------------------------------------
		//                 General UI
		// ---------------------------------------------------------
		// Add File to the menu:
		menubar.add(file);
		// Add Edit to the menu:
		menubar.add(edit);
		// Add Analysis to the menu:
		menubar.add(analysis);
		// Set menubar as this JFrame's menu
		setJMenuBar(menubar);

		// call updateUI(), which handles the image stuff.
		if( !(icon_label instanceof JLabel) )
			icon_label = new JLabel();
		icon_label.setHorizontalAlignment(SwingConstants.CENTER);
		add(icon_label,BorderLayout.CENTER);
		updateUI();

		// set size automatically:
		pack();
		// if the window is closed, just dispose it:
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setTitle(title);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}


	// ---------------------------------------------------------
	//           Implement clipboard functionality
	// ---------------------------------------------------------
	public void lostOwnership( Clipboard clip, Transferable trans ) {
		System.out.println( "Lost Clipboard Ownership" );
	}

	private class TransferableImage implements Transferable {

		Image i;

		public TransferableImage( Image i ) {
			this.i = i;
		}

		public Object getTransferData( DataFlavor flavor )
		throws UnsupportedFlavorException, IOException {
			if ( flavor.equals( DataFlavor.imageFlavor ) && i != null ) {
				return i;
			}
			else {
				throw new UnsupportedFlavorException( flavor );
			}
		}

		public DataFlavor[] getTransferDataFlavors() {
			DataFlavor[] flavors = new DataFlavor[ 1 ];
			flavors[ 0 ] = DataFlavor.imageFlavor;
			return flavors;
		}

		public boolean isDataFlavorSupported( DataFlavor flavor ) {
			DataFlavor[] flavors = getTransferDataFlavors();
			for ( int i = 0; i < flavors.length; i++ ) {
				if ( flavor.equals( flavors[ i ] ) ) {
					return true;
				}
			}

			return false;
		}
	}

	// ---------------------------------------------------------
	//                 Vector graphics save util
	// ---------------------------------------------------------
	/**
	 * Exports a JFreeChart to a EPS file using Adobe XML graphics library.
	 * @param psFile the output file.
	 * @param mode the file output mode ("ps","eps")
	 * @throws IOException if writing the file fails.
	 */
	protected void exportImageAsPS(File psFile, String mode) throws IOException
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
		g2d.setupDocument(outputStream, (int)width, (int)height);
		//out is the OutputStream to write the to

		// draw the image to g2d:
		g2d.drawImage(image,new RescaleOp((float)1.0,(float)0.0,null),0,0);
		

		// write and close file:    
		g2d.finish(); //Wrap up and finalize the file
		outputStream.flush();
		outputStream.close();
	}
}