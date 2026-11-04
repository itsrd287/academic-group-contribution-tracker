package tracker.model;

public class GroupLeader extends User {
    public GroupLeader(String name) {
        super(name);
    }

    @Override
    public String getRole() {
        return "LEADER";
    }

    @Override
    public boolean canAssignTasks() {
        return true;
    }

    @Override
    public boolean canUpdateProgress() {
        return true;
    }
}