package services;

import data_access.AuthTokenDao;
import exceptions.DataAccessException;
import data_access.Database;
import data_access.UserDao;
import exceptions.IncorrectCredentialsException;
import model.AuthToken;
import model.User;
import request_result.LoginRequest;
import request_result.LoginResult;

import java.sql.Connection;

/**
 * Creates LoginService
 */
public class LoginService {
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TOKEN_LENGTH = 12;
    private String generateAuthToken() {
        StringBuilder builder = new StringBuilder();
        int count = 0;
        while (count < TOKEN_LENGTH) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
            ++count;
        }
        return builder.toString();
    }
    /**
     * Logs in the user and returns an auth token.
     * @param request the LoginRequest
     * @return LoginResult Object
     */
    public LoginResult login(LoginRequest request) throws DataAccessException{
        Database db = new Database();
        LoginResult result;
        try {
            Connection conn;
            conn = db.openConnection();

            UserDao uDao = new UserDao(conn);
            User currUser = uDao.loginUser(request.getUserName(), request.getPassword());

            db.closeConnection(true);

            conn = db.openConnection();

            AuthTokenDao aDao = new AuthTokenDao(conn);
            String AuthToken = generateAuthToken();
            System.out.printf("AuthToken = %s\n", AuthToken);

            AuthToken currAuthToken = new AuthToken(currUser.getPersonID(), AuthToken);

            boolean success = aDao.insert(currAuthToken);

            db.closeConnection(true);

            result = new LoginResult(currAuthToken.getAuthorizationNum(), request.getUserName(),
                    currAuthToken.getPersonID(), success);
            return result;

        } catch (DataAccessException ex) {
            db.closeConnection(false);
            result = new LoginResult("Error: Couldn't access database");

        }
        catch (IncorrectCredentialsException ex) {
            db.closeConnection(false);
            result = new LoginResult("Error: Invalid Credentials");
        }
        return result;
    }
}
