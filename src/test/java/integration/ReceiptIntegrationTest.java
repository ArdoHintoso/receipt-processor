package integration;

import constants.RewardsRules;
import controllers.ReceiptController;
import domain.Receipt;
import dto.ReceiptDTO;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import middleware.ReceiptMapper;
import middleware.ReceiptValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.ReceiptRepository;
import services.ReceiptService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

class ReceiptIntegrationTest {
    private static Javalin app;
    private static ReceiptRepository repository;
    private static ReceiptController receiptController;

    @BeforeAll
    static void setUp() {
        repository = new ReceiptRepository();
        ReceiptValidator validator = new ReceiptValidator();
        ReceiptMapper mapper = new ReceiptMapper();
        ReceiptService receiptService = new ReceiptService(repository, validator, mapper);
        receiptController = new ReceiptController(receiptService);

        app = Javalin.create(config -> config.router.apiBuilder(() -> path("receipts", () -> {
            post(receiptController::processReceipt);
            get("/{id}/points", receiptController::getPoints);
        }))).start(7000);

        app.exception(BadRequestException.class, (e, ctx) -> ctx.status(400).json(Map.of("error", e.getMessage())));

        app.exception(NotFoundException.class, (e, ctx) -> ctx.status(404).json(Map.of("error", e.getMessage())));

        // Configure Unirest to use localhost for integration tests
        Unirest.config().defaultBaseUrl("http://localhost:7000");
    }

    @AfterAll
    static void tearDown() {
        app.stop();
        Unirest.shutDown();
    }

    @Test
    void processReceipt_ValidReceipt_ReturnsId() {
        // Arrange
        ReceiptDTO receiptDTO = new ReceiptDTO(
                "Target",
                "2022-01-01",
                "13:01",
                "35.35",
                List.of(
                        new ReceiptDTO.ItemDTO("Mountain Dew 12PK", "6.49"),
                        new ReceiptDTO.ItemDTO("Emils Cheese Pizza", "12.25"),
                        new ReceiptDTO.ItemDTO("Knorr Creamy Chicken", "1.26"),
                        new ReceiptDTO.ItemDTO("Doritos Nacho Cheese", "3.35"),
                        new ReceiptDTO.ItemDTO("   Klarbrunn 12-PK   ", "12.00")
                )
        );

        // Act
        HttpResponse<JsonNode> response = Unirest.post("/receipts")
                .header("Content-Type", "application/json")
                .body(receiptDTO)
                .asJson();

        // Assert
        assertEquals(200, response.getStatus());
        assertNotNull(response.getBody());
        String receiptId = response.getBody().getObject().getString("id");
        assertNotNull(receiptId);
    }

    @Test
    void getPoints_ValidId_ReturnsPoints() {
        // Arrange
        String validId = "valid-id";
        Receipt receipt = new Receipt(
                validId,
                "Target",
                LocalDate.parse("2022-01-01", RewardsRules.DATE_FORMATTER),
                LocalTime.parse("13:01", RewardsRules.TIME_FORMATTER),
                List.of(),
                35.35,
                100
        );
        repository.save(validId, receipt);

        // Act
        HttpResponse<JsonNode> response = Unirest.get("/receipts/" + validId + "/points")
                .asJson();

        // Assert
        assertEquals(200, response.getStatus());
        assertNotNull(response.getBody());
        int points = response.getBody().getObject().getInt("points");
        assertEquals(100, points);
    }

    @Test
    void getPoints_InvalidId_Returns404() {
        // Arrange
        String invalidId = "invalid-id-for-integration-test";

        // Act
        HttpResponse<JsonNode> response = Unirest.get("/receipts/" + invalidId + "/points")
                .asJson();

        // Assert
        assertEquals(404, response.getStatus());
    }
}