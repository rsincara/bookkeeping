package com.boots.controller;

import com.boots.entity.Balance;
import com.boots.entity.User;
import com.boots.service.BalanceService;
import com.boots.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.logging.Logger;

@Controller
public class BalanceController {

    private static final Logger log = Logger.getLogger(BalanceController.class.getName());

    @Autowired
    private UserService userService;
    @Autowired
    private BalanceService balanceService;

    @PostMapping("/add-balance")
    public String addNewBalance(@RequestParam String userName, @RequestParam String balanceName, @RequestParam Double balanceAmount) {
        log.info(String.format("params: userName:%s, balanceName:%s, balanceAmount:%s", userName, balanceName, balanceAmount));
        User user = userService.loadUserByUsername(userName);
        Balance newBalance = new Balance();
        newBalance.setUserId(user.getId());
        newBalance.setName(balanceName);
        newBalance.setAmount(balanceAmount);
        balanceService.saveBalance(newBalance);
        return "redirect:/";
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @GetMapping("/remove-balance")
    public void removeBalance(@RequestParam String userName, @RequestParam String balanceName) {
        log.info(String.format("params: userName:%s, balanceName:%s", userName, balanceName));
        User user = userService.loadUserByUsername(userName);
        Balance removingBalance = balanceService.getBalanceByUserIdAndBalanceName(user.getId(), balanceName);

        if (removingBalance != null) {
            balanceService.removeBalance(removingBalance);
        }
    }

    @PostMapping("/update-balance")
    public String addNewBalance(
            @RequestParam String userName,
            @RequestParam String oldBalanceName,
            @RequestParam String newBalanceName) {
        log.info(String.format("params: userName:%s, oldBalanceName:%s,newBalanceName:%s", userName, oldBalanceName, newBalanceName));
        User user = userService.loadUserByUsername(userName);
        Balance balance = balanceService.getBalanceByUserIdAndBalanceName(user.getId(), oldBalanceName);
        balance.setName(newBalanceName);
        balanceService.updateBalance(balance);
        return "redirect:/";
    }
}
