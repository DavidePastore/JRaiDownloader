/**
 * 
 */
package jraidownloader.downloader;

import java.util.ArrayList;
import java.util.logging.Level;

import jraidownloader.Downloader;
import jraidownloader.JRaiFrame;
import jraidownloader.dialog.SceltaQualita;
import jraidownloader.downloader.abstractinterface.AbstractDownloader;
import jraidownloader.logging.JRaiLogger;
import jraidownloader.properties.PropertiesManager;
import jraidownloader.video.Videos;

/**
 * @author <a href="https://github.com/DavidePastore">DavidePastore</a>
 * http://txt2re.com/index-java.php3?s=http://www.rai.tv/dl/replaytv/replaytv.html%23ch=31%26day=2013-08-26%26v=257809%26vd=2013-08-26%26vc=31&-20&-18&-155&-124&-125&-27&-6&-126&-32&-127&-11&-128&-2&-112&-31&-156&143&134&-113&-25&-157&4&-114&-77&-158&97&104&107&109&88&111&-115&-35&-159&5&-116&-34&-160&94&103
 *
 */
public class RaiReplay extends AbstractDownloader {

	/**
	 * @param videos
	 */
	public RaiReplay(String url) {
		super(url);
		regularExpression = new String[]{
				"http:\\/\\/www\\.rai\\.tv\\/dl\\/replaytv\\/replaytv\\.html",
				"ch=\\d+",
				"day=((?:(?:[1]{1}\\d{1}\\d{1}\\d{1})|(?:[2]{1}\\d{3}))[-:\\/.](?:[0]?[1-9]|[1][012])[-:\\/.](?:(?:[0-2]?\\d{1})|(?:[3][01]{1})))(?![\\d])",
				"v=\\d+",
				"vd=((?:(?:[1]{1}\\d{1}\\d{1}\\d{1})|(?:[2]{1}\\d{3}))[-:\\/.](?:[0]?[1-9]|[1][012])[-:\\/.](?:(?:[0-2]?\\d{1})|(?:[3][01]{1})))(?![\\d])",
				"vc=\\d+"
		};
		//regularExpression = "http:\\/\\/www\\.rai\\.tv\\/dl\\/replaytv\\/replaytv\\.html#ch=\\d+&day=((?:(?:[1]{1}\\d{1}\\d{1}\\d{1})|(?:[2]{1}\\d{3}))[-:\\/.](?:[0]?[1-9]|[1][012])[-:\\/.](?:(?:[0-2]?\\d{1})|(?:[3][01]{1})))(?![\\d])&v=\\d+&vd=((?:(?:[1]{1}\\d{1}\\d{1}\\d{1})|(?:[2]{1}\\d{3}))[-:\\/.](?:[0]?[1-9]|[1][012])[-:\\/.](?:(?:[0-2]?\\d{1})|(?:[3][01]{1})))(?![\\d])&vc=\\d+";
	}

	/* (non-Javadoc)
	 * @see jraidownloader.downloader.abstractinterface.AbstractDownloader#run()
	 */
	@Override
	public void run() {
		try {
			videos = new Videos(url);
			
			/* Impostazione di qualità di default o no */
			boolean defaultQualityEnabled = Boolean.parseBoolean(PropertiesManager.getProperty(PropertiesManager.DEFAULT_QUALITY_ENABLED));
			String defaultQuality = PropertiesManager.getProperty(PropertiesManager.DEFAULT_QUALITY);
			if(defaultQualityEnabled && defaultQuality != null) {
				ArrayList<String> listaQualita = Videos.getAllVideoQuality();
				
				if(!listaQualita.contains(defaultQuality)){
					throw new Exception("Qualità del video non disponibile.");
				}
				
				/* Ciclo tutte le qualità finchè non trovo la qualità migliore disponibile in base a quella scelta */
				boolean videoTrovato = false;
				for(int i = listaQualita.indexOf(defaultQuality); i >= 0 && !videoTrovato; i--){
					String qualita = listaQualita.get(i);
					video = videos.getVideoByQuality(qualita);
					if(video != null){
						JRaiLogger.getLogger().log(Level.INFO, "Qualità video trovata: " + qualita);
						videoTrovato = true;
					}
				}
			}
			else {
				SceltaQualita sceltaQualita = new SceltaQualita(JRaiFrame.getInstance(), true, videos);
				sceltaQualita.setAlwaysOnTop(true);
				sceltaQualita.setVisible(true);
				
				if(videos.getVideoScelto() == null){
					throw new Exception("Qualità del video non scelta.");
				}
				
				video = videos.getVideoScelto();
			}
			
			downloading();
			Downloader downloader = new Downloader();
		
			downloader.downloadFile(video.getUrl(), videos.getNomeProgramma() + ".mp4", videos.getData(), downloadProgress, downloadSpeed);
			downloadFinished();
		} catch (Exception e) {
			JRaiLogger.getLogger().log(Level.INFO, "Exception: " + e);
		}
		
	}

}
