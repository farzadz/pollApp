package com.farzadz.poll.domain.mapper;

import com.farzadz.poll.dataentry.entity.AnswerOption;
import com.farzadz.poll.dataentry.entity.Question;
import com.farzadz.poll.domain.dto.AnswerOptionDTO;
import com.farzadz.poll.domain.dto.PollUserDTO;
import com.farzadz.poll.domain.dto.QuestionDTO;
import com.farzadz.poll.security.user.PollUser;
import java.util.stream.Collectors;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DTOMapper extends ConfigurableMapper {

  @Override
  protected void configure(MapperFactory factory) {

    factory.classMap(Question.class, QuestionDTO.class).field("questionText", "text").byDefault().register();
    factory.classMap(AnswerOptionDTO.class, AnswerOption.class).field("text", "optionText").byDefault().register();
    factory.classMap(PollUser.class, PollUserDTO.class).customize(new CustomMapper<PollUser, PollUserDTO>() {
      @Override
      public void mapAtoB(PollUser user, PollUserDTO pollUserDTO, MappingContext context) {
        pollUserDTO.setRoles(
            user.getUserRoles().stream().map(role -> role.getRoleType().getAuthority()).collect(Collectors.toList()));
      }
    }).byDefault().register();
    ConverterFactory converterFactory = factory.getConverterFactory();
    converterFactory.registerConverter(new AnswerOptionConverter());

  }

  @Bean
  public MapperFacade mapper() {
    return new DTOMapper();
  }

}
