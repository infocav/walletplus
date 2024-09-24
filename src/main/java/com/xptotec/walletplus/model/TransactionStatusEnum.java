package com.xptotec.walletplus.model;

import com.xptotec.walletplus.exception.BusinessException;

public enum TransactionStatusEnum {

    PENDING,
    COMPLETED,
    FAILED,
    CANCELLED,
    REFUNDED;


    @Override
    public String toString() {
        return this.name();
    }

    public static TransactionStatusEnum fromString(String type) {
        for (TransactionStatusEnum transactionType : TransactionStatusEnum.values()) {
            if (transactionType.name().equalsIgnoreCase(type)) {
                return transactionType;
            }
        }
        throw new BusinessException("Status de transação inválido: " + type);
    }


}