package exceptions;

public class PersonNotFoundException extends Exception {
    public PersonNotFoundException(String error) {
        super(error);
    }
}
