package data_access;

import exceptions.DataAccessException;
import data_access.Database;
import data_access.PersonDao;
import exceptions.IncorrectUserException;
import model.AllEvents;
import model.Event;
import model.MyTree;
import model.Person;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class PersonDaoTest {
    private Database db;
    private Person firstPerson;
    private Person secondPerson;
    private Person firstTreePerson;
    private Person secondTreePerson;
    private Person thirdTreePerson;
    private Person fourthTreePerson;
    private MyTree passTree;
    Connection conn;
    PersonDao pDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        firstPerson = new Person("Stacy123", "hikegirl24", "Stacy", "Willis",
                "f", "Greg13", "Amy34", "Bobby9");
        secondPerson = new Person("Bill123", "fleabot", "Bill", "Jeffries",
                "m", "Jim2", "Stacy3", null);
        firstTreePerson = new Person("23423423", "hikegirl24", "Greg", "Daniels",
                "m", "Greg13", "Amy34", "Bobby9");
        secondTreePerson = new Person("43535435", "hikegirl24", "Matt", "Peterson",
                "m", "Greg13", "Amy34", "Bobby9");
        thirdTreePerson = new Person("23423423423", "hikegirl24", "Janet", "Jackson",
                "m", "Greg13", "Amy34", "Bobby9");
        fourthTreePerson = new Person("345233252", "hikegirl24", "Bobby", "Jones",
                "m", "Greg13", "Amy34", "Bobby9");
        passTree = new MyTree(firstPerson);
        passTree.addFamilyMember(firstPerson);
        passTree.addFamilyMember(firstTreePerson);
        passTree.addFamilyMember(secondTreePerson);
        passTree.addFamilyMember(thirdTreePerson);
        passTree.addFamilyMember(fourthTreePerson);
        conn = db.openConnection();
        db.clearTables();
        pDao = new PersonDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    void insertPass() throws DataAccessException {
        pDao.insert(firstPerson);
        Person comparePerson = pDao.find(firstPerson.getPersonID());
        assertNotNull(comparePerson);
        assertEquals(firstPerson, comparePerson);
    }

    @Test
    void insertFail() throws DataAccessException{
        pDao.insert(firstPerson);
        assertThrows(DataAccessException.class, ()-> pDao.insert(firstPerson));
    }

    @Test
    void findPass() throws DataAccessException{
        pDao.insert(firstPerson);
        pDao.insert(secondPerson);
        Person foundPerson = pDao.find(firstPerson.getPersonID());
        Person foundPerson_2 = pDao.find(secondPerson.getPersonID());
        assertNotNull(foundPerson);
        assertNotNull(foundPerson_2);
        assertEquals(firstPerson,foundPerson);
        assertEquals(secondPerson,foundPerson_2);
    }

    @Test
    void findAllPass() throws DataAccessException, IncorrectUserException {
        pDao.insert(firstPerson);
        pDao.insert(firstTreePerson);
        pDao.insert(secondTreePerson);
        pDao.insert(thirdTreePerson);
        pDao.insert(fourthTreePerson);
        MyTree foundTree = pDao.findAll(firstPerson.getPersonID());
        assertNotNull(foundTree);
        assertEquals(passTree, foundTree);
    }

    @Test
    void findAllFail() throws DataAccessException, IncorrectUserException {
        assertThrows(IncorrectUserException.class, ()-> pDao.findAll("incorrect_personID"));

    }

    @Test
    void findFail() throws DataAccessException{
        pDao.insert(firstPerson);
        Person foundPerson = pDao.find(secondPerson.getPersonID());
        assertNull(foundPerson);
    }

    @Test
    void clearTest() throws DataAccessException{
        pDao.insert(firstPerson);
        pDao.insert(secondPerson);
//        find == true
        Person firstPersonTest = pDao.find(firstPerson.getPersonID());
        Person secondPersonTest = pDao.find(secondPerson.getPersonID());
        assertEquals(firstPerson, firstPersonTest);
        assertEquals(secondPerson, secondPersonTest);
        pDao.clear();
        Person firstTestNull = pDao.find(firstPerson.getPersonID());
        Person secondTestNull = pDao.find(secondPerson.getPersonID());
        assertNull(firstTestNull);
        assertNull(secondTestNull);
    }

    @Test
    public void removePass() throws DataAccessException {
        pDao.insert(firstPerson);
        Person foundPerson = pDao.find(firstPerson.getPersonID());
        assertNotNull(foundPerson);

        pDao.remove(firstPerson.getUsername());
        assertNull(pDao.find(firstPerson.getPersonID()));

    }

    @Test
    public void removeTwoPass() throws DataAccessException {
        pDao.insert(firstPerson);
        pDao.insert(secondPerson);
        pDao.insert(thirdTreePerson);
        Person foundPerson = pDao.find(firstPerson.getPersonID());
        assertNotNull(foundPerson);
        foundPerson = pDao.find(secondPerson.getPersonID());
        assertNotNull(foundPerson);
        foundPerson = pDao.find(thirdTreePerson.getPersonID());
        assertNotNull(foundPerson);

        pDao.remove(firstPerson.getUsername());
        assertNull(pDao.find(firstPerson.getPersonID()));
        assertNull(pDao.find(thirdTreePerson.getPersonID()));
        assertNotNull(pDao.find(secondPerson.getPersonID()));
    }

}