package com.farzadz.poll.service;

import com.farzadz.poll.dataentry.dao.AnswerOptionDAO;
import com.farzadz.poll.dataentry.dao.QuestionDAO;
import com.farzadz.poll.dataentry.entity.AnswerOption;
import com.farzadz.poll.dataentry.entity.Question;
import com.farzadz.poll.security.user.PollUser;
import java.util.List;
import javax.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor
@Transactional
public class PollService {

  private final AnswerOptionDAO answerOptionDAO;

  private final QuestionDAO questionDAO;

  private final MutableAclService aclService;

  public Question createQuestion(Question question) {

    Question questionInDb = questionDAO.saveAndFlush(question);
    PollUser user = (PollUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ObjectIdentity oid = new ObjectIdentityImpl(Question.class, questionInDb.getId());
    MutableAcl acl = aclService.createAcl(oid);
    acl.insertAce(0, BasePermission.READ, new PrincipalSid(user.getUsername()), true);
    acl.insertAce(0, BasePermission.ADMINISTRATION, new PrincipalSid("admin"), true);
    aclService.updateAcl(acl);
    return questionInDb;

  }

  public AnswerOption createAnswerOption(Long questionId, AnswerOption answerOption) {
    Question question = getQuestion(questionId);
    answerOption.setQuestion(question);
    return answerOptionDAO.saveAndFlush(answerOption);
  }

  @PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
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
