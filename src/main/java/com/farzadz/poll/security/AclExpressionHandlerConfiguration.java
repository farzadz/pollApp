package com.farzadz.poll.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AclExpressionHandlerConfiguration extends GlobalMethodSecurityConfiguration {

  private final MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler;

  @Override
  protected MethodSecurityExpressionHandler createExpressionHandler() {
    return defaultMethodSecurityExpressionHandler;
  }



}
