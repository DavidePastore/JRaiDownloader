/**
 * 
 */
package jraidownloader.httpclient;

import java.io.IOException;
import java.util.logging.Level;

import jraidownloader.logging.JRaiLogger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

/**
 * Classe di utilità per il client http.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class JRDHttpClientUtils {

	/**
	 * Effettua una richiesta GET alla pagina e ritorna il valore della Location.
	 * @param url l'url su cui fare la richiesta.
	 * @return Restituisce il valore della Location.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String getLocation(String url) throws ClientProtocolException, IOException{
		String location = null;
		HttpClient httpClient = JRDHttpClient.get();
		HttpGet httpGet = new HttpGet(url);
		
		HttpParams params = httpGet.getParams();
		params.setParameter(ClientPNames.HANDLE_REDIRECTS, Boolean.FALSE);
		httpGet.setParams(params);
		
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpEntity != null) {
			location = httpResponse.getFirstHeader("Location").getValue();
		}
		EntityUtils.consume(httpEntity);
		return location;
	}
	
	/**
	 * Legge la dimensione di un file presente sulla rete.
	 * @param url l'url da richiedere.
	 * @return la dimensione del file.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static long getContentLength(String url) throws ClientProtocolException, IOException{
		long contentLength = 0;
		JRaiLogger.getLogger().log(Level.FINE, "URL: " + url);
		HttpClient httpClient = JRDHttpClient.get();
		HttpGet httpGet = new HttpGet(url);
		
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpEntity != null) {
			contentLength = httpEntity.getContentLength();
		}
		httpGet.releaseConnection();
		JRaiLogger.getLogger().log(Level.FINE, "Content Length: " + contentLength);
		return contentLength;
	}
}
