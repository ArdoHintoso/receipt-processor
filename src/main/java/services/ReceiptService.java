package services;

import dto.ReceiptDTO;
import exceptions.ApiException;
import domain.Receipt;
import middleware.ReceiptMapper;
import middleware.ReceiptValidator;
import org.slf4j.Logger;
import utils.LoggerUtil;
import utils.PointsCalculator;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ReceiptService {
    private static final Logger logger = LoggerUtil.getLogger(ReceiptService.class);
    private final Map<String, Receipt> receipts = new ConcurrentHashMap<>();
    private final Map<String, Integer> rewards = new ConcurrentHashMap<>();
    private final ReceiptValidator validator;
    private final PointsCalculator pointsCalculator;
    private final ReceiptMapper mapper;

    public ReceiptService(ReceiptValidator validator, PointsCalculator pointsCalculator, ReceiptMapper mapper) {
        this.validator = validator;
        this.pointsCalculator = pointsCalculator;
        this.mapper = mapper;
    }

    public String processReceipt(ReceiptDTO receiptDTO) {
        logger.info("Processing new receipt...");
        logger.debug("Receipt data: {}", receiptDTO);

        validator.validateReceiptDTO(receiptDTO);
        logger.debug("DTO validation passed");

        String id = UUID.randomUUID().toString();

        Receipt receiptInProcess = mapper.toReceipt(receiptDTO);

        int calculatedPoints = pointsCalculator.calculatePoints(receiptInProcess);

        Receipt processedReceipt = new Receipt(id, receiptInProcess.retailer(), receiptInProcess.purchaseDate(), receiptInProcess.purchaseTime(), receiptInProcess.items(), receiptInProcess.total(), calculatedPoints);

        rewards.put(id, calculatedPoints);
        receipts.put(id, processedReceipt);

        return id;
    }

    public int getPoints(String id) {
        if (!receipts.containsKey(id)) {
            throw new ApiException("No receipt found for that ID.", 404);
        }

        return rewards.get(id);
    }
}
