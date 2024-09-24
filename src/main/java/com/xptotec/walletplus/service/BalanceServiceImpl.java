package com.xptotec.walletplus.service;

import com.xptotec.walletplus.exception.InsufficientBalanceException;
import com.xptotec.walletplus.model.Balance;
import com.xptotec.walletplus.repository.BalanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
public class BalanceServiceImpl implements BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;


    @Override
    @Transactional
    public Balance getBalance( String cpf) {
        Balance balance = balanceRepository.findByCpf(cpf);
        if (ObjectUtils.isNotEmpty(balance)) {
            return balance;
        }
        return balanceRepository.saveAndFlush(new Balance().builder()
                .cpf(cpf)
                .amount(BigDecimal.ZERO)
                .build());
    }

    @Override
    @Transactional
    public Balance updateBalance(BigDecimal amount, String cpf) {
        Balance balance = getBalance(cpf);
        if (balance.getAmount().add(amount).compareTo(BigDecimal.ZERO) < 0) {

            throw new InsufficientBalanceException("Saldo insuficiente");
        }
        balance.setAmount(balance.getAmount().add(amount));
        return balanceRepository.saveAndFlush(balance);
    }
}