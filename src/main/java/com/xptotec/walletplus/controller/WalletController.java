package com.xptotec.walletplus.controller;

import com.xptotec.walletplus.dto.AddTransactionRequest;
import com.xptotec.walletplus.dto.CancelTransactionRequest;
import com.xptotec.walletplus.dto.PurchaseTransactionRequest;
import com.xptotec.walletplus.dto.RefundTransactionRequest;
import com.xptotec.walletplus.dto.UserRequest;
import com.xptotec.walletplus.dto.WithdrawTransactionRequest;
import com.xptotec.walletplus.model.Balance;
import com.xptotec.walletplus.model.Transaction;
import com.xptotec.walletplus.model.TransactionStatusEnum;
import com.xptotec.walletplus.model.User;
import com.xptotec.walletplus.service.BalanceService;
import com.xptotec.walletplus.service.TransactionService;
import com.xptotec.walletplus.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final TransactionService transactionService;

    private final BalanceService balanceServiceImpl;

    private final UserService userService;

    @Operation(summary = "Adicionar uma nova transação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transação adicionada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping("/transaction/add")
    public CompletableFuture<Transaction> addTransaction(@RequestBody AddTransactionRequest request) {
        return transactionService.addTransaction(request);
    }

    @Operation(summary = "Sacar uma transação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transação sacada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping("/transaction/withdraw")
    public CompletableFuture<Transaction> withdrawTransaction(@RequestBody WithdrawTransactionRequest request) {
        return transactionService.withdrawTransaction(request);
    }

    @Operation(summary = "Comprar uma transação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transação comprada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping("/transaction/purchase")
    public CompletableFuture<Transaction> purchaseTransaction(@RequestBody PurchaseTransactionRequest request) {
        return transactionService.purchaseTransaction(request);
    }

    @PutMapping("/transaction/purchase/{id}")
    public CompletableFuture<Transaction> purchaseTransaction(@PathVariable("id") UUID id,
                                                              @RequestParam("status")TransactionStatusEnum status) {
        return transactionService.purchaseTransaction( id,  status);
    }

    @Operation(summary = "Cancelar uma transação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transação cancelada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping("/transaction/cancel")
    public CompletableFuture<Transaction> cancelTransaction(@RequestBody CancelTransactionRequest request) {
        return transactionService.cancelTransaction(request);
    }

    @Operation(summary = "Reembolsar uma transação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transação reembolsada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping("/transaction/refund")
    public CompletableFuture<Transaction> refundTransaction(@RequestBody RefundTransactionRequest request) {
        return transactionService.refundTransaction(request);
    }

    @Operation(summary = "Retornar saldo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saldo recuperado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/balance/{cpf}")
    public Balance getBalance(@PathVariable("cpf") String cpf) {
        return balanceServiceImpl.getBalance(cpf);
    }

    @Operation(summary = "Obter todas as transações")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transações recuperadas com sucesso")
    })
    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @Operation(summary = "Obter extrato de transações por usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Extrato de transações recuperado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/transactions/{cpf}")
    public List<Transaction> getTransactionsByUserId(@PathVariable String cpf) {
        return transactionService.getTransactionsByCpf(cpf);
    }

    @Operation(summary = "Criar um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping("/user")
    public User createUser(@RequestBody UserRequest user) {
        return userService.createUser(user);
    }

    @Operation(summary = "Obter usuário por cpf")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário recuperado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/user/{cpf}")
    public User getUserById(@PathVariable String cpf) {
        return userService.getUserByCpf(cpf);
    }

    @Operation(summary = "Obter todos os usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários recuperados com sucesso")
    })
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Atualizar usuário por cpf")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PutMapping("/user/{cpf}")
    public User updateUser(@PathVariable String cpf, @RequestBody UserRequest user) {
        return userService.updateUser(cpf, user);
    }

    @Operation(summary = "Deletar usuário por cpf")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/user/{cpf}")
    public void deleteUser(@PathVariable String cpf) {
        userService.deleteUser(cpf);
    }
}