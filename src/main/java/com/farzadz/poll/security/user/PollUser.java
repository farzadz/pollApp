package com.farzadz.poll.security.user;

import com.farzadz.poll.security.user.UserRole;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Entity
@Table(name = "user_account")
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })
@ToString(of = {"username"})
public class PollUser implements UserDetails {

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "user")
  List<UserRole> userRoles = new LinkedList<>();

  @Id
  @NonNull
  @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NonNull
  private String username;

  @NonNull
  private String password;

  private boolean enabled = true;

  public PollUser(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public Set<GrantedAuthority> getAuthorities() {
    return userRoles.stream()
        .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.getRoleType().getAuthority()))
        .collect(Collectors.toSet());
  }

  @Override
  public boolean isAccountNonExpired() {
    return isEnabled();
  }

  @Override
  public boolean isAccountNonLocked() {
    return isEnabled();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return isEnabled();
  }

}
