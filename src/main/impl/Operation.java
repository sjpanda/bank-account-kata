package main.impl;

import main.IOperation;

import java.util.Date;
import java.util.Objects;

public class Operation implements IOperation {

    private final Date date;
    private final Double amount; // signed : "+" for deposit and "-" for withdrawal

    public Operation(Double amount) {
        this.date = new Date(); // current date
        this.amount = amount;
    }

    public Operation(Date date, Double amount) {
        this.date = date;
        this.amount = amount;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public Double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return Objects.equals(date, operation.date) &&
                Objects.equals(amount, operation.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, amount);
    }

    @Override
    public String toString() {
        return "Operation{" +
                "date=" + date +
                ", amount=" + amount +
                '}';
    }
}
