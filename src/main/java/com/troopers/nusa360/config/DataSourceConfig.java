package com.troopers.nusa360.config;


import com.zaxxer.hikari.HikariDataSource;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration(proxyBeanMethods = false)
public class DataSourceConfig {

    // --- MySQL Datasource Configuration ---
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.sql")
    public DataSourceProperties sqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public HikariDataSource sqlDataSource(@Qualifier("sqlDataSourceProperties") DataSourceProperties sqlDataSourceProperties) {
        return sqlDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    // --- PGVector Datasource Configuration ---
    @Bean
    @ConfigurationProperties("spring.datasource.vector")
    public DataSourceProperties vectorDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public HikariDataSource vectorDataSource(@Qualifier("vectorDataSourceProperties") DataSourceProperties vectorDataSourceProperties) {
        return vectorDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }


    @Bean
    public JdbcTemplate pgvectorJdbcTemplate(@Qualifier("vectorDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // Spring AI PGVectorStore Bean
//    @Bean
//    public VectorStore vectorStore(
//            JdbcTemplate pgvectorJdbcTemplate,
//            PgVectorStore.PgVectorStoreConfig pgvectorConfig // Injected by Spring AI auto-configuration
//    ) {
//        return new PgVectorStore(pgvectorJdbcTemplate, pgvectorConfig);
//    }
}
