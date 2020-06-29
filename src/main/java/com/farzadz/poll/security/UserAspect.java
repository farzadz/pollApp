package com.farzadz.poll.security;

import com.farzadz.poll.security.user.PollUserDetailsService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserAspect {

  private PollUserDetailsService userDetailsService;

  private UserAccessChecker userAccessChecker;

  @Autowired
  public void setUserAccessChecker(UserAccessChecker userAccessChecker) {
    this.userAccessChecker = userAccessChecker;
  }

  @Autowired
  public void setUserDetailsService(PollUserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Pointcut("@annotation(com.farzadz.poll.security.SecurityAnnotations.UserReadAccess) && args(username,..)")
  private void userReadAccessPointcut(String username) {
  }

  @Before(value = "userReadAccessPointcut(username)", argNames = "username")
  public void doCheckUserReadAccess(String username) {
    userAccessChecker.checkReadAccessById(userDetailsService.loadUserByUsername(username).getId());
  }

  @Pointcut("@annotation(com.farzadz.poll.security.SecurityAnnotations.UserWriteAccess) && args(username,..)")
  private void userWriteAccessPointcut(String username) {
  }

  @Before(value = "userWriteAccessPointcut(username)", argNames = "username")
  public void doCheckUserWriteAccess(String username) {
    userAccessChecker.checkWriteAccessById(userDetailsService.loadUserByUsername(username).getId());
  }
}
