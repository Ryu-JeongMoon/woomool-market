package com.woomoolmarket.config.security;


import com.woomoolmarket.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final JwtSecurityConfig jwtSecurityConfig;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .antMatchers("/h2-console/**", "/favicon.ico", "/error", "/xss", "/", "/home", "/request");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()

            .headers()
            .frameOptions()
            .sameOrigin()

            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .apply(jwtSecurityConfig)

            .and()
            .authorizeRequests()
            .antMatchers("/api/members", "/api/auth/**", "/", "/oauth2/**")
            .permitAll()
            .anyRequest()
            .authenticated();
    }
}

/*
여기서 jwtSecurityConfig apply 안 해주면 적용 안 되네!!
다른 놈들도 마찬가지인가?
 */