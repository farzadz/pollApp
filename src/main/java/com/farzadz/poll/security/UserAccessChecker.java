package com.farzadz.poll.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class UserAccessChecker {

  @PreAuthorize("hasPermission(#userId, 'com.farzadz.poll.security.user.PollUser', 'READ') OR hasRole('ROLE_ADMIN')")
  public void checkReadAccessById(Long userId) {
  }

  @PreAuthorize("hasPermission(#userId, 'com.farzadz.poll.security.user.PollUser', 'WRITE') OR hasRole('ROLE_ADMIN')")
  public void checkWriteAccessById(Long userId) {

  }
}
