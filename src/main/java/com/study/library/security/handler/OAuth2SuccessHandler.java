package com.study.library.security.handler;

import com.study.library.entity.User;
import com.study.library.jwt.JwtProvider;
import com.study.library.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${client.deploy-address}")
    private String clientAddress;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String name = authentication.getName();
        User user = userMapper.findUserByOAuth2name(name);

        if(user == null) {
            // OAuth2 로그인을 통해 회원가입이 되어있지 않은 상태(연동이 처음인지)
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            String providerName = oAuth2User.getAttribute("provider").toString();
            String id = oAuth2User.getAttribute("id").toString();

            response.sendRedirect("http://" + clientAddress + "/auth/oauth2?name=" + name + "&provider=" + providerName);
            return;
        }

        // OAuth2 로그인을 통해 회원가입을 진행한 기록이 있는 상태(연동이 이미 되어 있는지)
        String accessToken = jwtProvider.generateToken(user);
        response.sendRedirect("http://" + clientAddress + "/auth/oauth2/signin?accessToken=" + accessToken);

        
        // OAuth2 동기화
    }

}
