package services;

import data_access.AuthTokenDao;
import exceptions.DataAccessException;
import data_access.Database;
import data_access.UserDao;
import model.AuthToken;
import model.User;
import request_result.RegisterRequest;
import request_result.RegisterResult;

import java.sql.Connection;

/**
 * A RegisterService Object
 */
public class RegisterService {
    /**
     * Creates user and generates a tree for them
     * @param request the RegisterRequest
     * @return RegisterResult object
     */
    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        Database db = new Database();
        try {
            Connection conn;
            conn = db.openConnection();

            String newPersonID = generateRandomNum();
            User newUser = new User(newPersonID, request.getUserName(), request.getPassword(), request.getEmail(),
                    request.getFirstName(), request.getLastName(), request.getGender());

            UserDao uDao = new UserDao(conn);
            uDao.insert(newUser);

            db.closeConnection(true);

            conn = db.openConnection();

            AuthTokenDao aDao = new AuthTokenDao(conn);
            String AuthToken = generateRandomNum();
            System.out.printf("AuthToken = %s\n", AuthToken);

            AuthToken currAuthToken = new AuthToken(newUser.getPersonID(), AuthToken);

            boolean success = aDao.insert(currAuthToken);

            db.closeConnection(true);

            RegisterResult result = new RegisterResult(currAuthToken.getAuthorizationNum(), request.getUserName(),
                    currAuthToken.getPersonID(), success);
            return result;
        }
        catch (DataAccessException ex) {
            db.closeConnection(false);
            RegisterResult errorResult = new RegisterResult("Error: Username already taken", false);
            return errorResult;
        }
    }
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TOKEN_LENGTH = 12;
    private String generateRandomNum() {
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
