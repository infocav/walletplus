package com.xptotec.walletplus.exception.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseError {
	private String erro;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<ResponseErrorValidacoes> validacoes;

}
