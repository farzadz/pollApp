package com.farzadz.poll.security.user;

public enum RoleType {
  USER("USER"),
  ADMIN("ADMIN");

  protected String authority;

  RoleType(String authority) {
    this.authority = authority;
  }
  public String getAuthority(){
    return authority;
  }
}
