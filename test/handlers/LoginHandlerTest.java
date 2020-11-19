package handlers;

import exceptions.DataAccessException;
import data_access.Database;
import data_access.UserDao;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request_result.LoginRequest;
import request_result.LoginResult;
import services.LoginService;

import java.sql.Connection;

class LoginHandlerTest {
    private LoginRequest request;
    private LoginRequest falseRequest;
    private LoginRequest falsePasswordRequest;
    private LoginService service;
    private LoginResult result;
    private Database db;
    UserDao uDao;
    User user;
    Connection conn;

    @BeforeEach
    void setUp() throws DataAccessException {
        db = new Database();
        conn = db.openConnection();
        db.clearTables();

        user = new User("Jack2134", "Mamba123", "4321", "greg@gmail.com",
                "Jack", "Adams", "m");
        request = new LoginRequest("Mamba123", "4321");
        falseRequest = new LoginRequest("Jake123", "4532");
        falsePasswordRequest = new LoginRequest("Mamba123", "2813");
        service = new LoginService();
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
    private void handlePass() {

    }
}