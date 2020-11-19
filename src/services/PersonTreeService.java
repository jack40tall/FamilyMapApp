package services;

import data_access.*;
import exceptions.DataAccessException;
import exceptions.IncorrectUserException;
import exceptions.InvalidAuthTokenException;
import model.AuthToken;
import model.MyTree;
import request_result.PersonTreeRequest;
import request_result.PersonTreeResult;

import java.sql.Connection;

/**
 * A PersonTreeService object
 */
public class PersonTreeService {
    /**
     * Returns the Family Tree of the current user
     * @param request the PersonTreeRequest
     * @return PersonTreeResult Object
     */
    public PersonTreeResult getPersonTree(PersonTreeRequest request) throws DataAccessException {
        Database db = new Database();
        PersonTreeResult result = null;

        try {
//            Check AuthToken
            AuthToken validToken = checkAndGetAuthToken(db, request.getAuthToken());
//            String userName = getAssociatedUsername(db, validToken);

//            Find requested Person
            Connection conn;
            conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            MyTree requestedTree = pDao.findAll(validToken.getPersonID());
            db.closeConnection(true);

            result = new PersonTreeResult(requestedTree.getTreeArray(), true);

        } catch (InvalidAuthTokenException ex) {
            db.closeConnection(false);
            result = new PersonTreeResult("Error: Invalid AuthToken", false);
        } catch (IncorrectUserException e) {
            result = new PersonTreeResult("Error: PersonID not found in Database", false);
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
