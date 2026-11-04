package tracker.model;

import tracker.exception.InvalidInputException;

public class Task implements Trackable {
    private final int id;
    private final String title;
    private final User assignee; // the responsible member
    private int progress;        // 0 to 100

    public Task(int id, String title, User assignee, int progress) {
        this.id = id;
        if (title == null || title.isBlank()) {
            throw new InvalidInputException("Task title cannot be blank");
        }
        this.title = title.trim();
        if (assignee == null) {
            throw new InvalidInputException("Task assignee cannot be null");
        }
        this.assignee = assignee;
        updateProgress(progress);
    }

    public void updateProgress(int progress) {
        if (progress < 0 || progress > 100) {
            throw new InvalidInputException("Progress must be between 0 and 100");
        }
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getAssignee() {
        return assignee;
    }

    @Override
    public int getProgress() {
        return progress;
    }

    @Override
    public boolean isComplete() {
        return progress == 100;
    }

    public String getStatus() {
        if (progress == 0) return "NOT STARTED";
        if (isComplete()) return "COMPLETED";
        return "IN PROGRESS";
    }
}