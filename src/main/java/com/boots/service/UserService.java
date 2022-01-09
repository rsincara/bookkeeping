package com.boots.service;

import com.boots.entity.Balance;
import com.boots.entity.ETransactionTypes;
import com.boots.entity.TransactionType;
import com.boots.entity.User;
import com.boots.model.BalanceOnDate;
import com.boots.model.BalanceWithTransactions;
import com.boots.repository.BalanceRepository;
import com.boots.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BalanceService balanceService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public ArrayList<BalanceOnDate> getBalanceByDates(Long userId) {
        ArrayList<BalanceOnDate> result = new ArrayList<>();
        ArrayList<BalanceWithTransactions> allUserBalancesWithTransaction =
                balanceService.getBalancesWithTransactionsByUserId(userId);

        for (BalanceWithTransactions balanceWithTransactions : allUserBalancesWithTransaction) {
            for (TransactionType transaction : balanceWithTransactions.transactions) {
                Optional<BalanceOnDate> optionalFoundBalanceOnDate = result
                                .stream()
                                .filter(x -> x.getDate()
                                .equals(transaction.getDate()))
                                .findFirst();
                BalanceOnDate addingBalanceOnDate;
                if (optionalFoundBalanceOnDate.isPresent()) {
                    addingBalanceOnDate = optionalFoundBalanceOnDate.get();
                    addingBalanceOnDate.setAmount(addingBalanceOnDate.getAmount() +
                            (transaction.getTransactionType() == ETransactionTypes.income ?
                                    transaction.getAmount() : -transaction.getAmount()));
                } else {
                    addingBalanceOnDate = new BalanceOnDate();
                    addingBalanceOnDate.setDate(transaction.getDate());
                    addingBalanceOnDate.setAmount(transaction.getTransactionType() == ETransactionTypes.income ?
                            transaction.getAmount() : -transaction.getAmount());
                    result.add(addingBalanceOnDate);
                }
            }
        }

        return result;
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public List<User> usergtList(Long idMin) {
        return em.createQuery("SELECT u FROM User u WHERE u.id > :paramId", User.class)
                .setParameter("paramId", idMin).getResultList();
    }
}
