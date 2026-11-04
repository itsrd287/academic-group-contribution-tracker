package tracker.model;

public class Lecturer extends User {
    public Lecturer(String name) {
        super(name);
    }

    @Override
    public String getRole() {
        return "LECTURER";
    }

    @Override
    public boolean canAssignTasks() {
        return false;
    }

    @Override
    public boolean canUpdateProgress() {
        return false; // view only
    }
}