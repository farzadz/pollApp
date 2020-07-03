package com.farzadz.poll.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDTO {

  private Long answerOptionId;

  private String username;

  private Long createdAt;
}
