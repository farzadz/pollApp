package com.farzadz.poll.security.user;

import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Users fed to methods have plaintext passwords.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PollUserDetailsService implements UserDetailsService {

  private final UserDAO userDAO;

  private final UserRoleDAO roleDAO;

  private final PasswordEncoder passwordEncoder;

  public PollUser createUser(PollUser user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setUserRoles(List.of(
        roleDAO.findByRoleType(RoleType.USER).orElseThrow(() -> new IllegalArgumentException("User role not found"))));
    return userDAO.save(user);
  }

  /**
   * Editing user in non-administrative fashion (by users themselves).
   */
  public PollUser updateUser(Long userId, PollUser user) {
    PollUser userInDb = userDAO.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User with id %s not found"));
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userInDb.updateUpdatableProperties(user);
    return userDAO.save(userInDb);
  }

  public void deleteUser(Long userId) {
    if (!userDAO.existsById(userId)) {
      throw new IllegalArgumentException(String.format("User %s not found", userId));
    }
    userDAO.deleteById(userId);
  }

  public PollUser getUserById(Long userId) {
    return userDAO.findById(userId).orElseThrow(() -> new IllegalArgumentException("User %s not found"));
  }

  @Override
  public PollUser loadUserByUsername(String username) throws UsernameNotFoundException {
    return userDAO.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException(String.format("No user found with %s", username)));
  }

  public List<PollUser> getAllUsers() {
    return userDAO.findAll();
  }
}
