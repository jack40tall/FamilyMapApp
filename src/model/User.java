package model;

import java.util.Objects;

public class User {
    private String personID;
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    /**
     * Includes gender, which is either 'm' or 'f'
     */
    private String gender;

    /**
     * Creates a user, which basically includes account information
     * @param personID the personID
     * @param userName the userName
     * @param password the password
     * @param email the email
     * @param firstName the first name
     * @param lastName the last name
     * @param gender the gender
     */
    public User(String personID, String userName, String password, String email, String firstName,
                String lastName, String gender) {
        this.personID = personID;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail_address() {
        return email;
    }

    public void setEmail_address(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return personID.equals(user.personID) &&
                userName.equals(user.userName) &&
                password.equals(user.password) &&
                email.equals(user.email) &&
                firstName.equals(user.firstName) &&
                lastName.equals(user.lastName) &&
                gender.equals(user.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personID, userName, password, email, firstName, lastName, gender);
    }
}
