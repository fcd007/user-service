package br.dev.dantas.user.configuration;

import static br.dev.dantas.user.commons.Constants.BASE_URI;
import static br.dev.dantas.user.commons.Constants.PASSWORD;
import static br.dev.dantas.user.commons.Constants.USERNAME;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@TestConfiguration
@Lazy
public class RestAssuredConfig {

  @LocalServerPort
  int port;

  @Bean
  public RequestSpecification requestSpecification() {
    return RestAssured.given()
        .baseUri(BASE_URI + port)
        .auth().preemptive().basic(USERNAME, PASSWORD);
  }
}
