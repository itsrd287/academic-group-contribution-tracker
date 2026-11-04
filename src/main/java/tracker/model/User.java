package tracker.model;

public abstract class User {
    private final String name; // encapsulated: only readable through getName()

    protected User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Each role must define these (polymorphism)
    public abstract String getRole();
    public abstract boolean canAssignTasks();
    public abstract boolean canUpdateProgress();
}
