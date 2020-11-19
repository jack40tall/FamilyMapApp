package services;

import data_access.*;
import exceptions.DataAccessException;
import exceptions.IncorrectUserException;
import load_data.Location;
import model.Event;
import model.Person;
import model.User;
import request_result.FillRequest;
import request_result.FillResult;
import server.Server;

import java.io.IOException;
import java.sql.Connection;
import java.util.Random;
import java.util.UUID;

/**
 * Creates FillService
 */
public class FillService {
    private int peopleAdded = 0;
    private int eventsAdded = 0;
    private static final int CURRENT_GEN = 0;
    private Server tempServer;
    /**
     * uses the username and int numGenerations parameters contained in the fill object to populate a tree for the user.
     * @param request the FillRequest
     * @return FillResult object
     */
    public FillResult fill(FillRequest request) throws DataAccessException, IOException {
        tempServer = new Server();
        tempServer.LoadJsonFiles();
        System.out.println("In FillService");
        Database db = new Database();
        boolean success;
        FillResult result;
        try {
            Connection conn;
//            Get personID of user
            conn = db.openConnection();

            UserDao uDao = new UserDao(conn);
            User currUser = uDao.find(request.getUsername());

            if(currUser == null) {
                db.closeConnection(false);
                throw new IncorrectUserException("Incorrect User");
            }

            db.closeConnection(true);

            conn = db.openConnection();

            String fatherID = generatePersonID();
            String motherID = generatePersonID();

//            Remove old Data and Add User
            PersonDao pDao = new PersonDao(conn);
            pDao.remove(currUser.getUsername());
            EventDao eDao = new EventDao(conn);
            eDao.remove(currUser.getUsername());
            Person currPerson = new Person(currUser.getPersonID(), request.getUsername(), currUser.getFirstName(),
                    currUser.getLastName(), currUser.getGender(), fatherID, motherID, null);

            success = pDao.insert(currPerson);
            if(success) {
                ++peopleAdded;
            }
            db.closeConnection(true);

            createRootEvents(db, currPerson);
            ++eventsAdded;

            createParentsAndEvents(request.getUsername(), fatherID, motherID, currUser.getLastName(), CURRENT_GEN,
                    request.getGenerations(), db);


            result = new FillResult("Successfully added " + peopleAdded + " persons and " + eventsAdded +
                            " events to " + "the database.", true);

        } catch (DataAccessException | IOException ex) {
            System.out.println("DataAccessException thrown");
            db.closeConnection(false);
            result = new FillResult("Error: Could not access database", false);

        } catch (IncorrectUserException e) {
            result = new FillResult("Error: User not found in database", false);
        }

        return result;
    }

    private void createParentsAndEvents(String associatedUser, String maleID, String femaleID, String familyName,
                                  int currGens, int totalGens, Database db) throws DataAccessException, IOException {
        System.out.printf("In create parents with currGens = %d\n", currGens);
        if (currGens == totalGens) {
//            return;
        }
        else {
            Connection conn;
            conn = db.openConnection();

//            Generate random numbers for name files
            Random rand = new Random();
            int upperBound  = 144;
            int rand_mname = rand.nextInt(upperBound);

            upperBound = 147;
            int rand_fname = rand.nextInt(upperBound);

            upperBound = 152;
            int rand_sname = rand.nextInt(upperBound);

            String husbandFatherID = generatePersonID();
            String wifeFatherID = generatePersonID();
            String husbandMotherID = generatePersonID();
            String wifeMotherID = generatePersonID();

//            Make sure to not create relationships for top generation
            if(currGens == (totalGens - 1)) {
                husbandFatherID = null;
                wifeFatherID = null;
                husbandMotherID = null;
                wifeMotherID = null;
            }

//            Add User
            PersonDao pDao = new PersonDao(conn);
            Person husband = new Person(maleID, associatedUser, tempServer.getmNames().getData()[rand_mname],familyName,
                    "m", husbandFatherID, husbandMotherID, femaleID);

            Person wife = new Person(femaleID, associatedUser, tempServer.getfNames().getData()[rand_fname],
                    tempServer.getsNames().getData()[rand_sname], "f", wifeFatherID, wifeMotherID, maleID);


            boolean maleSuccess = pDao.insert(husband);
            boolean femaleSuccess = pDao.insert(wife);

            if(maleSuccess && femaleSuccess) {
                peopleAdded += 2;
            }

            db.closeConnection(true);

//            Create Events for Current Generation
            createEvents(associatedUser, husband, wife, db, currGens);

//            Create Father
            createParentsAndEvents(associatedUser, husbandFatherID, husbandMotherID, familyName, (currGens + 1),
                    totalGens, db);

//            Create Mother
            createParentsAndEvents(associatedUser, wifeFatherID, wifeMotherID,
                    tempServer.getsNames().getData()[rand_sname], (currGens + 1), totalGens, db);
        }
    }

