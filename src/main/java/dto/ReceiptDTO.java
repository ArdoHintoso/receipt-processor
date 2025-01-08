package dto;

import java.util.List;

public record ReceiptDTO(
        String retailer,
        String purchaseDate,
        String purchaseTime,
        String total,
        List<ItemDTO> items
) {
    public record ItemDTO(
            String shortDescription,
            String price
    ) {}
}
