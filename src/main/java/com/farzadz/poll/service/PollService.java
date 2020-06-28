package com.farzadz.poll.service;

import com.farzadz.poll.dataentry.dao.AnswerOptionDAO;
import com.farzadz.poll.dataentry.dao.QuestionDAO;
import com.farzadz.poll.dataentry.entity.AnswerOption;
import com.farzadz.poll.dataentry.entity.Question;
import com.farzadz.poll.security.SecurityAnnotations.QuestionRead;
import com.farzadz.poll.security.SecurityAnnotations.QuestionWrite;
import com.farzadz.poll.security.SecurityAnnotations.ReadAccessFilter;
import com.farzadz.poll.security.user.PollUser;
import com.farzadz.poll.security.user.PollUserDetailsService;
import java.util.List;
import javax.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor
@Transactional
public class PollService {

  private final AnswerOptionDAO answerOptionDAO;

  private final QuestionDAO questionDAO;

  private final PollAclService pollAclService;

  private final PollUserDetailsService userDetailsService;

  @PreAuthorize("isAuthenticated()")
  public Question createQuestion(Question question) {
    Question questionInDb = questionDAO.saveAndFlush(question);

    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    PollUser user = userDetailsService.loadUserByUsername(((UserDetails) principal).getUsername());

    pollAclService.boundAclForObject(questionInDb, user);
    return questionInDb;

  }

  @QuestionWrite
  public AnswerOption createAnswerOption(Long questionId, AnswerOption answerOption) {
    Question question = getQuestion(questionId);
    answerOption.setQuestion(question);
    AnswerOption answerOptionInDb = answerOptionDAO.saveAndFlush(answerOption);

    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    PollUser user = userDetailsService.loadUserByUsername(((UserDetails) principal).getUsername());

    pollAclService.boundAclForObject(answerOptionInDb, user);
    return answerOptionInDb;
  }

  @ReadAccessFilter
  public List<Question> getAllQuestions() {
    return questionDAO.findAll();
  }

  @ReadAccessFilter
  public List<AnswerOption> getAllAnswerOptions() {
    return answerOptionDAO.findAll();
  }

  @QuestionRead
  public Question getQuestion(Long questionId) {
    return questionDAO.findById(questionId).orElseThrow(
        () -> new IllegalArgumentException(String.format("No question found with id %s in the database", questionId)));
  }

  @QuestionRead
  public List<AnswerOption> getAnswerOptionsForQuestion(Long questionId) {
    return answerOptionDAO.findByQuestionId(questionId);
  }

  @QuestionWrite
  public Question updateQuestion(Long questionId, Question question) {
    Question questionInDB = questionDAO.findById(questionId).orElseThrow(
        () -> new IllegalArgumentException(String.format("No question found with id %s in the database", questionId)));
    questionInDB.updateUpdatableProperties(question);
    return questionDAO.saveAndFlush(questionInDB);
  }

  @QuestionWrite
  public AnswerOption updateAnswerOption(Long answerOptionId, Long questionId, AnswerOption answerOption) {
    AnswerOption answerOptionInDB = answerOptionDAO.findByIdAndQuestionId(answerOptionId, questionId).orElseThrow(
        () -> new IllegalArgumentException(
            String.format("No answer option found for question %s and id %s.", questionId, answerOptionId)));
    answerOptionInDB.updateUpdatableProperites(answerOption);
    return answerOptionDAO.saveAndFlush(answerOptionInDB);
  }

  @QuestionWrite
  public void deleteQuestion(Long questionId) {
    if (questionDAO.existsById(questionId)) {
      questionDAO.deleteById(questionId);
    } else {
      throw new IllegalArgumentException(String.format("No question found with id %s in the database", questionId));
    }
  }

  @QuestionWrite
  public void deleteAnswerOption(Long questionId, Long answerOptionId) {
    if (answerOptionDAO.existsByIdAndQuestionId(answerOptionId, questionId)) {
      answerOptionDAO.deleteById(answerOptionId);
    } else {
      throw new IllegalArgumentException(String
          .format("No answer option found for question %s and id %s in the database", questionId, answerOptionId));
    }
  }

  @QuestionRead
  public AnswerOption getAnswerOptionForQuestion(Long questionId, Long answerOptionId) {
    return answerOptionDAO.findByIdAndQuestionId(answerOptionId, questionId).orElseThrow(
        () -> new IllegalArgumentException(String
            .format("No answer option found for question %s and id %s in the database", questionId, answerOptionId)));
  }

}
