/**
 * 
 */
package jraidownloader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import jraidownloader.httpclient.JRDHttpClient;
import jraidownloader.logging.JRaiLogger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe rappresentante il video.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class Video {
	
	
	/**
	 * Il canale del video.
	 */
	private int canale = -1;
	
	/**
	 * ID del video.
	 */
	private int id = -1;
	
	/**
	 * Data di messa in onda del video.
	 */
	private String data;
	
	
	/**
	 * Nome programma.
	 */
	private String nomeProgramma;
	
	
	/**
	 * Descrizione del video
	 */
	private String descrizione;
	
	/**
	 * Url con tutti i video in json.
	 */
	private String jSonUrl;
	
	
	/**
	 * Tutti i video disponibili in base alle qualità.
	 */
	private Map<String, String> urls = new HashMap<String, String>();
	
	
	/**
	 * La qualità del video scelta.
	 */
	private String qualitaVideo;


	/**
	 * Costruttore per il video.
	 * @param url l'url in cui è presente il video.
	 * @throws Exception 
	 */
	public Video(String url) throws Exception{
		this.checkUrl(url);
		this.parseUrl(url);
		this.cercaJsonUrl();
		this.trovaVideo();
	}
	
	
	private void checkUrl(String url) throws Exception{
		//Se l'url è vuoto attiva l'eccezione
		if(url.isEmpty()){
			throw new Exception("L'Url è vuoto");
		}
		
		try {
		    new java.net.URI(url);
		} catch(URISyntaxException e) {
			throw new Exception("L'url non è corretto");
		}
	}
	
	/**
	 * Fa il parsing del video per trovare le caratteristiche relative ad esso:
	 * ch = channel
	 * v  = id del video
	 * vd = data del video
	 * vc = id del channel
	 * @throws Exception 
	 *
	 */
	private void parseUrl(String url) throws Exception{
		String[] parametri;
		url = url.substring(url.indexOf("#") + 1);
		parametri = url.split("&");
		
		for(int i = 0; i < parametri.length; i++){
			//Get canale
			if(parametri[i].startsWith("ch=")){
				canale = Integer.parseInt(parametri[i].substring("ch=".length()));
				JRaiLogger.getLogger().log(Level.FINE, "Channel: " + canale);
			}
			
			//Get id del video
			if(parametri[i].startsWith("v=")){
				id = Integer.parseInt(parametri[i].substring("v=".length()));
				JRaiLogger.getLogger().log(Level.FINE, "ID: " + id);
			}
			
			//Get data del video
			if(parametri[i].startsWith("vd=")){
				data = parametri[i].substring("vd=".length());
				JRaiLogger.getLogger().log(Level.FINE, "data: " + data);
			}
			JRaiLogger.getLogger().log(Level.FINE, "Parametro[" + i + "]: " + parametri[i]);
		}
		
		if(canale == -1 || id == -1 || data == null){
			throw new Exception("L'url non contiene i parametri aspettati: " + canale + " " + id + " " + data);
		}
	}
	
	/**
	 * Cerca l'url con tutti i video in JSON.
	 */
	private void cercaJsonUrl(){
		String underserscoreData = data.replace("-", "_");
		jSonUrl = String.format(Canale.REPLAY_BASE_URL, Canale.getChannelName(canale), underserscoreData);
	}
	
	/**
	 * Trova il video all'interno del file json.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws JSONException 
	 */
	private void trovaVideo() throws ClientProtocolException, IOException, JSONException{
		HttpClient httpClient = JRDHttpClient.get();
		HttpGet httpGet = new HttpGet(this.jSonUrl);
		HttpResponse httpResponse;
		httpResponse = httpClient.execute(httpGet);
		String jSonText = EntityUtils.toString(httpResponse.getEntity());
		JSONObject jSonObject = new JSONObject(jSonText);
		JRaiLogger.getLogger().log(Level.FINEST, "" + jSonObject);
		
		JSONObject jSonObjectGiorno = jSonObject.getJSONObject(Integer.toString(canale));
		
		
		JSONObject jSonObjectData = jSonObjectGiorno.getJSONObject(data);
		Iterator iterator = jSonObjectData.keys();
		while(iterator.hasNext()){
			JSONObject jSonObjectProgramma = jSonObjectData.getJSONObject((String) iterator.next());
			if(jSonObjectProgramma.getString("i").equals(Integer.toString(this.id))){
				
				//Popolo con tutti gli url
				if(!jSonObjectProgramma.getString("h264").equals("")){
					urls.put("h264", jSonObjectProgramma.getString("h264"));
				}
				
				if(!jSonObjectProgramma.getString("h264_400").equals("")){
					urls.put("h264_400", jSonObjectProgramma.getString("h264_400"));
				}
				
				if(!jSonObjectProgramma.getString("h264_600").equals("")){
					urls.put("h264_600", jSonObjectProgramma.getString("h264_600"));
				}
				
				if(!jSonObjectProgramma.getString("h264_800").equals("")){
					urls.put("h264_800", jSonObjectProgramma.getString("h264_800"));
				}
				
				if(!jSonObjectProgramma.getString("h264_1200").equals("")){
					urls.put("h264_1200", jSonObjectProgramma.getString("h264_1200"));
				}
				
				if(!jSonObjectProgramma.getString("h264_1500").equals("")){
					urls.put("h264_1500", jSonObjectProgramma.getString("h264_1500"));
				}
				
				if(!jSonObjectProgramma.getString("h264_1800").equals("")){
					urls.put("h264_1800", jSonObjectProgramma.getString("h264_1800"));
				}
				
				
				//Altre caratteristiche del video
				if(!jSonObjectProgramma.getString("d").equals("")){
					descrizione =  jSonObjectProgramma.getString("d");
				}
				
				if(!jSonObjectProgramma.getString("nomeProgramma").equals("")){
					nomeProgramma =  jSonObjectProgramma.getString("nomeProgramma");
				}
			}
		}
	}
	
	/**
	 * Trova l'url del video con la miglior qualità.
	 * @return Ritorna l'url del video con la miglior qualità.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public String findBestQualityUrl() throws ClientProtocolException, IOException{
		String url = "";
		String location = "";
		Set<String> keys = urls.keySet();
		Iterator iterator = keys.iterator();
		
		//Prende l'ultimo url inserito nella lista
		while(iterator.hasNext()){
			String key = (String) iterator.next();
			JRaiLogger.getLogger().log(Level.FINE, key + ": " + urls.get(key));
			url = urls.get(key);
		}
		
		HttpClient httpClient = JRDHttpClient.get();
		HttpGet httpGet = new HttpGet(url);
		HttpParams params = httpGet.getParams();
		params.setParameter(ClientPNames.HANDLE_REDIRECTS, Boolean.FALSE);
		httpGet.setParams(params);
		
		HttpResponse httpResponse;
		HttpEntity httpEntity = null;
		httpResponse = httpClient.execute(httpGet);
		httpEntity = httpResponse.getEntity();
		
		location = httpResponse.getFirstHeader("Location").getValue();
		try {
			EntityUtils.consume(httpEntity);
		} catch (IOException e) {
			JRaiLogger.getLogger().log(Level.SEVERE, "IOException: " + e);
		}
		return location;
	}

	/**
	 * @return the canale
	 */
	public int getCanale() {
		return canale;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the nomeProgramma
	 */
	public String getNomeProgramma() {
		return nomeProgramma;
	}

	/**
	 * @return the urls
	 */
	public Map<String, String> getUrls() {
		return urls;
	}
	
	/**
	 * @return the qualitaVideo
	 */
	public String getQualitaVideo() {
		return qualitaVideo;
	}


	/**
	 * @param qualitaVideo the qualitaVideo to set
	 */
	public void setQualitaVideo(String qualitaVideo) {
		this.qualitaVideo = qualitaVideo;
	}

}
