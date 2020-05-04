package com.farzadz.poll.domain;

import com.farzadz.poll.domain.dto.AnswerOptionDTO;
import com.farzadz.poll.domain.dto.QuestionDTO;
import java.util.List;
import lombok.Data;

@Data
public class FullQuestion {

  QuestionDTO questionDTO;

  List<AnswerOptionDTO> answerOptionDTOS;

  public Long getId(){
    return questionDTO.getId();
  }
}
