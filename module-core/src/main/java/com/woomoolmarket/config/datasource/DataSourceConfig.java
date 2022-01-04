package com.woomoolmarket.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

@Configuration
@Profile("mysql")
public class DataSourceConfig {

  private static final String SLAVE_DATASOURCE = "slaveDataSource";
  private static final String MASTER_DATASOURCE = "masterDataSource";

  @Bean(name = MASTER_DATASOURCE)
  @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
  public DataSource masterDataSource() {
    return DataSourceBuilder.create()
      .type(HikariDataSource.class)
      .build();
  }

  @Bean(name = SLAVE_DATASOURCE)
  @ConfigurationProperties(prefix = "spring.datasource.slave.hikari")
  public DataSource slaveDataSource() {
    return DataSourceBuilder.create()
      .type(HikariDataSource.class)
      .build();
  }

  @Bean
  @DependsOn({MASTER_DATASOURCE, SLAVE_DATASOURCE})
  public DataSource routingDataSource() {
    DataSourceRouter dataSourceRouter = new DataSourceRouter();
    dataSourceRouter.setTargetDataSources(Map.of("master", masterDataSource(), "slave", slaveDataSource()));
    dataSourceRouter.setDefaultTargetDataSource(masterDataSource());
    return dataSourceRouter;
  }

  @Bean(name = "dataSource")
  @Primary
  public DataSource dataSource(DataSource routingDataSource) {
    return new LazyConnectionDataSourceProxy(routingDataSource);
  }
}
