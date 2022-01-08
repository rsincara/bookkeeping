package com.boots.service;

import com.boots.entity.TransactionType;
import com.boots.entity.User;
import com.boots.repository.BalanceRepository;
import com.boots.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    public ArrayList<TransactionType> getTransactionsByBalanceId(Long id) {
        ArrayList<TransactionType> result = transactionRepository.findAllByBalanceId(id);
        return result;
    }

    public boolean addTransaction(TransactionType transactionType) {
        transactionRepository.save(transactionType);

        return true;
    }

    public void deleteTransactionById(Long id) {
        transactionRepository.deleteById(id);
    }

    public void deleteTransactionsByBalanceId(Long id) {
        transactionRepository.removeByBalanceId(id);
    }

}
