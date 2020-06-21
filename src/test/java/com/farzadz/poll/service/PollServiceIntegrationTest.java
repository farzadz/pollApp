package com.farzadz.poll.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.farzadz.poll.PollApplication;
import com.farzadz.poll.dataentry.dao.AnswerOptionDAO;
import com.farzadz.poll.dataentry.dao.QuestionDAO;
import com.farzadz.poll.dataentry.entity.AnswerOption;
import com.farzadz.poll.dataentry.entity.Question;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PollApplication.class)
@TestPropertySource(locations = "classpath:test.yaml")
@WithMockUser
public class PollServiceIntegrationTest {

  @Autowired
  private QuestionDAO questionDAO;

  @Autowired
  private AnswerOptionDAO answerOptionDAO;

  @Autowired
  private PollService pollService;

  @Test
  public void contextLoads() {
  }

  @Test
  public void testRepositoriesFilled() {
    assertNotNull(questionDAO.findAll());
    assertNotNull(answerOptionDAO.findAll());
    assertFalse(questionDAO.findAll().isEmpty());
    assertFalse(answerOptionDAO.findAll().isEmpty());
  }


  @Test
  public void createNewQuestion__ShouldReturnPersistedQuestion() {
    Question question = new Question();
    question.setQuestionText("test");
    Question questionInDb = pollService.createQuestion(question);
    assertEquals(question.getQuestionText(), questionInDb.getQuestionText());
  }

  @Test
  public void getQuestion_QuestionExists_ShouldReturnQuestionWithAnswerOptions() {
    Question question = new Question();
    question.setQuestionText("test");
    Question questionInDb = pollService.createQuestion(question);
    Question retrievedQuestion = pollService.getQuestion(questionInDb.getId());
    assertEquals(question.getQuestionText(), retrievedQuestion.getQuestionText());
    assertTrue(retrievedQuestion.getAnswerOptions().isEmpty());
    AnswerOption answerOption = new AnswerOption();
    answerOption.setVoteCount(0);
    answerOption.setQuestion(question);
    answerOption.setOptionText("optionText");
    pollService.createAnswerOption(questionInDb.getId(), answerOption);
    Question retrievedQuestionWithOptions = pollService.getQuestion(questionInDb.getId());
    assertFalse(retrievedQuestionWithOptions.getAnswerOptions().isEmpty());
  }

  @Test
  public void updateQuestion_QuestionExists_ShouldReturnUpdatedQuestion() {
    Question question = new Question();
    question.setQuestionText("test");
    Question questionInDb = pollService.createQuestion(question);
    Question newQuestion = new Question();
    newQuestion.setQuestionText("newText");
    Question updatedQuestionInDb = pollService.updateQuestion(questionInDb.getId(), newQuestion);
    assertEquals(newQuestion.getQuestionText(), updatedQuestionInDb.getQuestionText());
    assertEquals(questionInDb.getId(), updatedQuestionInDb.getId());
  }

  @Test
  public void createNewAnswerOption_QuestionExists_ShouldReturnPersistedAnswerOption() {
    Question question = new Question();
    question.setQuestionText("test");
    Question questionInDb = pollService.createQuestion(question);
    assertEquals(questionInDb.getQuestionText(), "test");
    AnswerOption answerOption = new AnswerOption();
    answerOption.setVoteCount(0);
    answerOption.setOptionText("optionText");
    answerOption.setQuestion(questionInDb);
    AnswerOption answerOptionInDb = pollService.createAnswerOption(questionInDb.getId(), answerOption);
    assertEquals(answerOption.getOptionText(), answerOptionInDb.getOptionText());
    assertEquals(answerOption.getQuestion().getId(), questionInDb.getId());
  }

  @Test(expected = Exception.class)
  public void createNewAnswerOption_QuestionDoesNotExist_ShouldThrowException() {
    AnswerOption answerOption = new AnswerOption();
    answerOption.setVoteCount(0);
    answerOption.setOptionText("optionText");
    pollService.createAnswerOption(null, answerOption);
  }

  @Test
  public void updateAnswerOption_AnswerOptionExists_ShouldReturnUpdatedAnswerOption() {
    Question question = new Question();
    question.setQuestionText("test");
    Question questionInDb = pollService.createQuestion(question);
    AnswerOption answerOption = new AnswerOption();
    answerOption.setVoteCount(0);
    answerOption.setOptionText("optionText");
    answerOption.setQuestion(questionInDb);
    AnswerOption answerOptionInDb = pollService.createAnswerOption(questionInDb.getId(), answerOption);
    AnswerOption newAnswerOption = new AnswerOption();
    newAnswerOption.setOptionText("newOptionText");
    newAnswerOption.setVoteCount(1);
    newAnswerOption.setQuestion(questionInDb);
    AnswerOption updatedAnswerOption = pollService
        .updateAnswerOption(answerOptionInDb.getId(), questionInDb.getId(), newAnswerOption);
    assertEquals(newAnswerOption.getVoteCount(), updatedAnswerOption.getVoteCount());
    assertEquals(newAnswerOption.getOptionText(), updatedAnswerOption.getOptionText());
  }

  @Test
  public void updateAnswerOption_QuestionExists_ShouldReturnUpdatedAnswerOption() {
    Question question = new Question();
    question.setQuestionText("test");
    Question questionInDb = pollService.createQuestion(question);
    AnswerOption answerOption = new AnswerOption();
    answerOption.setVoteCount(0);
    answerOption.setOptionText("optionText");
    answerOption.setQuestion(questionInDb);
    AnswerOption answerOptionInDb = pollService.createAnswerOption(questionInDb.getId(), answerOption);
    AnswerOption newAnswerOption = new AnswerOption();
    newAnswerOption.setVoteCount(3);
    newAnswerOption.setOptionText("newOptionText");
    AnswerOption updatedAnswerOptionInDb = pollService
        .updateAnswerOption(answerOptionInDb.getId(), answerOptionInDb.getQuestion().getId(), newAnswerOption);
    assertEquals(newAnswerOption.getOptionText(), updatedAnswerOptionInDb.getOptionText());
    assertEquals(newAnswerOption.getVoteCount(), updatedAnswerOptionInDb.getVoteCount());
    assertEquals(answerOptionInDb.getId(), updatedAnswerOptionInDb.getId());
  }

}
