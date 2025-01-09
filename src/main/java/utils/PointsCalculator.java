package utils;

import constants.RewardsRules;
import domain.Receipt;
import domain.ReceiptItem;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PointsCalculator {
    public static int calculatePoints(Receipt receipt) {
        int sumOfPoints = 0;

        sumOfPoints += getPointsFromRetailer(receipt.retailer()); // for RULE #1
        sumOfPoints += getPointsFromTotal(receipt.total()); // for RULES #2 and 3
        sumOfPoints += getPointsFromListOfItems(receipt.items()); // for RULES #4 and 5
        sumOfPoints += getPointsFromDateOfPurchase(receipt.purchaseDate()); // for RULE #7
        sumOfPoints += getPointsFromTimeOfPurchase(receipt.purchaseTime()); // for RULE #8

        return sumOfPoints;
    }

    private static int getPointsFromRetailer(String name) {
        if (name == null || name.isEmpty()) return 0;

        int pointsFromRetailerName = 0;

        // RULE #1: "One point for every alphanumeric character in the retailer name"
        pointsFromRetailerName += name.replaceAll("\\s+", "").length() * RewardsRules.POINTS_PER_ALPHANUMERIC;

        return pointsFromRetailerName;
    }

    private static int getPointsFromTotal(double price) {
        if (price < RewardsRules.MIN_TOTAL) return 0;

        int pointsFromTotalPrice = 0;

        // RULE #2: "50 points if the total is a round dollar amount with no cents"
        if (price == Math.floor(price)) {
            pointsFromTotalPrice += RewardsRules.POINTS_FOR_ROUND_DOLLAR;
        }

        // RULE #3: "25 points if the total is a multiple of 0.25"
        if (price % 0.25 == 0) {
            pointsFromTotalPrice += RewardsRules.POINTS_FOR_MULTIPLE_OF_25_CENTS;
        }

        return pointsFromTotalPrice;
    }

    private static int getPointsFromListOfItems(List<ReceiptItem> items) {
        if (items == null || items.size() < RewardsRules.MIN_ITEMS) return 0;

        int pointsFromItems = 0;

        // RULE #4: "5 points for every two items on the receipt"
        pointsFromItems += (items.size() / 2) * RewardsRules.POINTS_PER_TWO_ITEMS;

        // RULE #5: "If the trimmed length of the item description is a multiple of 3, multiply the price by 0.2 and round up to the nearest integer. The result is the number of points earned."
        for (ReceiptItem item: items) {
            int lenOfCurrentItem = StringManipulator.removeExtraSpacesAndReturnLength(item.shortDescription()); // alternatively, we can item.shortDescription().trim().length() if we allow extra spaces between valid substrings

            if (lenOfCurrentItem % 3 == 0) {
                pointsFromItems += (int) Math.ceil(item.price() * RewardsRules.MULTIPLY_IF_TRIMMED_LENGTH_IS_MULTIPLE_OF_3);
            }
        }

        return pointsFromItems;
    }

    private static int getPointsFromDateOfPurchase(LocalDate date) {
        if (date == null) return 0;

        int pointsFromDate = 0;

        // RULE #7: "6 points if the day in the purchase date is odd"
        if (date.getDayOfMonth() % 2 == 1) {
            pointsFromDate += 6;
        }

        return pointsFromDate;
    }

    private static int getPointsFromTimeOfPurchase(LocalTime time) {
        if (time == null) return 0;

        int pointsFromTime = 0;

        // RULE #8: "10 points if the time of purchase is after 2:00pm and before 4:00pm"
        LocalTime start = LocalTime.of(RewardsRules.PURCHASE_TIME_START_HOUR, RewardsRules.PURCHASE_TIME_START_MIN);
        LocalTime end = LocalTime.of(RewardsRules.PURCHASE_TIME_END_HOUR, RewardsRules.PURCHASE_TIME_END_MIN);

        if (time.isAfter(start) && time.isBefore(end)) {
            pointsFromTime += 10;
        }

        return pointsFromTime;
    }
}
