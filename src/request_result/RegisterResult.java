package request_result;
//Json data that gets sent back to the client

import java.util.Objects;

/**
 * Creates a RegisterResult Object, which stores a java object ready to be converted to json and sent back to client
 */
public class RegisterResult {
    private String authToken;
    private String userName;
    private String personID;
    private String message;

    public String getAuthToken() {
        return authToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getPersonID() {
        return personID;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    private boolean success;

    public RegisterResult(String authToken, String userName, String personID, boolean success) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
        this.success = success;
    }

    public RegisterResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegisterResult)) return false;
        RegisterResult that = (RegisterResult) o;
        return success == that.success &&
                Objects.equals(authToken, that.authToken) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(personID, that.personID) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, userName, personID, message, success);
    }
}
