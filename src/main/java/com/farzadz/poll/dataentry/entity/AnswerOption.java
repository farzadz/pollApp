package com.farzadz.poll.dataentry.entity;

import com.farzadz.poll.service.IdSupport;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Table(name = "answer_option")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = { "id", "optionText" })
public class AnswerOption implements IdSupport {

  @Id
  @SequenceGenerator(name = "answer_option_sequence", sequenceName = "answer_option_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "text")
  private String optionText;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_question")
  private Question question;

  @NonNull
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "id.answerOption")
  private List<UserVote> userVotes = new ArrayList<>();

  public void updateUpdatableProperites(AnswerOption answerOption) {
    if (answerOption.getOptionText() != null) {
      this.optionText = answerOption.getOptionText();
    }
  }
}
