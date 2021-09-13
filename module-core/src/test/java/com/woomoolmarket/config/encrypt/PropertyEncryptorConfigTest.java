package com.woomoolmarket.config.encrypt;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.junit.jupiter.api.Test;

@Log4j2
class PropertyEncryptorConfigTest {

    @Test
    void encryptTest() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setProvider(new BouncyCastleProvider());
        encryptor.setPoolSize(2);
        encryptor.setPassword("secret-key");
        encryptor.setAlgorithm("PBEWithSHA256And128BitAES-CBC-BC");

        String plainText = "panda-bear";
        String encryptedText = encryptor.encrypt(plainText);
        String decryptedText = encryptor.decrypt(encryptedText);

        Assertions.assertThat(decryptedText).isEqualTo(plainText);
        log.info("Enc => {}, Dec => {}", encryptedText, decryptedText);

    }
}