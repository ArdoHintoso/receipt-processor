package repository;

import constants.RewardsRules;
import domain.Receipt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReceiptRepositoryTest {
    private ReceiptRepository repository;
    private static final String VALID_ID = "valid-id";
    private static final String INVALID_ID = "invalid-id";
    private static final Receipt VALID_RECEIPT = new Receipt(VALID_ID, "Target", LocalDate.parse("2022-01-01", RewardsRules.DATE_FORMATTER), LocalTime.parse("13:01", RewardsRules.TIME_FORMATTER), List.of(), 35.35, 100);

    @BeforeEach
    void setUp() {
        repository = new ReceiptRepository();
    }

    @Test
    void save_ValidReceipt_Success() {
        // Act
        repository.save(VALID_ID, VALID_RECEIPT);

        // Assert
        Optional<Receipt> retrievedReceipt = repository.findById(VALID_ID);
        assertTrue(retrievedReceipt.isPresent());
        assertEquals(VALID_RECEIPT, retrievedReceipt.get());
    }

    @Test
    void findById_ValidId_ReturnsReceipt() {
        // Arrange
        repository.save(VALID_ID, VALID_RECEIPT);

        // Act
        Optional<Receipt> retrievedReceipt = repository.findById(VALID_ID);

        // Assert
        assertTrue(retrievedReceipt.isPresent());
        assertEquals(VALID_RECEIPT, retrievedReceipt.get());
    }

    @Test
    void findById_InvalidId_ReturnsEmpty() {
        // Act
        Optional<Receipt> retrievedReceipt = repository.findById(INVALID_ID);

        // Assert
        assertTrue(retrievedReceipt.isEmpty());
    }
}