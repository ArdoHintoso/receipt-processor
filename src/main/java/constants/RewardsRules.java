package constants;

import java.time.format.DateTimeFormatter;

public class RewardsRules {
    public static final int MIN_ITEMS = 1;
    public static final double MIN_TOTAL = 0.0;
    public static final double MIN_ITEM_PRICE = 0.0;
    public static final int POINTS_PER_ALPHANUMERIC = 1;
    public static final int POINTS_FOR_ROUND_DOLLAR = 50;
    public static final int POINTS_FOR_MULTIPLE_OF_25_CENTS = 25;
    public static final int POINTS_PER_TWO_ITEMS = 5;
    public static final double MULTIPLY_IF_TRIMMED_LENGTH_IS_MULTIPLE_OF_3 = 0.2;
    public static final int PURCHASE_TIME_START_HOUR = 14;
    public static final int PURCHASE_TIME_START_MIN = 0;
    public static final int PURCHASE_TIME_END_HOUR = 16;
    public static final int PURCHASE_TIME_END_MIN = 0;

    // Time constants
    public static final String TIME_FORMAT = "HH:mm";
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    // Date Constants
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
}
