package edu.academic.tracker.domain;

import java.util.Objects;

public abstract class User {
    private final String id;
    private String name;
    private String email;

    protected User(String id, String name, String email) {
        this.id = requireText(id, "User id");
        this.name = requireText(name, "Name");
        this.email = requireText(email, "Email");
    }

    public final String getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final String getEmail() {
        return email;
    }

    public final void updateContactDetails(String name, String email) {
        this.name = requireText(name, "Name");
        this.email = requireText(email, "Email");
    }

    public abstract UserRole getRole();

    static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");
        if (value.isBlank()) {
            throw new DomainException(fieldName + " cannot be blank");
        }
        return value.trim();
    }
}
