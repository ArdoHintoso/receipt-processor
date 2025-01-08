import controllers.ReceiptController;
import exceptions.ApiException;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import utils.PointsCalculator;
import services.ReceiptService;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ReceiptController receiptController = new ReceiptController(new ReceiptService(new PointsCalculator()));

        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson());
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost();
                });
            });
        });

        app.post("/receipts/process", receiptController::processReceipt);
        app.get("/receipts/{id}/points", receiptController::getPoints);

        app.exception(ApiException.class, (e, ctx) -> {
            ctx.status(e.getStatusCode()).json(Map.of("error", e.getMessage()));
        });

        app.start(7070);
    }
}
