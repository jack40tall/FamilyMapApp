package data_access;

import exceptions.DataAccessException;
import model.AllEvents;
import model.Event;

import java.sql.*;

/**
 * Creates an object that accesses the database for Events
 */
public class EventDao {
    private final Connection conn;

    /**
     * Contructs an EventDao with a connection to the Database
     * @param conn the database connection
     */
    public EventDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * puts a single event into the database
     * @param event the Event to create
     */
    public void insertEvent(Event event) throws DataAccessException {
// TODO: Does the sql insert order matter?
        String sql = "INSERT INTO Events (Event_ID, associated_username, person_id, latitude, longitude, " +
                "country, city, event_type, year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Clears the Events tabls
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException
    {

        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Events";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }


    /**
     * Removes a single event in the database
     * @param associatedUsername the Event to remove
     */
    public void remove(String associatedUsername) throws DataAccessException {
        String sql = "DELETE FROM Events WHERE associated_username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, associatedUsername);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting from the database");
        }
    }

    /**
     * Accesses and returns a requested event
     * @param eventID the eventID of the Event to query
     * @return the requested event, or null if it doesn't exist.
     */
    public Event find(String eventID) throws DataAccessException {
//        see if event exists returns event Object
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE Event_ID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("Event_ID"), rs.getString("Associated_Username"),
                        rs.getString("Person_ID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("Event_Type"),
                        rs.getInt("Year"));
                return event;
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
     * @param associatedUsername the personID to query
     * @return Requested Person Tree, or null
     */
    public AllEvents findAll(String associatedUsername) throws DataAccessException {

        AllEvents allEvents = new AllEvents();
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE associated_username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            rs = stmt.executeQuery();
            while (rs.next()) {
                event = new Event(rs.getString("Event_ID"), rs.getString("Associated_Username"),
                        rs.getString("Person_ID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("Event_Type"),
                        rs.getInt("Year"));
                allEvents.addEvent(event);
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
        return allEvents;
    }
}
