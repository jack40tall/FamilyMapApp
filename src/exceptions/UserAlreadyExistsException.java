package exceptions;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String error) {
        super(error);
    }
}

