package edu.academic.tracker.domain;

public final class GroupLeader extends Student {
    public GroupLeader(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public UserRole getRole() {
        return UserRole.GROUP_LEADER;
    }
}
