package com.woomoolmarket.config.p6spy;

import com.p6spy.engine.spy.P6SpyOptions;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(value = {"local", "mysql"})
public class P6spyConfig {

    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance()
            .setLogMessageFormat(P6spyPrettySqlFormatter.class.getName());
    }
}
