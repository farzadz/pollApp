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
import org.springframework.security.access.prepost.PostFilter;
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
    PollUser user = (PollUser) userDetailsService.loadUserByUsername(((UserDetails) principal).getUsername());

    pollAclService.boundAclForObject(questionInDb, user);
    return questionInDb;

  }

  @PreAuthorize("hasPermission(#questionId, 'com.farzadz.poll.dataentry.entity.Question', 'WRITE')")
  public AnswerOption createAnswerOption(Long questionId, AnswerOption answerOption) {
    Question question = getQuestion(questionId);
    answerOption.setQuestion(question);
    AnswerOption answerOptionInDb = answerOptionDAO.saveAndFlush(answerOption);

    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    PollUser user = (PollUser) userDetailsService.loadUserByUsername(((UserDetails) principal).getUsername());

    pollAclService.boundAclForObject(answerOptionInDb, user);
    return answerOptionInDb;
  }

  @PostFilter("hasPermission(filterObject, 'READ')")
  public List<Question> getAllQuestions() {
    return questionDAO.findAll();
  }

  @PostFilter("hasPermission(filterObject, 'READ')")
  public List<AnswerOption> getAllAnswerOptions() {
    return answerOptionDAO.findAll();
  }

  @PreAuthorize("hasPermission(#questionId, 'com.farzadz.poll.dataentry.entity.Question', 'READ')")
  public Question getQuestion(Long questionId) {
    return questionDAO.findById(questionId).orElseThrow(() -> new IllegalArgumentException(String.format("No question found with id %s in the database", questionId)));
  }

  @PreAuthorize("hasPermission(#questionId, 'com.farzadz.poll.dataentry.entity.Question', 'READ')")
  public List<AnswerOption> getAnswerOptionsForQuestion(Long questionId) {
    return answerOptionDAO.findByQuestionId(questionId);
  }

  @PreAuthorize("hasPermission(#questionId, 'com.farzadz.poll.dataentry.entity.Question', 'WRITE')")
  public Question updateQuestion(Long questionId, Question question) {
    Question questionInDB = questionDAO.findById(questionId).orElseThrow(() -> new IllegalArgumentException(String.format("No question found with id %s in the database", questionId)));
    questionInDB.updateUpdatableProperties(question);
    return questionDAO.saveAndFlush(questionInDB);
  }

  @PreAuthorize("hasPermission(#questionId, 'com.farzadz.poll.dataentry.entity.Question', 'WRITE')")
  public AnswerOption updateAnswerOption(Long answerOptionId, Long questionId, AnswerOption answerOption) {
    AnswerOption answerOptionInDB = answerOptionDAO.findByIdAndQuestionId(answerOptionId, questionId).orElseThrow(
        () -> new IllegalArgumentException(String.format("No answer option found for question %s and id %s.", questionId, answerOptionId)));
    answerOptionInDB.updateUpdatableProperites(answerOption);
    return answerOptionDAO.saveAndFlush(answerOptionInDB);
  }

  @PreAuthorize("hasPermission(#questionId, 'com.farzadz.poll.dataentry.entity.Question', 'DELETE')")
  public void deleteQuestion(Long questionId) {
    if (questionDAO.existsById(questionId)) {
      questionDAO.deleteById(questionId);
    } else {
      throw new IllegalArgumentException(String.format("No question found with id %s in the database", questionId));
    }
  }

  @PreAuthorize("hasPermission(#questionId, 'com.farzadz.poll.dataentry.entity.Question', 'WRITE')")
  public void deleteAnswerOption(Long questionId, Long answerOptionId) {
    if (answerOptionDAO.existsByIdAndQuestionId(answerOptionId, questionId)) {
      answerOptionDAO.deleteById(answerOptionId);
    } else {
      throw new IllegalArgumentException(String.format("No answer option found for question %s and id %s in the database", questionId, answerOptionId));
    }
  }

  @PreAuthorize("hasPermission(#questionId, 'com.farzadz.poll.dataentry.entity.Question', 'READ')")
  public AnswerOption getAnswerOptionForQuestion(Long questionId, Long answerOptionId) {
    return answerOptionDAO.findByIdAndQuestionId(answerOptionId, questionId).orElseThrow(
        () -> new IllegalArgumentException(String.format("No answer option found for question %s and id %s in the database", questionId, answerOptionId)));
  }

}
