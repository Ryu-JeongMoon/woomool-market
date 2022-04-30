package com.woomoolmarket.config;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@Slf4j
@TestInstance(Lifecycle.PER_CLASS)
class PropertyEncryptorConfigTest {

  private static PooledPBEStringEncryptor encryptor;

  @BeforeAll
  void init() {
    encryptor = new PooledPBEStringEncryptor();
    encryptor.setProvider(new BouncyCastleProvider());
    encryptor.setPoolSize(2);
    encryptor.setPassword("brown-bear");
    encryptor.setAlgorithm("PBEWithSHA256And128BitAES-CBC-BC");
  }

  @Test
  @DisplayName("PropertyEncryptor 동작한다")
  void encryptTest() {
    String plainText = "music-tiger";
    String encryptedText = encryptor.encrypt(plainText);
    String decryptedText = encryptor.decrypt(encryptedText);

    assertThat(decryptedText).isEqualTo(plainText);
    log.info("Enc => {}, Dec => {}", encryptedText, decryptedText);
  }

  @Test
  @DisplayName("yml 설정 암호화 process")
  void encryptTest2() {
    String encrypt = encryptor.encrypt("암호화 하고 싶은 값");
    log.info("암호화된 값 => {}", encrypt);
  }
}