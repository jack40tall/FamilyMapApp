package request_result;

/**
 * Creates a PersonRequest Object, which is converted Json data stored in a usable Java object
 */
public class PersonRequest {
    private String personID;
    private String authToken;

    public PersonRequest(String personID, String authToken) {
        this.personID = personID;
        this.authToken = authToken;
    }

    public String getPersonID() {
        return personID;
    }

    public String getAuthToken() {
        return authToken;
    }
}
