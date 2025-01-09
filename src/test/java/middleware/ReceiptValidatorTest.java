package middleware;

import dto.ReceiptDTO;
import exceptions.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReceiptValidatorTest {
    private ReceiptValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ReceiptValidator();
    }

    @Test
    void validateReceiptDTO_ValidReceipt_Success() {
        // Arrange
        ReceiptDTO validReceipt = new ReceiptDTO("Target", "2022-01-01", "13:01", "35.35", List.of(new ReceiptDTO.ItemDTO("item1", "35.35")));

        // Act & Assert
        assertDoesNotThrow(() -> validator.validateReceiptDTO(validReceipt));
    }

    @Test
    void validateReceiptDTO_TotalNotMatching_ThrowsBadRequestException() {
        // Arrange
        ReceiptDTO invalidReceipt = new ReceiptDTO("Target", "2022-01-01", "13:01", "35.35", List.of(new ReceiptDTO.ItemDTO("item1", "3.35")));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> validator.validateReceiptDTO(invalidReceipt));
    }

    @Test
    void validateReceiptDTO_NullRetailer_ThrowsBadRequestException() {
        // Arrange
        ReceiptDTO invalidReceipt = new ReceiptDTO(null, "2022-01-01", "13:01", "35.35", List.of());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> validator.validateReceiptDTO(invalidReceipt));
    }

    @Test
    void validateReceiptDTO_EmptyRetailer_ThrowsBadRequestException() {
        // Arrange
        ReceiptDTO invalidReceipt = new ReceiptDTO("", "2022-01-01", "13:01", "35.35", List.of());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> validator.validateReceiptDTO(invalidReceipt));
    }

    @Test
    void validateReceiptDTO_NullPurchaseDate_ThrowsBadRequestException() {
        // Arrange
        ReceiptDTO invalidReceipt = new ReceiptDTO("Target", null, "13:01", "35.35", List.of());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> validator.validateReceiptDTO(invalidReceipt));
    }

    @Test
    void validateReceiptDTO_EmptyPurchaseDate_ThrowsBadRequestException() {
        // Arrange
        ReceiptDTO invalidReceipt = new ReceiptDTO("Target", "", "13:01", "35.35", List.of());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> validator.validateReceiptDTO(invalidReceipt));
    }

    @Test
    void validateReceiptDTO_NullPurchaseTime_ThrowsBadRequestException() {
        // Arrange
        ReceiptDTO invalidReceipt = new ReceiptDTO("Target", "2022-01-01", null, "35.35", List.of());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> validator.validateReceiptDTO(invalidReceipt));
    }

    @Test
    void validateReceiptDTO_EmptyPurchaseTime_ThrowsBadRequestException() {
        // Arrange
        ReceiptDTO invalidReceipt = new ReceiptDTO("Target", "2022-01-01", "", "35.35", List.of());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> validator.validateReceiptDTO(invalidReceipt));
    }

    @Test
    void validateReceiptDTO_NullTotal_ThrowsBadRequestException() {
        // Arrange
        ReceiptDTO invalidReceipt = new ReceiptDTO("Target", "2022-01-01", "13:01", null, List.of());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> validator.validateReceiptDTO(invalidReceipt));
    }

    @Test
    void validateReceiptDTO_EmptyTotal_ThrowsBadRequestException() {
        // Arrange
        ReceiptDTO invalidReceipt = new ReceiptDTO("Target", "2022-01-01", "13:01", "", List.of());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> validator.validateReceiptDTO(invalidReceipt));
    }

    @Test
    void validateReceiptDTO_InvalidTotal_ThrowsBadRequestException() {
        // Arrange
        ReceiptDTO invalidReceipt = new ReceiptDTO("Target", "2022-01-01", "13:01", "invalid-total", List.of());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> validator.validateReceiptDTO(invalidReceipt));
    }
}