package com.farzadz.poll.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerOptionDTO {

  private Long id;

  private String text;

  private Long questionId;
}
