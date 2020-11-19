package data_access;

import exceptions.DataAccessException;
import data_access.Database;
import data_access.EventDao;
import model.AllEvents;
import model.Event;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class EventDAOTest {
    private Database db;
    private Event bestEvent;
    private Event secondEvent;
    private Event thirdEvent;
    private AllEvents passFoundEvents;
    Connection conn;
    EventDao eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        passFoundEvents = new AllEvents();
        //and a new event with random data
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        secondEvent = new Event("Flipping_123A", "Greg", "Greg23",
                10.5f, 10.9f, "China", "Bangkok",
                "Flipping_Around", 2004);
        thirdEvent = new Event("Booya", "Gale", "Greg23",
                10.5f, 10.9f, "China", "Bangkok",
                "Flipping_Around", 2004);
        passFoundEvents.addEvent(bestEvent);
        passFoundEvents.addEvent(thirdEvent);
        //Here, we'll open the connection in preparation for the test case to use it
        conn = db.openConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the EventDAO so it can access the database
        eDao = new EventDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        eDao.insertEvent(bestEvent);
        //So lets use a find method to get the event that we just put in back out
        Event compareTest = eDao.find(bestEvent.getEventID());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        eDao.insertEvent(bestEvent);
        //but our sql table is set up so that "eventID" must be unique. So trying to insert it
        //again will cause the method to throw an exception
        //Note: This call uses a lambda function. What a lambda function is is beyond the scope
        //of this class. All you need to know is that this line of code runs the code that
        //comes after the "()->" and expects it to throw an instance of the class in the first parameter.
        assertThrows(DataAccessException.class, ()-> eDao.insertEvent(bestEvent));
    }

    @Test
    public void findPass() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        eDao.insertEvent(secondEvent);
        Event foundEvent = eDao.find(bestEvent.getEventID());
        Event foundEvent_2 = eDao.find(secondEvent.getEventID());
        assertNotNull(foundEvent);
        assertNotNull(foundEvent_2);
        assertEquals(bestEvent,foundEvent);
        assertEquals(secondEvent,foundEvent_2);
    }

    @Test
    public void findFail() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        Event foundEvent = eDao.find(secondEvent.getEventID());
        assertNull(foundEvent);
    }

    @Test
    public void clearTest() throws DataAccessException {
        //    TODO:Is there an easy way to see if the database table is empty? Also can I view the contents through
        //     DB browser?
        eDao.insertEvent(bestEvent);
        eDao.insertEvent(secondEvent);
//        find == true
        Event bestEventTest = eDao.find(bestEvent.getEventID());
        Event secondEventTest = eDao.find(secondEvent.getEventID());
        assertEquals(bestEvent, bestEventTest);
        assertEquals(secondEvent, secondEventTest);
        eDao.clear();
        Event bestTestNull = eDao.find(bestEvent.getEventID());
        Event secondTestNull = eDao.find(secondEvent.getEventID());
        assertNull(bestTestNull);
        assertNull(secondTestNull);
//        find == false

    }

    @Test
    public void removePass() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        Event foundEvent = eDao.find(bestEvent.getEventID());
        assertNotNull(foundEvent);

        eDao.remove(bestEvent.getUsername());
        assertNull(eDao.find(bestEvent.getEventID()));

    }

    @Test
    public void removeTwoPass() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        eDao.insertEvent(secondEvent);
        eDao.insertEvent(thirdEvent);
        Event foundEvent = eDao.find(bestEvent.getEventID());
        assertNotNull(foundEvent);
        foundEvent = eDao.find(secondEvent.getEventID());
        assertNotNull(foundEvent);
        foundEvent = eDao.find(thirdEvent.getEventID());
        assertNotNull(foundEvent);

        eDao.remove(bestEvent.getUsername());
        assertNull(eDao.find(bestEvent.getEventID()));
        assertNull(eDao.find(thirdEvent.getEventID()));
        assertNotNull(eDao.find(secondEvent.getEventID()));
    }

    @Test
    public void findAllPass() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        eDao.insertEvent(secondEvent);
        eDao.insertEvent(thirdEvent);
        AllEvents allFoundEvents = eDao.findAll(bestEvent.getUsername());
        assertEquals(passFoundEvents, allFoundEvents);
    }
    @Test
    public void findAllFail() throws DataAccessException {
        AllEvents noFoundEvents = eDao.findAll(bestEvent.getUsername());
        assertEquals(noFoundEvents.getArray().length, 0);
    }
}
