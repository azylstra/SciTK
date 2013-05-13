/** Implement a GUI dialog box
 * to get a value from the user
 *
 * @package SciTK
 * @class Dialog_Prompt
 * @brief A GUI dialog box to prompt for a single value
 * @author Alex Zylstra
 * @date 2013/04/10
 * @copyright Alex Zylstra
 * @license MIT
 */

package SciTK;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.ImageIcon;

import java.awt.Dimension;
import java.awt.Font;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Dialog_Prompt extends SciTK_Dialog
{
    private Prompt_Value value; /* The value being prompted for */
    private boolean accepted; /* If the user 'accepted' the input [ie clicked OK] */
    private JTextField input; /* The text input field */

    // --------------------------------------
    //          Constructors
    // --------------------------------------
    /** Constructor
     * @param title the title of the prompt window
     */
    public Dialog_Prompt(String title)
    {
        value = new Prompt_Value("","");
        initUI(title,10);
    }

    /** Constructor
     * @param title the title of the prompt window
     * @param width the width in characters of text input box
     */
    public Dialog_Prompt(String title, int width)
    {
        value = new Prompt_Value("","");
        initUI(title,width);
    }

    /** Constructor
     * @param new_val the Prompt_Value representation of the value to get from user
     * @param title the title of the prompt window
     */
    public Dialog_Prompt(Prompt_Value new_val, String title)
    {
        value = new_val;
        initUI(title,10);
    }

    /** Constructor
     * @param new_val the Prompt_Value representation of the value to get from user
     * @param title the title of the prompt window
     * @param width the width in characters of text input box
     */
    public Dialog_Prompt(Prompt_Value new_val, String title, int width)
    {
        value = new_val;
        initUI(title,width);
    }

    /** Constructor
     * @param title the title of the prompt window
     * @param custom_font The Font to use when displaying text in the Dialog
     * @param custom_align The alignment to use for all UI elements (0=left, 0.5=center, 1=right)
     */
    public Dialog_Prompt(String title, Font custom_font, float custom_align)
    {
        super(custom_font,custom_align);
        value = new Prompt_Value("","");
        initUI(title,10);
    }

    /** Constructor
     * @param title the title of the prompt window
     * @param width the width in characters of text input box
     * @param custom_font The Font to use when displaying text in the Dialog
     * @param custom_align The alignment to use for all UI elements (0=left, 0.5=center, 1=right)
     */
    public Dialog_Prompt(String title, int width, Font custom_font, float custom_align)
    {
        super(custom_font,custom_align);
        value = new Prompt_Value("","");
        initUI(title,width);
    }

    /** Constructor
     * @param new_val the Prompt_Value representation of the value to get from user
     * @param title the title of the prompt window
     * @param custom_font The Font to use when displaying text in the Dialog
     * @param custom_align The alignment to use for all UI elements (0=left, 0.5=center, 1=right)
     */
    public Dialog_Prompt(Prompt_Value new_val, String title, Font custom_font, float custom_align)
    {
        super(custom_font,custom_align);
        value = new_val;
        initUI(title,10);
    }

    /** Constructor
     * @param new_val the Prompt_Value representation of the value to get from user
     * @param title the title of the prompt window
     * @param width the width in characters of text input box
     * @param custom_font The Font to use when displaying text in the Dialog
     * @param custom_align The alignment to use for all UI elements (0=left, 0.5=center, 1=right)
     */
    public Dialog_Prompt(Prompt_Value new_val, String title, int width, Font custom_font, float custom_align)
    {
        super(custom_font,custom_align);
        value = new_val;
        initUI(title,width);
    }


    // --------------------------------------
    //          Data access & UI
    // --------------------------------------
    /** Get the prompt text input value 
     * @return a Prompt_Value representing the value entered
    */
    public Prompt_Value get_value()
    {
        return value;
    }

    /** Return true if user selected OK 
     * @return true if accepted, false if user canceled or closed window
    */
    public boolean get_accepted()
    {
        return accepted;
    }


    /** Handle closing actions for the dialog */
    private void close()
    {
        value.set_value_str( input.getText() );
        // check to see if the value is valid:
        if( value.is_valid() )
        {
            // if so, then set accepted and get rid of window:
            accepted = true;
            dispose();
        }
        else
        {
            // otherwise, throw an error to the user:
            Dialog_Error emsg = new Dialog_Error(" Invalid value, try again. ");
        }
    }

    /** Initialize the UI
     * @param title the dialog window title
     * @param width the width (in characters) of the input text box
     */
    private final void initUI(String title, int width) {
        // boolean to store if user actually accepted (vs cancel)
        accepted = false;

        // Layout is a vertical box layout for the Content Pane:
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Add a text label containing information on the program
        // date:
        add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel text = new JLabel(value.get_description());
        text.setFont(new Font("Serif", Font.BOLD, 13));
        text.setAlignmentX(0.5f);
        add(text);

        //Add a text box for input
        add(Box.createRigidArea(new Dimension(width+2,10)));
        input = new JTextField(width);
        input.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
                if( event.getKeyCode() == KeyEvent.VK_ENTER )
                {
                    close();
                }
            }
        });
        add(input);

        // Add a button to close this dialog box
        add(Box.createRigidArea(new Dimension(0, 30)));
        JButton ok = new JButton("OK");
        ok.setMnemonic(KeyEvent.VK_X);
        ok.setDefaultCapable(true);
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                close();
            }
        });
        ok.setAlignmentX(0.5f);
        add(ok);

        // set info about the window:
        setTitle(title);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        input.requestFocusInWindow();
        setVisible(true);
    }
}