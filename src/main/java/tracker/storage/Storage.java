package tracker.storage;

import java.util.List;
import java.util.Map;
import tracker.model.Task;
import tracker.model.User;

public interface Storage {
    void saveUsers(List<User> users);
    List<User> loadUsers();
    void saveTasks(List<Task> tasks);
    List<Task> loadTasks(Map<String, User> users);
}