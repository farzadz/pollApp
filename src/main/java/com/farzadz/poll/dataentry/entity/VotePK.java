package com.farzadz.poll.dataentry.entity;

import com.farzadz.poll.security.user.PollUser;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@ToString(of = { "user", "answerOption" })
public class VotePK implements Serializable {

  @ManyToOne
  @JoinColumn(name = "id_user")
  private PollUser user;

  @ManyToOne
  @JoinColumn(name = "id_answer_option")
  private AnswerOption answerOption;

}