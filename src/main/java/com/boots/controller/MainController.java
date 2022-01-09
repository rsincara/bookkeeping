package com.boots.controller;

import com.boots.entity.Balance;
import com.boots.entity.ETransactionTypes;
import com.boots.entity.TransactionType;
import com.boots.entity.User;
import com.boots.helpers.TransactionHelper;
import com.boots.model.BalanceWithTransactions;
import com.boots.model.UserFullInfo;
import com.boots.service.BalanceService;
import com.boots.service.TransactionService;
import com.boots.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Optional;


@Controller
public class MainController {
    @Autowired
    private UserService userService;
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/get-user-info")
    @ResponseBody
    public UserFullInfo getUserDetails(@RequestParam String userName) {
        UserFullInfo userFullInfo = new UserFullInfo();
        userFullInfo.setUserName(userName);
        User user = userService.loadUserByUsername(userName);
        userFullInfo.setBalancesWithTransactions(balanceService.getBalancesWithTransactionsByUserId(user.getId()));
        for (BalanceWithTransactions balanceWithTransactions : userFullInfo.balancesWithTransactions) {
            userFullInfo.setGeneralBalance(userFullInfo.getGeneralBalance() + balanceWithTransactions.amount);
        }

        return userFullInfo;
    }

    @PostMapping("/add-balance")
    public String addNewBalance(@RequestParam String userName, @RequestParam String balanceName, @RequestParam Double balanceAmount) {
        User user = userService.loadUserByUsername(userName);
        Balance newBalance = new Balance();
        newBalance.setUserId(user.getId());
        newBalance.setName(balanceName);
        newBalance.setAmount(balanceAmount);
        balanceService.saveBalance(newBalance);

        return "redirect:/";
    }

    @PostMapping("/add-transaction")
    public String addNewBalance(
            @RequestParam String balanceName,
            @RequestParam String userName,
            @RequestParam Double amount,
            @RequestParam Date date,
            @RequestParam String transactionType,
            @RequestParam String commentary) {
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

    @GetMapping("/remove-balance")
    public String removeBalance(@RequestParam String userName, @RequestParam String balanceName) {
        User user = userService.loadUserByUsername(userName);
        Balance removingBalance = balanceService.getBalanceByUserIdAndBalanceName(user.getId(), balanceName);

        if (removingBalance != null) {
            balanceService.removeBalance(removingBalance);
        }

        return "redirect:/";
    }

    @GetMapping("/remove-transaction")
    public String removeBalance(@RequestParam Long id) {
        Optional<TransactionType> transactionType = transactionService.getTransactionById(id);
        if(transactionType.isPresent()) {
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
        Optional<TransactionType> optionalTransactionType = transactionService.getTransactionById(transactionId);
        if(optionalTransactionType.isPresent()) {
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

    @PostMapping("/update-balance")
    public String addNewBalance(
            @RequestParam String userName,
            @RequestParam String oldBalanceName,
            @RequestParam String newBalanceName) {
        User user = userService.loadUserByUsername(userName);
        Balance balance = balanceService.getBalanceByUserIdAndBalanceName(user.getId(), oldBalanceName);
        balance.setName(newBalanceName);
        balanceService.updateBalance(balance);
        return "redirect:/";
    }

}
