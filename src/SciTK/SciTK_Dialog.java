package SciTK;

import javax.swing.JDialog;
import javax.swing.JComponent;

// for keyboard shortcuts
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

import java.awt.event.InputEvent;
import javax.swing.ActionMap;
import javax.swing.Action;
import javax.swing.AbstractAction;
import java.awt.Toolkit;

import java.awt.Font;

/** Add some functionality to JDialog, to be extended by other dialogs within this toolkit
 *
 * @package SciTK
 * @class SciTK_Dialog
 * @brief Add some functionality to JDialog, to be extended by other dialogs
 * @author Alex Zylstra
 * @date 2013/04/10
 * @copyright Alex Zylstra
 * @license SciTK / MIT License
 */
public class SciTK_Dialog extends JDialog
{
    protected Font my_font; /* Font to use for displaying text */
    protected float alignment; /* Alignment factor for displaying UI elements */

    // Some defaults for the font:
    public static String DEFAULT_FONT = "SansSerif";
    public static int DEFAULT_FONT_STYLE = Font.PLAIN;
    public static int DEFAULT_FONT_SIZE = 13;

    // Default alignment:
    public static float DEFAULT_ALIGNMENT = 0.5f;

    /** Constructor */
    public SciTK_Dialog()
    {
        // call more complex constructor with default Font and alignment:
        this(new Font(DEFAULT_FONT,DEFAULT_FONT_STYLE,DEFAULT_FONT_SIZE), DEFAULT_ALIGNMENT);   
    }

    /* Constructor with custom font
    * @param custom_font The Font to use when displaying text in the Dialog
    */
    public SciTK_Dialog(Font custom_font)
    {
        // use user-supplied Font, but default alignment:
        this(custom_font, DEFAULT_ALIGNMENT);
    }

    /* Constructor with custom alignment
    * @param custom_align The alignment to use for all UI elements (0=left, 0.5=center, 1=right)
    */
    public SciTK_Dialog(float custom_align)
    {
        // use default Font, but user-supplied alignment:
        this(new Font(DEFAULT_FONT,DEFAULT_FONT_STYLE,DEFAULT_FONT_SIZE), custom_align);
    }

    /* Constructor with both custom font and alignment
    * @param custom_font The Font to use when displaying text in the Dialog
    * @param custom_align The alignment to use for all UI elements (0=left, 0.5=center, 1=right)
    */
    public SciTK_Dialog(Font custom_font, float custom_align)
    {
        super();

        // action listener for closing window:
        Action closeActionListener = new AbstractAction()
        {
            public void actionPerformed(ActionEvent actionEvent)
            {
                dispose();
            }
        };
        // keystroke to trigger close action:
        KeyStroke closeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_W,Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        // add to frame root pane:
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(closeKeyStroke, "CLOSE");
        this.getRootPane().getActionMap().put("CLOSE", closeActionListener);

        // let this dialog have focus:
        setFocusable(true);

        // set font:
        my_font = custom_font;

        // set default alignment:
        alignment = custom_align;
    }
}