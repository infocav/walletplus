package com.xptotec.walletplus.service;

import com.xptotec.walletplus.dto.AddTransactionRequest;
import com.xptotec.walletplus.dto.CancelTransactionRequest;
import com.xptotec.walletplus.dto.PurchaseTransactionRequest;
import com.xptotec.walletplus.dto.RefundTransactionRequest;
import com.xptotec.walletplus.dto.TransactionRequest;
import com.xptotec.walletplus.exception.BusinessException;
import com.xptotec.walletplus.model.Transaction;
import com.xptotec.walletplus.model.TransactionStatusEnum;
import com.xptotec.walletplus.model.TransactionTypeEnum;
import com.xptotec.walletplus.repository.TransactionRepository;
import com.xptotec.walletplus.strategy.TransactionStrategy;
import com.xptotec.walletplus.strategy.TransactionStrategyFactory;
import com.xptotec.walletplus.utils.CpfUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final TransactionStrategyFactory transactionStrategyFactory;

    private final UserService userService;

    @Override
    @Transactional
    public CompletableFuture<Transaction> addTransaction(AddTransactionRequest request) {

        Transaction transaction = buildTransaction(request, TransactionTypeEnum.ADD, TransactionStatusEnum.COMPLETED);

        return CompletableFuture.completedFuture(processTransaction(transaction));
    }

    @Override
    @Transactional
    public CompletableFuture<Transaction> withdrawTransaction(TransactionRequest request) {
        Transaction transaction = buildTransaction(request, TransactionTypeEnum.WITHDRAW, TransactionStatusEnum.COMPLETED);

        return CompletableFuture.completedFuture(processTransaction(transaction));
    }

    @Override
    @Transactional
    public CompletableFuture<Transaction> purchaseTransaction(UUID id, TransactionStatusEnum status) {

        Transaction transaction = new Transaction().builder()
                .id(id)
                .type(TransactionTypeEnum.PURCHASE)
                .status(status)
                .build();

        return CompletableFuture.completedFuture(processTransaction(transaction));
    }

    @Override
    @Transactional
    public CompletableFuture<Transaction> purchaseTransaction(PurchaseTransactionRequest request) {

        Transaction transaction = buildTransaction(request, TransactionTypeEnum.PURCHASE, ObjectUtils.isEmpty(request.getStatus()) ? TransactionStatusEnum.PENDING : request.getStatus());

        return CompletableFuture.completedFuture(processTransaction(transaction));
    }

    @Override
    @Transactional
    public CompletableFuture<Transaction> cancelTransaction(CancelTransactionRequest request) {


        Transaction transaction = new Transaction().builder()
                .id(request.getTransactionId())
                .type(TransactionTypeEnum.CANCEL)
                .status(TransactionStatusEnum.CANCELLED)
                .build();

        return CompletableFuture.completedFuture(processTransaction(transaction));
    }

    @Override
    @Transactional
    public CompletableFuture<Transaction> refundTransaction(RefundTransactionRequest request) {

        Transaction transaction = new Transaction().builder()
                .id(request.getTransactionId())
                .type(TransactionTypeEnum.REFUND)
                .status(TransactionStatusEnum.REFUNDED)
                .build();

        return CompletableFuture.completedFuture(processTransaction(transaction));
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> getTransactionsByCpf(String cpf) {
        return transactionRepository.findByCpf(cpf);
    }

    private Transaction buildTransaction(TransactionRequest request, TransactionTypeEnum transactionType, TransactionStatusEnum transactionStatus) {
        if ( !CpfUtils.isValidCpf(request.getCpf()) ){
            throw new BusinessException("CPF inválido");
        }

        return new Transaction().builder()
                .type(transactionType)
                .amount(request.getAmount())
                .timestamp(ObjectUtils.isEmpty(request.getTimestamp()) ? LocalDateTime.now():request.getTimestamp())
                .channel(request.getChannel())
                .cpf(CpfUtils.cleanCpf(request.getCpf()))
                .status(transactionStatus)
                .build();
    }


    private Transaction processTransaction(Transaction transaction) {

        TransactionStrategy transactionStrategy = transactionStrategyFactory.getStrategy(transaction.getType());

        return transactionStrategy.execute(transaction);
    }


}