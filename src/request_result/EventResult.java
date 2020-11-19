package request_result;

import model.Event;

import java.util.Objects;

/**
 * Creates a EventResult Object, which stores a java object ready to be converted to json and sent back to client
 */
public class EventResult {
    private String associatedUsername;
    private String eventID;
    private String personID;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;
    private String message;
    private boolean success;

    public EventResult(String message, boolean success) {
//        TODO: how do I make the lattitude and longitude not encoded by json?
        this.message = message;
        this.success = success;
    }

    public EventResult(Event event) {
        associatedUsername = event.getUsername();
        eventID = event.getEventID();
        personID = event.getPersonID();
        latitude = event.getLatitude();
        longitude = event.getLongitude();
        country = event.getCountry();
        city = event.getCity();
        eventType = event.getEventType();
        year = event.getYear();
        success = true;

    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventResult)) return false;
        EventResult that = (EventResult) o;
        return Float.compare(that.latitude, latitude) == 0 &&
                Float.compare(that.longitude, longitude) == 0 &&
                year == that.year &&
                success == that.success &&
                Objects.equals(associatedUsername, that.associatedUsername) &&
                Objects.equals(eventID, that.eventID) &&
                Objects.equals(personID, that.personID) &&
                Objects.equals(country, that.country) &&
                Objects.equals(city, that.city) &&
                Objects.equals(eventType, that.eventType) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(associatedUsername, eventID, personID, latitude, longitude, country, city, eventType, year, message, success);
    }
}