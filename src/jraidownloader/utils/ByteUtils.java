/**
 * 
 */
package jraidownloader.utils;

/**
 * Classe di utilità per la conversione delle grandezze dei byte.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class ByteUtils {

	/**
	 * Converte i byte in mega byte.
	 * @param bytes il numero di byte
	 * @return Il numero di megabyte.
	 */
	public static long fromBytesToMegaBytes(long bytes){
		return bytes/1024/1024;
	}
}
