/** Add some functionality to JFrame, to be extended by other dialogs
 *
 * @package SciTK
 * @class SciTK_Frame
 * @brief Add some extended functionality to JFrame
 * @author Alex Zylstra
 * @date 2013/04/10
 * @copyright Alex Zylstra
 * @license MIT
 */

package SciTK;

import javax.swing.JFrame;
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


public class SciTK_Frame extends JFrame
{
    /** Constructor
     * 
     */
    public SciTK_Frame() {
        super();

        // action listener for closing window:
        Action closeActionListener = new AbstractAction() {
        public void actionPerformed(ActionEvent actionEvent) {
                dispose();
            }
        };
        // keystroke to trigger close action:
        KeyStroke closeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_W,Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        // add to frame root pane:
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(closeKeyStroke, "CLOSE");
        this.getRootPane().getActionMap().put("CLOSE", closeActionListener);

        setFocusable(true);
    }
}