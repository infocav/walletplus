package com.xptotec.walletplus.exception.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseErrorValidacoes {
	private String campo;

	private String erro;

	private String valor;
}
