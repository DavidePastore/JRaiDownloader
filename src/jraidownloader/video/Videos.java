/**
 * 
 */
package jraidownloader.video;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;

import jraidownloader.Canale;
import jraidownloader.httpclient.JRDHttpClient;
import jraidownloader.httpclient.JRDHttpClientUtils;
import jraidownloader.logging.JRaiLogger;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe rappresentante la lista dei video con le differenti qualità, dimensioni e url.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class Videos {
	
	/**
	 * Costanti relative alla qualità del video.
	 */
	public static final String H264 = "h264";
	public static final String H264_400 = "h264_400";
	public static final String H264_600 = "h264_600";
	public static final String H264_800 = "h264_800";
	public static final String H264_1200 = "h264_1200";
	public static final String H264_1500 = "h264_1500";
	public static final String H264_1800 = "h264_1800";
	
	
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
	private ArrayList<Video> listaVideo = new ArrayList<Video>();
	
	/**
	 * Il video scelto.
	 */
	private Video videoScelto;


	/**
	 * Costruttore per il video.
	 * @param url l'url in cui è presente il video.
	 * @throws Exception 
	 */
	public Videos(String url) throws Exception{
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
				if(!jSonObjectProgramma.getString(H264).equals("")){
					String url = jSonObjectProgramma.getString(H264);
					Video video = new Video(url, H264, JRDHttpClientUtils.getContentLength(url));
					listaVideo.add(video);
				}
				
				if(!jSonObjectProgramma.getString(H264_400).equals("")){
					String url = jSonObjectProgramma.getString(H264_400);
					Video video = new Video(url, H264_400, JRDHttpClientUtils.getContentLength(url));
					listaVideo.add(video);
				}
				
				if(!jSonObjectProgramma.getString(H264_600).equals("")){
					String url = jSonObjectProgramma.getString(H264_600);
					Video video = new Video(url, H264_600, JRDHttpClientUtils.getContentLength(url));
					listaVideo.add(video);
				}
				
				if(!jSonObjectProgramma.getString(H264_800).equals("")){
					String url = jSonObjectProgramma.getString(H264_800);
					Video video = new Video(url, H264_800, JRDHttpClientUtils.getContentLength(url));
					listaVideo.add(video);
				}
				
				if(!jSonObjectProgramma.getString(H264_1200).equals("")){
					String url = jSonObjectProgramma.getString(H264_1200);
					Video video = new Video(url, H264_1200, JRDHttpClientUtils.getContentLength(url));
					listaVideo.add(video);
				}
				
				if(!jSonObjectProgramma.getString(H264_1500).equals("")){
					String url = jSonObjectProgramma.getString(H264_1500);
					Video video = new Video(url, H264_1500, JRDHttpClientUtils.getContentLength(url));
					listaVideo.add(video);
				}
				
				if(!jSonObjectProgramma.getString(H264_1800).equals("")){
					String url = jSonObjectProgramma.getString(H264_1800);
					Video video = new Video(url, H264_1800, JRDHttpClientUtils.getContentLength(url));
					listaVideo.add(video);
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
	 * TODO testare che funzioni come aspettato.
	 * @return Ritorna l'url del video con la miglior qualità.
	 * @throws IOException 
	 * @throws ClientProtocolException
	 */
	public String findBestQualityUrl() throws ClientProtocolException, IOException{
		String url = "";
		Iterator<Video> iterator = listaVideo.iterator();
		
		//Prende l'ultimo url inserito nella lista dei video
		while(iterator.hasNext()){
			Video video = iterator.next();
			JRaiLogger.getLogger().log(Level.FINE, video.toString());
			url = video.getUrl();
		}
		return JRDHttpClientUtils.getLocation(url);
	}
	
	/**
	 * Ritorna tutte le qualità video disponibili.
	 * @return Ritorna tutte le qualità video disponibili.
	 */
	public static ArrayList<String> getAllVideoQuality(){
		ArrayList<String> qualitaVideo = new ArrayList<String>();
		qualitaVideo.add(H264);
		qualitaVideo.add(H264_400);
		qualitaVideo.add(H264_600);
		qualitaVideo.add(H264_800);
		qualitaVideo.add(H264_1200);
		qualitaVideo.add(H264_1500);
		qualitaVideo.add(H264_1800);
		return qualitaVideo;
	}
	
	
	/**
	 * Trova il video data la sua qualità.
	 * @param qualita la qualità del video.
	 * @return Il video con la qualità scelta.
	 */
	public Video getVideoByQuality(String qualita){
		Video video;
		Iterator<Video> iterator = listaVideo.iterator();
		while(iterator.hasNext()){
			video = iterator.next();
			if(video.getQualita().equals(qualita)){
				return video;
			}
		}
		return null;
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
	 * @return the listaVideo
	 */
	public ArrayList<Video> getListaVideo() {
		return listaVideo;
	}


	/**
	 * @return the nomeProgramma
	 */
	public String getNomeProgramma() {
		return nomeProgramma;
	}


	/**
	 * @return the videoScelto
	 */
	public Video getVideoScelto() {
		return videoScelto;
	}


	/**
	 * @param videoScelto the videoScelto to set
	 */
	public void setVideoScelto(Video videoScelto) {
		this.videoScelto = videoScelto;
	}


}
