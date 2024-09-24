package com.xptotec.walletplus.strategy;

import com.xptotec.walletplus.model.Transaction;
import org.springframework.stereotype.Service;

@Service
public interface TransactionStrategy {
    Transaction execute(Transaction transaction);
}
