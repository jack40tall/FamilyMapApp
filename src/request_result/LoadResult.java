package request_result;

import java.util.Objects;

/**
 * Creates a LoadResult Object, which stores a java object ready to be converted to json and sent back to client
 */
public class LoadResult {
    private String message;
    private  boolean success;

    public LoadResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoadResult)) return false;
        LoadResult that = (LoadResult) o;
        return success == that.success &&
                message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, success);
    }
}
