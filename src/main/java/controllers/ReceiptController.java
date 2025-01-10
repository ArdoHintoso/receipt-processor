package controllers;

import domain.Receipt;
import dto.IdResponseDTO;
import dto.PointsResponseDTO;
import dto.ReceiptDTO;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.openapi.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.slf4j.Logger;
import services.ReceiptService;
import utils.LoggerUtil;

import java.util.Map;

public class ReceiptController {
    private static final Logger logger = LoggerUtil.getLogger(ReceiptController.class);
    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @OpenApi(
            path = "/receipts/process",
            methods = HttpMethod.POST,
            tags = {"Receipts"},
            summary = "Submits a receipt for processing.",
            description = "Submits a receipt for processing.",
            requestBody = @OpenApiRequestBody(
                    required = true,
                    content = @OpenApiContent(from = ReceiptDTO.class),
                    description = "The receipt to process"
            ),
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            content = {@OpenApiContent(from = IdResponseDTO[].class)},
                            description = "Returns the ID assigned to the receipt"
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "The receipt is invalid"
                    )
            }
    )
    public void processReceipt(Context ctx) {
        try {
            logger.info("Received receipt process request");
            ReceiptDTO receiptRequest = ctx.bodyAsClass(ReceiptDTO.class);
            String id = receiptService.processReceipt(receiptRequest);
            ctx.status(200).json(new IdResponseDTO(id));
            logger.info("ID: {} has been fully processed", id);
        } catch (BadRequestException e) {
            logger.error("Bad request processing receipt: {}", e.getMessage());
            ctx.status(400).json(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error processing receipt: {}", e.getMessage());
            ctx.status(500).json(Map.of("error", "Internal server error"));
        }
    }

    @OpenApi(
            path = "/receipts/{id}/points",
            methods = HttpMethod.GET,
            tags = {"Receipts"},
            summary = "Returns the points awarded for the receipt.",
            description = "Returns the points awarded for the receipt.",
            pathParams = {
                    @OpenApiParam(
                            name = "id",
                            description = "The ID of the receipt",
                            required = true,
                            type = String.class
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            content = {@OpenApiContent(from = PointsResponseDTO[].class)},
                            description = "The number of points awarded"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "No receipt found for that ID"
                    )
            }
    )
    public void getPoints(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            logger.info("Received points retrieval request for ID: {}", id);
            int points = receiptService.getPoints(id);
            ctx.status(200).json(new PointsResponseDTO(points));
            logger.info("Successfully retrieved rewards for ID: {}", id);
        } catch (NotFoundException e) {
            logger.error("Receipt not found: {}", e.getMessage());
            ctx.status(404).json(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error retrieving points: {}", e.getMessage());
            ctx.status(500).json(Map.of("error", "Internal server error"));
        }
    }

    public void registerRoutes(Javalin app) {
        app.post("/receipts/process", this::processReceipt);
        app.get("/receipts/{id}/points", this::getPoints);

        logger.info("Receipt routes registered successfully");
    }
}
