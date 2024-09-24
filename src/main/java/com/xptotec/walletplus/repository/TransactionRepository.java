package com.xptotec.walletplus.repository;

import com.xptotec.walletplus.model.Transaction;
import com.xptotec.walletplus.model.TransactionTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findById(UUID id);

    List<Transaction> findByCpf(String cpf);

    Optional<List<Transaction>>  findByCpfAndAmountAndTimestampAndType(String cpf, BigDecimal amount, LocalDateTime timestamp, TransactionTypeEnum type);

    Optional<List<Transaction>> findByCpfAndAmountAndTypeAndTimestampBetween(
            String cpf, BigDecimal amount, TransactionTypeEnum type, LocalDateTime startTimestamp, LocalDateTime endTimestamp);
}