package services;

import data_access.*;
import exceptions.DataAccessException;
import exceptions.InvalidAuthTokenException;
import exceptions.PersonNotFoundException;
import model.AuthToken;
import model.Person;
import request_result.PersonRequest;
import request_result.PersonResult;

import java.sql.Connection;

/**
 * A PersonService Object
 */
public class PersonService {
    /**
     * Returns one specific person
     * @param request the PersonRequest
     * @return PersonResult Object
     */
    public PersonResult getPerson(PersonRequest request) throws DataAccessException {
        //Returns one specific person
        Database db = new Database();
        PersonResult result;

        try {
//            Check AuthToken
            AuthToken validToken = checkAndGetAuthToken(db, request.getAuthToken());
            String userName = getAssociatedUsername(db, validToken);

//            Find requested Person
            Connection conn;
            conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            Person requestedPerson = pDao.find(request.getPersonID());
            db.closeConnection(true);
            if(requestedPerson == null) {
                throw new PersonNotFoundException("Error: Person with given personID not found in database");
            }

//            Make sure requested person belongs to logged in user
            if(userName.equals(requestedPerson.getUsername())) {
                result = new PersonResult(requestedPerson);
            }
            else {
//                else throw an error
                result = new PersonResult("Error: Requested person does not belong to this user", false);
            }

        } catch (DataAccessException ex) {
            db.closeConnection(false);
            result = new PersonResult("Error: Could not access database", false);

        } catch (InvalidAuthTokenException ex) {
            db.closeConnection(false);
            result = new PersonResult("Error: Invalid AuthToken", false);
        } catch (PersonNotFoundException e) {
            result = new PersonResult("Error: Person with given personID not found in database", false);
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
