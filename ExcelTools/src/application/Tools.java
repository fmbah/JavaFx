package application;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 
 * @author fmbah
 *
 */
public class Tools {
	public static final Logger logger = Logger.getLogger("Log");
	public static FileHandler fileHandler;
	static {
		try {
			fileHandler = new FileHandler(System.getProperty("user.home") + File.separator + LocalDate.now().toString() + ".log");
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
		} catch (SecurityException e) {
			logger.log(Level.SEVERE, null, e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, null, e);
		}
	}
}
