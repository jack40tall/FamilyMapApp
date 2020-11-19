package services;

import exceptions.DataAccessException;
import data_access.Database;
import request_result.ClearResult;

import java.sql.Connection;

/**
 * Creates ClearService
 */
public class ClearService {

    /**
     * Deletes ALL data from the database, including user accounts, auth tokens, and generated person and event data.
     * @return ClearResult object
     */
    public ClearResult clear(){
        try {
            Database db = new Database();
            Connection conn = db.openConnection();

            db.clearTables();
            db.closeConnection(true);
            return new ClearResult("Clear succeeded", true);
        }
        catch (DataAccessException ex) {
            return new ClearResult("Error: Unable to Clear Database", true);
        }


    }
}
