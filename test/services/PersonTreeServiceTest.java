package services;

import data_access.*;
import exceptions.DataAccessException;
import exceptions.InvalidAuthTokenException;
import model.AuthToken;
import model.MyTree;
import model.Person;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request_result.PersonRequest;
import request_result.PersonResult;
import request_result.PersonTreeRequest;
import request_result.PersonTreeResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class PersonTreeServiceTest {

    private MyTree tree;
    private PersonTreeRequest request;
    private PersonTreeRequest failRequest;
    private PersonTreeService service;
    private PersonTreeResult result;
    private PersonTreeResult passResult;
    private PersonTreeResult authTokenFailResult;
    private Database db;
    UserDao uDao;
    AuthTokenDao aDao;
    PersonDao pDao;
    AuthToken authToken;
    User user;
    Person person;
    Person firstTreePerson;
    Person secondTreePerson;
    Person thirdTreePerson;
    Person fourthTreePerson;
    Connection conn;

    @BeforeEach
    void setUp() throws DataAccessException {
        db = new Database();
//        db.closeConnection(false);
        conn = db.openConnection();
        db.clearTables();

        user = new User("Stacy123", "Mamba123", "4321", "greg@gmail.com",
                "Jack", "Adams", "m");
        person = new Person("Stacy123", "Mamba123", "Stacy", "Willis", "f",
                "Greg13", "Amy34", "Bobby9");
        firstTreePerson = new Person("23423423", "Mamba123", "Greg", "Daniels",
                "m", "Greg13", "Amy34", "Bobby9");
        secondTreePerson = new Person("43535435", "Mamba123", "Matt", "Peterson",
                "m", "Greg13", "Amy34", "Bobby9");
        thirdTreePerson = new Person("23423423423", "Mamba123", "Janet", "Jackson",
                "m", "Greg13", "Amy34", "Bobby9");
        fourthTreePerson = new Person("345233252", "Mamba123", "Bobby", "Jones",
                "m", "Greg13", "Amy34", "Bobby9");
        authToken = new AuthToken("Stacy123", "abcdef");

        authTokenFailResult = new PersonTreeResult("Error: Invalid AuthToken", false);

        request = new PersonTreeRequest("abcdef");
        failRequest = new PersonTreeRequest("invalid_authToken");
        service = new PersonTreeService();
        uDao = new UserDao(conn);
        pDao = new PersonDao(conn);
        aDao = new AuthTokenDao(conn);
        uDao.insert(user);
        pDao.insert(person);
        pDao.insert(firstTreePerson);
        pDao.insert(secondTreePerson);
        pDao.insert(thirdTreePerson);
        pDao.insert(fourthTreePerson);
        aDao.insert(authToken);
        db.closeConnection(true);
        tree = new MyTree(person);
        tree.addFamilyMember(person);
        tree.addFamilyMember(firstTreePerson);
        tree.addFamilyMember(secondTreePerson);
        tree.addFamilyMember(thirdTreePerson);
        tree.addFamilyMember(fourthTreePerson);
    }

    @Test
    void getPersonTreePass() throws DataAccessException {
        passResult = new PersonTreeResult(tree.getTreeArray(), true);

        result = service.getPersonTree(request);
        assertEquals(result, passResult);

    }
    @Test
    void getPersonTreeFail() throws DataAccessException {
        result = service.getPersonTree(failRequest);
        assertEquals(authTokenFailResult, result);
    }

}