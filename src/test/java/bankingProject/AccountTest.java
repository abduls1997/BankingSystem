package bankingProject;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("001","Abdul35", 1000.0, "SAVINGS");
    }

    @Test
    void testDeposit() {
        account.deposit(500.0);
        assertEquals(1500.0, account.getBalance());
    }

    @Test
    void testWithdraw() {
        account.withdraw(500.0);
        assertEquals(500.0, account.getBalance());
    }

    @Test
    void testNegativeDeposit() {
        account.deposit(-100.0);
        assertEquals(1000.0, account.getBalance());
    }

    @Test
    void testOverdraw() {
        account.withdraw(2000.0);
        assertEquals(1000.0, account.getBalance());
    }
}