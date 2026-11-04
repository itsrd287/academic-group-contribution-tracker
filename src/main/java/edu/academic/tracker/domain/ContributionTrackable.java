package edu.academic.tracker.domain;

import java.util.List;

public interface ContributionTrackable {
    List<Contribution> getContributions();

    void recordContribution(Contribution contribution);
}
