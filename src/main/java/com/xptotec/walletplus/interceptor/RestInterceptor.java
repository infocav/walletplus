package com.xptotec.walletplus.interceptor;


import com.xptotec.walletplus.model.token.Token;
import com.xptotec.walletplus.service.token.ParseToken;
import com.xptotec.walletplus.service.token.SaveToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;


@RequiredArgsConstructor
@Slf4j
@Service
public class RestInterceptor implements HandlerInterceptor {

    private final SaveToken saveToken;
    private final ParseToken parseToken;
    public static final String AUTHORIZATION_HEADER = "Authorization";


    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        final Token token = parseToken.execute(authorizationHeader);

        saveToken.execute(token);
        return true;
    }



}