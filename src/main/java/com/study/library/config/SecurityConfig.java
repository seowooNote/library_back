package com.study.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity // 3
@Configuration // 1
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override // 2
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/server/**", "/auth/**")
                .permitAll()
                .anyRequest()
                .authenticated();
    }
}
