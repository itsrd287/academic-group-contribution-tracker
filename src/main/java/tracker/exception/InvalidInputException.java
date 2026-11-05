package tracker.exception;

public class InvalidInputException extends TrackerException {
    public InvalidInputException(String message) {
        super(message, 400);
    }
}