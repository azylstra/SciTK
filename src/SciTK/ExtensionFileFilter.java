package SciTK;

import java.io.File;
import javax.swing.filechooser.*;

/** Implement a class for file loading
 * Selection is based on file extensions
 *
 * @package SciTK
 * @class ExtensionFileFilter
 * @brief implement file selection by extension filtering
 * @author Alex Zylstra
 * @date 2013/04/09
 * @copyright Alex Zylstra
 * @license SciTK / MIT License
 */
public class ExtensionFileFilter extends FileFilter {
  String description; /* text description of the filter */
  String extensions[]; /* extensions allowed */

  /** Construct a filter with one accepted extension
   * @param description a text description of the files allowed
   * @param extension the file extension to allow
   */
  public ExtensionFileFilter(String description, String extension) {
    this(description, new String[] { extension });
  }

  /** Construct a filter with many accepted extensions
   * @param description a text description of the files allowed
   * @param extensions an array of file extensions to allow
  */
  public ExtensionFileFilter(String description, String extensions[]) {
    if (description == null) {
      this.description = extensions[0];
    } else {
      this.description = description;
    }
    this.extensions = (String[]) extensions.clone();
    toLower(this.extensions);
  }

  /** Convert a string to lower case */
  private void toLower(String array[]) {
    for (int i = 0, n = array.length; i < n; i++) {
      array[i] = array[i].toLowerCase();
    }
  }

  /** Get the description of this file filter */
  public String getDescription() {
    return description;
  }

  /** Check to see if a file is accepted
   * @param file the file to check
   * @return true if the filter allows file
  */
  public boolean accept(File file) {
    if (file.isDirectory()) {
      return true;
    } else {
      String path = file.getAbsolutePath().toLowerCase();
      for (int i = 0, n = extensions.length; i < n; i++) {
        String extension = extensions[i];
        if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
          return true;
        }
      }
    }
    return false;
  }
}