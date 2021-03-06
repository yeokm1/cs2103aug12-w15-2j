//@author A0081007U
package test.storage;
/**  
 * FileManagementTest.java 
 * A Junit4 test for the FileManagement class
 * This test does not have complete branch coverage as some aspects 
 * 	like file locking cannot be simulated in the test.
 * 
 * Warning1: Database file will be cleared during and after the test
 * Warning2: If the read-only test fails, the database file attributes/permissions may remain at read only in the File System.
 * Subsequent tests may fail till you change it back.
 * @author  Yeo Kheng Meng
 */ 

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.shared.Task;
import main.storage.FileManagement;
import main.storage.WillNotWriteToCorruptFileException;

public class FileManagementTest {
	private static final String NAME_ONLY = "name only super long super long super long super long super long super long super long super long super long super long super long super long super long super long";
	private static final String NAME_TRUE = "name true";
	private static final String NAME_FALSE = "name false";
	private static final String NAME_1MONTH = "name +1month";
	private static final String NAME_1MONTH_TRUE = "name -1month true";
	private static final String NAME_FROM_YESTERDAY_TO_TOMORROW = "name from yesterday to tomorrow";
	private static final String NAME_FROM_NOW_TO_TOMORROW_0000_FALSE = "name from now to tomorrow 0000 false";

	private static final DateTime DEADLINE = new DateTime().plusMonths(1);
	private static final DateTime DEADLINE_TRUE = new DateTime().minusMonths(1);
	private static final DateTime TIMED_START = new DateTime().minusDays(1);
	private static final DateTime TIMED_END = new DateTime().plusDays(1);
	private static final DateTime TIMED_DONE_FALSE_START = new DateTime();
	private static final DateTime TIMED_DONE_FALSE_END = TIMED_DONE_FALSE_START.plusDays(1).withTimeAtStartOfDay();


	private static final String FILE_CORRUPT_DATE = 		"  1 | D | - | 21-O12 1729 +0800 | ---------------------- | event";
	private static final String FILE_MISSING_EVENT = 		"  2 | D | - | 21-Oct-2012 1729 +0800 | ---------------------- | ";
	private static final String FILE_WRONG_TYPE = 			"  3 | C | - | 21-Oct-2012 1729 +0800 | ---------------------- | event";
	private static final String FILE_NOT_DONE_OR_UNDONE = 	"  4 | D | X | 21-Oct-2012 1729 +0800 | 21-Oct-2012 1729 +0800 | event";
	private static final String FILE_MISSING_DELIMITER = 	"  5 | * | U | 21-Oct-2012 1729 +0800 ---------------------- | event";
	private static final String FILE_MISSING_FIELD = 		"  6 | D | * | 21-Oct-2012 1729 +0800 | event";
	private static final String FILE_START_DATE_AFTER_END = "  7 | T | - | 22-Oct-2012 1729 +0800 | 21-Oct-2012 1729 +0800 | event";
	private static final String FILE_MISSING_TIMEZONE =   	"  8 | D | * | 24-Oct-2012 2248 | ---------------------- | Test deadline";
	private static final String FILE_NO_SIGN_TIMEZONE =   	"  9 | D | * | 24-Oct-2012 2248 0600 | ---------------------- | Test deadline";
	private static final String FILE_WRONG_SIGN_TIMEZONE =  " 10 | D | * | 24-Oct-2012 2248 A0600 | ---------------------- | Test deadline";
	private static final String FILE_WRONG_TIMEZONE =   	" 11 | D | * | 24-Oct-2012 2248 89 | ---------------------- | Test deadline";
	
	//
	//	private static final String FILE_GOOD_STRING = "0 | T | * | 22-Oct-2012 1729 -1100 | 23-Oct-2013 1800 +1100 | event";

	private static final String[] corruptStrings = 
		{ 
		FILE_CORRUPT_DATE, FILE_MISSING_EVENT, FILE_WRONG_TYPE,  
		FILE_NOT_DONE_OR_UNDONE,FILE_MISSING_DELIMITER, FILE_MISSING_FIELD,
		FILE_START_DATE_AFTER_END, FILE_MISSING_TIMEZONE,  FILE_NO_SIGN_TIMEZONE, 
		FILE_WRONG_SIGN_TIMEZONE, FILE_WRONG_TIMEZONE
		
		};


	Task name;
	Task nameTrue;
	Task nameFalse;
	
	Task nameDeadline;
	Task nameDeadlineTrue;

	Task nameTimed;
	Task nameTimedFalse;
	List<Task> filledListing;
	List<Task> shortListing;
	List<Task> initialClearListing;

	FileManagement fileMgmt = FileManagement.getInstance();


