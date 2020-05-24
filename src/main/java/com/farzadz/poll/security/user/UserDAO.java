package com.farzadz.poll.security.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<PollUser, Long> {

  Optional<PollUser> findByUsername(String username);
}
