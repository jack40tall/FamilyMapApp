package services;

import data_access.*;
import exceptions.DataAccessException;
import model.Event;
import model.Person;
import model.User;
import object.EncoderDecoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request_result.LoadRequest;
import request_result.LoadResult;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class LoadServiceTest {
    private LoadRequest request;
    private LoadRequest failRequest;
    private LoadService service;
    private LoadResult result;
    private LoadResult passResult;
    private LoadResult failResult;
    private Database db;
    UserDao uDao;
    PersonDao pDao;
    EventDao eDao;
    User user;
    Person person;
    Event event1;
    Event event2;
    Connection conn;

    @BeforeEach
    void setUp() throws DataAccessException, IOException {
        db = new Database();
        conn = db.openConnection();
        db.clearTables();

        user = new User("Jack2134", "Mamba123", "4321", "greg@gmail.com",
                "Jack", "Adams", "m");
        event1 = new Event("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        event2 = new Event("Flipping_123A", "Greg", "Greg23",
                10.5f, 10.9f, "China", "Bangkok",
                "Flipping_Around", 2004);
        person = new Person("Stacy123", "hikegirl24", "Stacy", "Willis", "f",
                "Greg13", "Amy34", "Bobby9");
        LoadJsonFiles();
        failRequest = new LoadRequest();
        passResult = new LoadResult("Successfully added 1 users, 3 persons, and 2 events to the database.", true);
        failResult = new LoadResult("Error: Invalid request data", false);
        service = new LoadService();
        uDao = new UserDao(conn);
        pDao = new PersonDao(conn);
        eDao = new EventDao(conn);db.closeConnection(true);


    }

    @Test
    void loadPass() {
        result = service.load(request);
        assertTrue(result.isSuccess());
        assertEquals(passResult, result);
    }
    @Test
    void loadFail() {
        result = service.load(failRequest);
        assertEquals(failResult, result);
    }

    private void LoadJsonFiles() throws IOException {
        EncoderDecoder encoderDecoder = new EncoderDecoder();
        //        Load Data from Json
        System.out.println("Loading Json Data");
        Reader reader = new FileReader("json/example.json");
        request = encoderDecoder.deserializeFile(reader, LoadRequest.class);
    }
}