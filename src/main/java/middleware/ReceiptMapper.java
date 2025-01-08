package middleware;

import constants.RewardsRules;
import domain.Receipt;
import domain.ReceiptItem;
import dto.ReceiptDTO;
import exceptions.ValidationException;
import org.slf4j.Logger;
import utils.LoggerUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReceiptMapper {
    private static final Logger logger = LoggerUtil.getLogger(ReceiptMapper.class);

    public Receipt toReceipt(ReceiptDTO dto) {
        logger.debug("Mapping DTO to domain object: {}", dto);
        try {
            List<ReceiptItem> items = dto.items().stream().map(this::mapItem).collect(Collectors.toList());

            Receipt receipt = new Receipt(
                    null, // to be processed and generated by ReceiptService
                    dto.retailer(),
                    LocalDate.parse(dto.purchaseDate(), RewardsRules.DATE_FORMATTER),
                    LocalTime.parse(dto.purchaseTime(), RewardsRules.TIME_FORMATTER),
                    items,
                    Double.parseDouble(dto.total()),
                    0 // initial points to be set by ReceiptService
            );

            logger.debug("Successfully mapped DTO to domain object: {}", receipt);
            return receipt;
        } catch (Exception e) {
            logger.error("Failed to map DTO to domain object", e);
            throw new ValidationException("Invalid data format: " + e.getMessage());
        }
    }

    private ReceiptItem mapItem(ReceiptDTO.ItemDTO dto) {
        return new ReceiptItem(
                dto.shortDescription(),
                Double.parseDouble(dto.price())
        );
    }
}
