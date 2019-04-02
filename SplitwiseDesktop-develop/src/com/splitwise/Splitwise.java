/**
 * Splitwise.java : Entry class to initialize User Interface

 * @version		0.1
 * @since   	2019-02-14
 */
package com.splitwise;

import java.io.IOException;
import java.util.logging.*;

import com.splitwise.gui.MainFrame;
import com.splitwise.logger.SplitwiseLogger;


public class Splitwise {
	static SplitwiseGUI gui;
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	// Initialize Logger
	private static void setupLogger() {
		try {
			SplitwiseLogger.setup();
		} catch(IOException io) {
			System.err.println("Unable to setup logger");
		}
	}
	public static void main(String[] arg) {
		// Initialize core modules
		setupLogger();
		LOGGER.info("Logger test ok");
		LOGGER.setLevel(Level.FINEST);
		gui = new SplitwiseGUI();
		gui.init();
		gui.login();
	}
}
