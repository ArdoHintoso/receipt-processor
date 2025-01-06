package services;

import exceptions.ApiException;
import models.Receipt;
import models.ReceiptPointsResponse;
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

    public ReceiptPointsResponse getPoints(String id) {
        if (!receipts.containsKey(id)) {
            throw new ApiException("No receipt found for that ID.", 404);
        }

        return new ReceiptPointsResponse(points.get(id));
    }

    private void validateReceipt(Receipt receipt) {
        if (receipt == null) {
            throw new ApiException("The receipt is invalid.", 400);
        }

        if (receipt.retailer() == null || receipt.retailer().trim().isEmpty()) {
            throw new ApiException("The receipt is invalid.", 400);
        }
    }
}
