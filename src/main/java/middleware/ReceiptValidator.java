package middleware;

import constants.RewardsRules;
import dto.ReceiptDTO;
import exceptions.BadRequestException;
import org.slf4j.Logger;
import utils.LoggerUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReceiptValidator {
    private static final Logger logger = LoggerUtil.getLogger(ReceiptValidator.class);
    public void validateReceiptDTO(ReceiptDTO dto) {
        List<String> errors = new ArrayList<>();

        validateRetailer(dto, errors);
        validateDateTime(dto, errors);
        validateTotalAndItems(dto, errors);

        if (!errors.isEmpty()) {
            logger.error("Validation failed. Your receipt is invalid, please try again.");
            throw new BadRequestException("Validation failed: " + String.join(", ", errors));
        }
    }

    private void validateRetailer(ReceiptDTO dto, List<String> errors) {
        if (dto.retailer() == null || dto.retailer().trim().isEmpty()) {
            errors.add("Retailer name is required");
            return ;
        }

        if (!dto.retailer().matches("^[\\w\\s&'-]+$")) {
            errors.add("Retailer name contains invalid characters");
        }
    }

    private void validateDateTime(ReceiptDTO dto, List<String> errors) {
        if (dto.purchaseDate() == null || dto.purchaseDate().trim().isEmpty()) {
            errors.add("Purchase date is required");
        } else {
            try {
                LocalDate.parse(dto.purchaseDate(), RewardsRules.DATE_FORMATTER);
            } catch (Exception e) {
                errors.add("Invalid date format. Expected format: " + RewardsRules.DATE_FORMAT);
            }
        }

        if (dto.purchaseTime() == null || dto.purchaseTime().trim().isEmpty()) {
            errors.add("Purchase time is required");
        } else {
            try {
                LocalTime.parse(dto.purchaseTime(), RewardsRules.TIME_FORMATTER);
            } catch (Exception e) {
                errors.add("Invalid time format. Expected format: " + RewardsRules.TIME_FORMAT);
            }
        }
    }

    private void validateTotalAndItems(ReceiptDTO dto, List<String> errors) {
        if (dto.total() == null || dto.total().trim().isEmpty()) {
            errors.add("Total is required");
            return ;
        } else if (!dto.total().matches("^\\d+\\.\\d{2}$")) {
            errors.add("Invalid total format. Expected format: 0.00");
            return ;
        } else if (Double.parseDouble(dto.total()) <= RewardsRules.MIN_TOTAL) {
            errors.add("Total cannot be negative or zero");
            return ;
        }

        List<ReceiptDTO.ItemDTO> items = dto.items();
        double totalFromReceipt = Double.parseDouble(dto.total());

        if (items == null || items.isEmpty()) {
            errors.add("At least one item is required");
            return;
        }

        double totalFromItems = 0;

        for (int i = 0; i < items.size(); i++) {
            ReceiptDTO.ItemDTO item = items.get(i);
            totalFromItems += validateItem(item, i, errors);
        }

        if (Math.abs(totalFromItems - totalFromReceipt) > 0.01) {
            errors.add(String.format("The price sum of all items, %.2f, doesn't match the total displayed on the receipt, %.2f", totalFromItems, totalFromReceipt));
        }
    }

    private double validateItem(ReceiptDTO.ItemDTO item, int index, List<String> errors) {
        if (item.shortDescription() == null || item.shortDescription().trim().isEmpty()) {
            errors.add("Item " + (index + 1) + ": Short description is required");
        } else if (!item.shortDescription().matches("^[\\w\\s&'-]+$")) {
            errors.add("Item " + (index + 1) + ": Short description contains invalid characters");
        }

        if (item.price() == null || item.price().trim().isEmpty()) {
            errors.add("Item " + (index + 1) + ": Price is required");
        } else if (!item.price().matches("^\\d+\\.\\d{2}$")) {
            errors.add("Item " + (index + 1) + ": Invalid price format. Expected format: 0.00");
        } else {
            try {
                double price = Double.parseDouble(item.price());
                if (price <= RewardsRules.MIN_ITEM_PRICE) {
                    errors.add("Item " + (index + 1) + ": Price cannot be negative or zero");
                }
            } catch (NumberFormatException e) {
                errors.add("Item " + (index + 1) + ": Invalid price format");
            }
        }

        return Double.parseDouble(item.price());
    }
}
