package com.farzadz.poll.security;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PollUserDetailsService implements UserDetailsService {

  private List<PollUser> users;

  @Autowired
  public PollUserDetailsService(PasswordEncoder passwordEncoder) {
    users = List.of(
        new PollUser("admin", passwordEncoder.encode("password"),
            Set.of(new SimpleGrantedAuthority("ROLE_" + "ADMIN"))
        ),
        new PollUser("user", passwordEncoder.encode("password"),
            Set.of(new SimpleGrantedAuthority("ROLE_" + "USER"))));
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return users.stream().filter(user -> user.getUsername().equals(username)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("User not found"));
  }
}
