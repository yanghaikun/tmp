package net.rlair.flight.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Yang Haikun
 */
@Configuration
@EnableAutoConfiguration
//@Import(JpaConfig.class)
@ComponentScan(basePackages = {"net.rlair.flight.test"})
@EntityScan(basePackages={"net.rlair.flight.entity"})
@EnableJpaRepositories(basePackages = {"net.rlair.flight.repository"})
public class ApplicationConfig {
}
