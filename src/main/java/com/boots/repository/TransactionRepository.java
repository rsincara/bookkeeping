package com.boots.repository;

import com.boots.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionType, Long> {
    Optional<TransactionType> findById(Long id);
    ArrayList<TransactionType> findAllByBalanceId(Long id);
    ArrayList<TransactionType> findAll();
    void removeByBalanceId(Long id);
    void deleteById(Long id);
}