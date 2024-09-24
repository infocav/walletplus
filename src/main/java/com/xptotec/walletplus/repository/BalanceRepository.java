package com.xptotec.walletplus.repository;

import com.xptotec.walletplus.model.Balance;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, String> {

    Balance findByCpf(String cpf);
}
