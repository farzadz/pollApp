package com.farzadz.poll.domain.mapper;

import com.farzadz.poll.dataentry.entity.AnswerOption;
import com.farzadz.poll.dataentry.entity.Question;
import com.farzadz.poll.domain.dto.AnswerOptionDTO;
import com.farzadz.poll.domain.dto.QuestionDTO;
import java.util.LinkedList;
import java.util.List;
import ma.glasnost.orika.BoundMapperFacade;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.stubbing.Answer;

public class DTOMapperTest {

//  @BeforeClass
//  public void setUp() {
//  }

  @Test
  public void test(){
//    AnswerOption answerOption = new AnswerOption(1L, "text", 3, );
    AnswerOption answerOption = new AnswerOption(2L, "text", 3, new Question());
    Question question = new Question(1L, "text", 1000L, List.of(answerOption, answerOption));
    DTOMapper mapper = new DTOMapper();
    QuestionDTO map = mapper.map(question, QuestionDTO.class);
//    AnswerOptionDTO map = mapper.map(answerOption, AnswerOptionDTO.class);
    System.out.println(map);
  }
}