package com.woomoolmarket.config.security;


import static com.woomoolmarket.domain.member.entity.AuthProvider.FACEBOOK;
import static com.woomoolmarket.domain.member.entity.AuthProvider.GOOGLE;
import static com.woomoolmarket.domain.member.entity.AuthProvider.KAKAO;
import static com.woomoolmarket.domain.member.entity.AuthProvider.NAVER;

import com.woomoolmarket.security.handler.JwtAccessDeniedExceptionHandler;
import com.woomoolmarket.security.jwt.JwtAuthenticationEntryPoint;
import com.woomoolmarket.security.jwt.JwtAuthenticationFilter;
import com.woomoolmarket.security.oauth2.CustomOAuth2Provider;
import com.woomoolmarket.security.oauth2.CustomOAuth2UserService;
import com.woomoolmarket.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.woomoolmarket.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.woomoolmarket.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.woomoolmarket.security.service.CustomUserDetailsService;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //jsr250Enabled = true,
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final static List<String> clients = Arrays.asList("google", "facebook", "naver", "kakao", "github");
    private final static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final JwtSecurityConfig jwtSecurityConfig;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAccessDeniedExceptionHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CorsFilter corsFilter;
    private final Environment env;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = clients.stream()
            .map(client -> getRegistration(client))
            .filter(registration -> registration != null)
            .collect(Collectors.toList());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration getRegistration(String client) {
        String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");
        String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");

        switch (client) {
            case "kakao":
                return CustomOAuth2Provider.KAKAO.getBuilder(client).clientId(clientId).clientSecret(clientSecret)
                    .build();
            case "naver":
                return CustomOAuth2Provider.NAVER.getBuilder(client).clientId(clientId).clientSecret(clientSecret)
                    .build();
            case "google":
                return CommonOAuth2Provider.GOOGLE.getBuilder(client).clientId(clientId).clientSecret(clientSecret)
                    .build();
            case "github":
                return CommonOAuth2Provider.GITHUB.getBuilder(client).clientId(clientId).clientSecret(clientSecret)
                    .build();
            case "facebook":
                return CommonOAuth2Provider.FACEBOOK.getBuilder(client).clientId(clientId).clientSecret(clientSecret)
                    .build();
            default:
                return null;
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .antMatchers("/h2-console/**", "/favicon.ico", "/error", "/xss");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf().disable()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)

            .and()
            .headers()
            .frameOptions()
            .sameOrigin()

            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .authorizeRequests()
            .antMatchers("/api/hello", "/api/authenticate", "/api/login", "/api/members", "/h2-console/**", "/xss",
                "api/member/admin-only/**", "/", "/oauth2/**", "/login")
            .permitAll()
            .antMatchers("/facebook").hasAuthority(FACEBOOK.toString())
            .antMatchers("/google").hasAuthority(GOOGLE.toString())
            .antMatchers("/kakao").hasAuthority(KAKAO.toString())
            .antMatchers("/naver").hasAuthority(NAVER.toString())
//            .antMatchers("/api/members/admin-only/**").hasRole("ADMIN")
            .anyRequest().authenticated()

            .and()
            .apply(jwtSecurityConfig)

            .and()
            .oauth2Login()
            .clientRegistrationRepository(clientRegistrationRepository())
            .authorizationEndpoint()
            .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)

            .and()
            .redirectionEndpoint()
            .baseUri("/oauth2/callback/*")

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
