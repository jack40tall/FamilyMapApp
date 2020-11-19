package data_access;

import exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
//TODO: This class will control the connection with the database. Not totally sure how it works yet, but we'll get
// there. Also handles the dataAccessException.

/**
 * Database Object that contains and handles database connections
 */
public class Database {
    private Connection conn;
    private AuthTokenDao aDao;
    private EventDao eDao;
    private PersonDao pDao;
    private UserDao uDao;

//Whenever we want to make a change to our database we will have to open a connection and use
    //Statements created by that connection to initiate transactions

    /**
     * Tries to open a connects with the database
     * @return the Connection object, that can then be used in other functions
     * @throws DataAccessException
     */
    public Connection openConnection() throws DataAccessException {
        try {
            //The Structure for this Connection is driver:language:path
            //The path assumes you start in the root of your project unless given a non-relative path
            final String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";
//            TODO: How to make this go to the database folder?

            // Open a database connection to the file given in the path
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }

    /**
     * Gets the already open connection
     * @return a reference to the connection
     * @throws DataAccessException
     */
    public Connection getConnection() throws DataAccessException {
        if(conn == null) {
            return openConnection();
        } else {
            return conn;
        }
    }

    //When we are done manipulating the database it is important to close the connection. This will
    //End the transaction and allow us to either commit our changes to the database or rollback any
    //changes that were made before we encountered a potential error.

    //IMPORTANT: IF YOU FAIL TO CLOSE A CONNECTION AND TRY TO REOPEN THE DATABASE THIS WILL CAUSE THE
    //DATABASE TO LOCK. YOUR CODE MUST ALWAYS INCLUDE A CLOSURE OF THE DATABASE NO MATTER WHAT ERRORS
    //OR PROBLEMS YOU ENCOUNTER

    /**
     * Closes the connection with the database
     * @param commit commit
     * @throws DataAccessException
     */
    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                //This will commit the changes to the database
                conn.commit();
            } else {
                //If we find out something went wrong, pass a false into closeConnection and this
                //will rollback any changes we made during this connection
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    /**
     * Clears the tables in the database
     * @throws DataAccessException
     */
    public void clearTables() throws DataAccessException
    {

        try (Statement stmt = conn.createStatement()){
            String sql_a = "DELETE FROM auth_tokens";
            stmt.executeUpdate(sql_a);
            String sql_b = "DELETE FROM persons";
            stmt.executeUpdate(sql_b);
            String sql_c = "DELETE FROM events";
            stmt.executeUpdate(sql_c);
            String sql_d = "DELETE FROM users";
            stmt.executeUpdate(sql_d);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}
