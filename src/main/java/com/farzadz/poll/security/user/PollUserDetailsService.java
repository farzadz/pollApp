package com.farzadz.poll.security.user;

import com.farzadz.poll.security.SecurityAnnotations.AdminOnly;
import com.farzadz.poll.security.SecurityAnnotations.UserReadAccess;
import com.farzadz.poll.security.SecurityAnnotations.UserWriteAccess;
import com.farzadz.poll.service.PollAclService;
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

  private final PasswordEncoder passwordEncoder;

  private final PollAclService aclService;

  public PollUser createUser(PollUser user) {

    if (userDAO.existsByUsername(user.getUsername())) {
      throw new IllegalArgumentException(String.format("User with id %s already exists", user.getUsername()));
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.addRoleType(RoleType.USER);
    PollUser userInDb = userDAO.save(user);
    aclService.boundAclForObject(userInDb, user);

    return userInDb;
  }

  /**
   * Editing user in non-administrative fashion (by users themselves).
   */
  @UserWriteAccess
  public PollUser updateUser(String username, PollUser user) {
    PollUser userInDb = getUserByUsername(username);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userInDb.updateUpdatableProperties(user);
    return userDAO.save(userInDb);
  }

  @UserWriteAccess
  public void deleteUser(String username) {
    PollUser user = loadUserByUsername(username);
    userDAO.deleteById(user.getId());
  }

  @UserReadAccess
  public PollUser getUserByUsername(String username) {
    return loadUserByUsername(username);
  }

  @Override
  public PollUser loadUserByUsername(String username) throws UsernameNotFoundException {
    return userDAO.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException(String.format("No user found with username %s", username)));
  }

  @AdminOnly
  public List<PollUser> getAllUsers() {
    return userDAO.findAll();
  }
}
