package server;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Random;

class GameLogicTest {
//    private final GameLogic gameLogic = new GameLogic();
    private GameLogic gameLogic;

    @BeforeEach
    void setUp() {
        gameLogic = new GameLogic();
    }

    @Test
    void testValidateGuess_validInput() {
        Result result = GameLogic.validateGuess("1234");
        assertEquals(1234, result.getGuess());
        assertNull(result.getError());
    }

    @Test
    void testValidateGuess_validInputWithSpaces() {
        Result result = GameLogic.validateGuess(" 1234 ");
        assertEquals(1234, result.getGuess());
        assertNull(result.getError());
    }

    @Test
    void testValidateGuess_validInputWithLeadingZero() {
        // This should be invalid as per our validation logic
        Result result = GameLogic.validateGuess("0123");
        assertEquals(-1, result.getGuess());
        assertNotNull(result.getError());
    }

    @Test
    void testValidateGuess_invalidNullInput() {
        Result result = GameLogic.validateGuess(null);
        assertEquals(-1, result.getGuess());
        assertNotNull(result.getError());
    }

    @Test
    void testValidateGuess_invalidEmptyInput() {
        Result result = GameLogic.validateGuess("");
        assertEquals(-1, result.getGuess());
        assertNotNull(result.getError());
    }

    @Test
    void testValidateGuess_invalidWhitespaceInput() {
        Result result = GameLogic.validateGuess("   ");
        assertEquals(-1, result.getGuess());
        assertNotNull(result.getError());
    }

    @Test
    void testValidateGuess_invalidNonDigitInput() {
        Result result = GameLogic.validateGuess("123$");
        assertEquals(-1, result.getGuess());
        assertNotNull(result.getError());
    }

    @Test
    void testValidateGuess_invalidTooShortInput() {
        Result result = GameLogic.validateGuess("123");
        assertEquals(-1, result.getGuess());
        assertNotNull(result.getError());
    }

    @Test
    void testValidateGuess_invalidTooLongInput() {
        Result result = GameLogic.validateGuess("12345");
        assertEquals(-1, result.getGuess());
        assertNotNull(result.getError());
    }

    @Test
    void testValidateGuess_invalidNegativeInput() {
        Result result = GameLogic.validateGuess("-123");
        assertEquals(-1, result.getGuess());
        assertNotNull(result.getError());
    }

    @Test
    void testGenerateSecretCode_evenSum_notPalindrome() throws Exception {
        // Create a mock Random that will return a fixed value
        TestableRandom mockRandom = new TestableRandom(1234); // Sum = 10 (even)

        // Inject the mock Random into the GameLogic instance
        injectMockRandom(mockRandom);

        // Expected behavior: reverse the number since sum is even
        // 1234 -> 4321
        assertEquals(4321, gameLogic.generateSecretCode());
    }

    @Test
    void testGenerateSecretCode_oddSum_notPalindrome() throws Exception {
        // Create a mock Random that will return a fixed value
        TestableRandom mockRandom = new TestableRandom(1235); // Sum = 11 (odd)

        // Inject the mock Random into the GameLogic instance
        injectMockRandom(mockRandom);

        // Expected behavior: increment each digit since sum is odd
        // 1235 -> 2346
        assertEquals(2346, gameLogic.generateSecretCode());
    }

    @Test
    void testGenerateSecretCode_becomesPalindrome() throws Exception {
        // Create a mock Random that will return a fixed value
        TestableRandom mockRandom = new TestableRandom(1221); // Sum = 6 (even)

        // Inject the mock Random into the GameLogic instance
        injectMockRandom(mockRandom);

        // Expected behavior:
        // 1. Reverse since sum is even: 1221 -> 1221 (still the same)
        // 2. It's a palindrome, so replace with 7777
        assertEquals(7777, gameLogic.generateSecretCode());
    }

