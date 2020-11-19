package services;

import data_access.*;
import exceptions.DataAccessException;
import request_result.LoadRequest;
import request_result.LoadResult;

import java.sql.Connection;

/**
 * Creates LoadService
 */
public class LoadService {
    /**
     * Clears all data from the database (just like the /clear API), and then loads the posted user, person, and event data into the database.
     * @param request the LoadRequest
     * @return LoadResult Object
     */
    public LoadResult load(LoadRequest request) {
        Database db = new Database();
        int numUsers = 0;
        int numPersons = 0;
        int numEvents = 0;
        LoadResult result;

        try {
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            PersonDao pDao = new PersonDao(conn);
            EventDao eDao = new EventDao(conn);

            for(int i = 0; i < request.getUsers().length; ++i) {
                uDao.insert(request.getUsers()[i]);
                ++numUsers;
            }
            for(int i = 0; i < request.getPersons().length; ++i) {
                pDao.insert(request.getPersons()[i]);
                ++numPersons;
            }
            for(int i = 0; i < request.getEvents().length; ++i) {
                eDao.insertEvent(request.getEvents()[i]);
                ++numEvents;
            }
            StringBuilder message = new StringBuilder("Successfully added " + numUsers + " users, " + numPersons +
                                                        " persons, and " + numEvents + " events to the database.");
            result = new LoadResult(message.toString(), true);
            db.closeConnection(true);


        }
        catch (DataAccessException ex) {
            result = new LoadResult("Error: Could not load data into database", false);
        }
        catch (NullPointerException ex) {
            result = new LoadResult("Error: Invalid request data", false);
        }
        return result;
    }
}
