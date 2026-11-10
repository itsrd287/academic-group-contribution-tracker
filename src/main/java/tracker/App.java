package tracker;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.util.Map;
import tracker.exception.TrackerException;
import tracker.model.Task;
import tracker.service.TrackerService;
import tracker.storage.FileStorage;

public class App {
    public static void main(String[] args) {
        TrackerService service = new TrackerService(new FileStorage());

        Javalin app = Javalin.create(config ->
            config.staticFiles.add("/public", Location.CLASSPATH));

        app.get("/api/users", ctx -> ctx.json(service.getUsers()));
        app.get("/api/tasks", ctx -> ctx.json(service.getTasks()));
        app.get("/api/report", ctx -> ctx.json(service.averageProgressByMember()));

        app.post("/api/tasks", ctx -> {
            Task task = service.assignTask(
                ctx.formParam("actor"), ctx.formParam("title"), ctx.formParam("assignee"));
            ctx.status(201).json(task);
        });

        app.post("/api/tasks/{id}/progress", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            int progress = Integer.parseInt(ctx.formParam("progress"));
            ctx.json(service.updateProgress(ctx.formParam("actor"), id, progress));
        });

        // Delete a task (group leader only)
        app.delete("/api/tasks/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            service.deleteTask(ctx.queryParam("actor"), id);
            ctx.status(204);
        });

        // Handle custom exceptions
        app.exception(TrackerException.class, (e, ctx) ->
            ctx.status(e.getStatus()).json(Map.of("error", e.getMessage())));

        app.exception(NumberFormatException.class, (e, ctx) ->
            ctx.status(400).json(Map.of("error", "ID and progress must be whole numbers")));

        app.start(7070);
    }
}