package com.xptotec.walletplus.strategy;

import com.xptotec.walletplus.exception.BusinessException;
import com.xptotec.walletplus.exception.UserNotFoundException;
import com.xptotec.walletplus.model.Transaction;
import com.xptotec.walletplus.model.TransactionStatusEnum;
import com.xptotec.walletplus.model.TransactionTypeEnum;
import com.xptotec.walletplus.repository.TransactionRepository;
import com.xptotec.walletplus.service.BalanceService;
import com.xptotec.walletplus.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

@Slf4j
public class RefundTransactionStrategy extends AbstractTransactionStrategy {
    private final BalanceService balanceService;
    private final UserService userService;


    public RefundTransactionStrategy(TransactionRepository transactionRepository, BalanceService balanceService, UserService userService) {

        super(transactionRepository, userService);
        this.balanceService = balanceService;
        this.userService = userService;
    }

    protected Transaction validateTransaction(UUID id) {
        Optional<Transaction> transactionSaved = transactionRepository.findById(id);

        if (!transactionSaved.isPresent()) {
            throw new UserNotFoundException("Transação não encontrada");
        }

        if (!transactionSaved.get().getStatus().equals(TransactionStatusEnum.COMPLETED)) {
            throw new BusinessException("Transação não pode ser estornada pois não foi completada, status atual: " + transactionSaved.get().getStatus());
        }

        if (transactionSaved.get().getType().equals(TransactionTypeEnum.REFUND)) {
            throw new BusinessException("Transação não pode ser estornada pois já é um estorno");
        }

        if (transactionSaved.get().getType().equals(TransactionTypeEnum.CANCEL)) {
            throw new BusinessException("Transação não pode ser estornada pois já foi cancelada");
        }

        return transactionSaved.get();

    }

    @Override
    public Transaction execute(Transaction transaction) {

        Transaction transactionToRefund = validateTransaction(transaction.getId());

        balanceService.updateBalance(transactionToRefund.getAmount(), transactionToRefund.getCpf());

        transactionToRefund.setStatus(TransactionStatusEnum.REFUNDED);

        return transactionRepository.save(transactionToRefund);

    }

}
