package com.xptotec.walletplus.strategy;

import com.xptotec.walletplus.model.TransactionTypeEnum;

public interface TransactionStrategyFactory {
    TransactionStrategy getStrategy(TransactionTypeEnum type);
}