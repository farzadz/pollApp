package com.farzadz.poll.service;

import com.farzadz.poll.dataentry.dao.AnswerOptionDAO;
import com.farzadz.poll.dataentry.dao.QuestionDAO;
import com.farzadz.poll.dataentry.entity.AnswerOption;
import com.farzadz.poll.dataentry.entity.Question;
import com.farzadz.poll.security.user.PollUser;
import com.farzadz.poll.security.user.PollUserDetailsService;
import java.util.List;
import javax.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @ReadAccessFilter and @QuestionRead annotations also available see  {@link com.farzadz.poll.security.SecurityAnnotations}.
 */

@Service
@Data
@RequiredArgsConstructor
@Transactional
public class PollService {

  private final AnswerOptionDAO answerOptionDAO;

  private final QuestionDAO questionDAO;

  private final PollAclService pollAclService;

  private final PollUserDetailsService userDetailsService;

  public Question createQuestion(Question question, PollUser user) {

    Question questionInDb = questionDAO.saveAndFlush(question);
    pollAclService.boundAclForObject(questionInDb, user);
    return questionInDb;

  }

  public Question getQuestion(Long questionId) {
    return questionDAO.findById(questionId).orElseThrow(
        () -> new IllegalArgumentException(String.format("No question found with id %s in the database", questionId)));
  }

  public Question updateQuestion(Long questionId, Question question) {
    Question questionInDB = questionDAO.findById(questionId).orElseThrow(
        () -> new IllegalArgumentException(String.format("No question found with id %s in the database", questionId)));
    questionInDB.updateUpdatableProperties(question);
    return questionDAO.saveAndFlush(questionInDB);
  }

  public void deleteQuestion(Long questionId) {
    if (questionDAO.existsById(questionId)) {
      questionDAO.deleteById(questionId);
    } else {
      throw new IllegalArgumentException(String.format("No question found with id %s in the database", questionId));
    }
  }

  public List<Question> getAllQuestions() {
    return questionDAO.findAll();
  }

  public AnswerOption createAnswerOption(Long questionId, AnswerOption answerOption, PollUser user) {
    Question question = getQuestion(questionId);
    answerOption.setQuestion(question);
    AnswerOption answerOptionInDb = answerOptionDAO.saveAndFlush(answerOption);

    pollAclService.boundAclForObject(answerOptionInDb, user);
    return answerOptionInDb;
  }

  public List<AnswerOption> getAllAnswerOptions() {
    return answerOptionDAO.findAll();
  }

  public List<AnswerOption> getAnswerOptionsForQuestion(Long questionId) {
    return answerOptionDAO.findByQuestionId(questionId);
  }

  public AnswerOption getAnswerOption(Long answerOptionId) {
    return answerOptionDAO.findById(answerOptionId).orElseThrow(
        () -> new IllegalArgumentException(String.format("No answer option found with %s id", answerOptionId)));
  }

  public AnswerOption updateAnswerOption(Long answerOptionId, Long questionId, AnswerOption answerOption) {
    AnswerOption answerOptionInDB = answerOptionDAO.findByIdAndQuestionId(answerOptionId, questionId).orElseThrow(
        () -> new IllegalArgumentException(
            String.format("No answer option found for question %s and id %s.", questionId, answerOptionId)));
    answerOptionInDB.updateUpdatableProperites(answerOption);
    return answerOptionDAO.saveAndFlush(answerOptionInDB);
  }

  public void deleteAnswerOption(Long questionId, Long answerOptionId) {
    if (answerOptionDAO.existsByIdAndQuestionId(answerOptionId, questionId)) {
      answerOptionDAO.deleteById(answerOptionId);
    } else {
      throw new IllegalArgumentException(String
          .format("No answer option found for question %s and id %s in the database", questionId, answerOptionId));
    }
  }

  public AnswerOption getAnswerOptionForQuestion(Long questionId, Long answerOptionId) {
    return answerOptionDAO.findByIdAndQuestionId(answerOptionId, questionId).orElseThrow(
        () -> new IllegalArgumentException(String
            .format("No answer option found for question %s and id %s in the database", questionId, answerOptionId)));
  }

}
