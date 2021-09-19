package com.woomoolmarket.config.encrypt;

import static org.assertj.core.api.Assertions.*;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Log4j2
class PropertyEncryptorConfigTest {

    @Test
    @DisplayName("PropertyEncryptor 동작한다")
    void encryptTest() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setProvider(new BouncyCastleProvider());
        encryptor.setPoolSize(2);
        encryptor.setPassword("brown-bear");
        encryptor.setAlgorithm("PBEWithSHA256And128BitAES-CBC-BC");

        String plainText = "music-tiger";
        String encryptedText = encryptor.encrypt(plainText);
        String decryptedText = encryptor.decrypt(encryptedText);

        assertThat(decryptedText).isEqualTo(plainText);
        System.out.println(String.format("Enc => %s, Dec => %s", encryptedText, decryptedText));
    }
}