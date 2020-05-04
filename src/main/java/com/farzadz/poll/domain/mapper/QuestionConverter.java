package com.farzadz.poll.domain.mapper;

import com.farzadz.poll.dataentry.entity.Question;
import com.farzadz.poll.domain.dto.QuestionDTO;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

public class QuestionConverter extends CustomConverter<Question, QuestionDTO> {

  @Override
  public QuestionDTO convert(Question source, Type<? extends QuestionDTO> destinationType,
      MappingContext mappingContext) {
    return new QuestionDTO(source.getId(), source.getQuestionText(), source.getCreatedAt());
  }
}
