package com.woomoolmarket.redis.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import redis.embedded.RedisServer;

@Log4j2
@Configuration
@Profile(value = "test")
public class EmbeddedRedisConfig {

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() throws IOException {
        int port = isRedisRunning() ? findAvailablePort() : redisPort;
        redisServer = new RedisServer(port);
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(redisPort));
    }

    public int findAvailablePort() throws IOException {

        for (int port = 10000; port <= 65535; port++) {
            Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }

        throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
    }

    private Process executeGrepProcessCommand(int port) throws IOException {
        String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
        String[] shell = {"/bin/sh", "-c", command};
        return Runtime.getRuntime().exec(shell);
    }

    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }

        } catch (Exception e) {
            log.error(e);
        }

        return StringUtils.hasText(pidInfo.toString());
    }
}
