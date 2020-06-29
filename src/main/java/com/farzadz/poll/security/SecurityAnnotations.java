package com.farzadz.poll.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

public interface SecurityAnnotations {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @PreAuthorize("hasPermission(#questionId, 'com.farzadz.poll.dataentry.entity.Question', 'WRITE') OR hasRole('ROLE_ADMIN')")
  @interface QuestionWrite {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @PreAuthorize("hasPermission(#questionId, 'com.farzadz.poll.dataentry.entity.Question', 'READ') OR hasRole('ROLE_ADMIN')")
  @interface QuestionRead {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @PostFilter("hasPermission(filterObject, 'READ') OR hasRole('ROLE_ADMIN')")
  @interface ReadAccessFilter {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface UserReadAccess {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface UserWriteAccess {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @interface AdminOnly {

  }
}
