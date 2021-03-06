//@author A0081007U
package main;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHandler {


	private static final String LOG_FILE_NAME = "DoItLog.log";
	private static FileHandler fh;
	private static Logger theOneLogger = null;

	public static boolean logOn = false;
	
	
	public static Logger getLogInstance(){
		if(theOneLogger == null){
			try {
				theOneLogger = Logger.getLogger("DoItLogger");

				// Log everything! (while we're still debugging)...
				// this should be able to be changed via a command-line argument
				
				if(logOn) {
					theOneLogger.setLevel(Level.INFO);
					theOneLogger.addHandler(getFileHandler());
				} else {
					theOneLogger.setLevel(Level.OFF);
				}
				
				//To disable logging to Standard Error
				theOneLogger.setUseParentHandlers(false);
				
				
				
	
				// Commented away as may be needed for debugging in future.
				//theOneLogger.addHandler(new SocketHandler("127.0.0.1", 8888));
			} catch (SecurityException | IOException e) {
			}
		}
		return theOneLogger;
	}


	private static FileHandler getFileHandler() throws SecurityException, IOException{	
			if(fh == null){
				fh = new FileHandler(LOG_FILE_NAME);
			}
		return fh;
	}
	





}
