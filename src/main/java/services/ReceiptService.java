package services;

import dto.ReceiptDTO;
import domain.Receipt;
import exceptions.NotFoundException;
import middleware.ReceiptMapper;
import middleware.ReceiptValidator;
import org.slf4j.Logger;
import repository.ReceiptRepository;
import utils.LoggerUtil;
import utils.PointsCalculator;

import java.util.UUID;

public class ReceiptService {
    private static final Logger logger = LoggerUtil.getLogger(ReceiptService.class);
    private final ReceiptRepository repository;
    private final ReceiptValidator validator;
    private final ReceiptMapper mapper;

    public ReceiptService(ReceiptRepository repository, ReceiptValidator validator, ReceiptMapper mapper) {
        this.repository = repository;
        this.validator = validator;
        this.mapper = mapper;
    }

    public String processReceipt(ReceiptDTO receiptDTO) {
        logger.info("Processing new receipt...");
        logger.debug("Receipt data: {}", receiptDTO);

        validator.validateReceiptDTO(receiptDTO);
        logger.debug("DTO validation passed");

        Receipt receiptInProcess = mapper.toReceipt(receiptDTO);

        String id = UUID.randomUUID().toString();
        int calculatedPoints = PointsCalculator.calculatePoints(receiptInProcess);

        Receipt processedReceipt = new Receipt(id, receiptInProcess.retailer(), receiptInProcess.purchaseDate(), receiptInProcess.purchaseTime(), receiptInProcess.items(), receiptInProcess.total(), calculatedPoints);

        repository.save(id, processedReceipt);

        logger.info("Receipt processed successfully with ID: {} and points: {}", id, calculatedPoints);

        return id;
    }

    public int getPoints(String id) {
        logger.info("Retrieving points for receipt ID: {}", id);

        Receipt retrieved = repository.findById(id).orElseThrow(() -> {
            logger.error("Receipt not found for ID: {}", id);
            throw new NotFoundException("No receipt found for that ID.");
        });

        logger.info("{} points was rewarded for receipt ID: {}", retrieved.points(), id);

        return retrieved.points();
    }
}
