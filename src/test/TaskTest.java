package test;

/**  
 * TaskTest.java 
 * A Junit4 test for the Task class
 * @author  Yeo Kheng Meng
 */ 



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import shared.Task;
import shared.Task.TaskType;

public class TaskTest {

	private static final String NAME_ONLY = "name only";
	private static final String NAME_TRUE = "name true";
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
	
	
	private static final int COMPARETO_SMALLER = -1;
	private static final int COMPARETO_EQUAL = 0;
	private static final int COMPARETO_BIGGER = 1;
	
	Task name;
	Task nameTrue;

	Task nameDeadline;
	Task nameDeadlineTrue;

	Task nameTimed;
	Task nameTimedFalse;

	ArrayList<Task> listing;

	
	@Before
	public void runBeforeEveryTest() {
		name = new Task(NAME_ONLY);
		nameTrue = new Task(NAME_TRUE, true);

		nameDeadline = new Task(NAME_1MONTH, DEADLINE);
		nameDeadlineTrue = new Task(NAME_1MONTH_TRUE, DEADLINE_TRUE, true);

		nameTimed = new Task(NAME_FROM_YESTERDAY_TO_TOMORROW, TIMED_START, TIMED_END);
		nameTimedFalse = new Task(NAME_FROM_NOW_TO_TOMORROW_0000_FALSE, TIMED_DONE_FALSE_START, TIMED_DONE_FALSE_END, false);
	
		listing = new ArrayList<Task>();
		
		listing.add(name);
		listing.add(nameDeadline);
		listing.add(nameTimedFalse);
		
		listing.add(nameTrue);
		listing.add(nameDeadlineTrue);
		
		listing.add(nameTimed);
		listing.add(name);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testTaskString() {
		new Task("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTaskStringBooleanString() {
		new Task(null, true);
	}

	@Test
	public void testTaskStringDateTime() {

		try{
			new Task("test", null);
			fail();
		} catch (IllegalArgumentException e) {
		}


		try{
			new Task("test", Task.INVALID_DATE_FIELD);
			fail();
		} catch (IllegalArgumentException e) {
		}

	}
	
	@Test
	public void testTaskStringDateTimeBoolean() {

		try{
			new Task("test", TIMED_DONE_FALSE_START, true);
		} catch (AssertionError e) {
			fail();
		}


		try{
			new Task("test", TIMED_DONE_FALSE_START, false);
		} catch (AssertionError e) {
			fail();
		}

	}


	@Test
	public void testTaskStringDateTimeDateTime() {
		try{
			new Task("test", new DateTime(), null);
		} catch (IllegalArgumentException e) {
		}

		try{
			new Task("test", null, new DateTime());
			fail();
		} catch (IllegalArgumentException e) {
		}

		try{
			new Task("test", new DateTime(), Task.INVALID_DATE_FIELD);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try{
			new Task("test", Task.INVALID_DATE_FIELD, new DateTime());
			fail();
		} catch (IllegalArgumentException e) {
		}


		try{
			new Task("test", null, null);
			fail();
		} catch (IllegalArgumentException e) {
		}

		
		try{
			new Task("test", Task.INVALID_DATE_FIELD, Task.INVALID_DATE_FIELD);
			fail();
		} catch (IllegalArgumentException e) {
		}


		try{
			new Task("test", TIMED_DONE_FALSE_START.plusHours(1), TIMED_DONE_FALSE_START.plusHours(1).minusSeconds(1));
			fail();
		} catch (IllegalArgumentException e) {
		}


		try{
			new Task("test", new DateTime(1000), new DateTime(1000));
		} catch (IllegalArgumentException e) {
			fail();
		}

	}
	
	@Test
	public void testTaskStringDateTimeDateTimeBoolean() {
		try{
			new Task("test", TIMED_DONE_FALSE_START, TIMED_DONE_FALSE_START, true);
		} catch (AssertionError e) {
			fail();
		}


		try{
			new Task("test", TIMED_DONE_FALSE_START, TIMED_DONE_FALSE_START, false);
		} catch (AssertionError e) {
			fail();
		}
	
	}


	@Test
	public void testTaskTask() {		
		Task toBeTakenOver;
		
		toBeTakenOver = new Task(name);
		assertEquals(toBeTakenOver.showInfo(), name.showInfo());
		assertEquals(toBeTakenOver.getSerial(), name.getSerial());
		
		toBeTakenOver = new Task(nameTrue);
		assertEquals(toBeTakenOver.showInfo(), nameTrue.showInfo());
		assertEquals(toBeTakenOver.getSerial(), nameTrue.getSerial());
		
		toBeTakenOver = new Task(nameDeadline);
		assertEquals(toBeTakenOver.showInfo(), nameDeadline.showInfo());
		assertEquals(toBeTakenOver.getSerial(), nameDeadline.getSerial());
		
		toBeTakenOver = new Task(nameDeadlineTrue);
		assertEquals(toBeTakenOver.showInfo(), nameDeadlineTrue.showInfo());
		assertEquals(toBeTakenOver.getSerial(), nameDeadlineTrue.getSerial());
		
		toBeTakenOver = new Task(nameTimed);
		assertEquals(toBeTakenOver.showInfo(), nameTimed.showInfo());
		assertEquals(toBeTakenOver.getSerial(), nameTimed.getSerial());
		
		toBeTakenOver = new Task(nameTimedFalse);
		assertEquals(toBeTakenOver.showInfo(), nameTimedFalse.showInfo());
		assertEquals(toBeTakenOver.getSerial(), nameTimedFalse.getSerial());
		


	}


	@Test
	public void testIsFloatingTask() {
		
		assertTrue(nameTrue.isFloatingTask());
		assertFalse(nameDeadlineTrue.isFloatingTask());
		assertFalse(nameTimedFalse.isFloatingTask());
	}

	@Test
	public void testIsTimedTask() {
		assertFalse(nameTrue.isTimedTask());
		assertFalse(nameDeadlineTrue.isTimedTask());
		assertTrue(nameTimedFalse.isTimedTask());
	}

	@Test
	public void testIsDeadlineTask() {
		assertFalse(nameTrue.isDeadlineTask());
		assertTrue(nameDeadlineTrue.isDeadlineTask());
		assertFalse(nameTimedFalse.isDeadlineTask());
	}

	@Test
	public void testGetSerial() {
		
		int start = name.getSerial();
		assertEquals(name.getSerial(), start + 0);
		assertEquals(nameTrue.getSerial(), start + 1);
		
		assertEquals(nameDeadline.getSerial(), start + 2);
		assertEquals(nameDeadlineTrue.getSerial(), start + 3);
		
		assertEquals(nameTimed.getSerial(), start + 4);
		assertEquals(nameTimedFalse.getSerial(), start + 5);
	}

	@Test
	public void testGetType() {

		assertEquals(nameTrue.getType(), Task.TaskType.FLOATING);
		assertEquals(nameDeadlineTrue.getType(), Task.TaskType.DEADLINE);
		assertEquals(nameTimedFalse.getType(), Task.TaskType.TIMED);
	}

	@Test
	public void testGetTaskName() {
		assertEquals(nameTrue.getTaskName(), NAME_TRUE);
		assertEquals(nameDeadlineTrue.getTaskName(), NAME_1MONTH_TRUE);
		assertEquals(nameTimedFalse.getTaskName(), NAME_FROM_NOW_TO_TOMORROW_0000_FALSE);
	}

	@Test
	public void testGetStartDate() {
		assertEquals(name.getStartDate(), Task.INVALID_DATE_FIELD);
		assertEquals(nameTrue.getStartDate(), Task.INVALID_DATE_FIELD);
		
		assertEquals(nameDeadline.getStartDate(), Task.INVALID_DATE_FIELD);
		assertEquals(nameDeadlineTrue.getStartDate(), Task.INVALID_DATE_FIELD);
		
		assertEquals(nameTimed.getStartDate(), TIMED_START);
		assertEquals(nameTimedFalse.getStartDate(), TIMED_DONE_FALSE_START);
	}

	@Test
	public void testGetEndDate() {
		assertEquals(name.getEndDate(), Task.INVALID_DATE_FIELD);
		assertEquals(nameTrue.getEndDate(), Task.INVALID_DATE_FIELD);
		
		assertEquals(nameDeadline.getEndDate(), Task.INVALID_DATE_FIELD);
		assertEquals(nameDeadlineTrue.getEndDate(), Task.INVALID_DATE_FIELD);
		
		assertEquals(nameTimed.getEndDate(), TIMED_END);
		assertEquals(nameTimedFalse.getEndDate(), TIMED_DONE_FALSE_END);
	}

	@Test
	public void testGetDeadline() {
		assertEquals(name.getDeadline(), Task.INVALID_DATE_FIELD);
		assertEquals(nameTrue.getDeadline(), Task.INVALID_DATE_FIELD);
		
		assertEquals(nameDeadline.getDeadline(), DEADLINE);
		assertEquals(nameDeadlineTrue.getDeadline(), DEADLINE_TRUE);
		
		assertEquals(nameTimed.getDeadline(), Task.INVALID_DATE_FIELD);
		assertEquals(nameTimedFalse.getDeadline(), Task.INVALID_DATE_FIELD);
	}
	
	@Test
	public void testChangeName() {
		try{
			name.changeName(null);
			fail();
		} catch (IllegalArgumentException e) {
		}

		
		try{
			name.changeName("");
			fail();
		} catch (IllegalArgumentException e) {
		}

		
		nameTimedFalse.changeName(NAME_ONLY);
		assertEquals(nameTimedFalse.getTaskName(), NAME_ONLY);
		
	}


	@Test
	public void testChangetoFloating() {
		try{
			name.changetoFloating();
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try{
			nameTrue.changetoFloating();
			fail();
		} catch (IllegalArgumentException e) {
		}

		
		nameDeadline.changeName(NAME_ONLY);
		nameDeadline.changetoFloating();
		
		assertEquals(name.showInfo(), nameDeadline.showInfo());

		
	}



	@Test
	public void testChangeStartAndEndTime() {
		try{
			nameTrue.changeStartAndEndDate(TIMED_DONE_FALSE_START, TIMED_DONE_FALSE_END);
			fail();
		} catch (IllegalArgumentException e) {
		}

		
		try{
			nameDeadlineTrue.changeStartAndEndDate(TIMED_DONE_FALSE_START, TIMED_DONE_FALSE_END);
			fail();
		} catch (IllegalArgumentException e) {
		}

		
		try{
			nameTimed.changeStartAndEndDate( TIMED_DONE_FALSE_END, TIMED_DONE_FALSE_START);
			fail();
		} catch (IllegalArgumentException e) {
		}

		
		try{
			nameTimed.changeStartAndEndDate(TIMED_DONE_FALSE_START, Task.INVALID_DATE_FIELD);
			fail();
		} catch (IllegalArgumentException e) {
		}

		
		try{
			nameTimed.changeStartAndEndDate(Task.INVALID_DATE_FIELD, Task.INVALID_DATE_FIELD);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		
		
		try{
			nameTimed.changeStartAndEndDate( TIMED_DONE_FALSE_END, TIMED_DONE_FALSE_END);
		} catch (IllegalArgumentException e) {
			fail();
		}
		
		
		
		nameTimed.changeStartAndEndDate(TIMED_DONE_FALSE_START, TIMED_DONE_FALSE_END);
		assertEquals(nameTimed.getStartDate(), TIMED_DONE_FALSE_START);
		assertEquals(nameTimed.getEndDate(), TIMED_DONE_FALSE_END);
		
	}
	@Test
	public void testchangetoDeadline() {
		try{
			nameDeadline.changetoDeadline(DEADLINE);
			fail();
		} catch (IllegalArgumentException e) {
		}	
	}
	
	@Test
	public void testChangeStartDateAndEndDate() {
		try{
			nameTimed.changeStartAndEndDate(null, TIMED_DONE_FALSE_END);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try{
			nameTimed.changeStartAndEndDate(TIMED_DONE_FALSE_END, null);
			fail();
		} catch (IllegalArgumentException e) {
		}	
	}

	@Test
	public void testChangeDeadline() {
		try{
			nameTrue.changeDeadline(DEADLINE);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try{
			nameTimedFalse.changeDeadline(DEADLINE);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try{
			nameDeadline.changeDeadline(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try{
			nameDeadline.changeDeadline(Task.INVALID_DATE_FIELD);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		nameDeadline.changeDeadline(DEADLINE_TRUE);
		assertEquals(nameDeadline.getDeadline(), DEADLINE_TRUE);
		
		
	}

	@Test
	public void testCompareTo() {
		try{
			name.compareTo(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		
		
		assertEquals(name.compareTo(nameTrue), COMPARETO_EQUAL);
		
		assertEquals(nameTrue.compareTo(nameDeadline), COMPARETO_BIGGER);
		
		assertEquals(nameTimedFalse.compareTo(name), COMPARETO_SMALLER);
		
		assertEquals(nameDeadlineTrue.compareTo(nameDeadline), nameDeadlineTrue.getDeadline().compareTo(nameDeadline.getDeadline()));
		
		assertEquals(nameTimed.compareTo(nameDeadlineTrue), nameTimed.getStartDate().compareTo(nameDeadlineTrue.getDeadline()));
		
		assertEquals(nameDeadlineTrue.compareTo(nameTimed), nameDeadlineTrue.getDeadline().compareTo(nameTimed.getEndDate()));
		
	}

	@Test
	public void testSearchName() {
		try{
			name.containsTerm(null);
			fail();
		} catch (IllegalArgumentException e) {
		}

		
		try{
			name.containsTerm("");
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		assertTrue(nameDeadline.containsTerm("name"));
		assertFalse(nameDeadline.containsTerm("rubbish"));
	}

	@Test
	public void testIsWithinDateRange() {
		try{
			nameTimed.isWithinDateRange(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		}


		try{
			nameTimedFalse.isWithinDateRange(Task.INVALID_DATE_FIELD, null);
		} catch (IllegalArgumentException e) {
		}

		try{
			nameTimed.isWithinDateRange(null, Task.INVALID_DATE_FIELD);
			fail();
		} catch (IllegalArgumentException e) {
		}

		try{
			nameTimedFalse.isWithinDateRange(Task.INVALID_DATE_FIELD, Task.INVALID_DATE_FIELD);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try{
			nameTimed.isWithinDateRange(TIMED_START, null);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try{
			nameTimed.isWithinDateRange(null, TIMED_END);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try{
			nameTimed.isWithinDateRange(TIMED_START, Task.INVALID_DATE_FIELD);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try{
			nameTimed.isWithinDateRange(TIMED_END, TIMED_START);
			fail();
		} catch (IllegalArgumentException e) {
		}

		
		
		assertFalse(nameTrue.isWithinDateRange(TIMED_START, TIMED_END));
		assertTrue(nameDeadline.isWithinDateRange(DEADLINE, DEADLINE.plus(1)));
		assertTrue(nameDeadlineTrue.isWithinDateRange(DEADLINE_TRUE.minus(1), DEADLINE_TRUE));
		
		assertTrue(nameTimed.isWithinDateRange(TIMED_START.minus(1), TIMED_END.plus(1)));
		assertFalse(nameTimed.isWithinDateRange(TIMED_START.minus(100), TIMED_START.minus(90)));
		assertFalse(nameTimedFalse.isWithinDateRange(TIMED_DONE_FALSE_START.plus(1), TIMED_DONE_FALSE_END.minus(1)));
	
	}
	
	

	
	@Test
	public void testClashesWithRange(){
		//Use Task name
		//Use Task nameDeadline + 1month
		//Use Task nameTimed minus 1day to plus 1 day
		
		DateTime start = new DateTime().minusMonths(1);
		DateTime end = new DateTime().minusMonths(1).plusMinutes(1);
		
		assertFalse(name.clashesWithRange(start, end));
		assertTrue(name.clashesWithRange(start, end));
		
		//TODO:
		
		
	}
	
	@Test
	public void testComparatorType() {
		Collections.sort(listing, new Task.SortByType());

		boolean fail = false;
		
		//In order of deadline, timed and floating
		TaskType previous = TaskType.DEADLINE;
		for(Task comp : listing) {
			if(previous.equals(TaskType.TIMED) && comp.getType().equals(TaskType.DEADLINE)) {
				fail = true;
			}
			
			if(previous.equals(TaskType.FLOATING) && comp.getType().equals(TaskType.TIMED)) {
				fail = true;
			}
			
			if(previous.equals(TaskType.FLOATING) && comp.getType().equals(TaskType.DEADLINE)) {
				fail = true;
			}
			
			previous = comp.getType();
		}
		assertFalse(fail);

	}
	
	@Test
	public void testComparatorDone() {
		Collections.sort(listing, new Task.SortByDone());

		boolean fail = false;
		
		//Undone tasks come first
		boolean previous = false;
		for(Task comp : listing) {
			if(previous && (comp.isDone() == false)) {
				fail = true;
			}
			
			previous = comp.isDone();
		}
		assertFalse(fail);

	}
	
	@Test
	public void testComparatorStart() {
		Collections.sort(listing, new Task.SortByStartDate());

		boolean fail = false;
		
		
		DateTime previous = new DateTime(Long.MIN_VALUE);
		for(Task comp : listing) {
			DateTime currentComp;
			
			if(comp.isDeadlineTask()) {
				currentComp = comp.getDeadline();
			} else if( comp.isTimedTask()) {
				currentComp = comp.getStartDate();
			} else {
				currentComp = Task.INVALID_DATE_FIELD;
			}
			
			if(previous.isAfter(currentComp)) {
				fail = true;
			}
			
			previous = currentComp;
			
		}
		assertFalse(fail);

	}
	
	@Test
	public void testComparatorEnd() {
		Collections.sort(listing, new Task.SortByEndDate());

		boolean fail = false;
		
		
		DateTime previous = new DateTime(Long.MIN_VALUE);
		for(Task comp : listing) {
			DateTime currentComp;
			
			if(comp.isDeadlineTask()) {
				currentComp = comp.getDeadline();
			} else if( comp.isTimedTask()) {
				currentComp = comp.getEndDate();
			} else {
				currentComp = Task.INVALID_DATE_FIELD;
			}
			
			if(previous.isAfter(currentComp)) {
				fail = true;
			}
			
			previous = currentComp;
			
		}
		assertFalse(fail);

	}
	
	@Test
	public void testComparatorName() {
		Collections.sort(listing, new Task.SortByName());

		boolean fail = false;
		
		//Undone tasks come first
		String previous = " ";
		for(Task comp : listing) {
			String compName = comp.getTaskName();
			if(previous.compareTo(compName) == COMPARETO_BIGGER) {
				fail = true;
			}
			
			previous = compName;
		}
		assertFalse(fail);

	}


}