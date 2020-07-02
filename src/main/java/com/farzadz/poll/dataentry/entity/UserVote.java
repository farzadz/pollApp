package com.farzadz.poll.dataentry.entity;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "user_vote")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@ToString(of = { "id" })
public class UserVote {

  @NonNull
  @EmbeddedId
  private VotePK id;

  @Column(name = "epoch_time")
  private Long createdAt;

  @PrePersist
  void setCreatedAt() {
    this.createdAt = Instant.now().toEpochMilli();
  }

}
