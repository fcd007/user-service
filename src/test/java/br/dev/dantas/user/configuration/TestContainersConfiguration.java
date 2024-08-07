package br.dev.dantas.user.configuration;

import static br.dev.dantas.user.configuration.IntegrationTestContainers.MY_SQL_CONTAINER;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration(proxyBeanMethods = false)
@ImportTestcontainers(IntegrationTestContainers.class)
public class TestContainersConfiguration {

  @Bean
  @ServiceConnection
  public MySQLContainer<?> mySQLContainer() {
    return MY_SQL_CONTAINER;
  }
}