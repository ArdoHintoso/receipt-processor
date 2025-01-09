package repository;

import domain.Receipt;
import org.slf4j.Logger;
import utils.LoggerUtil;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ReceiptRepository {
    private static final Logger logger = LoggerUtil.getLogger(ReceiptRepository.class);
    private final Map<String, Receipt> processedReceiptsStorage = new ConcurrentHashMap<>();
    public void save(String id, Receipt receipt) {
        logger.debug("Generating new ID for receipt: {}", id);

        processedReceiptsStorage.put(id, receipt);

        logger.info("Receipt saved successfully with ID: {}", id);
    }
    public Optional<Receipt> findById(String id) {
        logger.debug("Searching for receipt with ID: {}", id);

        Optional<Receipt> receipt = Optional.ofNullable(processedReceiptsStorage.get(id));

        logger.debug(receipt.isPresent() ? "Receipt found for ID: {}" : "No receipt found for ID: {}", id);

        return receipt;
    }
}
