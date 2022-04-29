package com.woomoolmarket.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEncryptableProperties
public class PropertyEncryptorConfig {

  @Bean("encryptorBean")
  public PooledPBEStringEncryptor stringEncryptor() {
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    encryptor.setProvider(new BouncyCastleProvider());
    encryptor.setPoolSize(2);
    encryptor.setPassword("panda-bear");
    encryptor.setAlgorithm("PBEWithSHA256And128BitAES-CBC-BC");
    return encryptor;
  }
}
