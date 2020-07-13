package com.farzadz.poll.dataentry.dao;

import com.farzadz.poll.dataentry.entity.AnswerOption;
import com.farzadz.poll.dataentry.entity.UserVote;
import com.farzadz.poll.dataentry.entity.VotePK;
import com.farzadz.poll.security.user.PollUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVoteDAO extends JpaRepository<UserVote, VotePK> {

  List<UserVote> findByIdUser(PollUser user);

  List<UserVote> findByIdAnswerOption(AnswerOption answerOption);

  Optional<UserVote> findByIdUserAndIdAnswerOption(PollUser user, AnswerOption answerOption);

  Long countByIdAnswerOption(AnswerOption answerOption);

  Long countByIdAnswerOptionId(Long answerOptionId);

  Long countByIdUser(PollUser user);

  Long countByIdUserUsername(String username);

  List<UserVote> findByIdAnswerOptionId(Long answerOptionId);

  List<UserVote> findByIdAnswerOptionQuestionId(Long questionId);
}
