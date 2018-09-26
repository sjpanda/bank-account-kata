package main;

public class BadBalanceException extends Exception {

    private Double balance;

    public BadBalanceException(Double illegalBalance) {
        balance = illegalBalance;
    }

    public String toString() {
        return "Bad account balance: " + balance;
    }
}