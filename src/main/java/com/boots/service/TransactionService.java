package com.boots.service;

import com.boots.entity.TransactionType;
import com.boots.exceptions.TransactionNotFoundException;
import com.boots.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    public ArrayList<TransactionType> getAllTransactions()  {
        return transactionRepository.findAll();
    }

    public TransactionType getTransactionById(Long id) throws TransactionNotFoundException {
        Optional<TransactionType> transactionType = transactionRepository.findById(id);
        if (transactionType.isPresent()) {
            return transactionType.get();
        }
        throw new TransactionNotFoundException(id);
    }

    public void updateTransaction(TransactionType transactionType) {
        transactionRepository.save(transactionType);
    }

    public ArrayList<TransactionType> getTransactionsByBalanceId(Long id) {
        return transactionRepository.findAllByBalanceId(id);
    }

    public void addTransaction(TransactionType transactionType) {
        transactionRepository.save(transactionType);

    }

    public void deleteTransactionById(Long id) {
        transactionRepository.deleteById(id);
    }

    public void deleteTransactionsByBalanceId(Long id) {
        transactionRepository.removeByBalanceId(id);
    }

}
