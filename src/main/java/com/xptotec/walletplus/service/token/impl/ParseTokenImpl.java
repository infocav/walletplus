package com.xptotec.walletplus.service.token.impl;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xptotec.walletplus.model.token.Token;
import com.xptotec.walletplus.service.token.ParseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Objects;



@Slf4j
@Service
@RequiredArgsConstructor
public class ParseTokenImpl implements ParseToken {

    private static final int BEARER_SPACE_SIZE = 7;


    @Override
    public Token execute(final String bearerToken) {
        log.debug("[ParseTokenImpl] [execute] Bearer Token Handling {}", bearerToken);

        try {
            if (Objects.nonNull(bearerToken) && bearerToken.startsWith("Bearer ")) {
                final var encodeToken = bearerToken.substring(BEARER_SPACE_SIZE);
                final DecodedJWT decodedJWT = JWT.decode(encodeToken);
                final var decodePayload = new String(Base64.decodeBase64(decodedJWT.getPayload()
                        .getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
                final Token token = new ObjectMapper().readValue(decodePayload, Token.class);

                return token;
            }

        } catch (final Exception e) {
            log.error("[JsonWebTokenHandler] Token inválído: ", e);
        }

        log.error("[JsonWebTokenHandler] Token inválído: ");
        return null;

    }
}
