package com.dryxtech.software.sample.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.op.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.dw.datasource")
    public DataSource dataWarehouseSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("operationJdbcTemplate")
    public JdbcTemplate operationJdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean("dataWarehouseJdbcTemplate")
    public JdbcTemplate dataWarehouseJdbcTemplate(@Qualifier("dataWarehouseSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
