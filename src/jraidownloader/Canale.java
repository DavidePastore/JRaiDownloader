/**
 * 
 */
package jraidownloader;

import java.util.HashMap;
import java.util.Map;

/**
 * Questa classe contiene la lista dei nomi dei canali, altre info generiche e metodi utili.
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class Canale {
	
	/**
	 * Canale Rai 1.
	 */
	public static final String RAI_UNO = "RaiUno";
	
	/**
	 * Canale Rai 2.
	 */
	public static final String RAI_DUE = "RaiDue";
	
	/**
	 * Canale Rai 3.
	 */
	public static final String RAI_TRE = "RaiTre";
	
	/**
	 * Canale Rai 5.
	 */
	public static final String RAI_CINQUE = "RaiCinque";
	
	/**
	 * Canale Rai Premium.
	 */
	public static final String RAI_PREMIUM = "RaiPremium";
	
	
	/**
	 * L'url di base per il replay.
	 */
	public static final String REPLAY_BASE_URL = "http://www.rai.tv/dl/portale/html/palinsesti/replaytv/static/%s_%s.html";
	
	
	/**
	 * Corrispondenza tra id del canale e il suo nome.
	 */
	private static Map<Integer, String> canali = new HashMap<Integer, String>();
	
	
	/**
	 * Ottieni il nome del canale dato il suo ID.
	 * @param idCanale l'id del canale.
	 * @return il nome del canale.
	 */
	public static String getChannelName(int idCanale){
		popolaMappaCanali();
		return canali.get(idCanale);
	}
	
	
	/**
	 * Popola la mappa con tutti i canali.
	 */
	public static void popolaMappaCanali(){
		if(canali.isEmpty()){
			canali.put(1, RAI_UNO);
			canali.put(2, RAI_DUE);
			canali.put(3, RAI_TRE);
			canali.put(31, RAI_CINQUE);
			canali.put(32, RAI_PREMIUM);
		}
	}
	
}
