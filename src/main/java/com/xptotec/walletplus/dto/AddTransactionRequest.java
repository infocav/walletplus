package com.xptotec.walletplus.dto;

import com.xptotec.walletplus.model.TransactionChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class AddTransactionRequest extends TransactionRequest {

    @Schema(description = "Valor da transação", example = "100.00")
    private BigDecimal amount;

    @Schema(description = "Data e hora da transação", example = "2023-10-01T12:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "Canal da transação", example = "ATM", allowableValues = {"ATM",
            "INTERNET_BANKING",
            "MOBILE_BANKING",
            "BRANCH",
            "TELEPHONE_BANKING",
            "POS",
            "MAIL_BANKING",
            "ONLINE_WEBSITE",
            "ONLINE_MOBILE_APP",
            "WALLET"})
    private TransactionChannelEnum channel;

    @Schema(description = "ID do usuário válido na carteira que realizou a transação", example = "12345678901")
    private String cpf;

}