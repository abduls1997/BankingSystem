package bankingProject;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class BankingSystemTest {
    private static final String TEST_USERS_FILE = "users.txt";
    private static final String TEST_ACCOUNTS_FILE = "accounts.txt";

    @BeforeEach
    void setUp() {
        // Delete test files before each test
        try {
            Files.deleteIfExists(Paths.get(TEST_USERS_FILE));
            Files.deleteIfExists(Paths.get(TEST_ACCOUNTS_FILE));
        } catch (Exception e) {
            System.err.println("Error cleaning up test files: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        // Clean up test files after each test
        try {
            Files.deleteIfExists(Paths.get(TEST_USERS_FILE));
            Files.deleteIfExists(Paths.get(TEST_ACCOUNTS_FILE));
        } catch (Exception e) {
            System.err.println("Error cleaning up test files: " + e.getMessage());
        }
    }

    @Test
    void testIsUsernameUnique() throws IOException {
        // Create a test user file with a known username
        try (FileWriter writer = new FileWriter(TEST_USERS_FILE)) {
            writer.write("testuser,password123\n");
        }

        assertFalse(BankingSystem.isUsernameUnique("testuser"));
        assertTrue(BankingSystem.isUsernameUnique("newuser"));
    }

    
    
    
}