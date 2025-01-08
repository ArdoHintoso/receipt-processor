package domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record Receipt(
        String id,
        String retailer,
        LocalDate purchaseDate,
        LocalTime purchaseTime,
        List<ReceiptItem> items,
        double total,
        int points
) {
}
