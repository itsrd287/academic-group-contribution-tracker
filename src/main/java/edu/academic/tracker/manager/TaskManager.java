package edu.academic.tracker.manager;

import edu.academic.tracker.domain.Contribution;
import edu.academic.tracker.domain.DomainException;
import edu.academic.tracker.domain.GroupLeader;
import edu.academic.tracker.domain.Project;
import edu.academic.tracker.domain.ReviewStatus;
import edu.academic.tracker.domain.Student;
import edu.academic.tracker.domain.Task;
import edu.academic.tracker.domain.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class TaskManager {
    private final ProjectManager projectManager;

    public TaskManager(ProjectManager projectManager) {
        this.projectManager = Objects.requireNonNull(projectManager, "Project manager cannot be null");
    }

    public Task createTask(
            String projectId,
            GroupLeader leader,
            String assigneeId,
            String title,
            String description,
            LocalDate dueDate) {
        Project project = projectManager.getProject(projectId);
        projectManager.requireLeader(project, leader);
        project.getMember(assigneeId);
        Task task = new Task(
                UUID.randomUUID().toString(), projectId, title, description, assigneeId, dueDate);
        project.addTask(task);
        return task;
    }

    public Contribution updateProgress(
            String projectId, String taskId, Student student, int progressPercent, String summary) {
        Objects.requireNonNull(student, "Student cannot be null");
        Project project = projectManager.getProject(projectId);
        Task task = project.getTask(taskId);
        requireAssignee(task, student);
        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new DomainException("A completed task cannot be updated");
        }
        Contribution contribution = new Contribution(
                UUID.randomUUID().toString(),
                projectId,
                taskId,
                student.getId(),
                summary,
                LocalDateTime.now());
        task.updateProgress(progressPercent);
        student.recordContribution(contribution);
        return contribution;
    }

    public Contribution markCompleted(String projectId, String taskId, Student student, String summary) {
        Objects.requireNonNull(student, "Student cannot be null");
        Project project = projectManager.getProject(projectId);
        Task task = project.getTask(taskId);
        requireAssignee(task, student);
        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new DomainException("Task is already completed");
        }
        Contribution contribution = new Contribution(
                UUID.randomUUID().toString(),
                projectId,
                taskId,
                student.getId(),
                summary,
                LocalDateTime.now());
        task.markCompleted();
        student.recordContribution(contribution);
        return contribution;
    }

    public Task reviewCompletedTask(
            String projectId, String taskId, GroupLeader leader, boolean approved) {
        Project project = projectManager.getProject(projectId);
        projectManager.requireLeader(project, leader);
        Task task = project.getTask(taskId);
        task.review(approved ? ReviewStatus.APPROVED : ReviewStatus.NEEDS_CHANGES);
        return task;
    }

    private void requireAssignee(Task task, Student student) {
        if (!task.getAssigneeId().equals(student.getId())) {
            throw new DomainException("Only the assigned student can update this task");
        }
    }
}
