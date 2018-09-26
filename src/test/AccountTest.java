package test;

import main.IAccount;
import main.IAccountRule;
import main.IOperation;
import main.impl.Account;
import main.impl.AccountRule;
import main.BadBalanceException;
import main.impl.Operation;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class AccountTest {

    private IAccount account;
    private IAccountRule rule;

    @Before
    public void setUp() {
        account = new Account();
    }

    /**
     * Tests that the balance of an empty account is always 0.0, not NULL.
     */
    @Test(timeout = 5000L)
    public void testZeroBalance() {
        // empty account already created in setUp()
        assertNotNull("Failed precondition: empty account not created yet", account);

        // get current account balance
        Double balance = account.getBalance();

        // check that the balance is not null
        assertNotNull("Null balance", balance);

        // check that the balance is 0.0
        assertEquals(String.format("Expected 0.0 balance but got %s", balance), 0, Double.compare(0.0, balance));
        // or assertEquals(0.0, balance, 0.0000000001);
    }

    /**
     * Test the deposit.
     */
    @Test(timeout = 5000L)
    public void testAddPositiveAmount() {
        // empty account already created in setUp()
        assertNotNull("Failed precondition: empty account not created yet", account);

        // define the amount to add
        double amountToAdd = 65866879436.35;

        // add positive amount
        account.add(amountToAdd);

        // get the balance after adding amount
        Double newBalance = account.getBalance();

        // check the new balance
        // check that the balance is not null
        assertNotNull("Null balance", newBalance);

        // check that the balance is expected
        assertEquals(String.format("Balance: expected %s but got %s", amountToAdd, newBalance), 0, Double.compare(amountToAdd, newBalance));
        // or assertEquals(amountToAdd, newBalance, 0.0000000001);
    }

    /**
     * Tests the withdrawal.
     */
    @Test(timeout = 5000L, expected= BadBalanceException.class)
    public void testWithdrawAndGetBadBalance() throws BadBalanceException {
        // empty account already created in setUp()
        assertNotNull("Failed precondition: empty account not created yet", account);

        // create a account rule
        rule = new AccountRule();

        // withdraw balance
        account.withdrawAndGetBalance(1d, rule);
    }

    /**
     * Adds null money to the account and checks that the new balance is as expected.
     */
    @Test(timeout = 5000L)
    public void testAddNullAmount() {
        // empty account already created in setUp()
        assertNotNull("Failed precondition: empty account not created yet", account);

        // add positive amount
        account.add(null);

        // get the balance after adding amount
        Double newBalance = account.getBalance();

        // check the new balance
        // check that the balance is not null
        assertNotNull("Null balance", newBalance);

        // check that the balance is expected
        assertEquals(String.format("Balance: expected %s but got %s", 0.0, newBalance), 0, Double.compare(0.0, newBalance));
    }

    /**
     * Adds negative money to the account and checks that the new balance is as expected.
     */
    @Test(timeout = 5000L)
    public void testAddNegativeAmount() {
        // empty account already created in setUp()
        assertNotNull("Failed precondition: empty account not created yet", account);

        double amountToAdd = -65866879436.35;

        // add positive amount
        account.add(amountToAdd);

        // get the balance after adding amount
        Double newBalance = account.getBalance();

        // check the new balance
        // check that the balance is not null
        assertNotNull("Null balance", newBalance);

        // check that the balance is expected
        assertEquals(String.format("Balance: expected %s but got %s", 0.0, newBalance), 0, Double.compare(0.0, newBalance));
    }

    /**
     * Tests that a legal withdrawal doesn't throws the expected exception and the new balance is as expected.
     */
    @Test(timeout = 5000L)
    public void testWithdrawAndGetBalanceGoodBalance() {
        // empty account already created in setUp()
        assertNotNull("Failed precondition: empty account not created yet", account);

        // add money
        double oldBalance = 100.0;
        account.add(oldBalance);

        // create a account rule
        rule = new AccountRule();

        // withdraw oldBalance
        try {
            Double newBalance = account.withdrawAndGetBalance(1d, rule);
            assertEquals(String.format("Balance: expected %s but got %s", oldBalance - 1d, newBalance),
                    0, Double.compare(oldBalance - 1d, newBalance));
        } catch (BadBalanceException e) {
            // fail if we get the exception
            fail(e.getMessage());
        }
    }

    /**
     * Tests that a null withdrawal doesn't impact the balance.
     */
    @Test(timeout = 5000L)
    public void testWithdrawAndGetBalanceNullAmount() {
        // empty account already created in setUp()
        assertNotNull("Failed precondition: empty account not created yet", account);

        // create a account rule
        rule = new AccountRule();

        // withdraw balance
        try {
            Double newBalance = account.withdrawAndGetBalance(null, rule);
            // check that the balance is expected
            assertEquals(String.format("Balance: expected %s but got %s", 0.0, newBalance), 0, Double.compare(0.0, newBalance));
        } catch (BadBalanceException e) {
            // fail if we get the exception
            fail(e.getMessage());
        }
    }

    /**
     * Tests that a negative withdrawal doesn't impact the balance.
     */
    @Test(timeout = 5000L)
    public void testWithdrawAndGetBalanceNegativeAmount() {
        // empty account already created in setUp()
        assertNotNull("Failed precondition: empty account not created yet", account);

        // create a account rule
        rule = new AccountRule();

        // withdraw balance
        try {
            Double newBalance = account.withdrawAndGetBalance(-1.0, rule);
            // check that the balance is expected
            assertEquals(String.format("Balance: expected %s but got %s", 0.0, newBalance), 0, Double.compare(0.0, newBalance));
        } catch (BadBalanceException e) {
            // fail if we get the exception
            fail(e.getMessage());
        }
    }

    /**
     * Tests that a withdrawal with null rule doesn't impact the balance.
     */
    @Test(timeout = 5000L)
    public void testWithdrawAndGetBalanceNullRule() {
        // empty account already created in setUp()
        assertNotNull("Failed precondition: empty account not created yet", account);

        // withdraw balance
        try {
            Double newBalance = account.withdrawAndGetBalance(-1.0, null);
            // check that the balance is expected
            assertEquals(String.format("Balance: expected %s but got %s", 0.0, newBalance), 0, Double.compare(0.0, newBalance));
        } catch (BadBalanceException e) {
            // fail if we get the exception
            fail(e.getMessage());
        }
    }

    @Test(timeout = 5000L)
    public void testGetOperations() {
        // empty account already created in setUp()
        assertNotNull("Failed precondition: empty account not created yet", account);
        // create a account rule
        rule = new AccountRule();

        try {
            // add money
            account.add(100.0);
            // withdraw balance
            account.withdrawAndGetBalance(80.0, rule);

            List<IOperation> ops = account.getOperations(null, LocalDate.now().minusDays(10));
            assertEquals(0, ops.size());

            ops = account.getOperations(LocalDate.now().plusDays(10), null);
            assertEquals(0, ops.size());

            ops = account.getOperations(LocalDate.now(), LocalDate.now());
            assertEquals(2, ops.size());
            double delta = 0.0000000001;
            assertEquals(100.0, ops.get(0).getAmount(), delta);
            assertEquals(-80.0, ops.get(1).getAmount(), delta);

            ops = account.getOperations(null, null);
            assertEquals(2, ops.size());
            assertEquals(100.0, ops.get(0).getAmount(), delta);
            assertEquals(-80.0, ops.get(1).getAmount(), delta);
        } catch (BadBalanceException e) {
            // fail if we get the exception
            fail(e.getMessage());
        }
    }

    @Test(timeout = 5000L)
    public void testGetBalanceAtTheEndOf() {
        // empty account already created in setUp()
        assertNotNull("Failed precondition: empty account not created yet", account);
        // create a account rule
        rule = new AccountRule();

        try {
            // add money
            account.add(100.0);
            // withdraw balance
            account.withdrawAndGetBalance(80.0, rule);

            Double balanceAtTheEndOf = account.getBalanceAtTheEndOf(LocalDate.now().minusDays(1));
            double delta = 0.0000000001;
            assertEquals(0d, balanceAtTheEndOf, delta);

            balanceAtTheEndOf = account.getBalanceAtTheEndOf(LocalDate.now());
            assertEquals(20.0, balanceAtTheEndOf, delta);

            balanceAtTheEndOf = account.getBalanceAtTheEndOf(LocalDate.now().plusDays(1));
            assertEquals(20.0, balanceAtTheEndOf, delta);
        } catch (BadBalanceException e) {
            // fail if we get the exception
            fail(e.getMessage());
        }
    }

    @Test(timeout = 5000L)
    public void testGetBalanceAfterOperation() {
        // empty account already created in setUp()
        assertNotNull("Failed precondition: empty account not created yet", account);
        // create a account rule
        rule = new AccountRule();

        try {
            // add money
            account.add(100.0);
            // withdraw balance
            account.withdrawAndGetBalance(80.0, rule);

            List<IOperation> ops = account.getOperations(null, null);
            assertEquals(2, ops.size());
            double delta = 0.0000000001;
            assertEquals(100.0, ops.get(0).getAmount(), delta);
            assertEquals(-80.0, ops.get(1).getAmount(), delta);

            Double balanceAfterOperation = account.getBalanceAfterOperation(ops.get(0));
            assertEquals(100.0, balanceAfterOperation, delta);

            balanceAfterOperation = account.getBalanceAfterOperation(ops.get(1));
            assertEquals(20.0, balanceAfterOperation, delta);
        } catch (BadBalanceException e) {
            // fail if we get the exception
            fail(e.getMessage());
        }
    }
}
