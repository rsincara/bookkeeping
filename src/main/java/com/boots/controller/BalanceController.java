package com.boots.controller;

import com.boots.entity.Balance;
import com.boots.entity.User;
import com.boots.service.BalanceService;
import com.boots.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class BalanceController {
    @Autowired
    private UserService userService;
    @Autowired
    private BalanceService balanceService;

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
