package exceptions;

public class IncorrectCredentialsException extends Exception {
    public IncorrectCredentialsException(String error) {
        super(error);
    }
}

