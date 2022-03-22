package com.boots.repository;

import com.boots.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Optional<Balance> findById(Long id);
    ArrayList<Balance> findAllByUserId(Long id);
    ArrayList<Balance> findAll();
    void deleteById(Long id);
}
