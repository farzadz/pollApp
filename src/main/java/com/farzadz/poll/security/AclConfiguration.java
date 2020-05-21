package com.farzadz.poll.security;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.EhCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Configuration
@RequiredArgsConstructor
public class AclConfiguration {

  private final DataSource dataSource;

  @Bean
  public JdbcMutableAclService aclService() {
    JdbcMutableAclService jdbcMutableAclService = new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());
    jdbcMutableAclService.setClassIdentityQuery("select currval(pg_get_serial_sequence('acl_class', 'id'))");
    jdbcMutableAclService.setSidIdentityQuery("select currval(pg_get_serial_sequence('acl_sid', 'id'))");
    return jdbcMutableAclService;
  }

  @Bean
  public AclAuthorizationStrategy aclAuthorizationStrategy() {
    return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
  }

  @Bean
  public PermissionGrantingStrategy permissionGrantingStrategy() {
    return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
  }

  @Bean
  public EhCacheBasedAclCache aclCache() {
    return new EhCacheBasedAclCache(aclEhCacheFactoryBean().getObject(), permissionGrantingStrategy(),
        aclAuthorizationStrategy());
  }

  @Bean
  public EhCacheFactoryBean aclEhCacheFactoryBean() {
    EhCacheFactoryBean ehCacheFactoryBean = new EhCacheFactoryBean();
    ehCacheFactoryBean.setCacheManager(aclCacheManager().getObject());
    ehCacheFactoryBean.setCacheName("aclCache");
    return ehCacheFactoryBean;
  }

  @Bean
  public EhCacheManagerFactoryBean aclCacheManager() {
    return new EhCacheManagerFactoryBean();
  }

  @Bean
  public LookupStrategy lookupStrategy() {
    return new BasicLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), new ConsoleAuditLogger());
  }
}
