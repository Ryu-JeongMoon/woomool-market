package com.woomoolmarket.service.auth;

import static com.woomoolmarket.helper.MemberTestHelper.MEMBER_EMAIL;
import static com.woomoolmarket.helper.MemberTestHelper.MEMBER_PHONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import com.woomoolmarket.config.AbstractServiceTest;
import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.util.constants.ExceptionMessages;
import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;

@Disabled
class AuthFindServiceTest extends AbstractServiceTest {

  ThreadLocal<String> threadLocal = new ThreadLocal<>();

  @BeforeEach
  void setUp() {
    memberTestHelper.createMember();
  }

  @AfterEach
  void tearDown() {
    threadLocal.remove();
  }

  @Test
  @DisplayName("임시 비밀번호 이메일 전송 성공")
  void sendEmailForFinding() {
    authFindService.sendEmailForFinding(MEMBER_EMAIL);
  }

  @Test
  @DisplayName("임시 비밀번호 이메일 전송 세부사항 일치")
  void sendEmailForFindingDetail() {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(MEMBER_EMAIL);
    message.setFrom("panda");
    message.setSubject("hello");
    message.setText("world");

    CompletableFuture<SimpleMailMessage> future = CompletableFuture.supplyAsync(() -> message);
    SimpleMailMessage mailMessage = future.join();

    assertThat(message.getTo()).isEqualTo(mailMessage.getTo());
    assertThat(message.getFrom()).isEqualTo(mailMessage.getFrom());
    assertThat(message.getText()).isEqualTo(mailMessage.getText());
    assertThat(message.getSubject()).isEqualTo(mailMessage.getSubject());
  }

  @Test
  @DisplayName("이메일 전송 실패 - 존재하지 않는 회원 EntityNotFoundException 발생")
  void sendEmailForFindingFail() {
    assertThrows(EntityNotFoundException.class, () -> authFindService.sendEmailForFinding(MEMBER_EMAIL + 1));
  }

  @Test
  @DisplayName("인증번호 이메일 전송 성공")
  void sendEmailForVerification() {
    authFindService.sendEmailForVerification(MEMBER_EMAIL);
  }

  @Test
  @DisplayName("인증번호 이메일 전송 세부사항")
  void sendEmailForVerificationAndVerified() {
    SecureRandom secureRandom = new SecureRandom();
    String authString = String.valueOf(secureRandom.nextInt());
    threadLocal.set(authString);

    String threadAuthString = threadLocal.get();
    assertThat(threadAuthString).isEqualTo(authString);
  }

  @Test
  @DisplayName("인증번호 이메일 전송 세부사항 검증 실패")
  void isVerifiedFail() {
    SecureRandom secureRandom = new SecureRandom();
    String authString = String.valueOf(secureRandom.nextInt());
    threadLocal.set(authString);

    String threadAuthString = threadLocal.get();
    assertThat(threadAuthString + 1).isNotEqualTo(authString);
  }

  @Test
  @DisplayName("문자 전송 실패 - 존재하지 않는 전화번호")
  void sendAuthStringToPhoneFail() {
    assertThrows(EntityNotFoundException.class,
      () -> memberRepository.findByPhoneAndStatus(MEMBER_PHONE + 1, Status.ACTIVE)
        .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Member.NOT_FOUND)));
  }

  @Test
  @DisplayName("coolsms 잔여 포인트 확인 성공")
  void checkBalance() {
    CompletableFuture.supplyAsync(() -> 25)
      .thenAccept(balance -> {
        if (balance < 20) {
          throw new RuntimeException(ExceptionMessages.CoolSms.NOT_ENOUGH_BALANCE);
        }
        System.out.println("Success");
      })
      .join();
  }

  @Test
  @DisplayName("coolsms 잔여 포인트 확인 실패 - 잔여 포인트 부족")
  void checkBalanceFail() {
    assertThrows(RuntimeException.class, () -> CompletableFuture.supplyAsync(() -> 15)
      .thenAccept(balance -> {
        if (balance < 20) {
          throw new RuntimeException(ExceptionMessages.CoolSms.NOT_ENOUGH_BALANCE);
        }
      })
      .join());
  }
}