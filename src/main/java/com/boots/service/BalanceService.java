package com.boots.service;

import com.boots.entity.Balance;
import com.boots.entity.ETransactionTypes;
import com.boots.entity.TransactionType;
import com.boots.exceptions.BalanceAlreadyExists;
import com.boots.exceptions.BalanceNotFoundException;
import com.boots.model.BalanceWithTransactions;
import com.boots.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class BalanceService {
    @Autowired
    BalanceRepository balanceRepository;

    @Autowired
    TransactionService transactionService;


    public ArrayList<Balance> getAllBalances()  {
        return balanceRepository.findAll();
    }

    public Balance getBalanceById(Long id) throws BalanceNotFoundException {

        Optional<Balance> balance = balanceRepository.findById(id);
        if (balance.isPresent()) {
            return balance.get();
        } else {
            throw new BalanceNotFoundException(id);
        }
    }

    public ArrayList<BalanceWithTransactions> getBalancesWithTransactionsByUserId(long id) {
        ArrayList<BalanceWithTransactions> result = new ArrayList<>();
        ArrayList<Balance> foundBalance = balanceRepository.findAllByUserId(id);
            for (Balance balance : foundBalance) {
                ArrayList<TransactionType> transactions = transactionService.getTransactionsByBalanceId(balance.getId());
                result.add(new BalanceWithTransactions(balance, balance.getAmount(), transactions));
            }

            return result;
    }

    public Balance getBalanceByUserIdAndBalanceNameOrNull(Long userId, String balanceName) {
        ArrayList<Balance> foundBalances = balanceRepository.findAllByUserId(userId);
        for (Balance balance : foundBalances) {
            if (balance.getUserId().equals(userId) && balance.getName().equals(balanceName)) {
                return balance;
            }
        }
        return null;
    }

    public void removeBalance(Balance balance) {
        ArrayList<TransactionType> bindTransactions = transactionService.getTransactionsByBalanceId(balance.getId());
        for (TransactionType removingTransaction : bindTransactions) {
            transactionService.deleteTransactionById(removingTransaction.getId());
        }
        balanceRepository.deleteById(balance.getId());
    }

    public void updateBalance(Balance balance) {
        balanceRepository.save(balance);
    }


    public void saveBalance(Balance balance) throws BalanceAlreadyExists {
        ArrayList<Balance> foundBalances = balanceRepository.findAllByUserId(balance.getUserId());
        for (Balance foundBalance : foundBalances) {
            if (foundBalance.getName().equalsIgnoreCase(balance.getName())) {
                throw new BalanceAlreadyExists(foundBalance.getUserId(), balance.getName());
            }
        }

        TransactionType newTransaction = new TransactionType();
        newTransaction.setAmount(balance.getAmount());
        newTransaction.setTransactionType(ETransactionTypes.income);
        java.util.Date newDate = new java.util.Date();
        newTransaction.setDate(new Date(newDate.getYear(), newDate.getMonth(), newDate.getDate()));
        newTransaction.setCommentary("Начальный баланс");
        balanceRepository.save(balance);
        Balance addedBalance = getBalanceByUserIdAndBalanceNameOrNull(balance.getUserId(), balance.getName());
        // нужно, т.к. мы не знаем ID баланса на стадии добавления
        if (addedBalance != null) {
            newTransaction.setBalanceId(addedBalance.getId());
            transactionService.addTransaction(newTransaction);
        }
    }
}
