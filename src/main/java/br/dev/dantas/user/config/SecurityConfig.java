package br.dev.dantas.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private static final String[] WHITE_LIST = {"/swagger-ui/index.html", "/v3/api-docs/**",
      "/swagger-ui/**"};
  private static final String PASSWORD = "1234";
  private static final String GUEST = "guest";
  private static final String ADMIN = "admin";


  @Bean
  public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    var user = User.withUsername(GUEST).password(passwordEncoder.encode(PASSWORD)).roles(GUEST)
        .build();

    var admin = User.withUsername(ADMIN).password(passwordEncoder.encode(PASSWORD)).roles(ADMIN)
        .build();

    return new InMemoryUserDetailsManager(user, admin);
  }

  @Bean
  SecurityFilterChain securiyFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth -> auth
                .requestMatchers(WHITE_LIST).permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                .requestMatchers(HttpMethod.DELETE,"api/v1/users") .hasRole(ADMIN)
                .requestMatchers("api/v1/users") .hasRole(ADMIN).anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults())
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
