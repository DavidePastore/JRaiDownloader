/**
 * 
 */
package jraidownloader.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.logging.Level;

import jraidownloader.logging.JRaiLogger;
import jraidownloader.utils.FileUtils;

/**
 * Gestisce la scrittura e la lettura delle propriet�.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class PropertiesManager {
	
	/**
	 * La chiave per la propriet� del path.
	 */
	public static final String SAVE_PATH_KEY = "savePath";
	
	/**
	 * Il path del file in cui salvare le propriet�
	 */
	private static String filePath = "settings.xml";
	
	
	private static Properties saveProps = new Properties();
	
	/**
	 * Salva la propriet� nel file.
	 * @param key la chiave della propriet�.
	 * @param value il valore della propriet�.
	 */
	public static void setProperty(String key, String value){
		saveProps.setProperty(key, value);
	}
	
	/**
	 * Salva il tutto all'interno del file XML.
	 */
	public static void storeToXML(){
		try {
			saveProps.storeToXML(new FileOutputStream(filePath), "");
		} catch (FileNotFoundException e) {
			JRaiLogger.getLogger().log(Level.INFO, "Informazione non salvata correttamente.");
		} catch (IOException e) {
			JRaiLogger.getLogger().log(Level.INFO, "Informazione non salvata correttamente.");
		}
	}
	
	/**
	 * Legge la propriet� dal file.
	 * @param key la chiave della propriet�.
	 */
	public static String getProperty(String key){
		try {
			saveProps.loadFromXML(new FileInputStream(filePath));
		} catch (InvalidPropertiesFormatException e) {
			JRaiLogger.getLogger().log(Level.INFO, "Formato file errato.");
		} catch (FileNotFoundException e) {
			JRaiLogger.getLogger().log(Level.INFO, "File non trovato.");
		} catch (IOException e) {
			JRaiLogger.getLogger().log(Level.INFO, "Errore generico durante il caricamento dal file.");
		}
		return saveProps.getProperty(key);
	}
	
	/**
	 * Crea il file con i valori di default.
	 */
	public static void setDefaultValues(){
		if(!FileUtils.fileExists(filePath)){
			FileUtils.createFile(filePath);
			saveProps.setProperty(SAVE_PATH_KEY, "downloads");
			storeToXML();
		}
	}
	

}