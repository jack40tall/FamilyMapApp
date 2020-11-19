package services;

import data_access.*;
import exceptions.DataAccessException;
import model.AuthToken;
import model.Person;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request_result.PersonRequest;
import request_result.PersonResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {

    private PersonRequest request;
    private PersonRequest failRequest;
    private PersonRequest authTokenFailRequest;
    private PersonResult authTokenFailResult;
    private PersonService service;
    private PersonResult result;
    private PersonResult passResult;
    private PersonResult failResult;
    private Database db;
    UserDao uDao;
    AuthTokenDao aDao;
    PersonDao pDao;
    AuthToken authToken;
    User user;
    Person person;
    Person failPerson;
    Connection conn;

    @BeforeEach
    void setUp() throws DataAccessException {
        db = new Database();
//        db.closeConnection(false);
        conn = db.openConnection();
        db.clearTables();

        user = new User("Jack2134", "Mamba123", "4321", "greg@gmail.com",
                "Jack", "Adams", "m");
        person = new Person("Stacy123", "Mamba123", "Stacy", "Willis", "f",
                "Greg13", "Amy34", "Bobby9");
        failPerson = new Person("bill34", "not_logged_in", "Stacy", "Willis", "f",
                "Greg13", "Amy34", "Bobby9");
        authToken = new AuthToken("Jack2134", "abcdef");
        passResult = new PersonResult("Mamba123", "Stacy123", "Stacy", "Willis",
                "f", "Greg13", "Amy34", "Bobby9", true);
        failResult = new PersonResult("Error: Requested person does not belong to this user", false);

        authTokenFailResult = new PersonResult("Error: Invalid AuthToken", false);

        request = new PersonRequest("Stacy123", "abcdef");
        failRequest = new PersonRequest("bill34", "abcdef");
        authTokenFailRequest = new PersonRequest("Stacy123", "abcde");
//        falsePasswordRequest = new LoginRequest("Mamba123", "2813");
        service = new PersonService();
        uDao = new UserDao(conn);
        pDao = new PersonDao(conn);
        aDao = new AuthTokenDao(conn);
        uDao.insert(user);
        pDao.insert(person);
        pDao.insert(failPerson);
        aDao.insert(authToken);
        db.closeConnection(true);
    }


    @Test
    void getPersonPass() throws DataAccessException {
        result = service.getPerson(request);
        assertEquals(result, passResult);
    }

    @Test
    void getPersonFail() throws DataAccessException {    // Tests when person being accessed is under a different user
        result = service.getPerson(failRequest);
        assertEquals(result, failResult);
    }

    @Test
    void getPersonAuthTokenFail() throws DataAccessException {
        result = service.getPerson(authTokenFailRequest);
        assertEquals(result, authTokenFailResult);
    }
}