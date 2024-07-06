package br.dev.dantas.user.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public class IntegrationTestContainers {

  protected static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:8.0.33");
  private static final Logger log = LoggerFactory.getLogger(IntegrationTestContainers.class);

  static {
    log.info("Starting MySQL container...");
    MY_SQL_CONTAINER.start();
  }

  @DynamicPropertySource
  static void mysqProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
    registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
  }
}
