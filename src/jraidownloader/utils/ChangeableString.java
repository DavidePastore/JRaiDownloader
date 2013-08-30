/**
 * 
 */
package jraidownloader.utils;

/**
 * A changeable string class.
 * @author <a href="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class ChangeableString {
	
	String str;

	/**
	 * Constructor of the object.
	 * @param str initial string value.
	 */
    public ChangeableString(String str) {
        this.str = str;
    }

    /**
     * Change the value of the string to a new value.
     * @param newStr the new value.
     */
    public void changeTo(String newStr) {
        str = newStr;
    }

    public String toString() {
        return str;
    }
}
