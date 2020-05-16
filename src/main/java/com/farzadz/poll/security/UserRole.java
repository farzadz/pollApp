package com.farzadz.poll.security;

import static com.farzadz.poll.security.UserPermission.POLL_CREATE;
import static com.farzadz.poll.security.UserPermission.POLL_READ;

import java.util.Set;

public enum UserRole {
  ADMIN(Set.of(POLL_CREATE, POLL_READ));

  private final Set<UserPermission> permissions;

  UserRole(Set<UserPermission> permissions) {
    this.permissions = permissions;
  }
}
