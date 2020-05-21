package com.farzadz.poll.security;

import java.util.Set;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class PollUser implements UserDetails {

  private Long id;

  private String username;

  private String password;

  private boolean accountNonExpired = true;

  private boolean accountNonLocked = true;

  private boolean credentialsNonExpired = true;

  private boolean enabled = true;

  private Set<? extends GrantedAuthority> authorities;

  public PollUser(String username, String password, Set<? extends GrantedAuthority> authorities) {
    this.username = username;
    this.password = password;
    this.authorities = authorities;
  }

}
