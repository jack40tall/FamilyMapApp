package services;

import data_access.*;
import exceptions.DataAccessException;
import exceptions.InvalidAuthTokenException;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request_result.ClearResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {
    Database db;
    Connection conn;
    private User user;
    private Event firstEvent;
    private Event secondEvent;
    private Event thirdEvent;
    private Person person;
    private AuthToken authToken;
    private AuthTokenDao aDao;
    private EventDao eDao;
    private PersonDao pDao;
    private UserDao uDao;
    private ClearService service;
    private ClearResult result;
    private ClearResult passResult;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        conn = db.openConnection();
        db.clearTables();

        aDao = new AuthTokenDao(conn);
        pDao = new PersonDao(conn);
        eDao = new EventDao(conn);
        uDao = new UserDao(conn);

        authToken = new AuthToken("Jack2134", "abcdef");
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
        person = new Person("Stacy123", "hikegirl24", "Stacy", "Willis",
                "f", "Greg13", "Amy34", "Bobby9");

        passResult = new ClearResult("Clear succeeded", true);

        aDao.insert(authToken);
        uDao.insert(user);
        eDao.insertEvent(firstEvent);
        eDao.insertEvent(secondEvent);
        eDao.insertEvent(thirdEvent);
        pDao.insert(person);
        db.closeConnection(true);

        service = new ClearService();
    }

    @Test
    void clearPass() throws DataAccessException, InvalidAuthTokenException {
        result = service.clear();
        conn = db.openConnection();
        pDao = new PersonDao(conn);
        eDao = new EventDao(conn);
        uDao = new UserDao(conn);
        aDao = new AuthTokenDao(conn);
        assertNull(pDao.find(person.getPersonID()));
        assertNull(eDao.find(firstEvent.getEventID()));
        assertNull(uDao.find(user.getUsername()));
        assertThrows(InvalidAuthTokenException.class, ()-> aDao.find(authToken.getAuthorizationNum()));
        assertEquals(passResult, result);
        db.closeConnection(true);

    }
    @Test
    void twiceClearPass() throws DataAccessException {
        result = service.clear();
        result = service.clear();
        conn = db.openConnection();
        pDao = new PersonDao(conn);
        eDao = new EventDao(conn);
        uDao = new UserDao(conn);
        aDao = new AuthTokenDao(conn);
        assertNull(pDao.find(person.getPersonID()));
        assertNull(eDao.find(firstEvent.getEventID()));
        assertNull(uDao.find(user.getUsername()));
        assertThrows(InvalidAuthTokenException.class, ()-> aDao.find(authToken.getAuthorizationNum()));
        assertEquals(passResult, result);
        db.closeConnection(true);
    }
}