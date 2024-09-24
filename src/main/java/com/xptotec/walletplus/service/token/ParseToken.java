package com.xptotec.walletplus.service.token;


import com.xptotec.walletplus.model.token.Token;

public interface ParseToken {
    Token execute(String bearerToken);
}