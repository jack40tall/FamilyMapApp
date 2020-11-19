package services;

import data_access.AuthTokenDao;
import data_access.Database;
import data_access.EventDao;
import data_access.UserDao;
import exceptions.DataAccessException;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request_result.*;

import java.io.IOException;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class AllEventServiceTest {

    private AllEventRequest request;
    private AllEventRequest failRequest;
    private AuthToken authToken;
    private AuthTokenDao aDao;
    private Event firstEvent;
    private Event secondEvent;
    private Event thirdEvent;
    private AllEvents allEvents;
    private AllEventService service;
    private AllEventResult result;
    private AllEventResult failResult;
    private Database db;
    UserDao uDao;
    EventDao eDao;
    User user;
    Connection conn;

    @BeforeEach
    void setUp() throws DataAccessException, IOException {
        db = new Database();
        conn = db.openConnection();
        db.clearTables();

        authToken = new AuthToken("Jack2134", "abcdef");
        aDao = new AuthTokenDao(conn);
        aDao.insert(authToken);

        user = new User("Jack2134", "Mamba123", "4321", "greg@gmail.com",
                "Jack", "Adams", "m");
        firstEvent = new Event("Biking_123A", "Mamba123", "Jack2134",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        secondEvent = new Event("Flipping_123A", "Mamba123", "Jack2134",
                10.5f, 10.9f, "China", "Bangkok",
                "Flipping_Around", 2004);
        thirdEvent = new Event("Booya", "Billy", "Greg23",
                10.5f, 10.9f, "China", "Bangkok",
                "Flipping_Around", 2004);
        request = new AllEventRequest("abcdef");
        failRequest = new AllEventRequest("false_authToken");
        failResult = new AllEventResult("Error: Invalid AuthToken", false);
        allEvents = new AllEvents();
        allEvents.addEvent(firstEvent);
        allEvents.addEvent(secondEvent);
        eDao = new EventDao(conn);
        eDao.insertEvent(firstEvent);
        eDao.insertEvent(secondEvent);
        eDao.insertEvent(thirdEvent);
        service = new AllEventService();
        uDao = new UserDao(conn);
        uDao.insert(user);
        db.closeConnection(true);
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        db = new Database();
        conn = db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    void getAllEventsPass() throws DataAccessException {
        result = service.getAllEvents(request);
        assertArrayEquals(allEvents.getArray(), result.getData());
    }

    @Test
    void getAllEventsFail() throws DataAccessException {
        result = service.getAllEvents(failRequest);
        assertEquals(failResult, result);
    }
}