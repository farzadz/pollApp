package com.farzadz.poll.dataentry.entity;

import com.farzadz.poll.service.IdSupport;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Table(name = "question")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = { "id", "questionText" })
public class Question implements IdSupport {

  @Id
  @SequenceGenerator(name = "question_id_seq", sequenceName = "question_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "text")
  private String questionText;

  @Column(name = "epoch_time")
  private Long createdAt;

  @NonNull
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "question")
  private List<AnswerOption> answerOptions = new ArrayList<>();

  @PrePersist
  void setCreatedAt() {
    this.createdAt = Instant.now().toEpochMilli();
  }

  public void addAnswerOption(AnswerOption answerOption) {
    answerOptions.add(answerOption);
    answerOption.setQuestion(this);
  }

  public void updateUpdatableProperties(Question question) {
    this.questionText = question.getQuestionText();
  }
}
