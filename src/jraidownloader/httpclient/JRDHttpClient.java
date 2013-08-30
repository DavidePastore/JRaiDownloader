/**
 * 
 */
package jraidownloader.httpclient;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

/**
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class JRDHttpClient {
	
	private static HttpClient httpClient;
	
	/**
	 * Non instanziabile.
	 */
	private JRDHttpClient(){
		
	}

	/**
	 * Leggi l'HttpClient.
	 * @return httpClient.
	 */
	public static HttpClient get(){
		if(httpClient == null){
			httpClient = new DefaultHttpClient(new PoolingClientConnectionManager());
		}
		return httpClient;
	}

}
