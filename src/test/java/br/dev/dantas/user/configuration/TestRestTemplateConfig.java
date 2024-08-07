package br.dev.dantas.user.configuration;

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
    var uri = new DefaultUriBuilderFactory("http://localhost:" + port);
    var testRestTemplate = new TestRestTemplate().withBasicAuth("mestre.kame@anime.com.br", "1234");

    testRestTemplate.setUriTemplateHandler(uri);
    return testRestTemplate;
  }
}
