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
 * Gestisce la scrittura e la lettura delle proprietÓ.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class PropertiesManager {
	
	/**
	 * La chiave per la proprietÓ del percorso in cui salvare i file.
	 */
	public static final String SAVE_PATH_KEY = "savePath";

	/**
	 * La chiave per l'impostazione di default per la sovrascrittura dei file.
	 */
	public static final String OVERWRITE_FILES = "overwriteFiles";
	
	/**
	 * Il path del file in cui salvare le proprietÓ.
	 */
	private static String filePath = "settings.xml";
	
	
	private static Properties saveProps = new Properties();
	
	/**
	 * Salva la proprietÓ nel file.
	 * @param key la chiave della proprietÓ.
	 * @param value il valore della proprietÓ.
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
	 * Legge la proprietÓ dal file.
	 * @param key la chiave della proprietÓ.
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
			saveProps.setProperty(OVERWRITE_FILES, Boolean.toString(false));
			storeToXML();
		}
	}
	

}
