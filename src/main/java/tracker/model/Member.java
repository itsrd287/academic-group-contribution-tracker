package tracker.model;

public class Member extends User {
    public Member(String name) {
        super(name);
    }

    @Override
    public String getRole() {
        return "MEMBER";
    }

    @Override
    public boolean canAssignTasks() {
        return false;
    }

    @Override
    public boolean canUpdateProgress() {
        return true;
    }
}
    

