package com.woomoolmarket.config.security;


import com.woomoolmarket.security.oauth2.CustomOAuth2UserService;
import com.woomoolmarket.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.woomoolmarket.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.woomoolmarket.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.woomoolmarket.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final JwtSecurityConfig jwtSecurityConfig;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .antMatchers("/h2-console/**", "/favicon.ico", "/xss", "/", "/request", "/actuator/**");
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
            .authorizeRequests()
            .antMatchers("/api/**", "/api/auth/**", "/", "/login/**", "/oauth2/**", "/upload")
            .permitAll()
            .anyRequest()
            .authenticated()

            .and()
            .apply(jwtSecurityConfig)

            .and()
            .oauth2Login()
            .loginPage("/login")
            .clientRegistrationRepository(clientRegistrationRepository)
            .authorizationEndpoint()
            .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)

            .and()
            .userInfoEndpoint()
            .userService(customOAuth2UserService)

            .and()
            .successHandler(oAuth2AuthenticationSuccessHandler)
            .failureHandler(oAuth2AuthenticationFailureHandler)

            .and()
            .exceptionHandling()
            .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
    }
}