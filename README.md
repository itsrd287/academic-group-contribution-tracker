# Academic Group Contribution Tracker

## Project proposal

### Problem statement

In academic group projects, responsibilities are divided among students, but it can be difficult to see who is responsible for each task, how work is progressing, and what each member has contributed. Updates spread across messages and documents can leave group leaders and supervisors without a reliable view of project progress or completion.

The Academic Group Contribution Tracker will provide a shared place to organize projects, assigned tasks, progress updates, and individual contribution records.

### Users and core actions

| User | Core actions |
| --- | --- |
| Student / group member | View assigned tasks, update task progress, mark assigned work complete, and view personal contribution records. |
| Group leader | Create and manage projects, add members, assign tasks, monitor member progress, and review completed work. |
| Lecturer / supervisor | View registered projects, monitor progress, inspect member contributions, and review project completion reports. |

### Main classes and relationships

- `User` is an abstract base class for common identity and contact information.
- `Student` and `Lecturer` extend `User`; `GroupLeader` extends `Student`, since a leader is also a project group member.
- `ContributionTrackable` describes users whose contributions can be recorded. `Student` implements it.
- `Project` contains project members and tasks. A `ProjectMember` associates a student with a project.
- `Task` stores its assignee, progress, completion state, and review state.
- `Contribution` records a student's activity on a task.
- `ProjectManager` and `TaskManager` coordinate project membership, task assignment, progress updates, and reviews.
- `DomainException` represents invalid operations in the domain.

### Object-oriented principles

- **Encapsulation:** fields are private; state changes go through validated methods, and collection getters expose read-only views.
- **Inheritance:** `Student` and `Lecturer` share common `User` state; `GroupLeader` specializes `Student`.
- **Abstraction and interfaces:** `User` is abstract, and `ContributionTrackable` defines contribution-recording behavior.
- **Polymorphism:** users provide role-specific behavior through `getRole()`, and students (including group leaders) can be handled through the `ContributionTrackable` contract.
- **Custom exceptions:** `DomainException` reports invalid domain operations such as assigning work to a non-member.
- **Collections:** projects manage members and tasks; students manage contribution records.

## Planned workflow

1. A group leader creates a project.
2. The leader adds students to the project.
3. The leader creates tasks and assigns them to project members.
4. Members update progress and mark their tasks complete.
5. The leader reviews completed work; contribution records support progress monitoring.
6. Lecturers inspect project progress, member contributions, and completion reports.

## Development roadmap

Work will be delivered in small, reviewable increments on feature branches and committed step by step:

1. **Domain foundation (current increment):** Java OOP classes, manager operations, and focused tests.
2. **API:** Javalin endpoints for the student, group leader, and lecturer workflows.
3. **Persistence:** save and load projects and contribution data.
4. **Frontend:** a clear user interface for the three user roles.
5. **Integration and polish:** end-to-end verification, documentation, and collaboration-friendly repository checks.

## Build and test

The project uses Java 17 or newer and Maven.

```powershell
mvn test
```
