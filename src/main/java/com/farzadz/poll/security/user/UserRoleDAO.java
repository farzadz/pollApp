package com.farzadz.poll.security.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleDAO extends JpaRepository<UserRole, Long> {

  Optional<UserRole> findByUserId(Long userId);

  Optional<UserRole> findByUserUsername(String username);
}
