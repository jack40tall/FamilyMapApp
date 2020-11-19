package data_access;

import exceptions.DataAccessException;
import exceptions.IncorrectCredentialsException;
import exceptions.InvalidAuthTokenException;
import model.User;

import java.sql.*;

/**
 * UserDao Object that accesses user information from database for User
 */
public class UserDao {
    private final Connection conn;

    /**
     * Initializes UserDao with a connection
     * @param conn the database connection
     */
    public UserDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Creates a new person in the database
     * @param user the person to create
     */
    public void insert(User user) throws DataAccessException {
        String sql = "INSERT INTO Users (person_ID, username, password, email_address, first_name, " +
                "last_name, gender) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, user.getPersonID());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getEmail_address());
            stmt.setString(5, user.getFirstName());
            stmt.setString(6, user.getLastName());
            stmt.setString(7, user.getGender());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Returns a requested user from the database
     * @param username the personID to query
     * @return Requested User, or null
     */
    public User find(String username) throws DataAccessException {
//        See if person exists in database, returns Person object
//        could call other functions that request specific information about a person. Return true if successful.
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("person_id"), rs.getString("username"),
                        rs.getString("password"), rs.getString("email_address"), rs.getString("first_name"),
                        rs.getString("last_name"), rs.getString("gender"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }
/**
     * Returns a requested userName from the database using the personID
     * @param personID the personID to query
     * @return Requested User, or null
     */
    public String getUsername(String personID) throws DataAccessException, InvalidAuthTokenException {
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE person_id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new InvalidAuthTokenException("Invalid AuthToken");
    }

    /**
     * check username and password with values in database
     * @param username the username
     * @param password the password
     */
    public User loginUser(String username, String password) throws DataAccessException, IncorrectCredentialsException {
//check username and password with values in database
        User tempUser = this.find(username);

        if(tempUser == null) {
            throw new IncorrectCredentialsException("Incorrect Credentials");
        }
        else if(tempUser.getUsername().equals(username) && tempUser.getPassword().equals(password)) {
            return tempUser;
        }
        else {
            throw new IncorrectCredentialsException("Incorrect Credentials");
        }
    }


    /**
     * Clears the user table
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM users";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

}
