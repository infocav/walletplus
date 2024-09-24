package com.xptotec.walletplus.strategy;

import com.xptotec.walletplus.exception.InvalidTransactionTypeException;
import com.xptotec.walletplus.model.TransactionTypeEnum;
import com.xptotec.walletplus.repository.TransactionRepository;
import com.xptotec.walletplus.service.BalanceService;
import com.xptotec.walletplus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionStrategyFactoryImpl implements TransactionStrategyFactory {

    private final BalanceService balanceService;

    private final TransactionRepository transactionRepository;

    private final UserService userService;

    @Override
    public TransactionStrategy getStrategy(TransactionTypeEnum type ) {
        switch (type) {
            case ADD:
                return new AddTransactionStrategy(transactionRepository, balanceService, userService);
            case WITHDRAW:
                return new WithdrawTransactionStrategy(transactionRepository, balanceService, userService);
            case PURCHASE:
            case CANCEL:
                return new PurchaseTransactionStrategy(transactionRepository,balanceService, userService);
            case REFUND:
                return new RefundTransactionStrategy(transactionRepository,balanceService, userService);

            default:
                throw new InvalidTransactionTypeException("Tipo de transação inválida");
        }
    }
}