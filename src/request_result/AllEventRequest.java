package request_result;

/**
 * Creates an AllEventRequest, which is converted Json data stored in a usable Java object
 */
public class AllEventRequest {
    private String authToken;


    public AllEventRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}
