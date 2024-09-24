package com.xptotec.walletplus.exception;


import com.xptotec.walletplus.exception.models.ResponseError;
import com.xptotec.walletplus.exception.models.ResponseErrorValidacoes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {


    private final static String ARGUMENTO_INVALIDO = "Dados de requisição incorretos";
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> methodArgumentNotValidException(final MethodArgumentNotValidException exception) {

        final List<ResponseErrorValidacoes> validacoes = new ArrayList<>();

        exception.getFieldErrors().forEach( erros -> {
            validacoes.add(ResponseErrorValidacoes.builder()
                    .campo(erros.getField())
                    .erro(erros.getDefaultMessage())
                    .valor(erros.getRejectedValue() != null ? erros.getRejectedValue().toString() : null)
                    .build());
                }

        );


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.builder()
                        .erro(ARGUMENTO_INVALIDO)
                        .validacoes(validacoes)
                .build());

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseError> illegalArgumentException(final IllegalArgumentException exception){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((ResponseError.builder()
                .erro(exception.getMessage())
                .build()));
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ResponseError> transactionNotFoundException(final TransactionNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body((ResponseError.builder()
                .erro(exception.getMessage())
                .build()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseError> userNotFoundException(final UserNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body((ResponseError.builder()
                .erro(exception.getMessage())
                .build()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseError> businessException(final BusinessException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((ResponseError.builder()
                .erro(exception.getMessage())
                .build()));
    }

    //TODO MUDAR O STATUS CODE
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ResponseError> insufficientBalanceException(final InsufficientBalanceException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body((ResponseError.builder()
                .erro(exception.getMessage())
                .build()));
    }

    @ExceptionHandler(TransactionAlreadyExistsException.class)
    public ResponseEntity<ResponseError> transactionAlreadyExistsException(final TransactionAlreadyExistsException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body((ResponseError.builder()
                .erro(exception.getMessage())
                .build()));
    }


    @ExceptionHandler(InvalidTransactionTypeException.class)
    public ResponseEntity<ResponseError> invalidTransactionTypeException(final InvalidTransactionTypeException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((ResponseError.builder()
                .erro(exception.getMessage())
                .build()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> exception(final Exception exception){


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((ResponseError.builder()
                .erro("Sistema indisponível")
                .build()));
    }


}