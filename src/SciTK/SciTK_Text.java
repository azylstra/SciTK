package SciTK;

/** Some very simple functionality for reading/writing text files
 *
 * @package SciTK
 * @class SciTK_Text
 * @brief Define text constants for the toolkit
 * @author Alex Zylstra
 * @date 2013/05/19
 * @copyright Alex Zylstra
 * @license SciTK / MIT License
*/
public class SciTK_Text
{
    /** SciTK toolkit version */
	public static String TOOLKIT_VERSION = "0.1.0";
    /** SciTK toolkit author */
	public static String TOOLKIT_AUTHOR = "Alex Zylstra";
    /** SciTK toolkit date */
	public static String TOOLKIT_DATE = "May 19, 2013";
    /** Newline separator to be used by the toolkit */
	public static String TOOLKIT_NEWLINE = System.getProperty("line.separator");
    /** Delimiting character to be used for output */
	public static String TOOLKIT_CSV_DELIM = ",";
}