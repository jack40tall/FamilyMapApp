package exceptions;

public class EventNotFoundException extends Exception{
    public EventNotFoundException(String error) {
        super(error);
    }
}
