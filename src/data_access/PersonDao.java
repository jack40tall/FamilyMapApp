package data_access;

import exceptions.DataAccessException;
import exceptions.IncorrectUserException;
import model.MyTree;
import model.Person;

import java.sql.*;

/**
 * A PersonDao object that handles communication between Person objects and the database
 */
public class PersonDao {
    private final Connection conn;

    /**
     * Initializes a PersonDao object with a database connection
     * @param conn the database connection
     */
    public PersonDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Creates a new person in the database
     * @param person the person to create
     */
    public boolean insert(Person person) throws DataAccessException {
        boolean success = false;
        String sql = "INSERT INTO Persons (Person_ID, associated_username, first_name, last_name, gender, " +
                "father_id, mother_id, spouse_id) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();

            success = true;
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
            return success;
    }

    /**
     * Returns a requested person from the database
     * @param personID the personID to query
     * @return Requested Person, or null
     */
    public Person find(String personID) throws DataAccessException {
//        See if person exists in database, returns Person object
//        could call other functions that request specific information about a person. Return true if successful.
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE Person_ID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            TODO:What does this do^? and why is it in the try?
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("person_id"), rs.getString("Associated_Username"),
                        rs.getString("first_name"), rs.getString("last_name"), rs.getString("gender"),
                        rs.getString("father_id"), rs.getString("mother_id"), rs.getString("spouse_id"));
                return person;
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
        return null;
    }

    /**
     * Returns a requested persons Tree from the database
     * @param personID the personID to query
     * @return Requested Person Tree, or null
     */
    public MyTree findAll(String personID) throws DataAccessException, IncorrectUserException {

        Person rootPerson = find(personID);
        if (rootPerson == null) {
            throw new IncorrectUserException("personID not found");
        }
        MyTree tree = new MyTree(rootPerson);
        String associatedUsername = rootPerson.getUsername();
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE associated_username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            TODO:What does this do^? and why is it in the try?
            stmt.setString(1, associatedUsername);
            rs = stmt.executeQuery();
            while (rs.next()) {
                person = new Person(rs.getString("person_id"), rs.getString("Associated_Username"),
                        rs.getString("first_name"), rs.getString("last_name"), rs.getString("gender"),
                        rs.getString("father_id"), rs.getString("mother_id"), rs.getString("spouse_id"));
                tree.addFamilyMember(person);
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
        return tree;
    }

    public void remove(String associatedUsername) throws DataAccessException {
        String sql = "DELETE FROM Persons WHERE associated_username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, associatedUsername);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting from the database");
        }
    }

    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Persons";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}
