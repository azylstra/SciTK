/** Implement a dialog box for displaying info about the program
 *
 * @package SciTK
 * @class Dialog_About
 * @brief About dialog for the program and toolkit
 * @author Alex Zylstra
 * @date 2013/04/10
 * @copyright Alex Zylstra
 * @license MIT
 */

package SciTK;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.ImageIcon;

import java.awt.Dimension;
import java.awt.Font;

import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.List;
import java.util.ArrayList;

public class Dialog_About extends SciTK_Dialog 
{
    private boolean display_app_info; /* whether we should display extra info */
    private ImageIcon app_icon; /* an icon for the app using this toolkit */
    private String[] app_info; /* info about the app using this toolkit */
    private List<JLabel> app_info_label; /* for implementing the above app info */

    /** Constructor */
    public Dialog_About() 
    {
        super();
        display_app_info = false; // no app info provided
        initUI();
    }

    /** 
    * Constructor, including arguments to display info
    * about the application using SciTK
    *
    * @param app_icon_in An ImageIcon for your app.
    * @param app_info_in A String array containing info about your app to display (name, date, author). Each element is one line.
    */
    public Dialog_About(ImageIcon app_icon_in, String[] app_info_in)
    {
        super();
        display_app_info = true;

        // copy info to class variables:
        app_icon = app_icon_in;
        app_info = app_info_in;

        // generate UI:
        initUI();
    }

    /** 
    * Constructor with style options
    * @param custom_font The Font to use when displaying text in the Dialog
    * @param custom_align The alignment to use for all UI elements (0=left, 0.5=center, 1=right)
    */
    public Dialog_About(Font custom_font, float custom_align)
    {
        super(custom_font,custom_align);
        display_app_info = false; // no app info provided
        initUI();
    }

    /** 
    * Constructor, including arguments to display info and with style options
    * about the application using SciTK
    *
    * @param app_icon_in An ImageIcon for your app
    * @param app_info_in A String array containing info about your app to display (name, date, author). Each element is one line.
    * @param custom_font The Font to use when displaying text in the Dialog
    * @param custom_align The alignment to use for all UI elements (0=left, 0.5=center, 1=right)
    */
    public Dialog_About(ImageIcon app_icon_in, String[] app_info_in, Font custom_font, float custom_align)
    {
        super(custom_font,custom_align);
        display_app_info = true;

        // copy info to class variables:
        app_icon = app_icon_in;
        app_info = app_info_in;

        // generate UI:
        initUI();
    }

    /** Initialize the UI */
    private final void initUI()
    {
        // Layout is a vertical box layout for the Content Pane:
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // check to see if we should display any provided app info:
        if( display_app_info )
        {
            // Display app loco:
            add(Box.createRigidArea(new Dimension(0, 5))); // add vertical space
            JLabel app_icon_label = new JLabel(app_icon); // create a JLabel
            app_icon_label.setAlignmentX(alignment); // centered
            add(app_icon_label); // add to UI

            // set up the list:
            app_info_label = new ArrayList<JLabel>();

            // Display app info:
            // Need to loop over every string in app_info
            for( String i : app_info )
            {
                // add some vertical spacing:
                add(Box.createRigidArea(new Dimension(0, 5)));

                // create a new JLabel and set font, alignment:
                app_info_label.add( new JLabel(i) );
                app_info_label.get( app_info_label.size()-1 ).setFont(my_font);
                app_info_label.get( app_info_label.size()-1 ).setAlignmentX(alignment);

                // add the JLabel we just made to the UI:
                add( app_info_label.get( app_info_label.size()-1 ) );
            }
        }

        // Set up and display logo:
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/SciTK_logo.png"));
        add( Box.createRigidArea( new Dimension(0, 10) ) );
        JLabel icon_label = new JLabel(icon);
        icon_label.setAlignmentX(alignment); // centered on frame
        // add to UI:
        add(icon_label);

        // Add a text label containing information on the program
        // version number:
        add(Box.createRigidArea(new Dimension(0, 5)));
        JLabel version = new JLabel("SciTK v" + SciTK_Text.TOOLKIT_VERSION);
        version.setFont(my_font);
        version.setAlignmentX(alignment);
        add(version);

        // Add a text label containing information on the program
        // author:
        add(Box.createRigidArea(new Dimension(0, 5)));
        JLabel author = new JLabel("Author: " + SciTK_Text.TOOLKIT_AUTHOR);
        author.setFont(my_font);
        author.setAlignmentX(alignment);
        add(author);

        // Add a text label containing information on the program
        // date:
        add(Box.createRigidArea(new Dimension(0, 5)));
        JLabel date = new JLabel("Date: " + SciTK_Text.TOOLKIT_DATE);
        date.setFont(my_font);
        date.setAlignmentX(alignment);
        add(date);

        // Add a button to close this dialog box
        add(Box.createRigidArea(new Dimension(0, 15)));
        JButton close = new JButton("Close");
        close.setMnemonic(KeyEvent.VK_X);
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });
        close.setAlignmentX(alignment);
        add(close);

        // prevent input to other windows while active:
        setModalityType(ModalityType.APPLICATION_MODAL);
        // set title:
        setTitle("About Analysis");
        // let Java figure out the best size:
        pack();
        // general JFrame stuff:
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        close.requestFocusInWindow();
        setVisible(true);
    }
}