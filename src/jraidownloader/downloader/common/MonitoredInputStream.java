/**
 * 
 */
package jraidownloader.downloader.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Monitored Input Stream to download and get progress.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class MonitoredInputStream extends InputStream{
	private InputStream upperStream;
    private long totalSize = 0;
    private long downloadedSize = 0;
    private AtomicInteger jp = null;
    private int read;
    private int progressValue;

    public MonitoredInputStream(InputStream upperStream) {
        this(upperStream,Long.MAX_VALUE,null);
    }

    public MonitoredInputStream(InputStream upperStream, long totalSize) {
        this(upperStream,totalSize,null);
    }

    public MonitoredInputStream(InputStream upperStream, long totalSize, AtomicInteger jp) {
        this.upperStream = upperStream;
        this.totalSize = totalSize;
        this.jp = jp;
    }



    @Override
    public int read() throws IOException {
//        NULogger.getLogger().info("simple read");
        return upperStream.read();
    }

    @Override
    public int read(byte[]b) throws IOException {
        read = upperStream.read(b);
        if (read == -1) return -1;
        downloadedSize += read;
        progressValue = (int)((float)((downloadedSize*100)/totalSize));
        jp.set(progressValue);
//        NULogger.getLogger().info("byte read: " + read);
//        NULogger.getLogger().info("downloaded size: "+ downloadedSize);
//        NULogger.getLogger().info("Total size: "+ totalSize);
//        NULogger.getLogger().info("progress value: "+ progressValue);
        return read;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        read = upperStream.read(b, off, len);
        if (read == -1) return -1;
        downloadedSize += read;
        progressValue = (int)((float)((downloadedSize*100)/totalSize));
        jp.set(progressValue);
//        NULogger.getLogger().info("off read: " + progressValue);
        return read;
    }

    @Override
    public void close() throws IOException {
        upperStream.close();
        super.close();
    }
}
