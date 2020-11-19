package request_result;

import model.Person;

import java.util.Objects;

/**
 * Creates a PersonResult Object, which stores a java object ready to be converted to json and sent back to client
 */
public class PersonResult {
    private String associatedUsername;
    private String personID;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;
    private String message;
    private boolean success;

    public PersonResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }


    public PersonResult(String associatedUsername, String personID, String firstName, String lastName, String gender,
                        String fatherID, String motherID, String spouseID, boolean success) {
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
        this.success = success;
    }

    public PersonResult(Person person) {
        this.associatedUsername = person.getUsername();
        this.personID = person.getPersonID();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.gender = person.getGender();
        this.fatherID = person.getFatherID();
        this.motherID = person.getMotherID();
        this.spouseID = person.getSpouseID();
        this.success = true;

    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonResult)) return false;
        PersonResult that = (PersonResult) o;
        return success == that.success &&
                Objects.equals(associatedUsername, that.associatedUsername) &&
                Objects.equals(personID, that.personID) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(gender, that.gender) &&
                Objects.equals(fatherID, that.fatherID) &&
                Objects.equals(motherID, that.motherID) &&
                Objects.equals(spouseID, that.spouseID) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(associatedUsername, personID, firstName, lastName, gender, fatherID, motherID, spouseID, message, success);
    }
}
