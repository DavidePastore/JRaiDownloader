/**
 * 
 */
package jraidownloader.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import jraidownloader.downloader.DownloadStatus;

/**
 * Custom JTable for rendering of colours.
 * @author <a href="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class JRDTable extends JTable{
	
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
		Component c = super.prepareRenderer(renderer, row, column);

		//  Color row based on a cell value

		if (!isRowSelected(row))
		{
			c.setBackground(getBackground());
			int modelRow = convertRowIndexToModel(row);
			DownloadStatus downloadStatus = (DownloadStatus) getModel().getValueAt(modelRow, JRDTableModel.STATO);
			if(downloadStatus == DownloadStatus.DOWNLOADFINISHED){
				c.setBackground(Color.GREEN);
			}
			JRDTableModel tableModel = (JRDTableModel) getModel();
			tableModel.fireTableDataChanged();
		}

		return c;
	}

}
