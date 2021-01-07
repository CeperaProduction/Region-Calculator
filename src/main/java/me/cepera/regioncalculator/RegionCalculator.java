package me.cepera.regioncalculator;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class RegionCalculator {

	public static final String PROGRAM_NAME = "RegionCalculator";
	public static final String PROGRAM_VERSION = "1.0.0";

	public static final Logger LOGGER;
	
	static {
		try (InputStream in = RegionCalculator.class.getClassLoader().getResourceAsStream("logging.properties")){
			LogManager.getLogManager().readConfiguration(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOGGER = Logger.getLogger(PROGRAM_NAME);
	}
	
	public static void main(String[] args) {
		LOGGER.info("Starting "+PROGRAM_NAME+" version "+PROGRAM_VERSION);
		CalculateWindow.launch(CalculateWindow.class, args);
	}
	
	public static void logException(String message, Throwable throwable) {
		ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
		LOGGER.log(Level.SEVERE, message);
		try (PrintStream ps = new PrintStream(byteBuff, true, ResourceHelper.CHARSET.name())) {
	        throwable.printStackTrace(ps);
			LOGGER.log(Level.SEVERE, byteBuff.toString(ResourceHelper.CHARSET.name()), throwable);
	    } catch (UnsupportedEncodingException e) {
			LOGGER.log(Level.SEVERE, "Error while display another error.");
			e.printStackTrace();
		}
	}
	
}
