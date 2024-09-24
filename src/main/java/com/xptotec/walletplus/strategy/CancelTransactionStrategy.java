package com.xptotec.walletplus.strategy;

import com.xptotec.walletplus.model.Transaction;
import com.xptotec.walletplus.model.TransactionTypeEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CancelTransactionStrategy implements TransactionStrategy {

    private final TransactionStrategyFactory transactionStrategyFactory;

    public CancelTransactionStrategy(TransactionStrategyFactory transactionStrategyFactory) {
        this.transactionStrategyFactory = transactionStrategyFactory;
    }

    @Override
    public Transaction execute(Transaction transaction) {

        TransactionStrategy putchaseStrategyd = transactionStrategyFactory.getStrategy(TransactionTypeEnum.PURCHASE);
        return putchaseStrategyd.execute(transaction);
    }
}