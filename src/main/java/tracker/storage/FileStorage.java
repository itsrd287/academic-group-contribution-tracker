package tracker.storage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import tracker.model.GroupLeader;
import tracker.model.Lecturer;
import tracker.model.Member;
import tracker.model.Task;
import tracker.model.User;

public class FileStorage implements Storage {
    private final Path usersFile = Paths.get("data", "users.txt");
    private final Path tasksFile = Paths.get("data", "tasks.txt");

    @Override
    public void saveUsers(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User u : users) {
            lines.add(u.getName() + "|" + u.getRole());
        }
        write(usersFile, lines);
    }

    @Override
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        for (String line : read(usersFile)) {
            String[] p = line.split("\\|");
            switch (p[1]) {
                case "LEADER" -> users.add(new GroupLeader(p[0]));
                case "LECTURER" -> users.add(new Lecturer(p[0]));
                default -> users.add(new Member(p[0]));
            }
        }
        return users;
    }

    @Override
    public void saveTasks(List<Task> tasks) {
        List<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            String title = t.getTitle().replace("|", " ");
            lines.add(t.getId() + "|" + title + "|" + t.getAssignee().getName() + "|" + t.getProgress());
        }
        write(tasksFile, lines);
    }

    @Override
    public List<Task> loadTasks(Map<String, User> users) {
        List<Task> tasks = new ArrayList<>();
        for (String line : read(tasksFile)) {
            String[] p = line.split("\\|");
            User assignee = users.get(p[2]);
            if (assignee != null) {
                tasks.add(new Task(Integer.parseInt(p[0]), p[1], assignee, Integer.parseInt(p[3])));
            }
        }
        return tasks;
    }

    private void write(Path file, List<String> lines) {
        try {
            Files.createDirectories(file.getParent());
            Files.write(file, lines);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private List<String> read(Path file) {
        try {
            List<String> lines = new ArrayList<>();
            if (Files.exists(file)) {
                for (String line : Files.readAllLines(file)) {
                    if (!line.isBlank()) lines.add(line);
                }
            }
            return lines;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}