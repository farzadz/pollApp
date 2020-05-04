package com.farzadz.poll.dataentry.dao;

import com.farzadz.poll.dataentry.entity.AnswerOption;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerOptionDAO extends JpaRepository<AnswerOption, Long> {

  List<AnswerOption> findByQuestionId(Long questionId);

  Optional<AnswerOption> findByIdAndQuestionId(Long answerOptionId, Long questionId);

  boolean existsByIdAndQuestionId(Long answerOptionId, Long questionId);
}
