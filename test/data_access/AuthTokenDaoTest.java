package data_access;

import data_access.AuthTokenDao;
import exceptions.DataAccessException;
import data_access.Database;
import exceptions.InvalidAuthTokenException;
import model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class AuthTokenDaoTest {

    private Database db;
    private AuthToken firstToken;
    private AuthToken secondToken;
    Connection conn;
    AuthTokenDao aDao;
    AuthToken returnedToken;
    AuthToken passToken;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
//        db.closeConnection(false);
        firstToken = new AuthToken("Stacy123", "sdfsfsf");
        secondToken = new AuthToken("Bob123", "32423424");
        conn = db.openConnection();
        db.clearTables();
        aDao = new AuthTokenDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    void checkPass() throws DataAccessException {
        aDao.insert(firstToken);
        assertTrue(aDao.check(firstToken));
    }


    @Test
    void checkFail() throws DataAccessException {
        aDao.insert(firstToken);
        assertFalse(aDao.check(secondToken));
    }

    @Test
    void insertPass() throws DataAccessException, InvalidAuthTokenException {
        aDao.insert(firstToken);
        AuthToken compareToken = aDao.find(firstToken.getAuthorizationNum());
        assertNotNull(compareToken);
        assertEquals(firstToken, compareToken);
    }

    @Test
    void insertFail() throws DataAccessException {
        aDao.insert(firstToken);
        assertThrows(DataAccessException.class, ()-> aDao.insert(firstToken));
    }

    @Test
    void removePass() throws DataAccessException, InvalidAuthTokenException {
        aDao.insert(firstToken);
        AuthToken compareToken = aDao.find(firstToken.getAuthorizationNum());
        assertNotNull(compareToken);
        assertEquals(firstToken, compareToken);

        aDao.remove(firstToken);
        assertThrows(InvalidAuthTokenException.class, ()->aDao.find(firstToken.getAuthorizationNum()));

    }

    @Test
    void removeFail() throws DataAccessException, InvalidAuthTokenException {
        aDao.insert(firstToken);
        AuthToken compareToken = aDao.find(firstToken.getAuthorizationNum());
        assertNotNull(compareToken);
        assertEquals(firstToken, compareToken);

        aDao.remove(firstToken);
        aDao.remove(firstToken);
        assertThrows(InvalidAuthTokenException.class, ()->aDao.find(firstToken.getAuthorizationNum()));


    }

    @Test
    void findPass() throws DataAccessException, InvalidAuthTokenException {
        aDao.insert(firstToken);
        String currToken = "sdfsfsf";
        returnedToken = aDao.find(currToken);
        assertNotNull(returnedToken);
        assertEquals(firstToken, returnedToken);
    }
    @Test
    void findFail() throws DataAccessException {
        aDao.insert(firstToken);
        String currToken = "not_valid";
        assertThrows(InvalidAuthTokenException.class, ()-> aDao.find(currToken));

    }
}