package utils;

class StringMethods {
    public int trimAndReturnLength(String input) {
        if (input == null || input.length() == 0) return 0;

        int slow = 0;

        char[] arr = input.toCharArray();

        for (int fast = slow; fast < arr.length; fast++) {
            if (slow == 0 && arr[fast] == ' ') {
                fast++;
            } else if (slow == 0) {
                arr[slow++] = arr[fast];
            } else if (arr[fast] == ' ') {
                if (arr[slow - 1] != ' ') {
                    arr[slow++] = arr[fast];
                } else {
                    fast++;
                }
            } else {
                arr[slow++] = arr[fast];
            }
        }

        return slow - 1;
    }
}
