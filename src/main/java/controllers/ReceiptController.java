package controllers;

import io.javalin.http.Context;
import models.Receipt;
import services.ReceiptService;

import java.util.Map;

public class ReceiptController {
    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    public void processReceipt(Context ctx) {
        Receipt receipt = ctx.bodyAsClass(Receipt.class);
        String id = receiptService.processReceipt(receipt);
        ctx.status(200).json(Map.of("id", id));
    };

    public void getPoints(Context ctx) {
        String id = ctx.pathParam("id");
        ctx.status(200).json(receiptService.getPoints(id));
    };
}
