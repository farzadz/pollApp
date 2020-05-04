package com.farzadz.poll.dataentry.dao;

import com.farzadz.poll.dataentry.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionDAO extends JpaRepository<Question, Long> {

}
