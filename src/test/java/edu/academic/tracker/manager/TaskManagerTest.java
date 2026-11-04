package edu.academic.tracker.manager;

import edu.academic.tracker.domain.DomainException;
import edu.academic.tracker.domain.GroupLeader;
import edu.academic.tracker.domain.Lecturer;
import edu.academic.tracker.domain.ReviewStatus;
import edu.academic.tracker.domain.Student;
import edu.academic.tracker.domain.TaskStatus;
import edu.academic.tracker.domain.User;
import edu.academic.tracker.domain.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskManagerTest {
    private GroupLeader leader;
    private Student student;
    private ProjectManager projectManager;
    private TaskManager taskManager;
    private String projectId;

    @BeforeEach
    void setUp() {
        leader = new GroupLeader("leader-1", "Amina Leader", "amina@example.edu");
        student = new Student("student-1", "Brian Student", "brian@example.edu");
        projectManager = new ProjectManager();
        taskManager = new TaskManager(projectManager);
        projectId = projectManager.createProject(leader, "Study App", "Course group project").getId();
        projectManager.addMember(projectId, leader, student);
    }

    @Test
    void modelsRoleInheritanceAndPolymorphism() {
        List<User> users = List.of(leader, student, new Lecturer("lecturer-1", "Dr. Lee", "lee@example.edu"));

        assertEquals(
                List.of(UserRole.GROUP_LEADER, UserRole.STUDENT, UserRole.LECTURER),
                users.stream().map(User::getRole).toList());
    }

    @Test
    void recordsProgressAndCompletionContributions() {
        var task = taskManager.createTask(
                projectId, leader, student.getId(), "Design screen", "Create initial mockup",
                LocalDate.now().plusDays(3));

        taskManager.updateProgress(projectId, task.getId(), student, 40, "Created the first layout");
        taskManager.markCompleted(projectId, task.getId(), student, "Finished the layout");

        assertEquals(TaskStatus.COMPLETED, task.getStatus());
        assertEquals(2, student.getContributions().size());
        assertEquals(projectId, student.getContributions().get(0).getProjectId());
    }

    @Test
    void restrictsTaskUpdatesToAssigneeAndProjectLeader() {
        Student otherStudent = new Student("student-2", "Casey Student", "casey@example.edu");
        projectManager.addMember(projectId, leader, otherStudent);
        var task = taskManager.createTask(
                projectId, leader, student.getId(), "Design screen", "Create initial mockup",
                LocalDate.now().plusDays(3));

        assertThrows(DomainException.class,
                () -> taskManager.updateProgress(projectId, task.getId(), otherStudent, 50, "Not assigned"));
        assertThrows(DomainException.class,
                () -> taskManager.createTask(
                        projectId, new GroupLeader("leader-2", "Other Leader", "other@example.edu"),
                        student.getId(), "Wrong owner", "Should fail", LocalDate.now().plusDays(2)));
        assertEquals(0, otherStudent.getContributions().size());
    }

    @Test
    void letsProjectLeaderReviewCompletedWork() {
        var task = taskManager.createTask(
                projectId, leader, student.getId(), "Design screen", "Create initial mockup",
                LocalDate.now().plusDays(3));
        taskManager.markCompleted(projectId, task.getId(), student, "Finished the layout");

        taskManager.reviewCompletedTask(projectId, task.getId(), leader, false);

        assertEquals(ReviewStatus.NEEDS_CHANGES, task.getReviewStatus());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertEquals(100, task.getProgressPercent());
    }

    @Test
    void rejectsInvalidProgressWithoutChangingTask() {
        var task = taskManager.createTask(
                projectId, leader, student.getId(), "Design screen", "Create initial mockup",
                LocalDate.now().plusDays(3));

        assertThrows(DomainException.class,
                () -> taskManager.updateProgress(projectId, task.getId(), student, 101, "Invalid progress"));
        assertEquals(0, task.getProgressPercent());
        assertEquals(0, student.getContributions().size());
    }
}
