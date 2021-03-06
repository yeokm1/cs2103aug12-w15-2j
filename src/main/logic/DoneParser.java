//@author A0081007U
package main.logic;

import java.util.List;
import java.util.NoSuchElementException;

import main.logic.exceptions.EmptyDescriptionException;
import main.shared.Task;

public class DoneParser extends CommandParser {

	private String arguments;
	private Integer index;
	private List<Task> lastShownToUi;
	private int serial;

	public DoneParser(String arguments) {
		super(arguments);
		this.arguments = arguments;
		index = null;
		lastShownToUi = lastShownObject.getLastShownList();
	}


	@Override
	public void parse() throws NumberFormatException, EmptyDescriptionException{
		if (arguments.length() == 0) {
			throw new EmptyDescriptionException();
		}
		index = Integer.parseInt(arguments)-1;		// counting from 0
		if ((index < 0) || ((index + 1) > lastShownToUi.size())) {
			throw new NoSuchElementException();
		}
		serial = lastShownToUi.get(index).getSerial();
	}
	public int getToBeDoneSerial(){
		return serial;
	}

}
