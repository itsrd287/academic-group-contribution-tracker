package tracker.exception;

public class TaskNotFoundException extends TrackerException {
    public TaskNotFoundException(int id) {
        super("Task " + id + " not found", 404);
    }
}