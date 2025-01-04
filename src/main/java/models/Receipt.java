package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record Receipt(
        String retailer,
        LocalDate purchaseDate,
        LocalTime purchaseTime,
        List<Item> items,
        String total
) {
    public record Item(
            String shortDescription,
            String price
    ) {}
}
