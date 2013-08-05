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
import java.util.logging.Level;

import javax.swing.JProgressBar;

import jraidownloader.httpclient.JRDHttpClient;
import jraidownloader.logging.JRaiLogger;
import jraidownloader.utils.ByteUtils;

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
	
	private String basicPath = "C:\\Documents and Settings\\giuseppe\\Desktop\\Davide\\";

	/**
	 * Scarica un file.
	 * @param url l'url nel quale è presente il file.
	 * @param fileName il nome del file in cui salvare il video.
	 * @param progressBar la progress bar da aggiornare con il nuovo valore
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public void downloadFile(String url, String fileName, JProgressBar progressBar) throws ClientProtocolException, IOException{
		fileName = this.clearFileName(fileName);
		HttpClient httpClient = JRDHttpClient.get();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity entity = httpResponse.getEntity();
		if (entity != null) {
			long length = entity.getContentLength();
			progressBar.setMaximum((int) (ByteUtils.fromBytesToMegaBytes(entity.getContentLength())));
			long currentByte = 0;
			float percentuale = 0;
			InputStream instream = entity.getContent();
			BufferedInputStream bis = new BufferedInputStream(instream);
			String filePath = basicPath + fileName;
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
			int inByte;
	        while ((inByte = bis.read()) != -1 ) {
	        	currentByte++;
	        	progressBar.setValue((int) ((ByteUtils.fromBytesToMegaBytes(currentByte))));
	        	progressBar.setStringPainted(true);
	        	progressBar.setString((int) (currentByte/1024/1024) + " MB/" + (int) (entity.getContentLength()/1024/1024) + " MB");
	        	if(percentuale != 100 * currentByte / length){
	        		percentuale = 100 * currentByte / length;
	        		JRaiLogger.getLogger().log(Level.FINE, percentuale + "% -> " + currentByte + "/" + length);
	        	}
	        	bos.write(inByte);
	        }
	        bis.close();
	        bos.close();
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
