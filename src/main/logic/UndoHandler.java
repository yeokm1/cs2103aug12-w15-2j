//@author A0081007U
package main.logic;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import main.shared.LogicToUi;
import main.shared.Task;
import main.storage.WillNotWriteToCorruptFileException;

/**
 * Each object of this class handles a undo operation. The object call the undo
 * stack to revert the data to the previous state.
 * 
 */

public class UndoHandler extends CommandHandler {
	private static final String ERROR_NO_MORE_UNDO = "You don't have any more undo steps left";

	/**
	 * Constructor of the class.
	 * 
	 * @param arguments
	 *            : empty argument since undo command does not need any extra
	 *            information
	 */
	public UndoHandler(String arguments) {
		super(arguments);
	}
	/**
	 * This method overrides the execute method in command handler class.
	 * 
	 */
	public LogicToUi execute() {
		if (super.undoStepsRemaining() == 0) {
			return new LogicToUi(ERROR_NO_MORE_UNDO);
		}

		try {
			List<Task> previous = super.peekUndoClones();
			dataBase.setAll(previous);
			String status = "The " + super.popAndGetPrevUndoMsg()
					+ " has been undone";

			super.popUndoClones();
			feedback = new LogicToUi(status);

		} catch (IOException e) {
			feedback = new LogicToUi(ERROR_IO);
		} catch (WillNotWriteToCorruptFileException e) {
			feedback = new LogicToUi(ERROR_FILE_CORRUPTED);
		}

		return feedback;
	}

	@Override
	@Deprecated
	protected void updateDatabaseNSendToUndoStack()
			throws NoSuchElementException, IOException,
			WillNotWriteToCorruptFileException {
		// empty method

	}
}
