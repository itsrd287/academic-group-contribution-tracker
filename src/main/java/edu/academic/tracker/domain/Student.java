package edu.academic.tracker.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Student extends User implements ContributionTrackable {
    private final List<Contribution> contributions = new ArrayList<>();

    public Student(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public UserRole getRole() {
        return UserRole.STUDENT;
    }

    @Override
    public List<Contribution> getContributions() {
        return List.copyOf(contributions);
    }

    @Override
    public void recordContribution(Contribution contribution) {
        Objects.requireNonNull(contribution, "Contribution cannot be null");
        boolean alreadyRecorded = contributions.stream()
                .anyMatch(existing -> existing.getId().equals(contribution.getId()));
        if (alreadyRecorded) {
            throw new DomainException("Contribution is already recorded");
        }
        if (!contribution.getMemberId().equals(getId())) {
            throw new DomainException("Contribution belongs to a different student");
        }
        contributions.add(contribution);
    }
}
