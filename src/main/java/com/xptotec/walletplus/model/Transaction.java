package com.xptotec.walletplus.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "transactions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único da transação", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Tipo da transação", example = "WITHDRAW", allowableValues = {"ADD", "WITHDRAW", "PURCHASE", "CANCEL", "REFUND", "PENDING", "COMPLETED", "FAILED", "CANCELLED"})
    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum type;

    @Schema(description = "Quantia envolvida na transação", example = "100.00")
    private BigDecimal amount;

    @Schema(description = "Data e hora em que a transação ocorreu", example = "2023-10-01T12:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "Canal através do qual a transação foi realizada", example = "ATM", allowableValues = {"ATM", "INTERNET_BANKING", "MOBILE_BANKING", "BRANCH", "TELEPHONE_BANKING", "POS", "MAIL_BANKING", "ONLINE_WEBSITE", "ONLINE_MOBILE_APP"})
    @Enumerated(EnumType.STRING)
    private TransactionChannelEnum channel;


    @Schema(description = "ID do usuário que realizou a transação", example = "123e4567-e89b-12d3-a456-426614174002")
    private String cpf;

    @Enumerated(EnumType.STRING)
    private TransactionStatusEnum status;

}