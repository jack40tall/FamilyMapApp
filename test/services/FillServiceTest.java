package services;

import exceptions.DataAccessException;
import data_access.Database;
import data_access.UserDao;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request_result.FillRequest;
import request_result.FillResult;
import request_result.LoginRequest;
import server.Server;

import java.io.IOException;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FillServiceTest {

    private FillRequest request;
    private FillRequest failGenRequest;
    private LoginRequest falseRequest;
    private LoginRequest falsePasswordRequest;
    private FillService service;
    private FillResult result;
    private FillResult passResult;
    private FillResult failResult;
    private Database db;
    UserDao uDao;
    User user;
    Connection conn;
    Server server;

    @BeforeEach
    void setUp() throws DataAccessException, IOException {
        db = new Database();
        conn = db.openConnection();
        db.clearTables();

        user = new User("Jack2134", "Mamba123", "4321", "greg@gmail.com",
                "Jack", "Adams", "m");
        request = new FillRequest(4, "Mamba123");
        failGenRequest = new FillRequest(-1, "invalid_username");
        service = new FillService();
        passResult = new FillResult("Successfully added 31 persons and 91 events to the database.", true);
        failResult = new FillResult("Error: User not found in database", false);
        uDao = new UserDao(conn);
        uDao.insert(user);
        db.closeConnection(true);
        server = new Server();
        server.LoadJsonFiles();
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        db = new Database();
        conn = db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    void fillPass() throws DataAccessException, IOException {
        result = service.fill(request);
        assertEquals(passResult, result);
    }

    @Test
    void fillFail() throws DataAccessException, IOException {
        result = service.fill(failGenRequest);
        assertEquals(failResult, result);
    }
}