	@Before
	public void runBeforeEveryTest() {
		name = new Task(NAME_ONLY);
		nameTrue = new Task(NAME_TRUE, true);
		nameFalse = new Task(NAME_FALSE, false);


		nameDeadline = new Task(NAME_1MONTH, DEADLINE);
		nameDeadlineTrue = new Task(NAME_1MONTH_TRUE, DEADLINE_TRUE, true);

		nameTimed = new Task(NAME_FROM_YESTERDAY_TO_TOMORROW, TIMED_START, TIMED_END);
		nameTimedFalse = new Task(NAME_FROM_NOW_TO_TOMORROW_0000_FALSE, TIMED_DONE_FALSE_START, TIMED_DONE_FALSE_END, false);


		filledListing = new ArrayList<Task>();

		filledListing.add(name);
		filledListing.add(nameDeadline);
		filledListing.add(nameTimedFalse);

		filledListing.add(nameTrue);
		filledListing.add(nameDeadlineTrue);

		filledListing.add(nameTimed);
		filledListing.add(nameFalse);


		shortListing = new ArrayList<Task>();
		shortListing.add(nameDeadline);
		shortListing.add(nameTimedFalse);


		initialClearListing = new ArrayList<Task>();

		//To ensure the database file is blank
		try {
			BufferedWriter writeFile = new BufferedWriter(new FileWriter(fileMgmt.filename));
			writeFile.close();
		} catch (IOException e) {
			fail();
		}




	}

	@After
	public void runAfterEveryTest() {
		fileMgmt.closeFile();
	}


	@Test
	public void readAndWriteFunctionalityTest() {
		fileMgmt.prepareDatabaseFile();

		try {

			fileMgmt.writeDataBaseToFile(new ArrayList<Task>());
			initialClearListing = fileMgmt.readFileAndDetectCorruption();

			assertEquals(FileManagement.FileStatus.FILE_ALL_OK, fileMgmt.getFileAttributes());
			assertEquals(0, initialClearListing.size());

			initialClearListing = new ArrayList<Task>();

			fileMgmt.writeDataBaseToFile(filledListing);
			initialClearListing = fileMgmt.readFileAndDetectCorruption();

			//Check every bit of info written down is read back correctly
			for(int i = 0; i < filledListing.size(); i++){
				Task fromDisk = initialClearListing.get(i);
				Task original = filledListing.get(i);

				assertEquals(original.showInfo(), fromDisk.showInfo());
			}

			//Write a shortened file, to ensure no remnants of previous database remain on disk
			initialClearListing = new ArrayList<Task>();
			fileMgmt.writeDataBaseToFile(shortListing);
			initialClearListing = fileMgmt.readFileAndDetectCorruption();
			assertEquals(FileManagement.FileStatus.FILE_ALL_OK, fileMgmt.getFileAttributes());

			//Check every bit of info written down is read back correctly
			for(int i = 0; i < shortListing.size(); i++){
				Task fromDisk = initialClearListing.get(i);
				Task original = shortListing.get(i);

				assertEquals(original.showInfo(), fromDisk.showInfo());
			}


		} catch (IOException | WillNotWriteToCorruptFileException e) {
			fail();
		}


	}

	@Test
	public void corruptTest() {


		BufferedWriter writeFile;

		//Test that every corrupt String is caught
		String lastString = null;
		try {
			
			for(String spoiltString : corruptStrings)
			{
				lastString = spoiltString;
				writeFile = new BufferedWriter(new FileWriter(fileMgmt.filename));
				writeFile.close();

				writeFile = new BufferedWriter(new FileWriter(fileMgmt.filename));
				writeFile.write(spoiltString);
				writeFile.close();

				fileMgmt.prepareDatabaseFile();
				initialClearListing = fileMgmt.readFileAndDetectCorruption();
				fileMgmt.closeFile();

				System.out.println("Corrupt Strings: " + lastString);
				assertEquals(FileManagement.FileStatus.FILE_IS_CORRUPT, fileMgmt.getFileAttributes());
			}
		} catch (IOException e) {

			fail();
		}


		boolean catchCorruptException = false;
		try {
			fileMgmt.readFileAndDetectCorruption();
			fileMgmt.writeDataBaseToFile(new ArrayList<Task>());
			fail();
		} catch (IOException e) {
			fail();
		} catch (WillNotWriteToCorruptFileException e) {
			catchCorruptException = true;
		} finally {
			fileMgmt.closeFile();
		}

		assertTrue(catchCorruptException);
		fileMgmt.closeFile();

	}


	@Test
	public void readOnlyTest()
	{

		File dbFile = new File(fileMgmt.filename);

		//Set the file to read only to test
		if(dbFile.canWrite()){
			dbFile.setReadOnly();
		}

		fileMgmt.prepareDatabaseFile();

		assertEquals(FileManagement.FileStatus.FILE_READ_ONLY, fileMgmt.getFileAttributes());

		fileMgmt.readFileAndDetectCorruption();
		fileMgmt.closeFile();
		assertEquals(FileManagement.FileStatus.FILE_READ_ONLY, fileMgmt.getFileAttributes());

		dbFile.setWritable(true);

	}


}
