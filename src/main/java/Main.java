import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        var app = Javalin.create().get("/", ctx -> ctx.result("Hello World")).start(7070);
    }
}
