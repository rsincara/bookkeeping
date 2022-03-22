package com.boots.exceptions;

import javassist.NotFoundException;

public class BalanceNotFoundException extends NotFoundException {

    public BalanceNotFoundException(Long id ) {
        super(String.format("Not found balance with id:'%s'  ", id));
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
