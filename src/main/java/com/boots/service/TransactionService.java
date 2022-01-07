package com.boots.service;

import com.boots.entity.TransactionType;
import com.boots.repository.BalanceRepository;
import com.boots.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    public ArrayList<TransactionType> getTransactionsByBalanceId(Long id) {
        ArrayList<TransactionType> result = transactionRepository.findAllByBalanceId(id);
        return result;
    }
}
