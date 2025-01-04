package utils;

import models.Receipt;

import java.util.List;

public class PointsCalculator {
    int sumOfPoints;

    public int calculatePoints(Receipt receipt) {
        getPointsFromRetailer(receipt.retailer());
        getPointsFromTotal(Integer.parseInt(receipt.total()));
        getPointsFromListOfItems(receipt.items());

        return sumOfPoints;
    }

    private void getPointsFromRetailer(String name) {
        // RULE 1
    }

    private void getPointsFromTotal(int price) {
        if (price < 0) return ;

        // RULE 2: "50 points if the total is a round dollar amount with no cents"

        if (price % 0.25 == 0) {
            sumOfPoints += 25; // RULE 3: "25 points if the total is a multiple of 0.25"
        }
    }

    private void getPointsFromListOfItems(List<Receipt.Item> items) {
        if (items == null || items.size() == 0) return ;

        sumOfPoints += (items.size() / 2) * 5; // RULE 4: "5 points for every two items on the receipt"

        StringMethods strMethods = new StringMethods();

        for (Receipt.Item item: items) {
            int lenOfCurrentItem = strMethods.trimAndReturnLength(item.shortDescription());
            if (lenOfCurrentItem % 3 == 0) {
                sumOfPoints += (int) Math.ceil(Double.parseDouble(item.price()) * 0.2); // RULE 5
            }
        }

    }
}
