package SciTK;

import java.awt.Window;
import javax.swing.JFrame;
import java.util.ArrayList;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Dimension;

import java.awt.Toolkit;

/** Implement a window manager for the analysis GUI.
 * this class is used to place new windows in available
 * screen real estate.
 *
 * @package SciTK
 * @class WindowManager
 * @brief Manage placement of windows within the screen
 * @author Alex Zylstra
 * @date 2013/04/09
 * @copyright Alex Zylstra
 * @license SciTK / MIT License
 */
public class WindowManager
{
	ArrayList<JFrame> frames; /* windows managed by this WindowManager */

	/** Constructor */
	public WindowManager()
	{
		frames = new ArrayList<JFrame>();
	}

	/** Add a new window to the application window manager
	 * @param frame the new frame. Its position will be set by add.
	 */
	public void add(JFrame frame)
	{
		// Need to find a point to add the new frame to
		Point new_loc = new Point(0,0); // temporarily 0,0
		Dimension size = frame.getSize();
		Rectangle frame_rect = new Rectangle(new_loc,size);

		// get screen size:
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// iterate over possible locations to place new window:
		int dx = 10; // horizontal step size
		int dy = 10; // vertical step size
		// maximum values to place this window:
		int y_max = (int)(screenSize.getHeight() - size.getHeight());
		int x_max = (int)(screenSize.getWidth() - size.getWidth());
		for(int j=0; j<y_max; j+=dy)
		{
			for(int i=0; i<x_max; i+=dx)
			{
				new_loc.setLocation(i,j);
				frame_rect = new Rectangle(new_loc,size);
				if(!conflict(frame_rect))
				{
					frame.setLocation(i,j);
					frames.add(frame);
					return;
				}
			}
		}

		// if we did not find a place to put it:
		frame.setLocation(0,0);
		frames.add(frame);
	}

	/** Detect a conflict between a given rectangle and the existing frames */
	private boolean conflict(Rectangle r)
	{
		// iterate over all JFrames stored:
		Rectangle other;
		for(int i=0; i<frames.size(); i++)
		{
			// sanity check (make sure it's not deleted)
			if( frames.get(i).isVisible() )
			{
				other = frames.get(i).getBounds();
				if( other.intersects(r) )
					return true;
			}
		}
		return false;
	}
}