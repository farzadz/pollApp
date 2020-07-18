package com.farzadz.poll;

import com.farzadz.poll.dataentry.entity.AnswerOption;
import com.farzadz.poll.dataentry.entity.Question;
import com.farzadz.poll.security.user.PollUser;
import com.farzadz.poll.security.user.PollUserDetailsService;
import com.farzadz.poll.service.PollService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestUtils {

  private static final ObjectMapper mapper = new ObjectMapper();

  private final PollUserDetailsService userDetailsService;

  private final PollService pollService;

  private Random random = new Random();

  public static String asJsonString(Object val) throws JsonProcessingException {
    return mapper.writeValueAsString(val);
  }

  public Question createQuestionWithAnswerOptionForUser(String username) {
    PollUser user = createUser(username, "pass");
    Authentication oldAuth = SecurityContextHolder.getContext().getAuthentication();
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken(user.getUsername(), "pass", user.getAuthorities()));

    Question question = new Question();
    question.setQuestionText("Is this a question?");
    Question storedQuestion = pollService.createQuestion(question, user);
    AnswerOption firstOption = new AnswerOption();
    firstOption.setQuestion(storedQuestion);
    firstOption.setOptionText("This the first option");
    pollService.createAnswerOption(storedQuestion.getId(), firstOption, user);

    AnswerOption secondOption = new AnswerOption();
    secondOption.setQuestion(storedQuestion);
    secondOption.setOptionText("This the first option");
    pollService.createAnswerOption(storedQuestion.getId(), secondOption, user);

    Question questionInDB = pollService.getQuestion(storedQuestion.getId());
    //    questionInDB.getAnswerOptions();
    SecurityContextHolder.getContext().setAuthentication(oldAuth);
    return questionInDB;
  }

  public PollUser createUser(String username, String password) {
    PollUser userByUsername = userDetailsService.getUserByUsername(username);
    if (userByUsername != null) {
      return userByUsername;
    }
    return userDetailsService.createUser(new PollUser(username, password));
  }
}
