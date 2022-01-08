package com.boots.controller;

import com.boots.entity.Balance;
import com.boots.entity.User;
import com.boots.model.BalanceWithTransactions;
import com.boots.model.UserFullInfo;
import com.boots.service.BalanceService;
import com.boots.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {
    @Autowired
    private UserService userService;
    @Autowired
    private BalanceService balanceService;

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

    @GetMapping("/remove-balance")
    public String removeBalance(@RequestParam String userName, @RequestParam String balanceName) {
        User user = userService.loadUserByUsername(userName);
        Balance removingBalance = balanceService.getBalanceByUserIdAndBalanceName(user.getId(), balanceName);

        if (removingBalance != null) {
            balanceService.removeBalance(removingBalance);
        }

        return "redirect:/";
    }

}
