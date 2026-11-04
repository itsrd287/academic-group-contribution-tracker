package edu.academic.tracker.manager;

import edu.academic.tracker.domain.DomainException;
import edu.academic.tracker.domain.GroupLeader;
import edu.academic.tracker.domain.Project;
import edu.academic.tracker.domain.ProjectMember;
import edu.academic.tracker.domain.Student;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class ProjectManager {
    private final Map<String, Project> projects = new LinkedHashMap<>();

    public Project createProject(GroupLeader leader, String name, String description) {
        Objects.requireNonNull(leader, "Group leader cannot be null");
        Project project = new Project(
                UUID.randomUUID().toString(), name, description, leader.getId(), LocalDate.now());
        project.addMember(new ProjectMember(leader, LocalDate.now()));
        projects.put(project.getId(), project);
        return project;
    }

    public Project addMember(String projectId, GroupLeader leader, Student student) {
        Objects.requireNonNull(leader, "Group leader cannot be null");
        Objects.requireNonNull(student, "Student cannot be null");
        Project project = getProject(projectId);
        requireLeader(project, leader);
        project.addMember(new ProjectMember(student, LocalDate.now()));
        return project;
    }

    public Project getProject(String projectId) {
        Project project = projects.get(projectId);
        if (project == null) {
            throw new DomainException("Project does not exist");
        }
        return project;
    }

    public Collection<Project> getProjects() {
        return java.util.List.copyOf(projects.values());
    }

    public void requireLeader(Project project, GroupLeader leader) {
        if (!project.getLeaderId().equals(leader.getId())) {
            throw new DomainException("Only the project leader can manage this project");
        }
    }
}
