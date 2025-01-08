package controllers;

import dto.IdResponseDTO;
import dto.PointsResponseDTO;
import io.javalin.http.Context;
import domain.Receipt;
import services.ReceiptService;

public class ReceiptController {
    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    public void processReceipt(Context ctx) {
        Receipt receipt = ctx.bodyAsClass(Receipt.class);
        String id = receiptService.processReceipt(receipt);
        ctx.status(200).json(new IdResponseDTO(id));
    };

    public void getPoints(Context ctx) {
        String id = ctx.pathParam("id");
        int points = receiptService.getPoints(id);
        ctx.status(200).json(new PointsResponseDTO(points));
    };
}
