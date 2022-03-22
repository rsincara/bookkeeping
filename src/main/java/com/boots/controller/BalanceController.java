package com.boots.controller;

import com.boots.entity.Balance;
import com.boots.entity.User;
import com.boots.exceptions.BalanceAlreadyExists;
import com.boots.exceptions.BalanceNotFoundException;
import com.boots.model.NewBalanceForm;
import com.boots.service.BalanceService;
import com.boots.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.logging.Logger;

@RestController
@RequestMapping("balance")
public class BalanceController {

    private static final Logger log = Logger.getLogger(BalanceController.class.getName());

    @Autowired
    private UserService userService;
    @Autowired
    private BalanceService balanceService;

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("{id}")
    public Balance getBalance(
            @PathVariable Long id) throws BalanceNotFoundException {
        return balanceService.getBalanceById(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping()
    public ArrayList<Balance> getAllBalances() {
        return balanceService.getAllBalances();
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping()
    public void addBalance(
            @RequestBody NewBalanceForm balanceForm) throws BalanceAlreadyExists {
        log.info(String.format("params: userName:%s, balanceName:%s, balanceAmount:%s", balanceForm.getUserName(),
                balanceForm.getBalanceName(), balanceForm.getBalanceAmount()));
        User user = userService.loadUserByUsername(balanceForm.getUserName());
        Balance newBalance = new Balance();
        newBalance.setUserId(user.getId());
        newBalance.setName(balanceForm.getBalanceName());
        newBalance.setAmount(balanceForm.getBalanceAmount());
        balanceService.saveBalance(newBalance);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void removeBalance(
            @PathVariable Long id) throws BalanceNotFoundException {
        log.info(String.format("params: balanceId:%s", id));
        Balance removingBalance = balanceService.getBalanceById(id);

            balanceService.removeBalance(removingBalance);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PutMapping()
    public void updateBalance(
            @RequestParam String userName,
            @RequestParam Long balanceId,
            @RequestParam String newBalanceName) throws UsernameNotFoundException, BalanceNotFoundException {
        log.info(String.format("params: userName:%s, balanceId:%s,newBalanceName:%s", userName, balanceId, newBalanceName));
        Balance balance = balanceService.getBalanceById(balanceId);
        balance.setName(newBalanceName);
        balanceService.updateBalance(balance);
    }
}
