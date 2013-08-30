/**
 * 
 */
package jraidownloader.downloader.utils;

import jraidownloader.downloader.RaiReplay;

/**
 * Downloader Utils.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class DownloaderUtils {
	
	private static RaiReplay raiReplay;

	/**
	 * Get the downloader name by the url.
	 * @param url the url.
	 * @return Return the right downloader name.
	 */
	public static String getDownloaderName(String url){
		raiReplay = new RaiReplay(url);
		if(raiReplay.checkPattern()){
			return "RaiReplay";
		}
		return null;
	}
}
