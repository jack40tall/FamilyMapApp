package services;

import data_access.*;
import exceptions.DataAccessException;
import exceptions.EventNotFoundException;
import exceptions.InvalidAuthTokenException;
import exceptions.PersonNotFoundException;
import model.AuthToken;
import model.Event;
import model.Person;
import request_result.EventRequest;
import request_result.EventResult;
import request_result.PersonResult;

import java.sql.Connection;

/**
 * Creates EventService
 */
public class EventService {
    /**
     * Gets a single requested event
     * @param request the EventRequest
     * @return EventResult object
     */
    public EventResult getEvent(EventRequest request) throws DataAccessException {
        //Returns one specific person
        Database db = new Database();
        EventResult result;

        try {
//            Check AuthToken
            AuthToken validToken = checkAndGetAuthToken(db, request.getAuthToken());
            String userName = getAssociatedUsername(db, validToken);

//            Find requested Person
            Connection conn;
            conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            Event requestedEvent = eDao.find(request.getEventID());
            db.closeConnection(true);
            if(requestedEvent == null) {
                throw new EventNotFoundException("Error: Event with given eventID not found in database");
            }

//            Make sure requested event belongs to logged in user
            if(userName.equals(requestedEvent.getUsername())) {
                result = new EventResult(requestedEvent);
            }
            else {
                result = new EventResult("Error: Requested person does not belong to this user", false);
            }
        } catch (InvalidAuthTokenException ex) {
            db.closeConnection(false);
            result = new EventResult("Error: Invalid AuthToken", false);
        } catch (EventNotFoundException e) {
            result = new EventResult("Error: Event with given eventID not found in database", false);
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
