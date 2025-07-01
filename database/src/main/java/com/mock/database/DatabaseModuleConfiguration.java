package com.mock.database;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuration class for the database module.
 * This class explicitly enables scanning for components, JPA repositories,
 * and entities within this module's packages. This allows the main Spring Boot
 * application to discover and manage the beans defined in this module.
 */
@Configuration
@ComponentScan("com.mock.database")
@EnableJpaRepositories("com.mock.database.repository")
@EntityScan("com.mock.database.entity")
public class DatabaseModuleConfiguration
{
}