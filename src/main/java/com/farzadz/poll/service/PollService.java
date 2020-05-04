package com.farzadz.poll.service;

import com.farzadz.poll.dataentry.dao.AnswerOptionDAO;
import com.farzadz.poll.dataentry.dao.QuestionDAO;
import com.farzadz.poll.dataentry.entity.AnswerOption;
import com.farzadz.poll.dataentry.entity.Question;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor
public class PollService {

  private final AnswerOptionDAO answerOptionDAO;

  private final QuestionDAO questionDAO;

  public Question createQuestion(Question question) {
    return questionDAO.saveAndFlush(question);
  }

  public AnswerOption createAnswerOption(Long questionId, AnswerOption answerOption) {
    Question question = getQuestion(questionId);
    answerOption.setQuestion(question);
    return answerOptionDAO.saveAndFlush(answerOption);
  }

  public List<Question> getAllQuestions() {
    return questionDAO.findAll();
  }

  public List<AnswerOption> getAllAnswerOptions() {
    return answerOptionDAO.findAll();
  }

  public Question getQuestion(Long questionId) {
    return questionDAO.findById(questionId).orElseThrow(
        () -> new IllegalArgumentException(String.format("No question found with id %s in the database", questionId)));
  }

  public List<AnswerOption> getAnswerOptionsForQuestion(Long questionId) {
    return answerOptionDAO.findByQuestionId(questionId);
  }

  public Question updateQuestion(Long questionId, Question question) {
    Question questionInDB = questionDAO.findById(questionId).orElseThrow(
        () -> new IllegalArgumentException(String.format("No question found with id %s in the database", questionId)));
    questionInDB.updateUpdatableProperties(question);
    return questionDAO.saveAndFlush(questionInDB);
  }

  public AnswerOption updateAnswerOption(Long answerOptionId, Long questionId, AnswerOption answerOption) {
    AnswerOption answerOptionInDB = answerOptionDAO.findByIdAndQuestionId(answerOptionId, questionId).orElseThrow(
        () -> new IllegalArgumentException(
            String.format("No answer option found for question %s and id %s.", questionId, answerOptionId)));
    answerOptionInDB.updateUpdatableProperites(answerOption);
    return answerOptionDAO.saveAndFlush(answerOptionInDB);
  }

  public void deleteQuestion(Long questionId) {
    if (questionDAO.existsById(questionId)) {
      questionDAO.deleteById(questionId);
    } else {
      throw new IllegalArgumentException(String.format("No question found with id %s in the database", questionId));
    }
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
