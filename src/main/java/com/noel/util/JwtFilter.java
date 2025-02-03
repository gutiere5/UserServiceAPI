package com.noel.util;

import com.noel.service.CognitoUserService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.naming.spi.ObjectFactory;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final CognitoUserService userService;
    private final ObjectFactory<UserContext> userContextFactory;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // this is how we can get the content of every single Http request
        Optional.of(servletRequest)
                .map(HttpServletRequest.class::cast)
                .map(req -> req.getHeader("Authorization"))
                .map(token -> token.split("\\."))
                .filter(chunks->chunks.length > 1)
                .map(chunks -> Base64.getUrlDecoder().decode(chunks[1]))
                .map(String :: new)
                .ifPresent(this::setContext);
        filterChain.doFilter(servletRequest, servletResponse );
    }

    private void setContext(String payloadJson) {
        Optional.of(payloadJson)
                .map(Payload::generate)
                .map(Payload::getUserId)
                .map(userService::getUser)


    }
}
