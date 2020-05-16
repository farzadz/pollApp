package com.farzadz.poll.security;

import java.util.List;
import java.util.Set;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data

public class CustomUser implements UserDetails {

  public CustomUser(String username, String password, Set<? extends GrantedAuthority> authorities, List<Long> questions) {
    this.username = username;
    this.password = password;
    this.authorities = authorities;
    this.questions = questions;
  }

  private String username;

  private String password;

  private boolean accountNonExpired = true;

  private boolean accountNonLocked = true;

  private boolean credentialsNonExpired = true;

  private boolean enabled = true;

  private Set<? extends GrantedAuthority> authorities;

  private List<Long> questions;

}
