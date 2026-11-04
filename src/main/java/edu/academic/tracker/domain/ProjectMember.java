package edu.academic.tracker.domain;

import java.time.LocalDate;
import java.util.Objects;

public final class ProjectMember {
    private final Student student;
    private final LocalDate joinedOn;

    public ProjectMember(Student student, LocalDate joinedOn) {
        this.student = Objects.requireNonNull(student, "Student cannot be null");
        this.joinedOn = Objects.requireNonNull(joinedOn, "Join date cannot be null");
    }

    public Student getStudent() {
        return student;
    }

    public String getStudentId() {
        return student.getId();
    }

    public LocalDate getJoinedOn() {
        return joinedOn;
    }
}
