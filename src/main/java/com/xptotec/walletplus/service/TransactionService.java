package com.xptotec.walletplus.service;

import com.xptotec.walletplus.dto.AddTransactionRequest;
import com.xptotec.walletplus.dto.CancelTransactionRequest;
import com.xptotec.walletplus.dto.PurchaseTransactionRequest;
import com.xptotec.walletplus.dto.RefundTransactionRequest;
import com.xptotec.walletplus.dto.TransactionRequest;
import com.xptotec.walletplus.model.Transaction;
import com.xptotec.walletplus.model.TransactionStatusEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public interface TransactionService {
    CompletableFuture<Transaction> addTransaction(AddTransactionRequest request);
    CompletableFuture<Transaction> withdrawTransaction(TransactionRequest request);
    CompletableFuture<Transaction> purchaseTransaction(UUID id, TransactionStatusEnum status);
    CompletableFuture<Transaction> purchaseTransaction(PurchaseTransactionRequest request);
    CompletableFuture<Transaction> cancelTransaction(CancelTransactionRequest request);
    CompletableFuture<Transaction> refundTransaction(RefundTransactionRequest request);
    List<Transaction> getTransactionsByCpf(String cpf);

    List<Transaction> getAllTransactions();
}