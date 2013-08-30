/**
 * 
 */
package jraidownloader.downloader;

/**
 * The download status enum.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public enum DownloadStatus {
	QUEUED,
    INITIALISING,
    DOWNLOADING,
    DOWNLOADFINISHED,
    DOWNLOADFAILED,
    DOWNLOADSTOPPED,
    DOWNLOADINVALID,
    GETTINGERRORS,
    PLEASEWAIT,
    NA,
    
    TORETRY,
    RETRYING,
    REUPLOADING,
    RETRYFAILED,
}
