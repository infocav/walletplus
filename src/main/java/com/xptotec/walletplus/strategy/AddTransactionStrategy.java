package com.xptotec.walletplus.strategy;

import com.xptotec.walletplus.model.Transaction;
import com.xptotec.walletplus.repository.TransactionRepository;
import com.xptotec.walletplus.service.BalanceService;
import com.xptotec.walletplus.service.UserService;
import com.xptotec.walletplus.utils.CpfUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddTransactionStrategy extends AbstractTransactionStrategy {

    private final BalanceService balanceService;
    private final UserService userService;



    public AddTransactionStrategy(TransactionRepository transactionRepository, BalanceService balanceService, UserService userService) {
        super(transactionRepository, userService);
        this.balanceService = balanceService;
        this.userService = userService;
    }


    @Override
    public Transaction execute(Transaction transaction) {
        transaction.setCpf(CpfUtils.cleanCpf(transaction.getCpf()) );

        validate(transaction);

        Transaction transactionSaved = transactionRepository.save(transaction);

        balanceService.updateBalance(transaction.getAmount(), transaction.getCpf());

        return transactionSaved;
    }


}