package org.example.alacom.app.config;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class FlywayConfig {
 
    private final DataSource dataSource;

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        return new Flyway(
                Flyway.configure()
                        .baselineOnMigrate(true)
                        .dataSource(dataSource));
    }
}
