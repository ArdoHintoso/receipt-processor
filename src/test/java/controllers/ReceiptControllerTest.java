package controllers;

import dto.IdResponseDTO;
import dto.PointsResponseDTO;
import dto.ReceiptDTO;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.ReceiptService;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class ReceiptControllerTest {
    @Mock
    private ReceiptService receiptService;

    @Mock
    private Context ctx;

    private ReceiptController receiptController;

    @BeforeEach
    void setUp() {
        receiptController = new ReceiptController(receiptService);
    }

    @Test
    void processReceipt_ValidReceipt_ReturnsId() {
        // Arrange
        ReceiptDTO receiptDTO = new ReceiptDTO("Target", "2022-01-01", "13:01", "35.35", new ArrayList<>());
        String expectedId = "test-id-123";

        when(ctx.status(anyInt())).thenReturn(ctx);
        when(ctx.bodyAsClass(ReceiptDTO.class)).thenReturn(receiptDTO);
        when(receiptService.processReceipt(receiptDTO)).thenReturn(expectedId);

        // Act
        receiptController.processReceipt(ctx);

        // Assert
        verify(ctx).status(200);
        verify(ctx).json(new IdResponseDTO(expectedId));
        verify(receiptService).processReceipt(receiptDTO);
    }

    @Test
    void processReceipt_InvalidReceipt_ReturnsBadRequest() {
        // Arrange
        ReceiptDTO receiptDTO = new ReceiptDTO("Target", "2022-01-01", "13:01", "35.35", new ArrayList<>());

        when(ctx.status(anyInt())).thenReturn(ctx);
        when(ctx.bodyAsClass(ReceiptDTO.class)).thenReturn(receiptDTO);
        when(receiptService.processReceipt(receiptDTO))
                .thenThrow(new BadRequestException("Invalid receipt"));

        // Act & Assert
        try {
            receiptController.processReceipt(ctx);
        } catch (BadRequestException e) {
            verify(ctx, never()).status(anyInt());
            verify(ctx, never()).json(any());
        }
    }

    @Test
    void getPoints_ValidId_ReturnsPoints() {
        // Arrange
        String testId = "test-id-123";
        int expectedPoints = 100;

        when(ctx.status(anyInt())).thenReturn(ctx);
        when(ctx.pathParam("id")).thenReturn(testId);
        when(receiptService.getPoints(testId)).thenReturn(expectedPoints);

        // Act
        receiptController.getPoints(ctx);

        // Assert
        verify(ctx).status(200);
        verify(ctx).json(new PointsResponseDTO(expectedPoints));
    }

    @Test
    void getPoints_InvalidId_ReturnsNotFound() {
        // Arrange
        String invalidId = "invalid-id";

        when(ctx.status(anyInt())).thenReturn(ctx);
        when(ctx.pathParam("id")).thenReturn(invalidId);
        when(receiptService.getPoints(invalidId))
                .thenThrow(new NotFoundException("Receipt not found"));

        // Act & Assert
        try {
            receiptController.getPoints(ctx);
        } catch (NotFoundException e) {
            verify(ctx, never()).status(anyInt());
            verify(ctx, never()).json(any());
        }
    }
}