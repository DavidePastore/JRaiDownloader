/**
 * 
 */
package jraidownloader.table;

import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import jraidownloader.downloader.abstractinterface.Downloader;
import jraidownloader.logging.JRaiLogger;

/**
 * Custom table model.
 * @author <a href="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class JRDTableModel extends AbstractTableModel{
	
	//Singleton
    private static JRDTableModel INSTANCE = new JRDTableModel();
    
    //These are the names for the table's columns.
    private static final String[] columnNames = new String[]{
    	"Nome",
    	"Grandezza",
    	"Velocità",
    	"Progresso",
    	"Stato"
    };

    //These are the classes for each column's values.
    private static final Class[] columnClasses = {
    	String.class,
    	String.class,
    	String.class,
    	JProgressBar.class,
    	String.class,
    };

    //These int are used to access Column names without using explicit index
    public static final int NOME = 0;
    public static final int GRANDEZZA = 1;
    public static final int VELOCITA = 2;
    public static final int PROGRESSO = 3;
    public static final int STATO = 4;
    
    
    //The table's list of downloads.
    public static final ArrayList<Downloader> downloadList = new ArrayList<Downloader>();
    
    /**
     * Non instantiable. Use getInstance().
     */
    private JRDTableModel(){
    }
    
    /**
     * 
     * @return singleton instance of table model
     */
    public static JRDTableModel getInstance() {
        return INSTANCE;
    }
    

    /**
     * Adds a new upload to the table.
     */
    public void addDownload(Downloader downloader){
        downloadList.add(downloader);
        //Fire table row insertion notification to table.
        fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
        JRaiLogger.getLogger().log(Level.INFO, "{0}New download added", getClass().getName());
    }

    /**
     * Remove the selected row from table. 
     * Careful when removing as index of all rows change 
     * after removing a particular row
     * (if that row is not the last)
     */
    public void removeDownload(int selectedrow) {
    	downloadList.remove(selectedrow);
        //Fire table row insertion notification to table.
        fireTableRowsDeleted(selectedrow, selectedrow);
        JRaiLogger.getLogger().log(Level.INFO, "{0}: Row at {1} deleted", new Object[]{getClass().getName(), selectedrow});
    }
    
    /**
     * 
     * @return no of rows
     */
    @Override
    public int getRowCount() {
        return downloadList.size();
    }

    /**
     * 
     * @return no of columns
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Gets a column's name.
     */
    @Override
    public String getColumnName(int col){
        return columnNames[col];
    }

    /**
     * Gets a column's class.
     */
    @Override
    public Class getColumnClass(int col){
        return columnClasses[col];
    }

    /**
     * 
     * @param rowIndex
     * @param columnIndex
     * @return the value at the cell under particular row index and column index
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
    	Downloader downloader = downloadList.get(rowIndex);
    	try{
	    	switch(columnIndex){
		        case 0: //Filename
		            return downloader.getFileName();
		        case 1: //Size
		            return downloader.getFileSize();
		        case 2: //Speed
		            return downloader.getDownloadSpeed();
		        case 3: //Progress
		            return new Integer(downloader.getProgress());
		        case 4: //Status
		            return downloader.getStatus();
		    }
    	} catch(Exception ex){
    		//JRaiLogger.getLogger().log(Level.SEVERE, "{0}: {1}", new Object[]{getClass().getName(), ex});
    	}
    	return "";
    	/*
        try {
        Downloader download = downloadList.get(rowIndex);
        switch(columnIndex){
            case 0: //Filename
                return upload.getFileName();
            case 1: //Size
                return upload.getSize();
            case 2: //Host
                return upload.getHost();
            case 3: //Status
                return upload.getStatus();
            case 4: //Progress
                return new Integer(upload.getProgress());
            case 5: //DownloadURL
                return upload.getDownloadURL();
            case 6: //DeleteURL
                return upload.getDeleteURL();
        }
        
        } catch(Exception e) {
            ///Exception occurs when user removes some rows and progress bar requesting old index.. Must catch this otherwise runtime error
            //JRaiLogger.getLogger().log(Level.SEVERE, "{0}: {1}", new Object[]{getClass().getName(), e});
        }
        return "";
        */
        
    }

}
