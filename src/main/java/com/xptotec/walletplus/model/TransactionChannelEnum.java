package com.xptotec.walletplus.model;

import com.xptotec.walletplus.exception.BusinessException;

public enum TransactionChannelEnum {
    ATM,
    INTERNET_BANKING,
    MOBILE_BANKING,
    BRANCH,
    TELEPHONE_BANKING,
    POS,
    MAIL_BANKING,
    ONLINE_WEBSITE,
    ONLINE_MOBILE_APP,
    WALLET;

    @Override
    public String toString() {
        return this.name();
    }

    public static TransactionChannelEnum fromString(String channel) {
        for (TransactionChannelEnum transactionChannel : TransactionChannelEnum.values()) {
            if (transactionChannel.name().equalsIgnoreCase(channel)) {
                return transactionChannel;
            }
        }
        throw new BusinessException("Canal de transação inválido: " + channel);
    }
}