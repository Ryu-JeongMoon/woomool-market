package com.woomoolmarket.service.auth;

import com.woomoolmarket.common.constants.ExceptionConstants;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthFindService {

    private final Executor woomoolTaskExecutor;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Value("${coolsms.api-key}")
    private String COOL_SMS_KEY;
    @Value("${coolsms.api-secret}")
    private String COOL_SMS_SECRET;
    @Value("${mail.smtp.username}")
    private String WOOMOOL_MARKET_EMAIL;
    @Value("${coolsms.phone-number}")
    private String WOOMOOL_MARKET_PHONE;

    @Transactional
    public void sendEmailForFinding(String email) {
        Member member = memberRepository.findByEmailAndStatus(email, Status.ACTIVE)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.MEMBER_NOT_FOUND));

        SecureRandom secureRandom = new SecureRandom();
        String temporaryPassword = String.valueOf(secureRandom.nextInt());
        member.changePassword(passwordEncoder.encode(temporaryPassword));

        sendAuthStringToEmail(email, "Woomool-Market 임시 비밀번호 안내", "임시 비밀번호 : " + temporaryPassword);
    }

    public void sendEmailForVerification(String email) {
        SecureRandom secureRandom = new SecureRandom();
        String authString = String.valueOf(secureRandom.nextInt());
        threadLocal.set(authString);
        sendAuthStringToEmail(email, "Woomool-Market 인증 번호 안내", "인증 번호 : " + authString);
    }

    public boolean isVerified(String authString) {
        String threadAuthString = threadLocal.get();
        return authString.equals(threadAuthString);
    }

    private void sendAuthStringToEmail(String email, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(WOOMOOL_MARKET_EMAIL);
        message.setSubject(subject);
        message.setText(text);

        CompletableFuture.runAsync(() -> javaMailSender.send(message), woomoolTaskExecutor);
    }

    @Transactional(readOnly = true)
    public void sendAuthStringToPhone(String phone) {
        Member member = memberRepository.findByPhoneAndStatus(phone, Status.ACTIVE)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.MEMBER_NOT_FOUND));

        Message message = new Message(COOL_SMS_KEY, COOL_SMS_SECRET);

        HashMap<String, String> smsParams = new HashMap<>();
        smsParams.put("to", phone);
        smsParams.put("from", WOOMOOL_MARKET_PHONE);
        smsParams.put("type", "SMS");
        smsParams.put("text", "아이디 - " + member.getEmail());
        smsParams.put("app_version", "woomool-market-v1.0");

        CompletableFuture.supplyAsync(this::checkBalance, woomoolTaskExecutor)
            .thenAccept(balance -> {
                if (balance < 20) {
                    throw new RuntimeException(ExceptionConstants.NOT_ENOUGH_BALANCE);
                }

                try {
                    message.send(smsParams);
                } catch (CoolsmsException e) {
                    log.info(e);
                }
            });
    }

    public int checkBalance() {
        Message message = new Message(COOL_SMS_KEY, COOL_SMS_SECRET);

        try {
            return Integer.parseInt(message.balance().get("point").toString());
        } catch (CoolsmsException e) {
            log.info(e);
            return 0;
        }
    }
}
