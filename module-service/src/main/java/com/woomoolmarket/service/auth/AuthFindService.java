package com.woomoolmarket.service.auth;

import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.repository.MemberRepository;
import com.woomoolmarket.util.constants.ExceptionMessages;
import com.woomoolmarket.util.wrapper.ThrowingConsumer;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(CoolSmsProperties.class)
public class AuthFindService {

  private final Executor woomoolTaskExecutor;
  private final JavaMailSender javaMailSender;
  private final PasswordEncoder passwordEncoder;
  private final MemberRepository memberRepository;
  private final CoolSmsProperties coolSmsProperties;
  private final ThreadLocal<String> threadLocal = new ThreadLocal<>();

  @Transactional
  public void sendEmailForFinding(String email) {
    Member member = memberRepository.findByEmailAndStatus(email, Status.ACTIVE)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Member.NOT_FOUND));

    SecureRandom secureRandom = new SecureRandom();
    String temporaryPassword = String.valueOf(secureRandom.nextInt());
    member.changePassword(passwordEncoder.encode(temporaryPassword));

    sendAuthStringToEmail(email, "Woomool-Market 임시 비밀번호 안내", "임시 비밀번호 : " + temporaryPassword);
  }

  @Transactional
  public void sendEmailForVerification(String email) {
    SecureRandom secureRandom = new SecureRandom();
    String authString = String.valueOf(secureRandom.nextInt());
    threadLocal.remove();
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
    message.setFrom(coolSmsProperties.getEmail());
    message.setSubject(subject);
    message.setText(text);

    CompletableFuture.runAsync(() -> javaMailSender.send(message), woomoolTaskExecutor);
  }

  @Transactional(readOnly = true)
  public void sendAuthStringToPhone(String phone) {
    Member member = memberRepository.findByPhoneAndStatus(phone, Status.ACTIVE)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Member.NOT_FOUND));

    Message message = createMessage();

    HashMap<String, String> smsParams = new HashMap<>();
    smsParams.put("to", phone);
    smsParams.put("from", coolSmsProperties.getPhoneNumber());
    smsParams.put("type", "SMS");
    smsParams.put("text", "아이디 - " + member.getEmail());
    smsParams.put("app_version", "woomool-market-v1.0");

    CompletableFuture
      .supplyAsync(this::checkBalance, woomoolTaskExecutor)
      .thenAcceptAsync(ThrowingConsumer.unchecked(balance -> {
        if (balance < 20) {
          throw new RuntimeException(ExceptionMessages.CoolSms.NOT_ENOUGH_BALANCE);
        }
        message.send(smsParams);
      }), woomoolTaskExecutor);
  }

  public int checkBalance() {
    Message message = createMessage();

    try {
      return Integer.parseInt(message.balance().get("point").toString());
    } catch (CoolsmsException e) {
      log.info("", e);
      return 0;
    }
  }

  private Message createMessage() {
    return new Message(coolSmsProperties.getApiKey(), coolSmsProperties.getApiSecret());
  }
}
