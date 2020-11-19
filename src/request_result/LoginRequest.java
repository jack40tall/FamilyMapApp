package request_result;

import com.google.gson.*;

/**
 * Creates a LoginRequest Object, which is converted Json data stored in a usable Java object
 */
public class LoginRequest {
    String userName;
    String password;

    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
