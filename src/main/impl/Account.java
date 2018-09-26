package main.impl;

import main.BadBalanceException;
import main.IAccount;
import main.IAccountRule;
import main.IOperation;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Account implements IAccount {

    // it's better to use BigDecimal when we handle much money,
    // event if it's less performing than Double
    private BigDecimal balance;
    // ordered by date
    private List<IOperation> operations;

    public Account() {
        balance = BigDecimal.ZERO;
        operations = new ArrayList<>();
    }


    @Override
    public Double getBalance() {
        return balance == null ? null : balance.doubleValue();
    }

    @Override
    public Double getBalanceAtTheEndOf(LocalDate date) {
        List<IOperation> ops = getOperations(null, date);
        if (ops.size() == 0) {
            return 0d;
        }
        return getSum(ops);
    }

    @Override
    public Double getBalanceAfterOperation(IOperation operation) {
        int index = operations.indexOf(operation);
        if (index < 0) {
            return null;
        }
        return getSum(operations.subList(0, index + 1));
    }

    @Override
    public void add(Double amount) {
        if (amount == null || amount <= 0d) {
            // here I choose to ignore null or negative amount,
            // but we can also call withdrawAndReportBalance(-addedAmount, rule) for negative amount if wanted
            // it's better to report error message (System.out.println, Log, Customized error reporter in application, ...)
            return;
        }
        BigDecimal bigAddedAmount = BigDecimal.valueOf(amount);
        balance = balance == null ? bigAddedAmount : balance.add(bigAddedAmount);
        operations.add(new Operation(amount));
    }

    @Override
    public Double withdrawAndGetBalance(Double amount, IAccountRule accountRule) throws BadBalanceException {
        if (amount == null || amount <= 0d || accountRule == null) {
            // here I choose to ignore null or negative amount, and null rule
            // but we can also call add(-withdrawnAmount) for negative amount if wanted
            // it's better to report error message (System.out.println, Log, Customized error reporter in application, ...)
            return getBalance();
        }
        if (balance == null) {
            throw new BadBalanceException(-amount);
        }

        BigDecimal bigWithdrawnAmount = BigDecimal.valueOf(amount);
        BigDecimal resultingBalance = balance.subtract(bigWithdrawnAmount);
        double resultingBalanceDbl = resultingBalance.doubleValue();
        if (!accountRule.checkRule(resultingBalanceDbl)) {
            throw new BadBalanceException(resultingBalanceDbl);
        }
        balance = resultingBalance;
        operations.add(new Operation(-amount));
        return resultingBalanceDbl;
    }

    @Override
    public List<IOperation> getOperations(LocalDate start, LocalDate end) {
        return operations.stream().filter(o -> {
            LocalDate opLocalDate = dateToLocalDate(o.getDate());
            return (start == null || opLocalDate.equals(start) || opLocalDate.isAfter(start))
                    && (end == null || opLocalDate.equals(end) || opLocalDate.isBefore(end));
        }).collect(Collectors.toList());
    }

    private LocalDate dateToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        ZoneId defaultZoneId = ZoneId.systemDefault();
        // Convert Date -> Instant
        Instant instant = date.toInstant();
        // Instant + system default time zone + toLocalDate() = LocalDate
        return instant.atZone(defaultZoneId).toLocalDate();
    }

    private Double getSum(List<IOperation> ops) {
        BigDecimal ret = BigDecimal.ZERO;
        if (ops != null && ops.size() > 0) {
            for (IOperation op : ops) {
                ret = ret.add(new BigDecimal(op.getAmount()));
            }
        }
        return ret.doubleValue();
    }
}
