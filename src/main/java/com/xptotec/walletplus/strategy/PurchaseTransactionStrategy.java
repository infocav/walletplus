package com.xptotec.walletplus.strategy;

import com.xptotec.walletplus.exception.BusinessException;
import com.xptotec.walletplus.exception.UserNotFoundException;
import com.xptotec.walletplus.model.Transaction;
import com.xptotec.walletplus.model.TransactionStatusEnum;
import com.xptotec.walletplus.repository.TransactionRepository;
import com.xptotec.walletplus.service.BalanceService;
import com.xptotec.walletplus.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class PurchaseTransactionStrategy extends AbstractTransactionStrategy {

    private final BalanceService balanceService;
    private final UserService userService;

    public PurchaseTransactionStrategy(TransactionRepository transactionRepository, BalanceService balanceService , UserService userService) {
        super(transactionRepository, userService);
        this.userService = userService;
        this.balanceService = balanceService;
    }

    @Override
    public Transaction execute(Transaction transaction) {


        if (transaction.getStatus().equals(TransactionStatusEnum.PENDING)) {
            validate(transaction);
            return transactionRepository.save(transaction);
        }

        if (transaction.getStatus().equals(TransactionStatusEnum.COMPLETED)) {

            return executeCompleteTransaction(transaction);
        }

        if (transaction.getStatus().equals(TransactionStatusEnum.CANCELLED)) {
            return executeCancelTransaction(transaction);
        }

        return null;
    }

    private Transaction executeCancelTransaction(Transaction transaction) {
        Optional<Transaction> transactionSaved = transactionRepository.findById(transaction.getId());

        if (transactionSaved.isPresent()) {
            Transaction transactionToUpdate = transactionSaved.get();
            if (!transactionToUpdate.getStatus().equals(TransactionStatusEnum.PENDING)) {
                throw new BusinessException("Transação deve está como pendente para poder ser cancelada, status atual: "+ transactionToUpdate.getStatus());
            }


            balanceService.updateBalance(transactionToUpdate.getAmount(), transactionToUpdate.getCpf());

            transactionToUpdate.setStatus(TransactionStatusEnum.CANCELLED);
            return transactionRepository.save(transactionToUpdate);


        }

        return null;
    }

    private Transaction executeCompleteTransaction(Transaction transaction) {
        Optional<Transaction> transactionSaved = transactionRepository.findById(transaction.getId());

        if (transactionSaved.isEmpty()) {
            throw new UserNotFoundException("Transação não encontrada");
        }

        Transaction transactionToUpdate = transactionSaved.get();

        if (!transactionToUpdate.getStatus().equals(TransactionStatusEnum.PENDING)) {
            throw new BusinessException("Transação não pode ser completada pois não está pendente, status atual: " + transactionToUpdate.getStatus());
        }

        balanceService.updateBalance(transactionToUpdate.getAmount().negate(), transactionToUpdate.getCpf());

        transactionToUpdate.setStatus(TransactionStatusEnum.COMPLETED);
        return transactionRepository.save(transactionToUpdate);


    }

}