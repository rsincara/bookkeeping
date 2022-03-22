package com.boots.advices;

import com.boots.exceptions.BalanceAlreadyExists;
import com.boots.exceptions.BalanceNotFoundException;
import com.boots.exceptions.TransactionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultAdvice {
    @ExceptionHandler(BalanceNotFoundException.class)
    public ResponseEntity<String> balanceNotFoundException(BalanceNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BalanceAlreadyExists.class)
    public ResponseEntity<String> balanceAlreadyExists(BalanceAlreadyExists e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<String> transactionNotFoundException(TransactionNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
