package com.antiguedades.config;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FlywayConfigTest {

    @Test
    void usesDedicatedMigrationCredentialsInsteadOfRuntimeDataSource() {
        Flyway flyway = new FlywayConfig().flyway(
            "jdbc:postgresql://db:5432/antiguedades",
            "antiguedades_migrator",
            "migration-secret");

        assertThat(flyway.getConfiguration().getUser()).isEqualTo("antiguedades_migrator");
    }
}
