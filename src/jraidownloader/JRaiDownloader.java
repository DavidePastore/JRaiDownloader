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
 * @author <a href="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class JRaiDownloader {
	
	/**
	 * La versione del programma.
	 */
	private static final String VERSION = "0.1.0";
	
	/**
	 * The main JFrame.
	 */
	private static JRaiFrame jRaiFrame;
	
	private static JRaiDownloader jRaiDownloader = new JRaiDownloader();
	
	/**
	 * Not instantiable.
	 */
	private JRaiDownloader(){
		
	}
	
	/**
	 * Get the instance
	 * @return The instance of JRaiDownloader.
	 */
	public static JRaiDownloader get(){
		return jRaiDownloader;
	}
	
	
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

		jRaiFrame = JRaiFrame.getInstance();
		jRaiFrame.setTitle("JRaiDownloader " + VERSION);
		jRaiFrame.setVisible(true);
	}


	/**
	 * @return the jRaiFrame
	 */
	public static JRaiFrame getjRaiFrame() {
		return jRaiFrame;
	}
	
	/**
	 * Return the version.
	 * @return the version.
	 */
	public static String getVersion(){
		return VERSION;
	}

}
