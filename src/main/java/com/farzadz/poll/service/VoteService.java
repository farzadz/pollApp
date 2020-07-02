package com.farzadz.poll.service;

import com.farzadz.poll.dataentry.dao.UserVoteDAO;
import com.farzadz.poll.dataentry.entity.AnswerOption;
import com.farzadz.poll.dataentry.entity.UserVote;
import com.farzadz.poll.dataentry.entity.VotePK;
import com.farzadz.poll.security.user.PollUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class VoteService {

  private final PollService pollService;

  private final UserVoteDAO userVoteDAO;

  public UserVote vote(PollUser user, Long answerOptionId) {
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

}
