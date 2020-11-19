package data_access;

import exceptions.DataAccessException;
import exceptions.InvalidAuthTokenException;
import model.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//TODO:the DataAccess package interacts with database on behalf of the server. Isolated database logic here. This
// allows you to port it to a different database while only changing this package.

/**
 * Creates an object that accesses the database for AuthTokens
 */
public class AuthTokenDao {
    private final Connection conn;

    /**
     * Contructs an AuthTokenDao with a connection to the Database
     * @param conn the database connection
     */
    public AuthTokenDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * checks the Authentication Token to make sure it matches the username
     * @param authToken The AuthToken
     * @return boolean success
     */
    public boolean check(AuthToken authToken) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM Auth_Tokens WHERE Person_id = ?;";
        boolean isValid = false;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken.getPersonID());
            rs = stmt.executeQuery();
//            TODO: does this function like a while loop?
//            TODO: should I use check or find?
            while (rs.next()) {
                AuthToken tempToken = new AuthToken(rs.getString("Person_ID"), rs.getString("authorization_num"));
                if(tempToken.getPersonID().equals(authToken.getPersonID()) && tempToken.getAuthorizationNum().equals(authToken.getAuthorizationNum())) {
                    isValid = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return isValid;

    }

    /**
     * adds token to authToken table
     * @param authToken the AuthToken
     * @return success boolean success
     */
    public boolean insert(AuthToken authToken) throws DataAccessException {
//        adds token to authToken table
        boolean success = false;
        String sql = "INSERT INTO Auth_Tokens (person_id, authorization_num) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, authToken.getPersonID());
            stmt.setString(2, authToken.getAuthorizationNum());


            stmt.executeUpdate();
            success = true;
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
        return success;
    }

    /**
     * removes token from authToken table ex. logout feature
     * @param authToken the authToken which contains authNum and personID
     */
    public void remove(AuthToken authToken) throws DataAccessException {
        String sql = "DELETE FROM Auth_Tokens WHERE person_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, authToken.getPersonID());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting from the database");
        }
    }

     /**
     * checks to see if authToken is valid
     * @param currToken the Token
     */
    public AuthToken find(String currToken) throws DataAccessException, InvalidAuthTokenException {
        AuthToken authToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM Auth_Tokens WHERE authorization_num = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, currToken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authToken = new AuthToken(rs.getString("Person_ID"), rs.getString("authorization_num"));
                return authToken;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding authToken");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        throw new InvalidAuthTokenException("Error: Invalid AuthToken");
    }


}
