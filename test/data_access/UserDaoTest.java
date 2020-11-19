package data_access;

import exceptions.DataAccessException;
import data_access.Database;
import data_access.UserDao;
import exceptions.IncorrectCredentialsException;
import exceptions.InvalidAuthTokenException;
import model.AuthToken;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
    private Database db;
    private User firstUser;
    private User secondUser;
    Connection conn;
    UserDao uDao;


    @BeforeEach
    void setUp() throws DataAccessException {
        db = new Database();
        firstUser = new User("Jeff123", "blackmamba2", "123456", "jman23@gmail.com", "Jeff", "Bezos", "m");
        secondUser = new User("Maria123", "greenmamba2", "654321", "mwoman23@gmail.com", "Maria", "Stoogus", "f");
        conn = db.openConnection();
        db.clearTables();
        uDao = new UserDao(conn);
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    void insertPass() throws DataAccessException {
        uDao.insert(firstUser);
        User compareUser = uDao.find(firstUser.getUsername());
        assertNotNull(compareUser);
        assertEquals(firstUser, compareUser);
    }

    @Test
    void insertFail() throws DataAccessException{
        uDao.insert(firstUser);
        assertThrows(DataAccessException.class, ()-> uDao.insert(firstUser));
    }

    @Test
    void findPass() throws DataAccessException{
        uDao.insert(firstUser);
        uDao.insert(secondUser);
        User foundUser = uDao.find(firstUser.getUsername());
        User foundUser_2 = uDao.find(secondUser.getUsername());
        assertNotNull(foundUser);
        assertNotNull(foundUser_2);
        assertEquals(firstUser,foundUser);
        assertEquals(secondUser,foundUser_2);
    }

    @Test
    void findFail() throws DataAccessException{
        uDao.insert(firstUser);
        User foundUser = uDao.find(secondUser.getUsername());
        assertNull(foundUser);
    }

    @Test
    void loginPass() throws DataAccessException, IncorrectCredentialsException {
        uDao.insert(firstUser);
        User loginUser = uDao.loginUser(firstUser.getUsername(), firstUser.getPassword());
        assertNotNull(loginUser);
        assertEquals(loginUser, firstUser);
    }

    @Test
    void loginFail() throws DataAccessException {
        uDao.insert(firstUser);
        assertThrows(IncorrectCredentialsException.class, () -> uDao.loginUser(secondUser.getUsername(),
                secondUser.getPassword()));
    }

    @Test
    void clearTest() throws DataAccessException{
        uDao.insert(firstUser);
        uDao.insert(secondUser);
//        find == true
        User firstUserTest = uDao.find(firstUser.getUsername());
        User secondUserTest = uDao.find(secondUser.getUsername());
        assertEquals(firstUser, firstUserTest);
        assertEquals(secondUser, secondUserTest);
        uDao.clear();
        User firstTestNull = uDao.find(firstUser.getUsername());
        User secondTestNull = uDao.find(secondUser.getUsername());
        assertNull(firstTestNull);
        assertNull(secondTestNull);
    }

    @Test
    void getUsernamePass() throws DataAccessException, InvalidAuthTokenException {
        uDao.insert(firstUser);
        String personID = "Jeff123";
        String username = uDao.getUsername(personID);
        assertEquals("blackmamba2", username);
    }

    @Test
    void getUsernameFail() throws DataAccessException, InvalidAuthTokenException {
        String personID = "Jeff123";
        assertThrows(InvalidAuthTokenException.class, ()-> uDao.getUsername(personID));

    }
}