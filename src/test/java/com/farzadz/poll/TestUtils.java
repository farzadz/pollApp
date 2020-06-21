package com.farzadz.poll;

import com.farzadz.poll.security.user.PollUser;
import com.farzadz.poll.security.user.RoleType;
import com.farzadz.poll.security.user.UserRole;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for mock login. Creates fresh security context and places the specified user, authenticated in it.
 */

public class TestUtils {

  public static void login(String username, String password, List<RoleType> roles) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();

    PollUser user = new PollUser(username, password);
    user.setUserRoles(roles.stream().map(role -> new UserRole(1L, user, role)).collect(Collectors.toList()));

    Authentication auth = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    context.setAuthentication(auth);
    SecurityContextHolder.setContext(context);
  }

  public static void loginAdmin() {
    login("admin", "admin", List.of(RoleType.ADMIN, RoleType.USER));
  }

  public static void loginUser() {
    login("user", "password", List.of(RoleType.USER));
  }
}
