/**
 * 
 */
package jraidownloader;

import java.io.IOException;
import java.util.logging.Level;

import jraidownloader.logging.JRaiLogger;
import jraidownloader.properties.PropertiesManager;
import jraidownloader.utils.FileUtils;

import org.apache.http.client.ClientProtocolException;

/**
 * Classe principale. L'applicazione inizia qui.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class JRaiDownloader {
	
	/**
	 * La versione del programma.
	 */
	private static final String VERSION = "0.1.0";
	
	
	/**
	 * L'applicazione viene avviata qui.
	 * @param args
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static void main(String[] args) throws ClientProtocolException, IOException {
		JRaiLogger.getLogger().setLevel(Level.INFO);
		
		//Setta i valori di default delle proprietà
		PropertiesManager.setDefaultValues();
		
		JRaiFrame jRaiFrame = new JRaiFrame("JRaiDownloader " + VERSION);
		jRaiFrame.setVisible(true);
	}

}
