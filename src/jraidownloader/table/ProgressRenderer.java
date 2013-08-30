/**
 * 
 */
package jraidownloader.table;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
//This class renders a JProgressBar in a table cell.
public class ProgressRenderer extends JProgressBar
implements TableCellRenderer {

	// Constructor for ProgressRenderer.
	public ProgressRenderer(int min, int max) {
		super(min, max);
	}

	/* Returns this JProgressBar as the renderer
  		for the given table cell. */
	public Component getTableCellRendererComponent(
			JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		// Set JProgressBar's percent complete value.
		if(value!=null && value!=""){
			setValue(((Integer)value).intValue());
		}
	    return this;
	}
}
