/**
 * 
 */
package jraidownloader.queue;

import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.JProgressBar;
import jraidownloader.JRaiDownloader;
import jraidownloader.downloader.DownloadStatus;
import jraidownloader.downloader.abstractinterface.Downloader;
import jraidownloader.logging.JRaiLogger;
import jraidownloader.table.JRDTableModel;
import jraidownloader.video.Video;

/**
 * The Queue Manager that handles the downloads.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class QueueManager {
	
	/**
	 * Singleton.
	 */
	private static QueueManager queueManager = new QueueManager();
	
	private ArrayList<Video> queueVideo = new ArrayList<Video>();
	
	/**
	 * Number of current downloads
	 */
	private int currentlyDownloading;
	
	/**
	 * Max no. of simultaneous downloads. 2 by default.
	 */
	private int maxNoOfDownloads = 2;
	
	/**
	 * Boolean variable to check whether queue is locked or not.
	 */
    private boolean queueLock = false;
    
    /**
     * Boolean variable to check whether stopfurther is set or not.
     */
    private boolean stopfurther = true;
	
	/**
	 * Not instantiable.
	 */
	private QueueManager(){
	}
	
	/**
     * set the maximum number of simultaneous downloads
     *
     * @param maxNoOfDownloads
     */
    public void setMaxNoOfDownloads(int maxNoOfDownloads) {
        this.maxNoOfDownloads = maxNoOfDownloads;
    }

    /**
     * Get the max no of simultaneous downloads
     *
     * @return the max number of download
     */
    public int getMaxNoOfDownloads() {
        return maxNoOfDownloads;
    }
	
    /**
     * Get instance of Queue Manager.
     * @return the queue manager
     */
	public static QueueManager getInstance(){
		return queueManager;
	}
	
	 /**
    *
    * @return the number of uploads queued
    */
   public int getQueuedUploadCount() {
       //Initialize count as 0
       int count = 0;

       //Iterate through every row
       for (int i = 0; i < JRDTableModel.downloadList.size(); i++) {
           //if a row is queued, increment the count.
           if (JRaiDownloader.getjRaiFrame().getTable().getValueAt(i, JRDTableModel.STATO)
                   == DownloadStatus.QUEUED) {
               count++;
           }
       }

       //and return it.
       return count;
   }
	
	/**
	 * Add a video to the queue.
	 * @param downloader The video to add.
	 
	public void addToQueue(Downloader downloader){
		JRDTableModel model = JRaiDownloader.getjRaiFrame().getModel();
		JProgressBar jProgressBar = new JProgressBar();
		jProgressBar.setValue(10);
		model.addDownload(downloader);
		JRaiDownloader.getjRaiFrame().setModel(model);
	}
	*/
	
	/**
     * To lock or unlock the queue. If you lock, don't forget to unlock it at
     * the end
     */
    public void setQueueLock(boolean queueLock) {
        this.queueLock = queueLock;
        JRaiLogger.getLogger().log(Level.INFO, "Queue Locked: {0}", queueLock);
        //if unlocking, update the queuing. 
        //If locking, this call will return.
        updateQueue();
    }

    /**
     * Set stopfurther true or false.
     *
     * @param value
     */
    public void setStopFurther(boolean value) {
        this.stopfurther = value;
        JRaiLogger.getLogger().log(Level.INFO, "Stop Further: {0}", stopfurther);
        //if false, update the queuing.
        //if true, this call will return back.
        updateQueue();
    }
	
	
	
	/**
     * Start next download if any.. Decrements the current downloads number and
     * updates the queuing mechanism.
     */
    public void startNextDownloadIfAny() {
        currentlyDownloading--;
        //If no more uploads in queue and no more uploads currently running, then display message
        if (getQueuedUploadCount() == 0 && currentlyDownloading==0) {
        	JRaiLogger.getLogger().log(Level.INFO, "{0}: Download list finished", getClass().getName());
        } else {
            updateQueue();
        }
    }
    
    
    /**
     * Updates the queuing mechanism.. Doesn't affect queuing by number of times
     * called.
     */
    public void updateQueue() {
        //if queue is locked or uploads are at max or stopfurther is set, then return
        if ((stopfurther) || (queueLock) || (currentlyDownloading >= maxNoOfDownloads)) {
        	//JRaiLogger.getLogger().log(Level.INFO, "{0}: Nothing to update in download", getClass().getName());
            return;
        }

        //Iterate from the top of the upload list everytime
        for (int i = 0; i < JRDTableModel.downloadList.size(); i++) {
            Downloader downloader = JRDTableModel.downloadList.get(i);
            
            //Find queued uploads
            if (downloader.getStatus() == DownloadStatus.QUEUED) {
                //Start uploading
            	JRaiLogger.getLogger().log(Level.INFO, "{0}: Starting next download..", getClass().getName());
                downloader.startDownload();

                //Increment the number of current uploads
                currentlyDownloading++;

                //If current upload less than max allowed, then continue the loop.. Else break..
                if (currentlyDownloading >= maxNoOfDownloads) {
                    break;
                }
            } /*else if (SettingsProperties.isPropertyTrue("autoretryfaileduploads")
                    && downloader.getStatus() == UploadStatus.UPLOADFAILED) {
                try {
                    NULogger.getLogger().log(Level.INFO, "{0}: Restarting failed upload ({1})", new Object[]{getClass().getName(), i});

                    File file = downloader.getFile();
                    Constructor<? extends Uploader> uploaderConstructor = downloader.getClass().getConstructor(File.class);
                    AbstractUploader retryingUploader = (AbstractUploader) uploaderConstructor.newInstance(file);
                    NUTableModel.uploadList.set(i, retryingUploader);
                    retryingUploader.setRetry(true);
                    retryingUploader.startUpload();
                    
                    //Increment the number of current uploads
                    currentlyUploading++;

                    //If current upload less than max allowed, then continue the loop.. Else break..
                    if (currentlyUploading >= maxNoOfUploads) {
                        break;
                    }
                } catch (Exception ex) {
                    JRaiLogger.getLogger().log(Level.INFO, "Retry failed for {0}. Cause: {1}", new Object[]{i, ex});
                }
            }*/
        }
    }

}
