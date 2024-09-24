package com.xptotec.walletplus.dto;

import com.xptotec.walletplus.model.TransactionChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CancelTransactionRequest extends TransactionRequest {

   private UUID transactionId;
}