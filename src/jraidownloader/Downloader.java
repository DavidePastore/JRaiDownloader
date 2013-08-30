/**
 * 
 */
package jraidownloader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import jraidownloader.httpclient.JRDHttpClient;
import jraidownloader.logging.JRaiLogger;
import jraidownloader.properties.PropertiesManager;
import jraidownloader.table.JRDTableModel;
import jraidownloader.utils.ByteUtils;
import jraidownloader.utils.ChangeableString;
import jraidownloader.utils.FileUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

/**
 * Classe per il download di un file.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class Downloader {

	/**
	 * Scarica un file.
	 * @param url l'url nel quale è presente il file.
	 * @param fileName il nome del file in cui salvare il video.
	 * @param dateDir il path in cui salvare il video (il nome della cartella sarà la data).
	 * @param downloadProgress the download progress.
	 * @param downloadSpeed the download speed.
	 * @throws Exception 
	 */
	public void downloadFile(String url, String fileName, String dateDir, AtomicInteger downloadProgress, ChangeableString downloadSpeed) throws Exception{
		fileName = this.clearFileName(fileName);
		HttpClient httpClient = JRDHttpClient.get();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity entity = httpResponse.getEntity();
		JRDTableModel tableModel = (JRDTableModel) JRaiFrame.getInstance().getTable().getModel();
		if (entity != null) {
			long length = entity.getContentLength();
			long currentByte = 0;
			float percentuale = 0;
			long tempoIniziale = System.currentTimeMillis()/1000;
			long speed = 0;
			//JRaiFrame jRaiFrame = (JRaiFrame) progressBar.getParent().getParent().getParent().getParent();
			//downloadProgress.setMaximum((int) (ByteUtils.fromBytesToMegaBytes(length)));
			
			InputStream instream = entity.getContent();
			BufferedInputStream bis = new BufferedInputStream(instream);
			
			//Legge il path del file impostato
			String filePath;
			filePath = PropertiesManager.getProperty(PropertiesManager.SAVE_PATH_KEY);
			
			
			if(!FileUtils.fileExists(filePath)){
				FileUtils.createDirectory(filePath);
			}
			
			if(!FileUtils.fileExists(filePath + File.separator + dateDir)){
				FileUtils.createDirectory(filePath + File.separator + dateDir);
			}
			
			filePath += File.separator + dateDir + File.separator + fileName;
			JRaiLogger.getLogger().log(Level.INFO, "filepath: " + filePath);
			
			//Se il file esiste già controlla se lo si vuole sovrascrivere
			if(FileUtils.fileExists(filePath)){
				
				//Se l'opzione relativa alla sovrascrittura è true si procede, altrimenti si chiede all'utente la conferma
				if(Boolean.parseBoolean(PropertiesManager.getProperty(PropertiesManager.OVERWRITE_FILES))){
					JRaiLogger.getLogger().log(Level.INFO, "Overwriting file.");
				}
				else{
					int risposta = JOptionPane.showConfirmDialog(null, "<html>File <b>\'" + fileName + "\'</b> già esistente. Desideri sovrascriverlo?</html>", "File già esistente", JOptionPane.YES_NO_OPTION);
					if(risposta != JOptionPane.YES_OPTION){
						httpGet.releaseConnection();
						throw new Exception("Il file non può essere sovrascritto.");
					}
				}
			}

			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
			int inByte;
	        while ((inByte = bis.read()) != -1 ) {
	        	currentByte++;
	        	//progressBar.setValue((int) ((ByteUtils.fromBytesToMegaBytes(currentByte))));
	        	//progressBar.setStringPainted(true);
	        	//progressBar.setString((int) (ByteUtils.fromBytesToMegaBytes(currentByte)) + " MB/" + (int) (ByteUtils.fromBytesToMegaBytes(length)) + " MB");
	        	
	        	if(System.currentTimeMillis()/1000 != tempoIniziale){
	        		//jRaiFrame.setSpeed(ByteUtils.fromBytesToKiloBytes(speed) + " KB/s");
	        		downloadSpeed.changeTo(ByteUtils.fromBytesToKiloBytes(speed) + " KB/s");
	        		
	        		//Updates the table model
	        		tableModel.fireTableDataChanged();
	        		speed = 0;
	        		tempoIniziale = System.currentTimeMillis()/1000;
	        	}
	        	else{
	        		speed++;
	        	}
	        	
	        	if(percentuale != 100 * currentByte / length){
	        		percentuale = 100 * currentByte / length;
	        		downloadProgress.set((int) percentuale);
	        		
	        		//Updates the table model
	        		tableModel.fireTableDataChanged();
	        		JRaiLogger.getLogger().log(Level.FINE, percentuale + "% -> " + currentByte + "/" + length);
	        	}
	        	bos.write(inByte);
	        }
	        JRaiLogger.getLogger().log(Level.FINE, "Download finito di " + fileName);
	        downloadSpeed.changeTo("");
	        bis.close();
	        bos.close();
	        //jRaiFrame.setSpeed("");
		}
	}
	
	/**
	 * Pulisce il nome del file.
	 * @param fileName il nome del file da pulire
	 * @return il nome del file ripulito.
	 */
	private String clearFileName(String fileName){
		return fileName.replace(":", " ");
	}
}
