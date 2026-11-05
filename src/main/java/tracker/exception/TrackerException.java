package tracker.exception;

// Base class for all our custom exceptions
public class TrackerException extends RuntimeException {
    private final int status; // HTTP status code to send back

    public TrackerException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}