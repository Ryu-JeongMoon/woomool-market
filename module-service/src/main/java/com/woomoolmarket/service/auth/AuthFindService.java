package com.woomoolmarket.service.auth;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.util.ExceptionUtil;
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
    @Value("${mail.smtp.username}")
    private String WOOMOOL_MARKET_EMAIL;
    @Value("${coolsms.api-key}")
    private String COOL_SMS_KEY;
    @Value("${coolsms.api-secret}")
    private String COOL_SMS_SECRET;
    @Value("${coolsms.phone-number}")
    private String WOOMOOL_MARKET_PHONE;

    @Transactional
    public void sendAuthStringToEmail(String email) {
        Member member = memberRepository.findByEmailAndStatus(email, Status.ACTIVE)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.MEMBER_NOT_FOUND));

        SecureRandom secureRandom = new SecureRandom();
        int temporaryPassword = secureRandom.nextInt();
        member.changePassword(passwordEncoder.encode(String.valueOf(temporaryPassword)));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(WOOMOOL_MARKET_EMAIL);
        message.setSubject("Woomool-Market 임시 비밀번호 안내");
        message.setText("임시 비밀번호 - " + temporaryPassword);

        CompletableFuture.runAsync(() -> javaMailSender.send(message), woomoolTaskExecutor);
    }

    @Transactional(readOnly = true)
    public void sendAuthStringToPhone(String phone) {
        Member member = memberRepository.findByPhoneAndStatus(phone, Status.ACTIVE)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.MEMBER_NOT_FOUND));

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
                    throw new RuntimeException(ExceptionUtil.NOT_ENOUGH_BALANCE);
                }

                try {
                    message.send(smsParams);
                } catch (CoolsmsException e) {
                    log.error(e);
                }
            });
    }

    public int checkBalance() {
        Message message = new Message(COOL_SMS_KEY, COOL_SMS_SECRET);

        try {
            return Integer.parseInt(message.balance().get("point").toString());
        } catch (CoolsmsException e) {
            log.error(e);
            return 0;
        }
    }
}
