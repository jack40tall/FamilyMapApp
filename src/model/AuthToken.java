package model;

//TODO: Model package stores database data in RAM so that it can be used with the program.
//TODO: These classes fuction as input and output of the data access classes. Used to shuttle data into and out of
// DataAccess classes

import java.util.Objects;

/**
 * Authentication Token
 */
public class AuthToken {
    /**
     * Unique personID
     */
    private String personID;
    /**
     * The authorization Number
     */
    private String authorizationNum;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken = (AuthToken) o;
        return personID.equals(authToken.personID) &&
                authorizationNum.equals(authToken.authorizationNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personID, authorizationNum);
    }


    /**
     * Creates an AuthToken
     * @param personID the personID
     * @param authorizationNum the Authorization Number
     */
    public AuthToken(String personID, String authorizationNum) {
        this.personID = personID;
        this.authorizationNum = authorizationNum;
    }


    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAuthorizationNum() {
        return authorizationNum;
    }

    public void setAuthorizationNum(String authorizationNum) {
        this.authorizationNum = authorizationNum;
    }
}
