/**
 * 
 */
package jraidownloader.downloader.common;

import jraidownloader.downloader.abstractinterface.Downloader;
import jraidownloader.logging.JRaiLogger;
import jraidownloader.queue.QueueManager;

/**
 * Common downloader tasks.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class CommonDownloaderTasks {
	
	/**
     * Non-instantiable
     */
    private CommonDownloaderTasks() {
    }

    /**
     * Call this method at the end of successful downloading. 
     * Without this, JRaiDownloader will not start the next download.
     * 
     * It will print the download record to file, send statistics 
     * and starts next download
     * @param downloader the downloader 
     */
    public synchronized static void downloadFinished(Downloader downloader) {
        writeRecentlyUploaded(downloader);
        sendStats(downloader);
        QueueManager.getInstance().startNextDownloadIfAny();
    }
    
    
    /**
     * Call this method if the downloading failed.
     * Without this, JRaiDownloader will not start the next download.
     * 
     * It will send failure statistics 
     * and starts next download
     * @param downloader the downloader 
     */
    public synchronized static void downloadFailed(Downloader downloader) {
        sendStats(downloader);
        QueueManager.getInstance().startNextDownloadIfAny();
    }

    /**
     * Same as downloadFailed but may add more in future
     * @param downloader
     */
    public synchronized static void downloadStopped(Downloader downloader) {
        sendStats(downloader);
        QueueManager.getInstance().startNextDownloadIfAny();
    }
    
    /**
     * This private method writes recently downloaded files to a file on user's home folder.
     * @param downloader the downloader 
     */
    private static void writeRecentlyUploaded(Downloader downloader) {
    	/*
        try {
            //Validate URL
            if (!up.getDownloadURL().equals(UploadStatus.NA.getLocaleSpecificString())) {
                new URL(up.getDownloadURL());
            }

            //Append to the file instead of overwriting.
            PrintWriter writer = new PrintWriter(new FileWriter(System.getProperty("user.home") + File.separator + "recent.log", true));
            writer.write(up.getFileName() + "<>" + up.getHost() + "<>" + up.getDownloadURL() + "<>" + up.getDeleteURL() + "\n");
            writer.close();
        } catch (Exception ex) {
            JRaiLogger.getLogger().log(Level.INFO, "Error while writing recent.log\n{0}", ex);
        }
        */
    }

    /**
     * This private method will send statistics to the server.
     * 
     * These data are used for analysis and cleared periodically to avoid exceeding quota.
     * 
     * @param downloader the downloader 
     */
    private static void sendStats(Downloader downloader) {
    	/*
        try {
            String status = JRaiLogger.getStatus().getDefaultLocaleSpecificString();
            if (!status.startsWith("Upload")) {
                return;
            }
            JRaiLogger.getLogger().info("Sending statistics..");
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("version", NeembuuUploader.version + ""));
            formparams.add(new BasicNameValuePair("filename", up.getFileName()));
            formparams.add(new BasicNameValuePair("size", up.getSize()));
            formparams.add(new BasicNameValuePair("host", up.getHost()));
            formparams.add(new BasicNameValuePair("status", status));
            formparams.add(new BasicNameValuePair("os", System.getProperty("os.name")));
            formparams.add(new BasicNameValuePair("locale", NeembuuUploaderLanguages.getUserLanguageCode()));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            HttpPost httppost = new HttpPost("http://neembuuuploader.sourceforge.net/insert.php");
            httppost.setEntity(entity);
            HttpParams params = new BasicHttpParams();
            params.setParameter(
                    "http.useragent",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
            DefaultHttpClient httpclient = new DefaultHttpClient(params);
            httpclient.execute(httppost).getEntity().consumeContent();
        } catch (Exception ex) {
            NULogger.getLogger().log(Level.INFO, "Error while sending statistics\n{0}", ex);
        }
        */
    }

}
