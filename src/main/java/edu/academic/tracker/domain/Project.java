package edu.academic.tracker.domain;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class Project {
    private final String id;
    private final String name;
    private final String description;
    private final String leaderId;
    private final LocalDate createdOn;
    private final Map<String, ProjectMember> members = new LinkedHashMap<>();
    private final Map<String, Task> tasks = new LinkedHashMap<>();

    public Project(String id, String name, String description, String leaderId, LocalDate createdOn) {
        this.id = User.requireText(id, "Project id");
        this.name = User.requireText(name, "Project name");
        this.description = User.requireText(description, "Project description");
        this.leaderId = User.requireText(leaderId, "Leader id");
        this.createdOn = Objects.requireNonNull(createdOn, "Creation date cannot be null");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public Collection<ProjectMember> getMembers() {
        return java.util.List.copyOf(members.values());
    }

    public Collection<Task> getTasks() {
        return java.util.List.copyOf(tasks.values());
    }

    public boolean hasMember(String studentId) {
        return members.containsKey(studentId);
    }

    public ProjectMember getMember(String studentId) {
        ProjectMember member = members.get(studentId);
        if (member == null) {
            throw new DomainException("Student is not a member of this project");
        }
        return member;
    }

    public Task getTask(String taskId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new DomainException("Task does not exist in this project");
        }
        return task;
    }

    public void addMember(ProjectMember member) {
        Objects.requireNonNull(member, "Project member cannot be null");
        if (members.putIfAbsent(member.getStudentId(), member) != null) {
            throw new DomainException("Student is already a member of this project");
        }
    }

    public void addTask(Task task) {
        Objects.requireNonNull(task, "Task cannot be null");
        if (!id.equals(task.getProjectId())) {
            throw new DomainException("Task belongs to a different project");
        }
        if (!hasMember(task.getAssigneeId())) {
            throw new DomainException("Task assignee must be a project member");
        }
        if (tasks.putIfAbsent(task.getId(), task) != null) {
            throw new DomainException("Task id already exists in this project");
        }
    }
}
