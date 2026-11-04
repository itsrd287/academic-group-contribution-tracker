package edu.academic.tracker.domain;

public final class Lecturer extends User {
    public Lecturer(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public UserRole getRole() {
        return UserRole.LECTURER;
    }
}
