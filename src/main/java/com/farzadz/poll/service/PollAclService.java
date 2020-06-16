package com.farzadz.poll.service;

import com.farzadz.poll.security.user.PollUser;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.stereotype.Service;

@Data
@RequiredArgsConstructor
@Service
public class PollAclService {

  private final MutableAclService aclService;

  public <T extends IdSupport> void boundAclForObject(T obj, PollUser user) {
    ObjectIdentity oid = new ObjectIdentityImpl(obj.getClass(), obj.getId());
    MutableAcl acl = aclService.createAcl(oid);
    acl.insertAce(0, BasePermission.ADMINISTRATION, new PrincipalSid(user.getUsername()), true);
    acl.insertAce(0, BasePermission.WRITE, new PrincipalSid(user.getUsername()), true);
    acl.insertAce(0, BasePermission.READ, new PrincipalSid(user.getUsername()), true);
    acl.insertAce(0, BasePermission.DELETE, new PrincipalSid(user.getUsername()), true);

    aclService.updateAcl(acl);
  }

}
