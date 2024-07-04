package br.dev.dantas.user.configuration;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public interface IntegrationTestContainers {

  @Container
  MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.33");
}
