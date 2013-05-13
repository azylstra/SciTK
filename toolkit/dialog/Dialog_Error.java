/** Implement a GUI error dialog box
 *
 * @package SciTK
 * @class Dialog_Error
 * @brief Dialog-based error message
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
import java.awt.event.KeyAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.Font;

public class Dialog_Error extends SciTK_Dialog
{
    /** Constructor
     * @param message the message to display
     */
    public Dialog_Error(String message) 
    {
        initUI(message);
    }

    /** Constructor with style options
     * @param message the message to display
     * @param custom_font The Font to use when displaying text in the Dialog
     * @param custom_align The alignment to use for all UI elements (0=left, 0.5=center, 1=right)
     */
    public Dialog_Error(String message, Font custom_font, float custom_align)
    {
        super(custom_font,custom_align);
        initUI(message);
    }

    /** Initialize the UI
     * @param message the message to display
     */
    private final void initUI(String message)
    {
        // Layout is a vertical box layout for the Content Pane:
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Add a text label containing information on the program
        // date:
        add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel text = new JLabel(message);
        text.setFont(my_font);
        text.setAlignmentX(alignment);
        add(text);

        // Add a button to close this dialog box
        add(Box.createRigidArea(new Dimension(0, 30)));
        JButton close = new JButton("Close");
        close.setMnemonic(KeyEvent.VK_X);
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });
        close.setAlignmentX(alignment);
        close.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
                if( event.getKeyCode() == KeyEvent.VK_ENTER )
                {
                    dispose();
                }
            }
        });
        add(close);

        
        // prevent input to other windows while active:
        setModalityType(ModalityType.APPLICATION_MODAL);

        // Title of the error window:
        setTitle("Error!");

        // some random JFrame stuff:
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        close.requestFocusInWindow();
        setVisible(true);
    }
}