package tracker.model;

// Anything that has progress and can be completed
public interface Trackable {
    int getProgress();
    boolean isComplete();
}