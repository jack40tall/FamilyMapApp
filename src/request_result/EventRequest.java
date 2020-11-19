package request_result;

/**
 * Creates a ClearRequest Object, which is converted Json data stored in a usable Java object
 */
public class EventRequest {
    private String eventID;
    private String authToken;

    public EventRequest(String eventID, String authToken) {
        this.eventID = eventID;
        this.authToken = authToken;
    }

    public String getEventID() {
        return eventID;
    }

    public String getAuthToken() {
        return authToken;
    }
}
