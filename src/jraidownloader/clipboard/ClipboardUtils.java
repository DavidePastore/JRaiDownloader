/**
 * 
 */
package jraidownloader.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;

import jraidownloader.logging.JRaiLogger;

/**
 * Classe di utilità per le funzionalità della clipboard.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class ClipboardUtils {
	
	/**
	 * Legge il contenuto della clipboard.
	 * @return il contenuto della clipboard letto.
	 */
	public static String getContent() {
		String result = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		//odd: the Object param of getContents is not currently used
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText =
				(contents != null) &&
				contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				result = (String)contents.getTransferData(DataFlavor.stringFlavor);
			}
			catch (UnsupportedFlavorException ex){
				//highly unlikely since we are using a standard DataFlavor
				JRaiLogger.getLogger().log(Level.SEVERE, "UnsupportedFlavorException: " + ex);
			}
			catch (IOException ex) {
				JRaiLogger.getLogger().log(Level.SEVERE, "IOException: " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Setta il contenuto della clipboard.
	 * @param content la stringa da scrivere nella clipboard.
	 */
	public static void setContent(String content){
		StringSelection stringSelection = new StringSelection(content);
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents(stringSelection, new TextTransfer());
	}

}
