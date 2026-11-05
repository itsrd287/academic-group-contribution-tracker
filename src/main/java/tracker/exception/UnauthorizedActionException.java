package tracker.exception;

public class UnauthorizedActionException extends TrackerException {
    public UnauthorizedActionException(String message) {
        super(message, 403);
    }
}