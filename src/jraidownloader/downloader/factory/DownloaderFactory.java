/**
 * 
 */
package jraidownloader.downloader.factory;

import jraidownloader.downloader.RaiReplay;
import jraidownloader.downloader.abstractinterface.Downloader;

/**
 * Downloader factory.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class DownloaderFactory {
	
	/**
	 * Create a new downloader.
	 * @param name the name of the downloader.
	 * @param url the url to download.
	 * @return the new Downloader object.
	 */
	public static Downloader createDownloader(String name, String url){
		if(name.equalsIgnoreCase("RaiReplay")){
			return new RaiReplay(url);
		}
		return null;
	}

}
