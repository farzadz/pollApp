package com.farzadz.poll.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.farzadz.poll.PollApplication;
import com.farzadz.poll.dataentry.dao.UserVoteDAO;
import com.farzadz.poll.dataentry.entity.AnswerOption;
import com.farzadz.poll.dataentry.entity.Question;
import com.farzadz.poll.dataentry.entity.UserVote;
import com.farzadz.poll.security.user.PollUser;
import com.farzadz.poll.security.user.PollUserDetailsService;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PollApplication.class)
@TestPropertySource(locations = "classpath:test.yaml")
@WithMockUser(username = "user")
class VoteServiceIntegrationTest {

  @Autowired
  private VoteService voteService;

  @Autowired
  private PollService pollService;

  @Autowired
  private PollUserDetailsService userDetailsService;

  @Autowired
  private UserVoteDAO userVoteDAO;

  private Random random = new Random();

  @Test
  void createVote_UserExists_ShouldCreateVoteInDB() {
    PollUser user = userDetailsService.createUser(new PollUser("sampleUser" + random.nextLong(), "pass"));
    Question question = new Question();
    question.setQuestionText("Is this a question?");
    Question storedQuestion = pollService.createQuestion(question, user);
    AnswerOption answerOption = new AnswerOption();
    answerOption.setQuestion(storedQuestion);
    answerOption.setOptionText("This is an option");
    AnswerOption storedAnswerOption = pollService.createAnswerOption(storedQuestion.getId(), answerOption, user);
    UserVote vote = voteService.vote(user, storedAnswerOption.getId());
    assertEquals(storedAnswerOption.getId(), vote.getId().getAnswerOption().getId());
    assertEquals(user.getUsername(), vote.getId().getUser().getUsername());
    assertFalse(userVoteDAO.findByIdUser(user).isEmpty());
    assertFalse(userVoteDAO.findByIdAnswerOption(answerOption).isEmpty());
  }

  @Test
  void retractVote_UserExists_ShouldRemoveVote() {
    PollUser user = userDetailsService.createUser(new PollUser("sampleUser" + random.nextLong(), "pass"));
    Question question = new Question();
    question.setQuestionText("Is this a question?");
    Question storedQuestion = pollService.createQuestion(question, user);
    AnswerOption answerOption = new AnswerOption();
    answerOption.setQuestion(storedQuestion);
    answerOption.setOptionText("This is an option");
    AnswerOption storedAnswerOption = pollService.createAnswerOption(storedQuestion.getId(), answerOption, user);
    voteService.vote(user, storedAnswerOption.getId());
    assertFalse(userVoteDAO.findByIdUser(user).isEmpty());

    voteService.retractVote(user, storedAnswerOption.getId());
    assertTrue(userVoteDAO.findByIdUser(user).isEmpty());
    assertTrue(userVoteDAO.findByIdAnswerOption(answerOption).isEmpty());
  }
}