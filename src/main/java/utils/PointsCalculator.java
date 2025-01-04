package utils;

import models.Receipt;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PointsCalculator {
    public int calculatePoints(Receipt receipt) {
        int sumOfPoints = 0;

        sumOfPoints += getPointsFromRetailer(receipt.retailer()); // for RULE #1
        sumOfPoints += getPointsFromTotal(Double.parseDouble(receipt.total())); // for RULES #2 and 3
        sumOfPoints += getPointsFromListOfItems(receipt.items()); // for RULES #4 and 5
        sumOfPoints += getPointsFromDateOfPurchase(receipt.purchaseDate()); // for RULE #7
        sumOfPoints += getPointsFromTimeOfPurchase(receipt.purchaseTime()); // for RULE #8

        return sumOfPoints;
    }

    private int getPointsFromRetailer(String name) {
        if (name == null || name.length() == 0) return 0;

        int pointsFromRetailerName = 0;

        // RULE #1: "One point for every alphanumeric character in the retailer name"
        pointsFromRetailerName += name.replaceAll("\\s+", "").length();

        return pointsFromRetailerName;
    }

    private int getPointsFromTotal(double price) {
        if (price < 0) return 0;

        int pointsFromTotalPrice = 0;

        // RULE #2: "50 points if the total is a round dollar amount with no cents"
        if (price == Math.floor(price)) {
            pointsFromTotalPrice += 50;
        }

        // RULE #3: "25 points if the total is a multiple of 0.25"
        if (price % 0.25 == 0) {
            pointsFromTotalPrice += 25;
        }

        return pointsFromTotalPrice;
    }

    private int getPointsFromListOfItems(List<Receipt.Item> items) {
        if (items == null || items.size() == 0) return 0;

        int pointsFromItems = 0;

        // RULE #4: "5 points for every two items on the receipt"
        pointsFromItems += (items.size() / 2) * 5;

        StringMethods strMethods = new StringMethods();

        // RULE #5: "If the trimmed length of the item description is a multiple of 3, multiply the price by 0.2 and round up to the nearest integer. The result is the number of points earned."
        for (Receipt.Item item: items) {
            int lenOfCurrentItem = strMethods.trimAndReturnLength(item.shortDescription());
            if (lenOfCurrentItem % 3 == 0) {
                pointsFromItems += (int) Math.ceil(Double.parseDouble(item.price()) * 0.2);
            }
        }

        return pointsFromItems;
    }

    private int getPointsFromDateOfPurchase(LocalDate date) {
        if (date == null) return 0;

        int pointsFromDate = 0;

        // RULE #7: "6 points if the day in the purchase date is odd"
        if (date.getDayOfMonth() % 2 == 1) {
            pointsFromDate += 6;
        }

        return pointsFromDate;
    }

    private int getPointsFromTimeOfPurchase(LocalTime time) {
        if (time == null) return 0;

        int pointsFromTime = 0;

        // RULE #8: "10 points if the time of purchase is after 2:00pm and before 4:00pm"
        LocalTime start = LocalTime.of(14, 0), end = LocalTime.of(16,0);

        if (time.isAfter(start) && time.isBefore(end)) {
            pointsFromTime += 10;
        }

        return pointsFromTime;
    }
}
