package services;

import data_access.*;
import exceptions.DataAccessException;
import exceptions.InvalidAuthTokenException;
import model.AllEvents;
import model.AuthToken;
import request_result.AllEventRequest;
import request_result.AllEventResult;

import java.sql.Connection;

/**
 * An AllEventService object
 */
public class AllEventService {
    /**
     * Gets all events related to a person and returns it
     * @param request the AllEventsRequest
     * @return The EventResult object
     */
    public AllEventResult getAllEvents(AllEventRequest request) throws DataAccessException {
        Database db = new Database();
        AllEventResult result = null;

        try {
//            Check AuthToken
            AuthToken validToken = checkAndGetAuthToken(db, request.getAuthToken());
            String userName = getAssociatedUsername(db, validToken);

//            Find requested Person
            Connection conn;
            conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            AllEvents requestedEvents = eDao.findAll(userName);
            db.closeConnection(true);

            result = new AllEventResult(requestedEvents.getArray(), true);

        } catch (InvalidAuthTokenException ex) {
            db.closeConnection(false);
            result = new AllEventResult("Error: Invalid AuthToken", false);
        }
        return result;
    }
    private AuthToken checkAndGetAuthToken(Database db, String authToken) throws DataAccessException, InvalidAuthTokenException {
        AuthToken returnedToken = null;
        Connection conn;
        conn = db.openConnection();

        AuthTokenDao aDao = new AuthTokenDao(conn);
        returnedToken = aDao.find(authToken);

        db.closeConnection(true);

        return returnedToken;
    }

    private String getAssociatedUsername(Database db, AuthToken authToken) throws DataAccessException, InvalidAuthTokenException {
        String returnedUsername = null;
        Connection conn;
        conn = db.openConnection();

        UserDao uDao = new UserDao(conn);
        returnedUsername = uDao.getUsername(authToken.getPersonID());

        db.closeConnection(true);

        return returnedUsername;
    }
}
