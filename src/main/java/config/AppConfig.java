package config;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import controllers.ReceiptController;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import middleware.ReceiptMapper;
import middleware.ReceiptValidator;
import repository.ReceiptRepository;
import services.ReceiptService;

import java.util.Map;

public class AppConfig {
    private final Javalin app;
    private final ReceiptController receiptController;

    public AppConfig() {
        // initialize relevant components:
        ReceiptRepository repository = new ReceiptRepository();
        ReceiptValidator validator = new ReceiptValidator();
        ReceiptMapper mapper = new ReceiptMapper();

        ReceiptService receiptService = new ReceiptService(repository, validator, mapper);

        this.receiptController = new ReceiptController(receiptService);

        // configure Javalin:
        this.app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson());
            config.registerPlugin(new OpenApiPlugin(pluginConfig -> {
                pluginConfig.withDefinitionConfiguration((version, definition) -> {
                    definition.withOpenApiInfo(info -> info.setTitle("Fetch Rewards Challenge: Receipt Processor v1.0.0"));
                });
            }));
            config.registerPlugin(new SwaggerPlugin());
        });

        configureExceptionHandler();
    }

    public void start(int port) {
        receiptController.registerRoutes(app);
        app.start(port);
    }

    private void configureExceptionHandler() {
        app.exception(BadRequestException.class, (e, ctx) -> ctx.status(400).json(Map.of("error", e.getMessage())));

        app.exception(NotFoundException.class, (e, ctx) -> ctx.status(404).json(Map.of("error", e.getMessage())));
    }
}
