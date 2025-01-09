package services;

import constants.RewardsRules;
import domain.Receipt;
import dto.ReceiptDTO;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import middleware.ReceiptMapper;
import middleware.ReceiptValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.ReceiptRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {
    private static final LocalDate SAMPLE_DATE = LocalDate.parse("2022-01-01", RewardsRules.DATE_FORMATTER);
    private static final LocalTime SAMPLE_TIME = LocalTime.parse("13:01", RewardsRules.TIME_FORMATTER);
    private static final String SAMPLE_RETAILER = "Target";
    private static final String SAMPLE_TOTAL = "35.35";
    private static final double SAMPLE_TOTAL_DOUBLE = 35.35;
    private static final String VALID_ID = "valid-id";

    @Mock
    private ReceiptRepository repository;
    @Mock
    private ReceiptValidator validator;
    @Mock
    private ReceiptMapper mapper;

    private ReceiptService receiptService;

    @BeforeEach
    void setUp() {
        receiptService = new ReceiptService(repository, validator, mapper);
    }

    // Helper methods
    private Receipt createTestReceipt(int points) {
        return new Receipt(null, SAMPLE_RETAILER, SAMPLE_DATE, SAMPLE_TIME,
                List.of(), SAMPLE_TOTAL_DOUBLE, points);
    }

    private ReceiptDTO createTestReceiptDTO() {
        return new ReceiptDTO(SAMPLE_RETAILER, SAMPLE_DATE.format(RewardsRules.DATE_FORMATTER),
                SAMPLE_TIME.format(RewardsRules.TIME_FORMATTER), SAMPLE_TOTAL, List.of());
    }

    private void verifyGetPointsThrowsNotFoundException(String invalidId) {
        when(repository.findById(invalidId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> receiptService.getPoints(invalidId));
        verify(repository).findById(invalidId);
    }

    private void verifyGetPointsReturnsExpectedPoints(int expectedPoints) {
        Receipt receipt = createTestReceipt(expectedPoints);
        when(repository.findById(VALID_ID)).thenReturn(Optional.of(receipt));

        int points = receiptService.getPoints(VALID_ID);

        assertEquals(expectedPoints, points);
        verify(repository).findById(VALID_ID);
    }

    @Test
    void processReceipt_ValidReceipt_ReturnsId() {
        ReceiptDTO receiptDTO = createTestReceiptDTO();
        Receipt receipt = createTestReceipt(100);

        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Receipt> receiptCaptor = ArgumentCaptor.forClass(Receipt.class);

        when(mapper.toReceipt(receiptDTO)).thenReturn(receipt);
        doNothing().when(validator).validateReceiptDTO(receiptDTO);
        doNothing().when(repository).save(idCaptor.capture(), receiptCaptor.capture());

        String resultId = receiptService.processReceipt(receiptDTO);

        assertEquals(resultId, idCaptor.getValue());
        verify(validator).validateReceiptDTO(receiptDTO);
        verify(repository).save(any(String.class), any(Receipt.class));
    }

    @Test
    void processReceipt_InvalidReceipt_ThrowsBadRequestException() {
        ReceiptDTO receiptDTO = createTestReceiptDTO();
        doThrow(new BadRequestException("Invalid receipt")).when(validator).validateReceiptDTO(receiptDTO);

        assertThrows(BadRequestException.class, () -> receiptService.processReceipt(receiptDTO));
        verify(validator).validateReceiptDTO(receiptDTO);
        verify(repository, never()).save(anyString(), any(Receipt.class));
    }

    @Test
    void processReceipt_RepositoryThrowsException_PropagatesException() {
        ReceiptDTO receiptDTO = createTestReceiptDTO();
        Receipt receipt = createTestReceipt(0);

        when(mapper.toReceipt(receiptDTO)).thenReturn(receipt);
        doThrow(new RuntimeException("Database error"))
                .when(repository).save(any(), any());

        assertThrows(RuntimeException.class,
                () -> receiptService.processReceipt(receiptDTO));
    }

    @Test
    void getPoints_ValidId_ReturnsPoints() {
        verifyGetPointsReturnsExpectedPoints(100);
    }

    @Test
    void getPoints_ValidId_ReturnsZeroPoints() {
        verifyGetPointsReturnsExpectedPoints(0);
    }

    @Test
    void getPoints_ValidId_ReturnsNonZeroPoints() {
        verifyGetPointsReturnsExpectedPoints(50);
    }

    @Test
    void getPoints_InvalidId_ThrowsNotFoundException() {
        verifyGetPointsThrowsNotFoundException("invalid-id");
    }

    @Test
    void getPoints_EmptyId_ThrowsNotFoundException() {
        verifyGetPointsThrowsNotFoundException("");
    }

    @Test
    void getPoints_NullId_ThrowsNotFoundException() {
        verifyGetPointsThrowsNotFoundException(null);
    }

    @Test
    void getPoints_WhitespaceId_ThrowsNotFoundException() {
        verifyGetPointsThrowsNotFoundException("   ");
    }

    @Test
    void getPoints_NonExistentId_ThrowsNotFoundException() {
        verifyGetPointsThrowsNotFoundException("non-existent-id");
    }
}