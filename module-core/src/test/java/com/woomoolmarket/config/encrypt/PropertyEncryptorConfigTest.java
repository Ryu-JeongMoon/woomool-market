package com.woomoolmarket.config.encrypt;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.log4j.Log4j2;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Log4j2
class PropertyEncryptorConfigTest {

    private static PooledPBEStringEncryptor encryptor;

    @BeforeEach
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
        System.out.printf("Enc => %s, Dec => %s%n", encryptedText, decryptedText);
    }

    @Test
    @DisplayName("yml 설정 암호화 process")
    void encryptTest2() {
        String encrypt = encryptor.encrypt("암호화 하고 싶은 값");
        System.out.printf("암호화된 값 => %s", encrypt);
    }
}