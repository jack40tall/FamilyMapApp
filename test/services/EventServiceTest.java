package services;

import data_access.AuthTokenDao;
import data_access.Database;
import data_access.EventDao;
import data_access.UserDao;
import exceptions.DataAccessException;
import exceptions.InvalidAuthTokenException;
import model.AllEvents;
import model.AuthToken;
import model.Event;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request_result.EventRequest;
import request_result.EventResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceTest {
    private Database db;
    private Event bestEvent;
    private Event secondEvent;
    private Event thirdEvent;
    private EventService service;
    private EventResult result;
    private EventResult passResult;
    private EventResult failResult;
    private EventRequest request;
    private EventRequest failRequest;
    private AuthToken authToken;
    private AuthTokenDao aDao;
    private User user;
    private UserDao userDao;
    Connection conn;
    EventDao eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        user = new User("James123", "Gale", "4321", "greg@gmail.com",
                "Jack", "Adams", "m");
        authToken = new AuthToken("James123", "abcdef");
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        secondEvent = new Event("Flipping_123A", "Greg", "Greg23",
                10.5f, 10.9f, "China", "Bangkok",
                "Flipping_Around", 2004);
        thirdEvent = new Event("Booya", "Gale", "Greg23",
                10.5f, 10.9f, "China", "Bangkok",
                "Flipping_Around", 2004);
        conn = db.openConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the EventDAO so it can access the database
        eDao = new EventDao(conn);
        aDao = new AuthTokenDao(conn);
        userDao = new UserDao(conn);
        userDao.insert(user);
        aDao.insert(authToken);
        eDao.insertEvent(bestEvent);
        eDao.insertEvent(secondEvent);
        service = new EventService();
        request = new EventRequest(bestEvent.getEventID(),"abcdef");
        failRequest = new EventRequest(secondEvent.getEventID(), "abcdef");
        passResult = new EventResult(bestEvent);
        failResult = new EventResult("Error: Requested person does not belong to this user", false);
        db.closeConnection(true);
    }

//    @AfterEach
//    public void tearDown() throws DataAccessException {
//        //Here we close the connection to the database file so it can be opened elsewhere.
//        //We will leave commit to false because we have no need to save the changes to the database
//        //between test cases
//        db.closeConnection(true);
//    }

    @Test
    void getEventPass() throws DataAccessException {
        result = service.getEvent(request);
        assertNotNull(result);
        assertEquals(passResult, result);
    }

    @Test
    void getEventFail() throws DataAccessException {
        result = service.getEvent(failRequest);
        assertEquals(failResult, result);
    }
}