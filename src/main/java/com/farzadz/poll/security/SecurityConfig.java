package com.farzadz.poll.security;

import static com.farzadz.poll.security.UserRole.ADMIN;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


  private final PasswordEncoder passwordEncoder;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.
        authorizeRequests().antMatchers("/", "index", "/css/*", "/js/*").permitAll().antMatchers("/api/**")
        .hasRole(ADMIN.name()).anyRequest().authenticated().and().httpBasic();
  }



//  @Bean
//  @Override
//  public UserDetailsService userDetailsService() {
//    UserDetails admin = User.builder().username("admin").password(passwordEncoder.encode("password"))
//        .roles(ADMIN.name()).build();
//    return new InMemoryUserDetailsManager(admin);
//  }

}
