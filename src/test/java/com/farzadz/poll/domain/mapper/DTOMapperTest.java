package com.farzadz.poll.domain.mapper;

import static org.junit.Assert.assertEquals;

import com.farzadz.poll.dataentry.entity.AnswerOption;
import com.farzadz.poll.dataentry.entity.Question;
import com.farzadz.poll.domain.dto.AnswerOptionDTO;
import com.farzadz.poll.domain.dto.PollUserDTO;
import com.farzadz.poll.domain.dto.QuestionDTO;
import com.farzadz.poll.security.user.PollUser;
import com.farzadz.poll.security.user.RoleType;
import com.farzadz.poll.security.user.UserRole;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class DTOMapperTest {

  private DTOMapper mapper;

  @Before
  public void setup() {
    this.mapper = new DTOMapper();
  }

  @Test
  public void testQuestionMapper() {

    Question question = new Question(1L, "text", 1000L, new LinkedList<>());
    QuestionDTO questionDTO = mapper.map(question, QuestionDTO.class);
    assertEquals("text", questionDTO.getText());
    assertEquals(1L, questionDTO.getId().longValue());
    assertEquals(1000L, questionDTO.getCreatedAt().longValue());
  }

  @Test
  public void testAnswerOptionMapper() {
    Question question = new Question(1L, "questionText", 1000L, new LinkedList<>());
    AnswerOption answerOption = new AnswerOption(2L, "text", 3, question);
    AnswerOptionDTO answerDTO = mapper.map(answerOption, AnswerOptionDTO.class);
    assertEquals(2L, answerDTO.getId().longValue());
    assertEquals("text", answerDTO.getText());
    assertEquals(3, answerDTO.getVoteCount().intValue());
    assertEquals(1L, answerDTO.getQuestionId().longValue());
  }

  @Test
  public void testUserMapper() {
    PollUser user = new PollUser("username", "password");
    UserRole role = new UserRole();
    role.setId(1L);
    role.setUser(user);
    role.setRoleType(RoleType.USER);
    user.setUserRoles(List.of(role));
    PollUserDTO userDTO = mapper.map(user, PollUserDTO.class);
    assertEquals(user.getUsername(), userDTO.getUsername());
    assertEquals(user.getPassword(), userDTO.getPassword());
    PollUser map = mapper.map(userDTO, PollUser.class);
  }
}