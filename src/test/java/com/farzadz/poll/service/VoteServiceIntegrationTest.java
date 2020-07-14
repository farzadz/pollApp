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
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PollApplication.class)
@TestPropertySource(locations = "classpath:test.yaml")
@WithMockUser(username = "user")
public class VoteServiceIntegrationTest {

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
  public void createVote_UserExists_ShouldCreateVoteInDB() {
    PollUser user = userDetailsService.createUser(new PollUser("sampleUser" + random.nextLong(), "pass"));
    Question question = new Question();
    question.setQuestionText("Is this a question?");
    Question storedQuestion = pollService.createQuestion(question, user);
    AnswerOption answerOption = new AnswerOption();
    answerOption.setQuestion(storedQuestion);
    answerOption.setOptionText("This is an option");
    AnswerOption storedAnswerOption = pollService.createAnswerOption(storedQuestion.getId(), answerOption, user);
    UserVote vote = voteService.vote(storedAnswerOption.getId(), user);
    assertEquals(storedAnswerOption.getId(), vote.getId().getAnswerOption().getId());
    assertEquals(user.getUsername(), vote.getId().getUser().getUsername());
    assertFalse(userVoteDAO.findByIdUser(user).isEmpty());
    assertFalse(userVoteDAO.findByIdAnswerOption(answerOption).isEmpty());
  }

  @Test
  public void voteCount_QuestionExists_ShouldUpdateAfterUserVote() {

    PollUser owner = userDetailsService.createUser(new PollUser("sampleUser" + random.nextLong(), "pass"));
    Question question = new Question();
    question.setQuestionText("Is this a question?");
    Question storedQuestion = pollService.createQuestion(question, owner);
    AnswerOption answerOption = new AnswerOption();
    answerOption.setQuestion(storedQuestion);
    answerOption.setOptionText("This is an option");
    AnswerOption storedAnswerOption = pollService.createAnswerOption(storedQuestion.getId(), answerOption, owner);
    assertEquals(voteService.voteCount(storedAnswerOption.getId()).intValue(), 0);
    voteService.vote(storedAnswerOption.getId(), owner);
    assertEquals(voteService.voteCount(storedAnswerOption.getId()).intValue(), 1);
    PollUser otherUser = userDetailsService.createUser(new PollUser("sampleUser" + random.nextLong(), "pass"));
    voteService.vote(storedAnswerOption.getId(), otherUser);
    assertEquals(voteService.voteCount(storedAnswerOption.getId()).intValue(), 2);

  }

  @Test
  public void questionStats_QuestionAndOptionsExist_ShouldUpdateAfterUserVote() {

    PollUser owner = userDetailsService.createUser(new PollUser("sampleUser" + random.nextLong(), "pass"));
    Question question = new Question();
    question.setQuestionText("Is this a question?");
    Question storedQuestion = pollService.createQuestion(question, owner);
    AnswerOption firstOption = new AnswerOption();
    firstOption.setQuestion(storedQuestion);
    firstOption.setOptionText("This is the first option");

    AnswerOption secondOption = new AnswerOption();
    secondOption.setQuestion(storedQuestion);
    secondOption.setOptionText("This is the second option");
    AnswerOption firstStoredOption = pollService.createAnswerOption(storedQuestion.getId(), firstOption, owner);
    AnswerOption secondStoredOption = pollService.createAnswerOption(storedQuestion.getId(), secondOption, owner);

    Map<AnswerOption, Long> questionOptionStats = voteService.questionStats(storedQuestion.getId());
    assertTrue(questionOptionStats.keySet().containsAll(Set.of(firstStoredOption, secondStoredOption)));
    assertEquals(questionOptionStats.get(firstStoredOption).intValue(), 0);
    assertEquals(questionOptionStats.get(secondStoredOption).intValue(), 0);

    voteService.vote(firstStoredOption.getId(), owner);
    questionOptionStats = voteService.questionStats(storedQuestion.getId());
    assertEquals(questionOptionStats.get(firstStoredOption).intValue(), 1);
    assertEquals(questionOptionStats.get(secondStoredOption).intValue(), 0);

    voteService.vote(secondStoredOption.getId(), owner);
    questionOptionStats = voteService.questionStats(storedQuestion.getId());
    assertEquals(questionOptionStats.get(firstStoredOption).intValue(), 1);
    assertEquals(questionOptionStats.get(secondStoredOption).intValue(), 1);

  }

  @Test
  public void userQuestionAnswers_QuestionAndOptionsExist_ShouldUpdateAfterUserVote() {

    PollUser owner = userDetailsService.createUser(new PollUser("sampleUser" + random.nextLong(), "pass"));
    Question question = new Question();
    question.setQuestionText("Is this a question?");
    Question storedQuestion = pollService.createQuestion(question, owner);
    AnswerOption firstOption = new AnswerOption();
    firstOption.setQuestion(storedQuestion);
    firstOption.setOptionText("This is the first option");

    AnswerOption secondOption = new AnswerOption();
    secondOption.setQuestion(storedQuestion);
    secondOption.setOptionText("This is the second option");
    AnswerOption firstStoredOption = pollService.createAnswerOption(storedQuestion.getId(), firstOption, owner);
    AnswerOption secondStoredOption = pollService.createAnswerOption(storedQuestion.getId(), secondOption, owner);

    Map<AnswerOption, Set<String>> usersAnswers = voteService.userQuestionAnswers(storedQuestion.getId());

    assertTrue(usersAnswers.isEmpty());

    voteService.vote(firstStoredOption.getId(), owner);
    usersAnswers = voteService.userQuestionAnswers(storedQuestion.getId());
    assertTrue(usersAnswers.get(firstStoredOption).contains(owner.getUsername()));

    voteService.vote(secondStoredOption.getId(), owner);
    usersAnswers = voteService.userQuestionAnswers(storedQuestion.getId());
    assertTrue(usersAnswers.get(secondStoredOption).contains(owner.getUsername()));
  }

  @Test
  public void retractVote_UserExists_ShouldRemoveVote() {
    PollUser user = userDetailsService.createUser(new PollUser("sampleUser" + random.nextLong(), "pass"));
    Question question = new Question();
    question.setQuestionText("Is this a question?");
    Question storedQuestion = pollService.createQuestion(question, user);
    AnswerOption answerOption = new AnswerOption();
    answerOption.setQuestion(storedQuestion);
    answerOption.setOptionText("This is an option");
    AnswerOption storedAnswerOption = pollService.createAnswerOption(storedQuestion.getId(), answerOption, user);
    voteService.vote(storedAnswerOption.getId(), user);
    assertFalse(userVoteDAO.findByIdUser(user).isEmpty());

    voteService.retractVote(user, storedAnswerOption.getId());
    assertTrue(userVoteDAO.findByIdUser(user).isEmpty());
    assertTrue(userVoteDAO.findByIdAnswerOption(answerOption).isEmpty());
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void voteTwice_UserExists_ShouldPreventSecond() {
    PollUser user = userDetailsService.createUser(new PollUser("sampleUser" + random.nextLong(), "pass"));
    Question question = new Question();
    question.setQuestionText("Is this a question?");
    Question storedQuestion = pollService.createQuestion(question, user);
    AnswerOption answerOption = new AnswerOption();
    answerOption.setQuestion(storedQuestion);
    answerOption.setOptionText("This is an option");
    AnswerOption storedAnswerOption = pollService.createAnswerOption(storedQuestion.getId(), answerOption, user);
    voteService.vote(storedAnswerOption.getId(), user);
    voteService.vote(storedAnswerOption.getId(), user);
  }

}