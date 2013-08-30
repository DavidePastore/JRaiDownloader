/**
 * 
 */
package jraidownloader.downloader.abstractinterface;

import jraidownloader.downloader.DownloadStatus;
import jraidownloader.video.Videos;

/**
 * Downloader interface.
 * @author <a href="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public interface Downloader extends Runnable {
	
	/**
	 * Check the pattern.
	 * @return is the pattern found?
	 */
	public boolean checkPattern();
	
	/**
	 * Return the download speed.
	 * @return the download speed.
	 */
	public String getDownloadSpeed();
	
	/**
	 * Return the file name.
	 * @return the file name.
	 */
	public String getFileName();
	
	/**
	 * Return the file size.
	 * @return the file size
	 */
	public String getFileSize();
	
	/**
	 * Return the progress.
	 * @return the progress.
	 */
	public int getProgress();
	
	/**
	 * Return the regular expression.
	 * @return the regular expression.
	 */
	public String[] getRegularExpression();
	
	/**
	 * Return the status.
	 * @return the status.
	 */
	public DownloadStatus getStatus();
	
	/**
	 * Return the videos.
	 * @return the videos.
	 */
	public Videos getVideos();
	
	/**
	 * Start the download.
	 */
	public void startDownload();
	
	

}
