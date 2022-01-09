package com.boots.service;

import com.boots.entity.ETransactionTypes;
import com.boots.entity.TransactionType;
import com.boots.entity.User;
import com.boots.model.BalanceOnDate;
import com.boots.model.BalanceWithTransactions;
import com.boots.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Service
public class UserService implements UserDetailsService {

    class DateComparator implements Comparator<BalanceOnDate> {
        @Override
        public int compare(BalanceOnDate a, BalanceOnDate b) {
            Date dateA = a.getDate();
            Date dateB = b.getDate();
            java.util.Date newDateA = new java.util.Date(dateA.getYear(), dateA.getMonth(), dateA.getDate());
            java.util.Date newDateB = new java.util.Date(dateB.getYear(), dateB.getMonth(), dateB.getDate());
            return newDateA.compareTo(newDateB);
        }
    }

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
        ArrayList<BalanceOnDate> transactionsAmountOnDate = new ArrayList<>();
        ArrayList<BalanceWithTransactions> allUserBalancesWithTransaction =
                balanceService.getBalancesWithTransactionsByUserId(userId);

        for (BalanceWithTransactions balanceWithTransactions : allUserBalancesWithTransaction) {
            for (TransactionType transaction : balanceWithTransactions.transactions) {
                Optional<BalanceOnDate> optionalFoundBalanceOnDate = transactionsAmountOnDate
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
                    transactionsAmountOnDate.add(addingBalanceOnDate);
                }
            }
        }

        Stream<BalanceOnDate> sortedTransactionsAmountOnDatesStream = transactionsAmountOnDate.stream().sorted(new DateComparator());
        ArrayList<BalanceOnDate> resultBalanceOnDate = new ArrayList<>();
        var ref = new Object() {
            Double prevAmount = 0d;
        };
        sortedTransactionsAmountOnDatesStream.forEach(balanceOnDateSorted -> {
            BalanceOnDate addingBalanceOnDate = new BalanceOnDate();
            addingBalanceOnDate.setDate(balanceOnDateSorted.getDate());
            addingBalanceOnDate.setAmount(ref.prevAmount + balanceOnDateSorted.getAmount());
            resultBalanceOnDate.add(addingBalanceOnDate);
            ref.prevAmount = addingBalanceOnDate.getAmount();
        });

        return resultBalanceOnDate;
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
