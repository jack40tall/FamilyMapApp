package request_result;

/**
 * Creates a FillRequest Object, which is converted Json data stored in a usable Java object
 */
public class FillRequest {
    private int generations;
    private String username;

    public FillRequest(int generations, String username) {
        this.generations = generations;
        this.username = username;
    }

    public int getGenerations() {
        return generations;
    }

    public String getUsername() {
        return username;
    }
}
