package com.boots.controller;

import com.boots.entity.Balance;
import com.boots.entity.ETransactionTypes;
import com.boots.entity.TransactionType;
import com.boots.entity.User;
import com.boots.helpers.TransactionHelper;
import com.boots.service.BalanceService;
import com.boots.service.TransactionService;
import com.boots.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Optional;
import java.util.logging.Logger;


@Controller
public class TransactionController {

    private static final Logger log = Logger.getLogger(TransactionController.class.getName());

    @Autowired
    private UserService userService;
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/add-transaction")
    public String addNewTransaction(
            @RequestParam String userName,
            @RequestParam String balanceName,
            @RequestParam Double amount,
            @RequestParam Date date,
            @RequestParam String transactionType,
            @RequestParam String commentary) {
        log.info(String.format("params: userName:%s, balanceName:%s, amount:%s, date:%s, transactionType:%s, commentary:%s ",
                userName, balanceName, amount, date, transactionType, commentary));

        User user = userService.loadUserByUsername(userName);
        Balance balance = balanceService.getBalanceByUserIdAndBalanceName(user.getId(), balanceName);

        TransactionType newTransaction = new TransactionType();
        newTransaction.setBalanceId(balance.getId());
        newTransaction.setCommentary(commentary);
        newTransaction.setDate(date);
        newTransaction.setAmount(amount);
        newTransaction.setTransactionType(transactionType.equalsIgnoreCase("доход") ?
                ETransactionTypes.income :
                ETransactionTypes.consumption);

        transactionService.addTransaction(newTransaction);
        balance.setAmount(newTransaction.getTransactionType() == ETransactionTypes.income ?
                balance.getAmount() + amount :
                balance.getAmount() - amount);
        balanceService.updateBalance(balance);

        return "redirect:/";
    }

    @GetMapping("/remove-transaction")
    public String removeTransaction(@RequestParam Long id) {
        log.info(String.format("params: id:%s", id));

        Optional<TransactionType> transactionType = transactionService.getTransactionById(id);
        if (transactionType.isPresent()) {
            TransactionType foundTransaction = transactionType.get();
            Balance bindBalance = balanceService.getBalanceById(foundTransaction.getBalanceId()).get();

            bindBalance.setAmount(foundTransaction.getTransactionType() == ETransactionTypes.income ?
                    bindBalance.getAmount() - foundTransaction.getAmount() :
                    bindBalance.getAmount() + foundTransaction.getAmount());
            balanceService.updateBalance(bindBalance);
            transactionService.deleteTransactionById(foundTransaction.getId());
        }
        return "redirect:/";
    }

    @PostMapping("/update-transaction")
    public String updateTransaction(
            @RequestParam Long transactionId,
            @RequestParam String transactionType,
            @RequestParam Double amount,
            @RequestParam Date date,
            @RequestParam String commentary
    ) {
        log.info(String.format("params: transactionId:%s, transactionType:%s, amount:%s, date:%s, commentary:%s, ",
                transactionId, transactionType, amount, date, commentary));
        Optional<TransactionType> optionalTransactionType = transactionService.getTransactionById(transactionId);
        if (optionalTransactionType.isPresent()) {
            TransactionType newTransaction = optionalTransactionType.get();
            ETransactionTypes oldTransactionType = newTransaction.getTransactionType();
            Double oldTransactionAmount = newTransaction.getAmount();

            newTransaction.setTransactionType(transactionType.equalsIgnoreCase("доход") ?
                    ETransactionTypes.income :
                    ETransactionTypes.consumption);
            newTransaction.setDate(date);
            newTransaction.setAmount(amount);
            newTransaction.setCommentary(commentary);

            Balance bindBalance = balanceService.getBalanceById(newTransaction.getBalanceId()).get();
            bindBalance.setAmount(TransactionHelper.getNewAmount(
                    oldTransactionType,
                    newTransaction.getTransactionType(),
                    oldTransactionAmount,
                    newTransaction.getAmount(),
                    bindBalance.getAmount()));
            balanceService.updateBalance(bindBalance);
            transactionService.updateTransaction(newTransaction);
        }
        return "redirect:/";
    }
}
