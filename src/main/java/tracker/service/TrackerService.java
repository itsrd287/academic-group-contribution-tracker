package tracker.service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import tracker.exception.InvalidInputException;
import tracker.exception.TaskNotFoundException;
import tracker.exception.UnauthorizedActionException;
import tracker.model.GroupLeader;
import tracker.model.Lecturer;
import tracker.model.Member;
import tracker.model.Task;
import tracker.model.User;
import tracker.storage.Storage;
public class TrackerService {
    private final Storage storage;
    private final Map<String, User> users = new LinkedHashMap<>();
    private final List<Task> tasks = new ArrayList<>();
    public TrackerService(Storage storage) {
        this.storage = storage;
        List<User> loaded = storage.loadUsers();
        if (loaded.isEmpty()) {
            loaded = List.of(
                new GroupLeader("Stacey"),
                new Member("Bukata"),
                new Member("Clement"),
                new Member("Emmanuel"),
                new Lecturer("Mr. Jacob C")
            );
            storage.saveUsers(loaded);
        }
        for (User u : loaded) {
            users.put(u.getName(), u);
        }
        tasks.addAll(storage.loadTasks(users));
    }
    public Collection<User> getUsers() {
        return users.values();
    }
    public List<Task> getTasks() {
        return tasks;
    }
    // Core action 1: leader assigns a task to a member
    public Task assignTask(String actorName, String title, String assigneeName) {
        User actor = findUser(actorName);
        if (!actor.canAssignTasks()) {
            throw new UnauthorizedActionException(
                "Only the group leader can assign tasks (you are " + actor.getRole() + ")"
            );
        }
        if (title == null || title.isBlank()) {
            throw new InvalidInputException("Task title is required");
        }
        User assignee = findUser(assigneeName);
        if (!assignee.canUpdateProgress()) {
            throw new InvalidInputException(
                assignee.getName() + " cannot be assigned tasks"
            );
        }
        Task task = new Task(nextId(), title.trim(), assignee, 0);
        tasks.add(task);
        storage.saveTasks(tasks);
        return task;
    }
    // Core action 2: update progress (100 = completed)
    public Task updateProgress(String actorName, int taskId, int progress) {
        User actor = findUser(actorName);
        Task task = findTask(taskId);
        boolean isOwner = task.getAssignee().getName().equals(actor.getName());
        if (!actor.canUpdateProgress() || !(isOwner || actor.canAssignTasks())) {
            throw new UnauthorizedActionException(
                "You can only update your own tasks"
            );
        }
        task.updateProgress(progress);
        storage.saveTasks(tasks);
        return task;
    }
    // Core action 3: leader deletes a task
    public void deleteTask(String actorName, int taskId) {
        User actor = findUser(actorName);
        if (!actor.canAssignTasks()) {
            throw new UnauthorizedActionException(
                "Only the group leader can delete tasks"
            );
        }
        Task task = findTask(taskId);
        tasks.remove(task);
        storage.saveTasks(tasks);
    }
    // Core action 4: report of average progress per member
    public Map<String, Double> averageProgressByMember() {
        Map<String, Double> report = new LinkedHashMap<>();
        for (User u : users.values()) {
            if (!u.canUpdateProgress()) continue;
            double total = 0;
            int count = 0;
            for (Task t : tasks) {
                if (t.getAssignee() == u) {
                    total += t.getProgress();
                    count++;
                }
            }
            report.put(u.getName(), count == 0 ? 0 : total / count);
        }
        return report;
    }
    private User findUser(String name) {
        User user = users.get(name);
        if (user == null) {
            throw new InvalidInputException("Unknown user: " + name);
        }
        return user;
    }
    private Task findTask(int id) {
        for (Task t : tasks) {
            if (t.getId() == id) {
                return t;
            }
        }
        throw new TaskNotFoundException(id);
    }
    private int nextId() {
        int max = 0;
        for (Task t : tasks) {
            max = Math.max(max, t.getId());
        }
        return max + 1;
    }
}