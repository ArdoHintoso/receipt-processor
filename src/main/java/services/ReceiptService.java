package services;

import exceptions.ApiException;
import models.Receipt;
import models.ReceiptPoints;
import utils.PointsCalculator;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ReceiptService {
    private final Map<String, Receipt> receipts = new ConcurrentHashMap<>();
    private final Map<String, Integer> points = new ConcurrentHashMap<>();
    private final PointsCalculator pointsCalculator;

    public ReceiptService() {
        this.pointsCalculator = new PointsCalculator();
    }

    public String processReceipt(Receipt receipt) {
        validateReceipt(receipt);

        String id = UUID.randomUUID().toString();
        receipts.put(id, receipt);

        int calculatedPoints = pointsCalculator.calculatePoints(receipt);
        points.put(id, calculatedPoints);

        return id;
    }

    public ReceiptPoints getPoints(String id) {
        if (!receipts.containsKey(id)) {
            throw new ApiException("Receipt does not exist", 404);
        }

        return new ReceiptPoints(id, points.get(id));
    }

    private void validateReceipt(Receipt receipt) {
        if (receipt == null) {
            throw new ApiException("Receipt data is required", 400);
        }

        if (receipt.retailer() == null || receipt.retailer().trim().isEmpty()) {
            throw new ApiException("Retailer is required", 400);
        }
    }
}
