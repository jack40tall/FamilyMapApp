package exceptions;

public class DataAccessException extends Exception{
    /**
     * Thrown when there is a failure in accessing the database
     * @param message the error message
     */
    public DataAccessException(String message)
    {
        super(message);
    }

    public DataAccessException()
    {
        super();
    }
}
