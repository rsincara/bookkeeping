package com.boots.exceptions;

import javassist.NotFoundException;

public class TransactionNotFoundException extends NotFoundException {

    public TransactionNotFoundException(Long transactionId ) {
        super(String.format("Not found transaction with id: '%s'", transactionId));
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
