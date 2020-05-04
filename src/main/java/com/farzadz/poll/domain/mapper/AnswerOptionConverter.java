package com.farzadz.poll.domain.mapper;

import com.farzadz.poll.dataentry.entity.AnswerOption;
import com.farzadz.poll.domain.dto.AnswerOptionDTO;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

public class AnswerOptionConverter extends CustomConverter<AnswerOption, AnswerOptionDTO> {

  @Override
  public AnswerOptionDTO convert(AnswerOption source, Type<? extends AnswerOptionDTO> destinationType,
      MappingContext mappingContext) {
    return new AnswerOptionDTO(source.getId(), source.getOptionText(), source.getVoteCount(), source.getQuestion().getId());
  }
}
