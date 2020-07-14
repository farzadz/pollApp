package com.farzadz.poll.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

import com.farzadz.poll.dataentry.dao.UserVoteDAO;
import com.farzadz.poll.dataentry.entity.AnswerOption;
import com.farzadz.poll.dataentry.entity.UserVote;
import com.farzadz.poll.dataentry.entity.VotePK;
import com.farzadz.poll.security.user.PollUser;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class VoteService {

  private final PollService pollService;

  private final UserVoteDAO userVoteDAO;

  public UserVote vote(Long answerOptionId, PollUser user) {
    AnswerOption answerOption = pollService.getAnswerOption(answerOptionId);
    UserVote userVote = new UserVote(new VotePK(user, answerOption));
    return userVoteDAO.save(userVote);
  }

  public void retractVote(PollUser user, Long answerOptionId) {
    UserVote userVote = userVoteDAO.findByIdUserAndIdAnswerOption(user, pollService.getAnswerOption(answerOptionId))
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("User %s has not voted for answerOption %s", user.getUsername(), answerOptionId)));
    userVoteDAO.delete(userVote);
  }

  public List<UserVote> userVotes(PollUser user) {
    return userVoteDAO.findByIdUser(user);
  }

  public UserVote getVote(Long answerOptionId, PollUser user) {
    AnswerOption answerOption = pollService.getAnswerOption(answerOptionId);
    return userVoteDAO.findByIdUserAndIdAnswerOption(user, answerOption).orElseThrow(() -> new IllegalArgumentException(
        String.format("User %s has not voted for answerOption %s", user.getUsername(), answerOptionId)));
  }

  public Long voteCount(Long answerOptionId) {
    return userVoteDAO.countByIdAnswerOptionId(answerOptionId);
  }

  public Map<AnswerOption, Long> questionStats(Long questionId) {
    return pollService.getAnswerOptionsForQuestion(questionId).stream()
        .collect(Collectors.toMap(Function.identity(), answerOption -> voteCount(answerOption.getId())));
  }

  public Map<AnswerOption, Set<String>> userQuestionAnswers(Long questionId) {
    List<UserVote> userChoices = userVoteDAO.findByIdAnswerOptionQuestionId(questionId);
    return userChoices.stream().collect(groupingBy(userVote -> userVote.getId().getAnswerOption(),
        mapping(userVote -> userVote.getUsername(), toSet())));
  }
}
