/**
 * 
 */
package jraidownloader.video;

import jraidownloader.Downloader;
import jraidownloader.utils.ByteUtils;

/**
 * Classe rappresentante il video.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class Video {
	
	/**
	 * L'url in cui è presente il video.
	 */
	private String url;
	
	/**
	 * La qualità del video.
	 */
	private String qualita;
	
	/**
	 * La dimensione del video.
	 */
	private long length;
	
	
	/**
	 * Costruttore.
	 * @param url l'url in cui è presente il video.
	 * @param qualita la qualità del video.
	 * @param length la dimensione del video in byte.
	 */
	public Video(String url, String qualita, long length){
		this.url = url;
		this.qualita = qualita;
		this.length = length;
	}
	
	/**
	 * Rappresentazione in stringa dell'oggetto.
	 */
	public String toString(){
		return qualita + " - " + ByteUtils.fromBytesToMegaBytes(length) + " MB";
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the qualita
	 */
	public String getQualita() {
		return qualita;
	}

	/**
	 * @param qualita the qualita to set
	 */
	public void setQualita(String qualita) {
		this.qualita = qualita;
	}

	/**
	 * @return the length
	 */
	public long getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(long length) {
		this.length = length;
	}

}
