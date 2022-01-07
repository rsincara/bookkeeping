package com.boots.service;

import com.boots.entity.Balance;
import com.boots.entity.TransactionType;
import com.boots.model.BalanceWithTransactions;
import com.boots.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class BalanceService {
    @Autowired
    BalanceRepository balanceRepository;

    @Autowired
    TransactionService transactionService;

    public ArrayList<BalanceWithTransactions> getBalancesWithTransactionsByUserId(long id) {
        ArrayList<BalanceWithTransactions> result = new ArrayList<>();
        ArrayList<Balance> foundBalance = balanceRepository.findAllByUserId(id);
        if(foundBalance.size() != 0) {
            for (Balance balance:foundBalance) {
                ArrayList<TransactionType> transactions = transactionService.getTransactionsByBalanceId(balance.getId());
                result.add(new BalanceWithTransactions(balance.getName(), balance.getAmount(), transactions ));
            }

            return result;
        }

        return null;
    }
}
