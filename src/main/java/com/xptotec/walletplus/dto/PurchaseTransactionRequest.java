package com.xptotec.walletplus.dto;

import com.xptotec.walletplus.model.TransactionChannelEnum;
import com.xptotec.walletplus.model.TransactionStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PurchaseTransactionRequest extends TransactionRequest {

    @Schema(description = "Valor da transação", example = "100.00")
    private BigDecimal amount;

    @Schema(description = "Data e hora da transação", example = "2023-10-01T12:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "Canal da transação", example = "ATM", allowableValues = {
            "ATM",
            "INTERNET_BANKING",
            "MOBILE_BANKING",
            "BRANCH",
            "TELEPHONE_BANKING",
            "POS",
            "MAIL_BANKING",
            "ONLINE_WEBSITE",
            "ONLINE_MOBILE_APP",
            "WALLET"
    })
    private TransactionChannelEnum channel;


    @Schema(description = "ID do usuário que realizou a transação", example = "12345678901")
    private String cpf;

    private TransactionStatusEnum status;

}