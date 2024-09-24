package com.xptotec.walletplus.strategy;

import com.xptotec.walletplus.exception.BusinessException;
import com.xptotec.walletplus.exception.TransactionAlreadyExistsException;
import com.xptotec.walletplus.exception.UserNotFoundException;
import com.xptotec.walletplus.model.Transaction;
import com.xptotec.walletplus.model.User;
import com.xptotec.walletplus.repository.TransactionRepository;
import com.xptotec.walletplus.service.UserService;
import com.xptotec.walletplus.utils.CpfUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public abstract class AbstractTransactionStrategy implements TransactionStrategy {

    protected final TransactionRepository transactionRepository;
    protected final UserService userService;


    protected void validate(Transaction transaction) {

        validadeUserByCpf(transaction);

        validateAmount(transaction);

        validateExistingTransaction(transaction);
    }

    private void validadeUserByCpf(Transaction transaction) {
        if (!CpfUtils.isValidCpf(transaction.getCpf())) {
            throw new BusinessException("CPF inválido");
        }
        String cpf = CpfUtils.cleanCpf(transaction.getCpf());
        User user = userService.getUserByCpf(cpf);
        if (user == null) {
            log.error("Usuário não encontrado");
            throw new UserNotFoundException("Usuário não encontrado");
        }
    }

    private static void validateAmount(Transaction transaction) {
        if ( transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Valor da transação deverá ser maior que zero");
            throw new BusinessException("Valor da transação deverá ser maior que zero");
        }
    }

    protected void validateExistingTransaction(Transaction transaction) {
        LocalDateTime startTimestamp = transaction.getTimestamp().minusMinutes(1);
        LocalDateTime endTimestamp = transaction.getTimestamp().plusMinutes(1);

        Optional<List<Transaction>> existingTransaction = transactionRepository.findByCpfAndAmountAndTypeAndTimestampBetween(
                transaction.getCpf(), transaction.getAmount(), transaction.getType(), startTimestamp, endTimestamp);

        if (existingTransaction.isPresent() && existingTransaction.get().size() > 0) {
            throw new TransactionAlreadyExistsException("Transação já existente");
        }
    }
}