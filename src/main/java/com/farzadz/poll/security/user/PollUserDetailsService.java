package com.farzadz.poll.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PollUserDetailsService implements UserDetailsService {

  private final UserDAO userDAO;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userDAO.findByUsername(username).orElseThrow(() -> new IllegalArgumentException(
        String.format("No user found with %s", username)));
  }
}
