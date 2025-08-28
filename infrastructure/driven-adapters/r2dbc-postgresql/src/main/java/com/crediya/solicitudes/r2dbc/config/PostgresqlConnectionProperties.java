package com.crediya.solicitudes.r2dbc.config;

// TODO: Load properties from the application.yaml file or from secrets manager
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

@ConfigurationProperties(prefix = "adapters.r2dbc")
@Profile("postgres")
public record PostgresqlConnectionProperties(
        String host,
        Integer port,
        String database,
        String schema,
        String username,
        String password) {
}
