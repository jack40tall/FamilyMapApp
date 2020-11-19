package exceptions;

public class IncorrectUserException extends Exception{
    public IncorrectUserException(String error) {
        super(error);
    }
}
