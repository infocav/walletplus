package com.xptotec.walletplus.model;

import com.xptotec.walletplus.exception.BusinessException;

public enum TransactionTypeEnum {
    ADD,
    WITHDRAW,
    PURCHASE,
    CANCEL,
    REFUND;

    @Override
    public String toString() {
        return this.name();
    }

    public static TransactionTypeEnum fromString(String type) {
        for (TransactionTypeEnum transactionType : TransactionTypeEnum.values()) {
            if (transactionType.name().equalsIgnoreCase(type)) {
                return transactionType;
            }
        }
        throw new BusinessException("Tipo de transação inválido: " + type);
    }


}