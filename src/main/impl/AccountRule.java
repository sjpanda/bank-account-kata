package main.impl;

import main.IAccountRule;

public class AccountRule implements IAccountRule {

    @Override
    public boolean checkRule(Double resultingAccountBalance) {
        return resultingAccountBalance != null && resultingAccountBalance >= 0;
    }
}
