package services;

import data_access.AuthTokenDao;
import data_access.Database;
import data_access.PersonDao;
import data_access.UserDao;
import exceptions.DataAccessException;
import model.AuthToken;
import model.MyTree;
import model.Person;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request_result.PersonTreeRequest;
import request_result.PersonTreeResult;
import request_result.RegisterRequest;
import request_result.RegisterResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {
    Database db;
    Connection conn;
    private RegisterResult failResult;
    private RegisterResult result;
    private RegisterRequest request;
    private RegisterRequest failRequest;
    private RegisterService service;

    @BeforeEach
    void setUp() throws DataAccessException {
        db = new Database();
        conn = db.openConnection();
        db.clearTables();
        db.closeConnection(true);

        request = new RegisterRequest("bill123", "123", "bill@gmail.com", "bill", "smith", "m");
        failRequest = new RegisterRequest("bill123", "435", "greg@gmail.com", "jeff", "denum", "m");
        failResult = new RegisterResult("Error: Username already taken", false);
        service = new RegisterService();
    }
    @Test
    void registerPass() throws DataAccessException {
        result = service.register(request);
        assertEquals(result.getUserName(), "bill123");
        assertNotNull(result.getAuthToken());
        assertNotNull(result.getPersonID());
    }

    @Test
    void registerFail() throws DataAccessException {
        result = service.register(request);
        assertEquals(result.getUserName(), "bill123");
        assertNotNull(result.getAuthToken());
        assertNotNull(result.getPersonID());
        result = service.register(failRequest);
        assertEquals(failResult, result);
    }
}