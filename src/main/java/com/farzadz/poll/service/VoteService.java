package com.farzadz.poll.service;

import com.farzadz.poll.dataentry.entity.AnswerOption;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService {

  private final PollService pollService;

  public AnswerOption vote(Long questionId, Long answerOptionId) {
    AnswerOption answerOption = pollService.getAnswerOptionForQuestion(questionId, answerOptionId);
    answerOption.setVoteCount(answerOption.getVoteCount() + 1);
    return pollService.updateAnswerOption(answerOptionId, questionId, answerOption);
  }

  public AnswerOption retractVote(Long questionId, Long answerOptionId) {
    AnswerOption answerOption = pollService.getAnswerOptionForQuestion(questionId, answerOptionId);
    answerOption.setVoteCount(answerOption.getVoteCount() - 1);
    return pollService.updateAnswerOption(answerOptionId, questionId, answerOption);
  }

}
