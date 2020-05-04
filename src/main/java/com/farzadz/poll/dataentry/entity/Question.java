package com.farzadz.poll.dataentry.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "question")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "text")
  private  String questionText;

  @Column(name = "epoch_time")
  private Long createdAt;

  @NonNull
  @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "question")
  List<AnswerOption> answerOptions = new ArrayList<>();

  @PrePersist
  void setCreatedAt() {
    this.createdAt = Instant.now().toEpochMilli();
  }

  public void updateUpdatableProperties(Question question) {
    this.questionText = question.getQuestionText();
  }
}
