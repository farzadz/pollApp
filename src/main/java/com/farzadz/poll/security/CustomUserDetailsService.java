package com.farzadz.poll.security;

import static com.farzadz.poll.security.UserRole.ADMIN;

import com.farzadz.poll.security.CustomUser;
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
public class CustomUserDetailsService implements UserDetailsService {

  private PasswordEncoder passwordEncoder;

  private List<CustomUser> users;

  @Autowired
  public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
    users = List.of(new CustomUser("amin", passwordEncoder.encode("password"),
        Set.of(new SimpleGrantedAuthority("ROLE_" + ADMIN.name())), List.of(1L)));

  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return users.stream().filter(user -> user.getUsername().equals(username)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("User not found"));
  }
}
