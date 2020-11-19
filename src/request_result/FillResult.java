package request_result;

import java.util.Objects;

/**
 * Creates a FillResult Object, which stores a java object ready to be converted to json and sent back to client
 */
public class FillResult {
    String message;
    boolean success;

    public FillResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FillResult)) return false;
        FillResult that = (FillResult) o;
        return success == that.success &&
                message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, success);
    }

    public boolean isSuccess() {
        return success;
    }
}