    @Test
    void testGenerateSecretCode_oddSum_becomesPalindrome() throws Exception {
        // Create a mock Random that will return a fixed value
        TestableRandom mockRandom = new TestableRandom(9889); // Sum = 34 (even)

        // Inject the mock Random into the GameLogic instance
        injectMockRandom(mockRandom);

        // Expected behavior:
        // 1. Reverse since sum is even: 9889 -> 9889 (still the same)
        // 2. It's a palindrome, so replace with 7777
        assertEquals(7777, gameLogic.generateSecretCode());
    }

    @Test
    void testGenerateSecretCode_oddSum_becomesPalindromeAfterIncrement() throws Exception {
        // Create a mock Random that will return a fixed value
        TestableRandom mockRandom = new TestableRandom(8998); // Sum = 34 (even)

        // Inject the mock Random into the GameLogic instance
        injectMockRandom(mockRandom);

        // Expected behavior:
        // 1. Reverse since sum is even: 8998 -> 8998 (still the same)
        // 2. It's a palindrome, so replace with 7777
        assertEquals(7777, gameLogic.generateSecretCode());
    }

    @Test
    void testGenerateSecretCode_nineWraparound() throws Exception {
        // Create a mock Random that will return a fixed value
        TestableRandom mockRandom = new TestableRandom(9999); // Sum = 36 (even)

        // Inject the mock Random into the GameLogic instance
        injectMockRandom(mockRandom);

        // Expected behavior:
        // 1. Reverse since sum is even: 9999 -> 9999 (still the same)
        // 2. It's a palindrome, so replace with 7777
        assertEquals(7777, gameLogic.generateSecretCode());
    }

    @Test
    void testGenerateSecretCode_incrementWithNineWraparound() throws Exception {
        // Create a mock Random that will return a fixed value
        TestableRandom mockRandom = new TestableRandom(3929); // Sum = 23 (odd)

        // Inject the mock Random into the GameLogic instance
        injectMockRandom(mockRandom);

        // Expected behavior:
        // 1. Increment each digit since sum is odd: 3929 -> 4030
        assertEquals(4030, gameLogic.generateSecretCode());
    }

    // Helper class to create a deterministic Random for testing
    private static class TestableRandom extends Random {
        private final int fixedNumber;

        public TestableRandom(int fixedNumber) {
            this.fixedNumber = fixedNumber;
        }

        @Override
        public int nextInt(int bound) {
            // Return a value that will result in our fixed number when added to 1000
            return fixedNumber - 1000;
        }
    }

    // Helper method to inject our mock Random into the GameLogic instance
    private void injectMockRandom(Random mockRandom) throws Exception {
        Field randomField = GameLogic.class.getDeclaredField("random");
        randomField.setAccessible(true);
        randomField.set(gameLogic, mockRandom);
    }

    @Test
    void testGenerateTimestampPrefix() {
        String prefix = GameLogic.generateTimestampPrefix();

        // Check that the prefix starts with "TIME: "
        assertTrue(prefix.startsWith("TIME: "));

        // Check that the prefix contains a valid timestamp
        String timestampStr = prefix.substring(6, prefix.length() - 3); // Remove "TIME: " and " | "
        try {
            long timestamp = Long.parseLong(timestampStr);
            // Check that the timestamp is recent (within the last minute)
            long currentTime = System.currentTimeMillis() / 1000;
            assertTrue(currentTime - timestamp < 60);
        } catch (NumberFormatException e) {
            fail("Prefix does not contain a valid timestamp");
        }
    }

    @Test
    void testApplyPrefixToResult() {
        int testNumber = 1234;
        String result = GameLogic.applyPrefixToResult(testNumber);

        // Check that the result starts with a timestamp prefix
        assertTrue(result.startsWith("TIME: "));

        // Check that the result ends with the correct number
        assertTrue(result.endsWith("" + testNumber));

        // Check that the prefix and number are properly separated
        assertTrue(result.contains(" | " + testNumber));
    }

//    @Test
//    void testValidateGuess() {
//	/* Example inputs:
//	valid: "007123", "1181", " 1022  "
//	invalid: "$", "-15", " "
//	*/
//        assertEquals(10, gameLogic.validateGuess("1000"));
//    }

//    @Test
//    void testGenerateSecretCode() {
//        assertEquals(1111, gameLogic.generateCode());
//    }
}
