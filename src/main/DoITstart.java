package main;

/**  
 * DoItstart.java 
 * The starting point of the program.
 * 
 * Default is GUI
 * CliWithJline if command line argument is -cli
 * Pure compliant CLI if argument is -clisafe or consoleless system
 * 
 * @author  Yeo Kheng Meng
 */ 

import main.ui.Cli;
import main.ui.CliWithJline;
import main.ui.GuiTrayIcon;
import main.ui.UI;


public class DoItStart {
	
	
	public static void main(String[] args){
		
		UI doITUi;
		
		if (args.length == 0) {
			doITUi = new GuiTrayIcon();
			
		} else if (args[0].equals("-cli") && isConsoleAttached()) {
			doITUi = new CliWithJline();
		
		} else if (args[0].equals("-clisafe")) {
			doITUi = new Cli();

		} else {
			doITUi = new Cli();
		}
			
		doITUi.runUI();	

	}

	/**
	 * To check if Terminal Window is attached to DoIt.
	 * <p>
	 * The Jline library employs a native hook to the terminal, a console must be attached for it to work.                     
	 *
	 * @return true if terminal is attached, false if inside Eclipse or in a console-less system
	 */
	private static boolean isConsoleAttached() {
		return System.console() != null;
	}

}
