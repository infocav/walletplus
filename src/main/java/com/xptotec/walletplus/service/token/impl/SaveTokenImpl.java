package com.xptotec.walletplus.service.token.impl;


import com.xptotec.walletplus.model.token.Authorization;
import com.xptotec.walletplus.model.token.Token;
import com.xptotec.walletplus.service.token.SaveToken;
import jakarta.annotation.Resource;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Setter
@Service
public class SaveTokenImpl implements SaveToken {

    @Resource(name = "requestScopeAuthorization")
    Authorization authorization;

    @Override
    public void execute(final Token token) {
        try {
            this.saveIntoSession(Objects.requireNonNull(token));
        } catch (final Exception e) {
            log.error("[TokenSession] Erro ao deserializar o Token");

        }
    }

    private void saveIntoSession(final Token token) {
        authorization.setToken(token);
    }
}