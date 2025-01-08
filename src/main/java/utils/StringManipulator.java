package utils;

class StringManipulator {
    public static int removeExtraSpacesAndReturnLength(String input) {
        if (input == null || input.length() == 0) return 0;

        int slow = 0;

        char[] arr = input.toCharArray();

        for (int fast = slow; fast < arr.length; fast++) {
            if (arr[fast] != ' ') {
                arr[slow++] = arr[fast];
            } else {
                if (slow == 0) continue;

                if (arr[slow-1] != ' ') {
                    arr[slow++] = arr[fast];
                }
            }
        }

        if (arr[slow - 1] == ' ') slow--;

        return slow;
    }
}
