package com.study.library.security.filter;

import com.study.library.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends GenericFilter {

    @Autowired
    private JwtProvider jwtProvider;
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Boolean isPermitAll = (Boolean) request.getAttribute("isPermitAll");

        if(!isPermitAll) {
            String accessToken = request.getHeader("Authorization");
            String removeBearerToken = jwtProvider.removeBearer(accessToken);
            Claims claims = jwtProvider.getClaims(removeBearerToken);
            if(claims == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // 인증실패(401)
                return;
            }

            Authentication authentication = jwtProvider.getAuthentication(claims);
            if(authentication == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // 인증실패(401)
                return;
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 전처리 영역
        filterChain.doFilter(request, response);
        // 후처리 영역
    }
    
}
