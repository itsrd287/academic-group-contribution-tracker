# academic-group-contribution-tracker
A Java-based academic group project contribution tracking system.

# Academic Group Contribution Tracker

## Problem Statement
Group assignments often suffer from unclear task ownership and no visible record of who did what. This system lets a group leader assign tasks to members, lets members update their progress, and lets a lecturer monitor each member's contribution and completion status.

## How to Run
**Requirements:** JDK 17+ and Maven.

```bash
git clone https://github.com/<itsrd287>/academic-group-contribution-tracker.git
cd academic-group-contribution-tracker
mvn compile exec:java
```

Then open http://localhost:7070 in your browser. Data is saved automatically in the `data/` folder.

## Classes Used

| Package | Class / Interface | Role |
|---|---|---|
| `App` | Starts Javalin and defines the REST endpoints |
| `User` (abstract) | Base class for all users |
 | `Member` | Can update progress on own tasks |
 | `GroupLeader` | Can assign tasks and update any task |
 | `Trackable` (interface) | Contract for progress and completion |
 `Lecturer` | View-only access |
 | `Task` | A task with an assignee, progress and status |
 | `TrackerService` | Core actions, permission checks, reports |
 | `Storage` (interface) | Contract for saving and loading data |
 | `FileStorage` | Saves data to text files |
 | `TrackerException` | Base custom exception |
 | `TaskNotFoundException` | Task ID does not exist (404) |
| `UnauthorizedActionException` | Role not allowed to do the action (403) |
 | `InvalidInputException` | Bad or missing input (400) |