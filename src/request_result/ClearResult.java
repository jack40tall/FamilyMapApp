package request_result;

import java.util.Objects;

/**
 * Creates a ClearResult Object, which stores a java object ready to be converted to json and sent back to client
 */
public class ClearResult {
    private String message;
    private boolean success;

    public ClearResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClearResult)) return false;
        ClearResult that = (ClearResult) o;
        return success == that.success &&
                message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, success);
    }
}
