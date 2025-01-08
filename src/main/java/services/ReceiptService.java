package services;

import exceptions.ApiException;
import domain.Receipt;
import dto.PointsResponseDTO;
import utils.PointsCalculator;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ReceiptService {
    private final Map<String, Receipt> receipts = new ConcurrentHashMap<>();
    private final Map<String, Integer> rewards = new ConcurrentHashMap<>();
    private final PointsCalculator pointsCalculator;

    public ReceiptService(PointsCalculator pointsCalculator) {
        this.pointsCalculator = pointsCalculator;
    }

    public String processReceipt(Receipt receipt) {
        validateReceipt(receipt);

        String id = UUID.randomUUID().toString();
        receipts.put(id, receipt);

        int calculatedPoints = pointsCalculator.calculatePoints(receipt);
        rewards.put(id, calculatedPoints);

        return id;
    }

    public int getPoints(String id) {
        if (!receipts.containsKey(id)) {
            throw new ApiException("No receipt found for that ID.", 404);
        }

        return rewards.get(id);
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
