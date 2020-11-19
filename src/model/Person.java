package model;

import java.util.Objects;

public class Person {
    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private String gender;
    /**
     * Includes fatherID, which can be null
     */
    private String fatherID;
    /**
     * Includes motherID, which can be null
     */
    private String motherID;
    /**
     * Includes spouseID, which can be null
     */
    private String spouseID;

    public Person(){};

    /**
     * Creates a person object that identifies and individual
     * @param personID the personID
     * @param username the username
     * @param firstName the first name
     * @param lastName the last name
     * @param gender the gender (m or f)
     * @param fatherID the fatherID (can be null)
     * @param motherID the motherID (can be null)
     * @param spouseID the spouseID (can be null)
     */









    public Person(String personID, String username, String firstName, String lastName, String gender, String fatherID
            , String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getUsername() {
        return associatedUsername;
    }

    public void setUsername(String username) {
        this.associatedUsername = username;
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

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

//    /**
//     * Checks if two events are equal to eachother
//     * @param o the object to test equality with
//     * @return true if equal
//     */
//    @Override
//    public boolean equals(Object o) {
//        if (o == null)
//            return false;
//        if (o instanceof Person) {
//            Person oPerson = (Person) o;
//            boolean equals = oPerson.getPersonID().equals(getPersonID()) &&
//                    oPerson.getUsername().equals(getUsername()) &&
//                    oPerson.getFirstName().equals(getFirstName()) &&
//                    oPerson.getLastName().equals(getLastName()) &&
//                    oPerson.getGender().equals(getGender());
//
//            if(oPerson == null)
//            return equals;
//                    oPerson.getFatherID().equals(getFatherID()) &&
//                    oPerson.getMotherID().equals(getMotherID()) &&
//                    (oPerson.getSpouseID().equals(getSpouseID()) || (oPerson.getSpouseID() == getSpouseID()));
//        } else {
//            return false;
//        }
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return personID.equals(person.personID) &&
                associatedUsername.equals(person.associatedUsername) &&
                firstName.equals(person.firstName) &&
                lastName.equals(person.lastName) &&
                gender.equals(person.gender) &&
                Objects.equals(fatherID, person.fatherID) &&
                Objects.equals(motherID, person.motherID) &&
                Objects.equals(spouseID, person.spouseID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personID, associatedUsername, firstName, lastName, gender, fatherID, motherID, spouseID);
    }
}
