package com.boots.controller;

import com.boots.entity.User;
import com.boots.model.BalanceOnDate;
import com.boots.model.BalanceWithTransactions;
import com.boots.model.UserFullInfo;
import com.boots.service.BalanceService;
import com.boots.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;


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

    @ResponseStatus(HttpStatus.OK)
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
        ArrayList<BalanceOnDate> userBalanceOnDates = userService.getBalanceByDates(user.getId());
        userFullInfo.setBalanceOnDates(userBalanceOnDates);
        return userFullInfo;
    }
}
