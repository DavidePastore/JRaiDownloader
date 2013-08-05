/**
 * 
 */
package jraidownloader.logging;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author <a reef="https://github.com/DavidePastore">DavidePastore</a>
 *
 */
public class JRaiLogger {
	
	private final static Logger LOGGER = Logger.getLogger("");
	
	static {
        try {
            //The Logger should log messages both to console and a log file
            FileHandler handler = new FileHandler("JRAILogger.log", false);
            handler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(handler);
            
        } catch (Exception ex) {
            System.out.println("Exception in creating Log file itself");
        }
        
    }
	
	/**
	 * Classe non instanziabile.
	 */
    private JRaiLogger() {
    }

	/**
	 * @return the logger
	 */
	public static Logger getLogger() {
		return LOGGER;
	} 

}
