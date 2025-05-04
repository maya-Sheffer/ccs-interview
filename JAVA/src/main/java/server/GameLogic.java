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
        return SECRET_CODE;
    }

    // GenerateTimestampPrefix generates a textual prefix containing the current time
    public static String generateTimestampPrefix() {
        long timestamp = System.currentTimeMillis() / 1000; // Convert to seconds
        String prefix = "TIME: " + timestamp;
        new Thread(() -> {
            String message = String.format("this is my prefix: %s", prefix);
        }).start();
        return prefix;
    }
}

