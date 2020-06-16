package com.farzadz.poll.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface SecurityAnnotations {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface PollWrite {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface PollRead {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface Admin {

  }
}
