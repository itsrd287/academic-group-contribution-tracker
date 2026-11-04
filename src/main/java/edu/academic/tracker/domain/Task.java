package edu.academic.tracker.domain;

import java.time.LocalDate;
import java.util.Objects;

public final class Task {
    private final String id;
    private final String projectId;
    private final String title;
    private final String description;
    private final String assigneeId;
    private final LocalDate dueDate;
    private int progressPercent;
    private TaskStatus status;
    private ReviewStatus reviewStatus;

    public Task(
            String id,
            String projectId,
            String title,
            String description,
            String assigneeId,
            LocalDate dueDate) {
        this.id = User.requireText(id, "Task id");
        this.projectId = User.requireText(projectId, "Project id");
        this.title = User.requireText(title, "Task title");
        this.description = User.requireText(description, "Task description");
        this.assigneeId = User.requireText(assigneeId, "Assignee id");
        this.dueDate = Objects.requireNonNull(dueDate, "Due date cannot be null");
        this.progressPercent = 0;
        this.status = TaskStatus.PENDING;
        this.reviewStatus = ReviewStatus.NOT_REVIEWED;
    }

    public String getId() {
        return id;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public int getProgressPercent() {
        return progressPercent;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public ReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public void updateProgress(int progressPercent) {
        if (status == TaskStatus.COMPLETED) {
            throw new DomainException("A completed task cannot be updated");
        }
        if (progressPercent < 0 || progressPercent > 100) {
            throw new DomainException("Progress must be between 0 and 100");
        }
        this.progressPercent = progressPercent;
        this.status = progressPercent == 0 ? TaskStatus.PENDING : TaskStatus.IN_PROGRESS;
    }

    public void markCompleted() {
        if (status == TaskStatus.COMPLETED) {
            throw new DomainException("Task is already completed");
        }
        progressPercent = 100;
        status = TaskStatus.COMPLETED;
        reviewStatus = ReviewStatus.NOT_REVIEWED;
    }

    public void review(ReviewStatus reviewStatus) {
        Objects.requireNonNull(reviewStatus, "Review status cannot be null");
        if (status != TaskStatus.COMPLETED) {
            throw new DomainException("Only completed tasks can be reviewed");
        }
        if (reviewStatus == ReviewStatus.NOT_REVIEWED) {
            throw new DomainException("A review must approve the task or request changes");
        }
        this.reviewStatus = reviewStatus;
        if (reviewStatus == ReviewStatus.NEEDS_CHANGES) {
            status = TaskStatus.IN_PROGRESS;
        }
    }
}
