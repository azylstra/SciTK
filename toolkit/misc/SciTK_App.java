/** Implement the framework for an application using SciTK.
 * The idea here is that a user, writing an application, can
 * implement SciTK_App and take advantage of generic UI elements here.
 * In general a class extending SciTK_App would be the main window
 * of an application, and can call / create other windows from
 * the SciTK suite or custom.
 * 
 * @class SciTK_App
 * @brief Generic (abstract) application window
 * @author Alex Zylstra
 * @date 2013/04/21
 * @copyright Alex Zylstra
 * @license MIT
 */

package SciTK;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;


import javax.swing.JFileChooser;
import java.io.File;

import java.util.List;

import SciTK.*;

public abstract class SciTK_App extends SciTK_Frame
{
    // Some static strings to describe the application:
    protected String NAME = ""; /** Name of the application */
    protected String AUTHOR = ""; /** Author of the application */
    protected String DATE = ""; /** Application date */
    protected ImageIcon about_icon; /** icon or logo to display in the "About" dialog */
    protected ImageIcon tray_icon; /** Icon or logo to display in the system tray or dock */

    // FileChooser dialog and filter:
    protected JFileChooser fc; /** For opening files */
    protected ExtensionFileFilter fc_filter; /** For filtering files by extension */
    protected String FILE_FILTER_NAME = "";
    protected String[] FILE_FILTER_EXTS = {};

    // Window manager:
    protected WindowManager wm; /* Window manager for generated plots */

    // some UI elements which the extending class may want to modify:
    protected JMenuBar menubar; /** Menus at the top of the window */

    // Class variable to hold the working file:
    protected File app_file;

    // window sizing:
    private int default_width;
    private int default_height;

    /** 
    * Constructor with automatic sizing
    */
	public SciTK_App()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        default_width = (int) (screenSize.getWidth() / 5);
        default_height = (int) (screenSize.getHeight() / 5);
    }

    /** 
    * Constructor with specified window size
    * @param width the width in pixels of the application window
    * @param height the height in pixels of the application window
    */
    public SciTK_App(int width, int height)
    {
        default_width = width;
        default_height = height;
    }

    abstract protected void open_file();
    abstract protected void app_ui();

    /** Load the initial UI */
	protected final void initUI()
	{
        // initialize window manager:
        wm = new WindowManager();

        // initialize a layout manager:
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // set up FileChooser for this class:
        fc = new JFileChooser();
        fc_filter = new ExtensionFileFilter(FILE_FILTER_NAME, FILE_FILTER_EXTS);
        fc.setFileFilter(fc_filter);

        // create a menu bar:
		menubar = new JMenuBar();

        // ---------------------------------------------------------
        //                 First dropdown menu: "File"
        // ---------------------------------------------------------
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);

        // Set up a open dialog option under the file menu:
        ImageIcon menu_file_open_icon = new ImageIcon(getClass().getResource("resources/document-open-2.png"));
        JMenuItem menu_file_open = new JMenuItem("Open", menu_file_open_icon);
        menu_file_open.setMnemonic(KeyEvent.VK_O);
        menu_file_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menu_file_open.setToolTipText("Open a file");
        menu_file_open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // Select a file using the JFileChooser dialog:
                int fc_return = fc.showOpenDialog(SciTK_App.this);
                if( fc_return == JFileChooser.APPROVE_OPTION )
                {
                    app_file = fc.getSelectedFile();
                    // call a function, implemented by the user, to
                    // do something with the file:
                    open_file();
                }
            }
        });
        file.add(menu_file_open);

        ImageIcon menu_file_exit_icon = new ImageIcon(getClass().getResource("resources/application-exit.png"));
        JMenuItem menu_file_exit = new JMenuItem("Exit", menu_file_exit_icon);
        menu_file_exit.setMnemonic(KeyEvent.VK_X);
        menu_file_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menu_file_exit.setToolTipText("Exit application");
        menu_file_exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }

        });
        file.add(menu_file_exit);

        // ---------------------------------------------------------
        //                 Second dropdown menu: "Help"
        // ---------------------------------------------------------
        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);

        JMenuItem about = new JMenuItem("About");
        help.add(about);

        about.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                String[] info = new String[3];
                if( about_icon!=null && NAME!=null && DATE!=null && AUTHOR!=null)
                {
                    info[0] =  NAME ;
                    info[1] = DATE ;
                    info[2] = AUTHOR ;
                    Dialog_About ad = new Dialog_About(about_icon,info);
                }
                else
                {
                    Dialog_About ad = new Dialog_About();
                }
            }
        });

        // Add File and Help to the menu:
        menubar.add(file);
        menubar.add(help);
        // Set menubar as this JFrame's menu
        setJMenuBar(menubar);


        // ---------------------------------------------------------
        //                 Misc
        // ---------------------------------------------------------

        // Some global setup for the JFrame:
		setTitle(NAME);
		setSize(default_width,default_height);
        wm.add(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

        // set the icon to use for tray/dock functionality:
        if( tray_icon != null )
            this.setIconImage(tray_icon.getImage());
	}
}