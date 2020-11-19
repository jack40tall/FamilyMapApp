package request_result;
/**
 * Creates a PersonTreeRequest Object, which is converted Json data stored in a usable Java object
 */
public class PersonTreeRequest {
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public PersonTreeRequest(String authToken) {
        this.authToken = authToken;
    }
}
