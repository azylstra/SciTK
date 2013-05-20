package SciTK;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.Box;

import java.awt.Dimension;
import java.awt.Font;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/** Implement a GUI dialog box
 * to get a value from the user. A variety of constructors
 * are implemented. The default ones (i.e. that don't take a PromptValue)
 * will result in this Prompt being for a String without any checks.
 * If you want to prompt for a numeric value (with or without limits),
 * then construct a PromptValue and pass to the appropriate
 * constructors of this class.
 *
 * @package SciTK
 * @class DialogPrompt
 * @brief A GUI dialog box to prompt for a single numeric value
 * @author Alex Zylstra
 * @date 2013/05/19
 * @copyright Alex Zylstra
 * @license SciTK / MIT License
 */
public class DialogPrompt extends SciTK_Dialog
{
    private PromptValue value; /* The value being prompted for */
    private boolean accepted; /* If the user 'accepted' the input [ie clicked OK] */
    private JTextField input; /* The text input field */

    // --------------------------------------
    //          Constructors
    // --------------------------------------
    /** Constructor
     * @param title the title of the prompt window
     */
    public DialogPrompt(String title)
    {
        value = new PromptValueString("","");
        initUI(title,10);
    }

    /** Constructor
     * @param title the title of the prompt window
     * @param width the width in characters of text input box
     */
    public DialogPrompt(String title, int width)
    {
        value = new PromptValueString("","");
        initUI(title,width);
    }

    /** Constructor
     * @param new_val the PromptValue representation of the value to get from user
     * @param title the title of the prompt window
     */
    public DialogPrompt(PromptValue new_val, String title)
    {
        value = new_val;
        initUI(title,10);
    }

    /** Constructor
     * @param new_val the PromptValue representation of the value to get from user
     * @param title the title of the prompt window
     * @param width the width in characters of text input box
     */
    public DialogPrompt(PromptValue new_val, String title, int width)
    {
        value = new_val;
        initUI(title,width);
    }

    /** Constructor
     * @param title the title of the prompt window
     * @param custom_font The Font to use when displaying text in the Dialog
     * @param custom_align The alignment to use for all UI elements (0=left, 0.5=center, 1=right)
     */
    public DialogPrompt(String title, Font custom_font, float custom_align)
    {
        super(custom_font,custom_align);
        value = new PromptValueString("","");
        initUI(title,10);
    }

    /** Constructor
     * @param title the title of the prompt window
     * @param width the width in characters of text input box
     * @param custom_font The Font to use when displaying text in the Dialog
     * @param custom_align The alignment to use for all UI elements (0=left, 0.5=center, 1=right)
     */
    public DialogPrompt(String title, int width, Font custom_font, float custom_align)
    {
        super(custom_font,custom_align);
        value = new PromptValueString("","");
        initUI(title,width);
    }

    /** Constructor
     * @param new_val the PromptValue representation of the value to get from user
     * @param title the title of the prompt window
     * @param custom_font The Font to use when displaying text in the Dialog
     * @param custom_align The alignment to use for all UI elements (0=left, 0.5=center, 1=right)
     */
    public DialogPrompt(PromptValue new_val, String title, Font custom_font, float custom_align)
    {
        super(custom_font,custom_align);
        value = new_val;
        initUI(title,10);
    }

    /** Constructor
     * @param new_val the PromptValue representation of the value to get from user
     * @param title the title of the prompt window
     * @param width the width in characters of text input box
     * @param custom_font The Font to use when displaying text in the Dialog
     * @param custom_align The alignment to use for all UI elements (0=left, 0.5=center, 1=right)
     */
    public DialogPrompt(PromptValue new_val, String title, int width, Font custom_font, float custom_align)
    {
        super(custom_font,custom_align);
        value = new_val;
        initUI(title,width);
    }


    // --------------------------------------
    //          Data access & UI
    // --------------------------------------
    /** Get the prompt text input value 
     * @return a PromptValue representing the value entered
    */
    public PromptValue getValue()
    {
        return value;
    }

    /** Return true if user selected OK 
     * @return true if accepted, false if user canceled or closed window
    */
    public boolean getAccepted()
    {
        return accepted;
    }


    /** Handle closing actions for the dialog */
    private void close()
    {
        value.setValueString( input.getText() );
        // check to see if the value is valid:
        if( value.isValid() )
        {
            // if so, then set accepted and get rid of window:
            accepted = true;
            dispose();
        }
        else
        {
            // otherwise, throw an error to the user:
            DialogError emsg = new DialogError(this," Invalid value, try again. ");
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
        JLabel text = new JLabel(value.getDescription());
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
        ok.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
                if( event.getKeyCode() == KeyEvent.VK_ENTER
                    || event.getKeyCode() == KeyEvent.VK_SPACE )
                {
                    close();
                }
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