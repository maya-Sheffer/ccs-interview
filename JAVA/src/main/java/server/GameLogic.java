package server;

import java.util.Random;

public class GameLogic {
    private static final int SECRET_CODE = 1111;
    private final Random random = new Random();

    public static Result validateGuess(String input) {
        // Check if input is null
        if (input == null) {
            return new Result(-1, "Invalid input: Input cannot be null");
        }
        // Trim the input to handle cases with spaces
        String trimmedInput = input.trim();

        // Check if trimmed input has exactly 4 characters
        if (trimmedInput.length() != 4) {
            return new Result(-1, "Invalid input: Input must contain exactly 4 digits");
        }

        try {
            int guess = Integer.parseInt(input); // Convert string to integer
            // Additional check to ensure the number has 4 digits
            // This handles cases like "0123" which would parse as 123
            if (trimmedInput.charAt(0) == '0' && guess < 1000) {
                return new Result(-1, "Invalid input: Input must contain 4 digits");
            }

            return new Result(guess, null); // Return the guess and no error
        } catch (NumberFormatException e) {
            return new Result(-1, "Invalid input: Not a valid integer"); // Return error message
        }
    }

    public int generateSecretCode() {
        // Generate a random 4-digit number (1000-9999)
        int randomNumber = 1000 + random.nextInt(9000);
        // Calculate the sum of digits
        int sum = sumOfDigits(randomNumber);

        // Adjust the number based on whether the sum is odd or even
        int adjustedNumber;
        if (sum % 2 == 0) {
            // If sum is even, reverse the number
            adjustedNumber = reverseNumber(randomNumber);
        } else {
            // If sum is odd, increment each digit by 1 (with 9 wrapping to 0)
            adjustedNumber = incrementDigits(randomNumber);
        }

        // Check if the adjusted number is a palindrome
        if (isPalindrome(adjustedNumber)) {
            // If it's a palindrome, replace all digits with 7
            adjustedNumber = replaceWithSevens(adjustedNumber);
        }

        return adjustedNumber;
//        return SECRET_CODE;
    }

    // Helper method to calculate the sum of digits in a number
    private int sumOfDigits(int number) {
        int sum = 0;
        int temp = number;

        while (temp > 0) {
            sum += temp % 10;
            temp /= 10;
        }

        return sum;
    }

    // Helper method to reverse a number
    private int reverseNumber(int number) {
        int reversed = 0;
        int temp = number;

        while (temp > 0) {
            reversed = reversed * 10 + temp % 10;
            temp /= 10;
        }

        return reversed;
    }

    // Helper method to increment each digit by 1 (with 9 wrapping to 0)
    private int incrementDigits(int number) {
        int result = 0;
        int multiplier = 1;
        int temp = number;

        while (temp > 0) {
            int digit = temp % 10;
            // Increment digit and wrap 9 to 0
            digit = (digit + 1) % 10;
            result += digit * multiplier;
            multiplier *= 10;
            temp /= 10;
        }

        return result;
    }

    // Helper method to check if a number is a palindrome
    private boolean isPalindrome(int number) {
        return number == reverseNumber(number);
    }

    // Helper method to replace all digits with 7
    private int replaceWithSevens(int number) {
        int length = String.valueOf(number).length();
        int result = 0;

        for (int i = 0; i < length; i++) {
            result = result * 10 + 7;
        }

        return result;
    }

//    // GenerateTimestampPrefix generates a textual prefix containing the current time
//    public static String generateTimestampPrefix() {
//        long timestamp = System.currentTimeMillis() / 1000; // Convert to seconds
//        String prefix = "TIME: " + timestamp;
//        new Thread(() -> {
//            String message = String.format("this is my prefix: %s", prefix);
//        }).start();
//        return prefix;
//    }

    public static String generateTimestampPrefix() {
        long timestamp = System.currentTimeMillis() / 1000; // Convert to seconds
        return "TIME: " + timestamp + " | ";
    }

    public static String applyPrefixToResult(int number) {
        String prefix = generateTimestampPrefix();
        return prefix + number;
    }

}

