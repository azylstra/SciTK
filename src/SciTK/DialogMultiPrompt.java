package SciTK;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridLayout;

import java.awt.Font;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.List;
import java.util.ArrayList;

/** Implement a GUI dialog box which prompts the user
 * for an arbirary number of parameters, with somewhat
 * different layout than the Dialog_Prompt class for better UI.
 *
 * @package SciTK
 * @class DialogMultiPrompt
 * @brief A GUI dialog box to prompt for multiple values
 * @author Alex Zylstra
 * @date 2013/05/19
 * @copyright Alex Zylstra
 * @license SciTK / MIT License
 */
public class DialogMultiPrompt extends SciTK_Dialog
{
    private List<PromptValue> values; /* The values being prompted for */
    private boolean accepted; /* If the user 'accepted' the input [ie clicked OK] */
    private List<JLabel> labels; /* Text label descriptions for each value */
    private List<JTextField> inputs; /* The text input fields */

    // --------------------------------------
    //          Constructors
    // --------------------------------------
    /** Constructor
     * @param new_val the PromptValue representation of the values to get from user, as Java array
     * @param title the title of the prompt window
     */
    public DialogMultiPrompt(PromptValue[] new_val, String title)
    {
        // have to manually copy:
        values = new ArrayList<PromptValue>(new_val.length);
        for(int i=0; i<new_val.length; i++)
            values.add( new_val[i] );

        initUI(title,10);
    }

    /** Constructor
     * @param new_val the PromptValue representation of the values to get from user, as Java array
     * @param title the title of the prompt window
     * @param width the width in characters of text input box
     */
    public DialogMultiPrompt(PromptValue[] new_val, String title, int width)
    {
        // have to manually copy:
        values = new ArrayList<PromptValue>(new_val.length);
        for(int i=0; i<new_val.length; i++)
            values.add( new_val[i] );

        initUI(title,width);
    }

    /** Constructor
     * @param new_val the PromptValue representation of the values to get from user, as Java List
     * @param title the title of the prompt window
     */
    public DialogMultiPrompt(List<PromptValue> new_val, String title)
    {
        values = new_val;
        initUI(title,10);
    }

    /** Constructor
     * @param new_val the PromptValue representation of the values to get from user, as Java List
     * @param title the title of the prompt window
     * @param width the width in characters of text input box
     */
    public DialogMultiPrompt(List<PromptValue> new_val, String title, int width)
    {
        values = new_val;
        initUI(title,width);
    }

    /** Constructor
     * @param new_val the PromptValue representation of the values to get from user, as Java array
     * @param title the title of the prompt window
     * @param custom_font The Font to use when displaying text in the Dialog
     */
    public DialogMultiPrompt(PromptValue[] new_val, String title, Font custom_font)
    {
        super(custom_font);

        // have to manually copy:
        values = new ArrayList<PromptValue>(new_val.length);
        for(int i=0; i<new_val.length; i++)
            values.add( new_val[i] );

        initUI(title,10);
    }

    /** Constructor
     * @param new_val the PromptValue representation of the values to get from user, as Java array
     * @param title the title of the prompt window
     * @param width the width in characters of text input box
     * @param custom_font The Font to use when displaying text in the Dialog
     */
    public DialogMultiPrompt(PromptValue[] new_val, String title, int width, Font custom_font)
    {
        super(custom_font);

        // have to manually copy:
        values = new ArrayList<PromptValue>(new_val.length);
        for(int i=0; i<new_val.length; i++)
            values.add( new_val[i] );

        initUI(title,width);
    }

    /** Constructor
     * @param new_val the PromptValue representation of the values to get from user, as Java List
     * @param title the title of the prompt window
     * @param custom_font The Font to use when displaying text in the Dialog
     */
    public DialogMultiPrompt(List<PromptValue> new_val, String title, Font custom_font)
    {
        super(custom_font);

        values = new_val;
        initUI(title,10);
    }

    /** Constructor
     * @param new_val the PromptValue representation of the values to get from user, as Java List
     * @param title the title of the prompt window
     * @param width the width in characters of text input box
     * @param custom_font The Font to use when displaying text in the Dialog
     */
    public DialogMultiPrompt(List<PromptValue> new_val, String title, int width, Font custom_font)
    {
        super(custom_font);

        values = new_val;
        initUI(title,width);
    }


    // --------------------------------------
    //          Data access & UI
    // --------------------------------------
    /** Get the prompt text input value 
     * @return an array of PromptValues representing the values entered
    */
    public PromptValue[] getValuesArray()
    {
        return (PromptValue[])values.toArray();
    }

    /** Get the prompt text input value 
     * @return an array of PromptValues representing the values entered
    */
    public List<PromptValue> getValuesList()
    {
        return values;
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
        // keep track of if all values are OK:
        boolean overall_valid = true;

        // iterate over all values:
        for(int i=0; i < values.size(); i++)
        {
            values.get(i).setValueString( inputs.get(i).getText() );
            // check to see if the value is valid:
            overall_valid = overall_valid && ( values.get(i).isValid() );
        }

        // Check if all values are OK:
        if( overall_valid )
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
        setLayout(new GridLayout(values.size()+1, 2));

        // initialize the arrays for labels and inputs:
        labels = new ArrayList<JLabel>();
        inputs = new ArrayList<JTextField>();

        // iterate over all values:
        for(int i=0; i<values.size(); i++)
        {

            // Add a text label containing information on the value
            labels.add( new JLabel(values.get(i).getDescription(),JLabel.CENTER));
            labels.get(i).setFont(my_font);
            add(labels.get(i));

            //Add a text box for input
            inputs.add( new JTextField(width) );
            inputs.get(i).addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent event) {
                    if( event.getKeyCode() == KeyEvent.VK_ENTER )
                    {
                        close();
                    }
                }
            });
            add(inputs.get(i));
        }

        // Add a button to close this dialog box
        //add(Box.createRigidArea(new Dimension(0, 30)));
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
        ok.setAlignmentX(alignment);
        add(ok);

        // set info about the window:
        setTitle(title);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        inputs.get(0).requestFocusInWindow();
        setVisible(true);
    }
}