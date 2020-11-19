package request_result;

import model.Person;

import java.util.Arrays;
import java.util.Objects;

/**
 * Creates a PersonTreeResult Object, which stores a java object ready to be converted to json and sent back to client
 */
public class PersonTreeResult {
    private Person[] data;
    private String message;
    private boolean success;

    public PersonTreeResult(Person[] data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public PersonTreeResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonTreeResult)) return false;
        PersonTreeResult that = (PersonTreeResult) o;
        return success == that.success &&
                Arrays.equals(data, that.data) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(message, success);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
