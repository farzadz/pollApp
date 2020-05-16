package com.farzadz.poll.security;

public enum UserPermission {

  POLL_CREATE("poll:create"),
  POLL_READ("poll:read");

  private final String permission;

  UserPermission(String permission) {
    this.permission = permission;
  }

  public String getPermission() {
    return permission;
  }
}

