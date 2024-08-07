package br.dev.dantas.user.configuration;

import static br.dev.dantas.user.commons.Constants.*;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.util.DefaultUriBuilderFactory;

@TestConfiguration
@Lazy
public class TestRestTemplateConfig {

  @LocalServerPort
  int port;

  @Bean
  public TestRestTemplate testRestTemplate() {
    var uri = new DefaultUriBuilderFactory(BASE_URI + port);
    var testRestTemplate = new TestRestTemplate().withBasicAuth(USERNAME, PASSWORD);

    testRestTemplate.setUriTemplateHandler(uri);
    return testRestTemplate;
  }
}
