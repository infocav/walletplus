package com.xptotec.walletplus.service;

import com.xptotec.walletplus.model.Balance;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface BalanceService {


//    Balance execute(BalancesDTO balancesDTO);

    Balance getBalance( String cpf);

    Balance updateBalance(BigDecimal amount, String cpf);
}
