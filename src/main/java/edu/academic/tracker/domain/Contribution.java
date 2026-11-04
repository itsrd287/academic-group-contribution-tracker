package edu.academic.tracker.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public final class Contribution {
    private final String id;
    private final String projectId;
    private final String taskId;
    private final String memberId;
    private final String summary;
    private final LocalDateTime recordedAt;

    public Contribution(
            String id,
            String projectId,
            String taskId,
            String memberId,
            String summary,
            LocalDateTime recordedAt) {
        this.id = User.requireText(id, "Contribution id");
        this.projectId = User.requireText(projectId, "Project id");
        this.taskId = User.requireText(taskId, "Task id");
        this.memberId = User.requireText(memberId, "Member id");
        this.summary = User.requireText(summary, "Contribution summary");
        this.recordedAt = Objects.requireNonNull(recordedAt, "Recorded time cannot be null");
    }

    public String getId() {
        return id;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getSummary() {
        return summary;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }
}
