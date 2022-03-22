package com.boots.controller;

import com.boots.entity.Balance;
import com.boots.entity.ETransactionTypes;
import com.boots.entity.TransactionType;
import com.boots.exceptions.BalanceNotFoundException;
import com.boots.exceptions.TransactionNotFoundException;
import com.boots.helpers.TransactionHelper;
import com.boots.model.NewTransactionForm;
import com.boots.service.BalanceService;
import com.boots.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.logging.Logger;


@RestController
@RequestMapping("transactions")
public class TransactionController {

    private static final Logger log = Logger.getLogger(TransactionController.class.getName());

    @Autowired
    private BalanceService balanceService;
    @Autowired
    private TransactionService transactionService;

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("{id}")
    public TransactionType getTransaction(
            @PathVariable Long id) throws TransactionNotFoundException {
        return transactionService.getTransactionById(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping()
    public ArrayList<TransactionType> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping()
    public void addNewTransaction(
            @RequestBody NewTransactionForm newTransactionForm) throws BalanceNotFoundException {
        log.info(String.format("params: userName:%s, balanceId:%s, amount:%s, date:%s, transactionType:%s, commentary:%s ",
                newTransactionForm.getUserName(), newTransactionForm.getBalanceId(), newTransactionForm.getAmount(),
                newTransactionForm.getDate(), newTransactionForm.getTransactionType(), newTransactionForm.getCommentary()));

        Balance balance = balanceService.getBalanceById(newTransactionForm.getBalanceId());

        TransactionType newTransaction = new TransactionType();
        newTransaction.setBalanceId(balance.getId());
        newTransaction.setCommentary(newTransactionForm.getCommentary());
        newTransaction.setDate(newTransactionForm.getDate());
        newTransaction.setAmount(newTransactionForm.getAmount());
        newTransaction.setTransactionType(newTransactionForm.getTransactionType().equalsIgnoreCase("доход") ?
                ETransactionTypes.income :
                ETransactionTypes.consumption);

        transactionService.addTransaction(newTransaction);
        balance.setAmount(newTransaction.getTransactionType() == ETransactionTypes.income ?
                balance.getAmount() + newTransactionForm.getAmount() :
                balance.getAmount() - newTransactionForm.getAmount());
        balanceService.updateBalance(balance);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void removeTransaction(
            @PathVariable Long id) throws TransactionNotFoundException, BalanceNotFoundException {
        log.info(String.format("params: id:%s", id));

        TransactionType foundTransaction = transactionService.getTransactionById(id);
            Balance bindBalance = balanceService.getBalanceById(foundTransaction.getBalanceId());

            bindBalance.setAmount(foundTransaction.getTransactionType() == ETransactionTypes.income ?
                    bindBalance.getAmount() - foundTransaction.getAmount() :
                    bindBalance.getAmount() + foundTransaction.getAmount());
            balanceService.updateBalance(bindBalance);
            transactionService.deleteTransactionById(foundTransaction.getId());
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PutMapping()
    public void updateTransaction(
            @RequestParam Long transactionId,
            @RequestParam String transactionType,
            @RequestParam Double amount,
            @RequestParam Date date,
            @RequestParam String commentary
    ) throws TransactionNotFoundException, BalanceNotFoundException {
        log.info(String.format("params: transactionId:%s, transactionType:%s, amount:%s, date:%s, commentary:%s, ",
                transactionId, transactionType, amount, date, commentary));
        TransactionType newTransaction = transactionService.getTransactionById(transactionId);
            ETransactionTypes oldTransactionType = newTransaction.getTransactionType();
            Double oldTransactionAmount = newTransaction.getAmount();

            newTransaction.setTransactionType(transactionType.equalsIgnoreCase("доход") ?
                    ETransactionTypes.income :
                    ETransactionTypes.consumption);
            newTransaction.setDate(date);
            newTransaction.setAmount(amount);
            newTransaction.setCommentary(commentary);

            Balance bindBalance = balanceService.getBalanceById(newTransaction.getBalanceId());
            bindBalance.setAmount(TransactionHelper.getNewAmount(
                    oldTransactionType,
                    newTransaction.getTransactionType(),
                    oldTransactionAmount,
                    newTransaction.getAmount(),
                    bindBalance.getAmount()));
            balanceService.updateBalance(bindBalance);
            transactionService.updateTransaction(newTransaction);
    }
}
