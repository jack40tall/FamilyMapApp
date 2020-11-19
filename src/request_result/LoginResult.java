package request_result;

import java.util.Objects;

/**
 * Creates a LoginResult Object, which stores a java object ready to be converted to json and sent back to client
 */
public class LoginResult {
    String authToken;
    String userName;
    String personID;
    String message;
    boolean success;

    public LoginResult(String authToken, String userName, String personID, boolean success) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
        this.success = success;
    }

    public LoginResult(String errorMessage) {
        message = errorMessage;
        this.success = false;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getPersonID() {
        return personID;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginResult)) return false;
        LoginResult that = (LoginResult) o;
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
