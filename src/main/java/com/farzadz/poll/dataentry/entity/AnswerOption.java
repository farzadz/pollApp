package com.farzadz.poll.dataentry.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answer_option")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerOption {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "text")
  private String optionText;

  @Column(name = "vote_count")
  private Integer voteCount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_question")
  Question question;

  public void updateUpdatableProperites(AnswerOption answerOption) {
    this.optionText = answerOption.getOptionText();
    this.voteCount = answerOption.getVoteCount();
  }
}
