package controllers;

import dto.IdResponseDTO;
import dto.PointsResponseDTO;
import dto.ReceiptDTO;
import io.javalin.http.Context;
import org.slf4j.Logger;
import services.ReceiptService;
import utils.LoggerUtil;

public class ReceiptController {
    private static final Logger logger = LoggerUtil.getLogger(ReceiptController.class);
    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    public void processReceipt(Context ctx) {
        logger.info("Received receipt process request");

        ReceiptDTO receiptRequest = ctx.bodyAsClass(ReceiptDTO.class);
        String id = receiptService.processReceipt(receiptRequest);
        ctx.status(200).json(new IdResponseDTO(id));

        logger.info("ID: {} has been fully processed", id);
    };

    public void getPoints(Context ctx) {
        String id = ctx.pathParam("id");

        logger.info("Received points retrieval request for ID: {}", id);

        int points = receiptService.getPoints(id);
        ctx.status(200).json(new PointsResponseDTO(points));

        logger.info("Successfully retrieved rewards for ID: {}", id);
    };
}
