package com.noel.util;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtFilter implements Filter {


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

    private void setContext(String s) {
        System.out.println(s);
    }
}
