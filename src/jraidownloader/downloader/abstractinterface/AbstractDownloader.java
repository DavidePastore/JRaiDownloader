/**
 * 
 */
package jraidownloader.downloader.abstractinterface;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jraidownloader.downloader.DownloadStatus;
import jraidownloader.downloader.common.CommonDownloaderTasks;
import jraidownloader.utils.ByteUtils;
import jraidownloader.utils.ChangeableString;
import jraidownloader.video.Video;
import jraidownloader.video.Videos;

/**
 * Abstract downloader.
 * @author <a href="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public abstract class AbstractDownloader implements Downloader{
	
	protected Videos videos;
	protected Video video;
	protected String url;
	protected final AtomicInteger downloadProgress = new AtomicInteger(0);
	protected DownloadStatus status = DownloadStatus.QUEUED;
	protected String[] regularExpression;
	protected ChangeableString downloadSpeed = new ChangeableString("");
    protected Thread thread = new Thread(this);
    private boolean retry = false;
	
	
	public AbstractDownloader(String url){
		this.url = url;
	}
	
	public boolean checkPattern(){
		for(int i = 0; i < regularExpression.length; i++){
			Pattern pattern = Pattern.compile(regularExpression[i]);
			Matcher m = pattern.matcher(url);
			if(!m.find()){
				return false;
			}
		}
		
		return true;
	}
	
	public String getDownloadSpeed(){
		return downloadSpeed.toString();
	}
	
	public String getFileName() {
		return videos.getNomeProgramma() + ".mp4";
    }
	
	public String getFileSize(){
		return Long.toString(ByteUtils.fromBytesToMegaBytes(video.getLength())) + " MB";
	}
	
	public int getProgress() {
        return downloadProgress.get();
    }
	
	public String[] getRegularExpression(){
		return regularExpression;
	}

    public String getSize() {
    	return "size";
        //return CommonUploaderTasks.getSize(file.length());
    }
    
    public DownloadStatus getStatus() {
        return status;
    }
    
    public Videos getVideos() {
    	return videos;
    }

    public void startDownload() {
        thread.start();
    }

    public void stopDownload() {
        status = DownloadStatus.DOWNLOADSTOPPED;
        CommonDownloaderTasks.downloadStopped(this);
        thread.stop();
    }

    public abstract void run();

    protected void uploadInitialising() {
        if (retry) {
            status = DownloadStatus.RETRYING;
        } else {
            status = DownloadStatus.INITIALISING;
        }
    }
    
    protected void downloading() {
        if (retry) {
            status = DownloadStatus.REUPLOADING;
        } else {
            status = DownloadStatus.DOWNLOADING;
        }
    } 
    
    protected void downloadInvalid() {
        status = DownloadStatus.DOWNLOADINVALID;
        CommonDownloaderTasks.downloadFailed(this);
    }
    
    protected void downloadFailed() {
        if (retry) {
            status = DownloadStatus.RETRYFAILED;
        } else {
            status = DownloadStatus.DOWNLOADFAILED;
        }
        //CommonUploaderTasks.uploadFailed(this);
    }

    protected void downloadFinished() {
        status = DownloadStatus.DOWNLOADFINISHED;
        //CommonUploaderTasks.uploadFinished(this);
    }
    
    public void setRetry(boolean retry) {
        this.retry = retry;
        status = DownloadStatus.TORETRY;
    }

}
