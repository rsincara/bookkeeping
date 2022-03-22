package com.boots.exceptions;

public class BalanceAlreadyExists extends Exception {
    public BalanceAlreadyExists(Long userId, String balanceName ) {
        super(String.format("User with id: '%s' already have balance with name: '%s'", userId, balanceName ));
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
