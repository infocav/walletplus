package com.xptotec.walletplus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalancesDTO {

    private UUID userId;
    private UUID sourceUserId;
    private BigDecimal amount;
    private UUID referenceId;

}
