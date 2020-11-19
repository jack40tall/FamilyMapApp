package request_result;

import model.Event;
import model.Person;
import model.User;

/**
 * Creates a LoadRequest Object, which is converted Json data stored in a usable Java object
 */
public class LoadRequest {
    private User[] users;
    private Person[] persons;
    private Event[] events;

    public User[] getUsers() {
        return users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public Event[] getEvents() {
        return events;
    }

}
