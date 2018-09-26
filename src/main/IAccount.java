package main;

import main.impl.AccountRule;

import java.time.LocalDate;
import java.util.List;

/**
 * An account in mono currency (eg. EUR)
 */
public interface IAccount {

    /**
     * Get the current balance
     * @return the current balance of the current account
     */
    public Double getBalance();

    /**
     * Get the balance at the end of the date in parameter
     * @param date the reference date
     * @return the balance at the end of the date
     */
    public Double getBalanceAtTheEndOf(LocalDate date);

    /**
     * Get the balance after the operation in parameter
     * @param operation the reference operation
     * @return the balance after the operation
     */
    public Double getBalanceAfterOperation(IOperation operation);

    /**
     * Add money to the current account
     * @param amount amount to add
     */
    public void add(Double amount);

    /**
     * Withdraw money and return the balance after the withdrawal
     * @param amount amount to withdraw
     * @param accountRule
     * @return the new balance
     * @throws BadBalanceException
     */
    public Double withdrawAndGetBalance(Double amount, IAccountRule accountRule) throws BadBalanceException;

    /**
     * Get the list of operation in a period
     * @param start start date included
     * @param end end date inclued
     * @return a list of operation
     */
    public List<IOperation> getOperations(LocalDate start, LocalDate end);
}
