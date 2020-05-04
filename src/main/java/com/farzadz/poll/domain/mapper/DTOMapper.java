package com.farzadz.poll.domain.mapper;

import com.farzadz.poll.dataentry.dao.AnswerOptionDAO;
import com.farzadz.poll.dataentry.entity.AnswerOption;
import com.farzadz.poll.dataentry.entity.Question;
import com.farzadz.poll.domain.dto.AnswerOptionDTO;
import com.farzadz.poll.domain.dto.QuestionDTO;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DTOMapper extends ConfigurableMapper {

  @Override
  protected void configure(MapperFactory factory) {

    factory.classMap(Question.class, QuestionDTO.class).field("questionText", "text").byDefault().register();
    factory.classMap(AnswerOption.class, AnswerOptionDTO.class).byDefault().register();
    ConverterFactory converterFactory = factory.getConverterFactory();
    converterFactory.registerConverter(new AnswerOptionConverter());

  }

  @Bean
  public MapperFacade mapper() {
    return new DTOMapper();
  }

}
