//package com.farzadz.poll.security;
//
//import com.farzadz.poll.dataentry.entity.Question;
//import com.farzadz.poll.service.PollAclService;
//import com.farzadz.poll.service.PollService;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PostAuthorize;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class AclAspect {
//
//  private PollAclService aclService;
//
//  private PollService pollService;
//
//  @Autowired
//  public void setAclService(PollAclService aclService) {
//    this.aclService = aclService;
//  }
//
//  @Autowired
//  public void setPollService(PollService pollService) {
//    this.pollService = pollService;
//  }
//
//  @PostAuthorize("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
//  public Question questionAdminAccess(Long questionId) {
//
//  }
//
//  @Before("com.farzadz.poll.security.AclAspect.adminOnly() && args(questionId,..)")
//  public void pollAdminAccessCheck(Long questionId) {
//    aclService
//  }
//
//  @Pointcut("@annotation(com.farzadz.poll.security.SecurityAnnotations.Admin)")
//  public void adminOnly() {
//
//  }
//
//}
