/**
 * 
 */
package jraidownloader.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import jraidownloader.logging.JRaiLogger;

/**
 * Utility per i file.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class FileUtils {
	
	/**
	 * Controlla se un file esiste.
	 * @param filePath il path del file.
	 * @return Ritorna true se il file esiste e false in caso contrario.
	 */
	public static boolean fileExists(String filePath){
		File f = new File(filePath);
		return f.exists();
	}
	
	/**
	 * Crea un nuovo file.
	 * @param filePath il path del file.
	 */
	public static void createFile(String filePath){
		File f = new File(filePath);
		try {
			f.createNewFile();
		} catch (IOException e) {
			JRaiLogger.getLogger().log(Level.INFO, "Creazione del file di settings non avvenuta con successo");
		}
	}
	
	/**
	 * Crea un percorso di cartelle.
	 * @param filePath il path da creare.
	 */
	public static void createPath(String filePath){
		File f = new File(filePath);
		f.getParentFile().mkdirs();
	}
	
	/**
	 * Crea una directory.
	 * @param directoryName il nome della cartella da creare.
	 */
	public static void createDirectory(String directoryName){
		File dir = new File(directoryName);
		dir.mkdir();
	}

}