    private void createRootEvents(Database db, Person root) throws DataAccessException {
            Connection conn;
            conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            int currentYear = 2020;
            Random rand = new Random();
            Location currLocation;
            int upperBound  = 488; // number of possible locations


//        Create user Birth
            int rand_location = rand.nextInt(upperBound);
            currLocation = tempServer.getLocData().getData()[rand_location];
            int birthYear = currentYear - (23);
            Event rootBirth = new Event(UUID.randomUUID().toString(), root.getUsername(), root.getPersonID(),
                    currLocation.getLatitude(), currLocation.getLongitude(), currLocation.getCountry(),
                    currLocation.getCity(), "Birth", birthYear);
            eDao.insertEvent(rootBirth);

            db.closeConnection(true);
    }

    private void createEvents(String associatedUser, Person husband, Person wife, Database db, int currGen) throws DataAccessException,
            IOException {
        Connection conn;
        conn = db.openConnection();
        EventDao eDao = new EventDao(conn);
        int currentYear = 2020;
        Random rand = new Random();
        Location currLocation;
        int upperBound  = 488; // number of possible locations



//        Create Husband Birth
        int rand_location = rand.nextInt(upperBound);
        currLocation = tempServer.getLocData().getData()[rand_location];
        int husbandBirthYear = currentYear - (23 * (currGen + 2));
        Event husbandBirth = new Event(UUID.randomUUID().toString(), associatedUser, husband.getPersonID(),
                currLocation.getLatitude(), currLocation.getLongitude(), currLocation.getCountry(),
                currLocation.getCity(), "Birth", husbandBirthYear);
        eDao.insertEvent(husbandBirth);

//        Create Wife Birth
        rand_location = rand.nextInt(upperBound);
        currLocation = tempServer.getLocData().getData()[rand_location];
        int wifeBirthYear = currentYear - (24 * (currGen + 2));
        Event wifeBirth = new Event(UUID.randomUUID().toString(), associatedUser, wife.getPersonID(),
                currLocation.getLatitude(), currLocation.getLongitude(), currLocation.getCountry(),
                currLocation.getCity(), "Birth", wifeBirthYear);
        eDao.insertEvent(wifeBirth);


//        Create Marriage
        rand_location = rand.nextInt(upperBound);
        currLocation = tempServer.getLocData().getData()[rand_location];
        int marriageYear = (wifeBirthYear + 21);
        Event husbandMarriage = new Event(UUID.randomUUID().toString(), associatedUser, husband.getPersonID(),
                currLocation.getLatitude(), currLocation.getLongitude(), currLocation.getCountry(),
                currLocation.getCity(), "Marriage", marriageYear);
        Event wifeMarriage = new Event(UUID.randomUUID().toString(), associatedUser, wife.getPersonID(),
                currLocation.getLatitude(), currLocation.getLongitude(), currLocation.getCountry(),
                currLocation.getCity(), "Marriage", marriageYear);
        eDao.insertEvent(husbandMarriage);
        eDao.insertEvent(wifeMarriage);


//        Create Husband Death
        rand_location = rand.nextInt(upperBound);
        currLocation = tempServer.getLocData().getData()[rand_location];
        int husbandDeathYear = (husbandBirthYear + 83);
        if(husbandDeathYear <= currentYear) {
            husbandDeathYear = (currentYear - 1);
        }
            Event husbandDeath = new Event(UUID.randomUUID().toString(), associatedUser, husband.getPersonID(),
                    currLocation.getLatitude(), currLocation.getLongitude(), currLocation.getCountry(),
                    currLocation.getCity(), "Death", husbandDeathYear);
            eDao.insertEvent(husbandDeath);


//        Create Wife Death
        rand_location = rand.nextInt(upperBound);
        currLocation = tempServer.getLocData().getData()[rand_location];
        int wifeDeathYear = (wifeBirthYear + 86);
        if(wifeDeathYear <= currentYear) {
            wifeDeathYear = (currentYear - 3);
        }
            Event wifeDeath = new Event(UUID.randomUUID().toString(), associatedUser, wife.getPersonID(),
                    currLocation.getLatitude(), currLocation.getLongitude(), currLocation.getCountry(),
                    currLocation.getCity(), "Death", wifeDeathYear);
            eDao.insertEvent(wifeDeath);

        db.closeConnection(true);
        eventsAdded += 6;
    }

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TOKEN_LENGTH = 12;
    private String generatePersonID() {
        StringBuilder builder = new StringBuilder();
        int count = 0;
        while (count < TOKEN_LENGTH) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
            ++count;
        }
        return builder.toString();
    }

}
